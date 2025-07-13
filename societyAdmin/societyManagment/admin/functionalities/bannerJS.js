import { db, collection, doc, addDoc, getDoc, getDocs, deleteDoc, updateDoc, getStorage, getDownloadURL, uploadBytesResumable, deleteObject, ref } from "../src/FireBaseConfig.js";
import { checkAdmin, logout } from "../src/loginCheck.js";

const storage = getStorage();
const downloadURL = "";

const nav = document.querySelector(".toggle-btn");
nav.addEventListener("click", () => {
  document.querySelector("#sidebar").classList.toggle("expand");
});

document.querySelector(".logout").addEventListener("click", () => {
  logout();
});

window.onload = function () {
  checkAdmin();
};

const addBanner = async (banner) => {
  try {
    const storageRef = ref(storage, `banners/${banner.bannerImage.name}`);
    // Update storage reference path
    const uploadTask = uploadBytesResumable(storageRef, banner.bannerImage);

    uploadTask.on("state_changed", (snapshot) => {
      // Handle upload progress and errors here
    });

    await uploadTask; // Wait for the upload to finish

    const downloadURL = await getDownloadURL(storageRef);

    // Add the download URL to the Firestore document
    await addDoc(collection(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq", "banners"), {
      bannerImage: downloadURL,
      bannerTitle: banner.bannerTitle,
      bannerAddDate: banner.bannerAddDate
    });

    fetchBanners();
    alert("Banner added successfully");
    location.reload();
  } catch (error) {
    console.error("Error adding banner:", error);
    alert("An error occurred while adding the banner. Please try again.");
  }
};

// Function to fetch banners and display in table
const fetchBanners = async () => {
  const bannerCollection = collection(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq", "banners");
  const bannerSnapshot = await getDocs(bannerCollection);

  const bannerList = document.querySelector(".table tbody");
  bannerList.innerHTML = "";

  bannerSnapshot.forEach((doc) => {
    const imageUrl = doc.data().bannerImage;
    const bannerRow = `
      <tr data-bannerid="${doc.id}">
        <td><img src="${doc.data().bannerImage}" /></td>
        <td>${doc.data().bannerTitle}</td>
        <td>${doc.data().bannerAddDate}</td>
        <td>
          <button type="button" class="btn btn-secondary">Edit</button>
        </td>
        <td>
          <button type="button" class="btn btn-danger">Delete</button>
        </td>
      </tr>
    `;
    bannerList.insertAdjacentHTML("beforeend", bannerRow);
  });
};

// Add event on click of add event button
document.querySelector(".btn-primary").addEventListener("click", async (e) => {
  e.preventDefault();
  const loading = document.getElementById('loading');
  loading.style.display = 'block';

  const bannerImage = document.querySelector("#bannerImage").files[0];
  const bannerTitle = document.querySelector("#bannerTitle").value;
  const rightNow = new Date();
  const formattedBannerAddDate = rightNow.toLocaleString("en-GB", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit"
  });

  const bannerData = {
    bannerImage,
    bannerTitle,
    bannerAddDate: formattedBannerAddDate
  };
  try {
    if (bannerTitle !== "") {
      await addBanner(bannerData);
    } else {
      const bannerDataU = {
        bannerImage,
        bannerTitle: "",
        bannerAddDate: formattedBannerAddDate
      };
      await addBanner(bannerDataU);
    }

  }
  catch (error) {
    alert("error occured");
  }
  finally {
    loading.style.display = 'none';
  }

  fetchBanners();
});

// Fetch events on page load
fetchBanners();


const deleteBanner = async (bannerId) => {
  try {
    // Find the banner document in Firestore
    const bannerDoc = doc(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq", "banners", bannerId);
    const bannerSnapshot = await getDoc(bannerDoc);
    const bannerData = bannerSnapshot.data(); // Corrected variable name

    // Delete the image from Firebase Storage
    const storageRef = ref(storage, bannerData.bannerImage);
    await deleteObject(storageRef);

    // Delete the banner document from Firestore
    await deleteDoc(bannerDoc);

    fetchBanners();
    alert("Banner deleted successfully");
  } catch (error) {
    console.log("Error in deleting Banner: ", error);
  }
};


// Add delete event on click of delete button
document.querySelector(".table tbody").addEventListener("click", async (e) => {
  if (e.target.tagName === "BUTTON") {
    const targetTr = e.target.closest("tr");
    if (!targetTr) {
      console.error("banner target is not within a table row element.");
      return;
    }

    const bannerId = targetTr.dataset.bannerid;

    if (e.target.classList.contains("btn-danger")) {
      deleteBanner(bannerId);
    }
    else if (e.target.classList.contains("btn-secondary")) {
      const bannerData = await fetchBannerById(bannerId);
      populateFormFields(bannerData);
      const btnUpdate = document.querySelector(".btn-success");
      btnUpdate.style.display = "block";
      document.querySelector(".btn-primary").style.display = "none";

      //for updating
      btnUpdate.addEventListener("click", async (e) => {
        e.preventDefault();

        const updatedBannerImage = document.querySelector("#bannerImage").files[0];
        const updatedBannerTitle = document.querySelector("#bannerTitle").value;

        const updatedBannerData = {
          bannerTitle: updatedBannerTitle
        };

        const bannerDoc = doc(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq", "banners", bannerId);
        const bannerSnapshot = await getDoc(bannerDoc);
        const bannerData = bannerSnapshot.data();

        // If there is a new banner image
        if (updatedBannerImage) {
          const storageRef = ref(storage, `banners/${updatedBannerImage.name}`); // Update storage reference path
          const uploadTask = uploadBytesResumable(storageRef, updatedBannerImage);

          uploadTask.on("state_changed", (snapshot) => {
            // Handle upload progress and errors here
          });

          await uploadTask; // Wait for the upload to finish

          const downloadURL = await getDownloadURL(storageRef);
          updatedBannerData.bannerImage = downloadURL;
        }

        // Update the banner document in Firestore
        await updateBanner(bannerId, updatedBannerData);

        fetchBanners();
        alert("Banner updated successfully");
        location.reload();
      });
    }
  }
});

// Function to fetch banner data by id
const fetchBannerById = async (bannerId) => {
  const bannerDoc = doc(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq", "banners", bannerId);
  const bannerSnapshot = await getDoc(bannerDoc);
  return bannerSnapshot.data();
};

// Function to populate form fields with banner data
const populateFormFields = (bannerDoc) => {
  document.querySelector("#bannerImage").src = bannerDoc.bannerImage;
  document.querySelector("#bannerTitle").value = bannerDoc.bannerTitle;
};

// Updated updateBanner function
const updateBanner = async (bannerId, updatedBannerData) => {
  try {
    const bannerDoc = doc(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq", "banners", bannerId);
    const bannerSnapshot = await getDoc(bannerDoc);
    const bannerData = bannerSnapshot.data();

    // If there is a new banner image
    if (updatedBannerData.bannerImage) {
      const storageRef = ref(storage, `banners/${updatedBannerData.bannerImage.name}`); // Update storage reference path
      const uploadTask = uploadBytesResumable(storageRef, updatedBannerData.bannerImage);

      uploadTask.on("state_changed", (snapshot) => {
        // Handle upload progress and errors here
      });

      await uploadTask; // Wait for the upload to finish

      const downloadURL = await getDownloadURL(storageRef);
      
      // Update the banner image URL in updatedBannerData
      updatedBannerData.bannerImage = downloadURL;

      // Update the banner document in Firestore
      await updateDoc(bannerDoc, updatedBannerData);

      // Update the image source in the table
      const bannerImageElement = document.querySelector(`tr[data-bannerid="${bannerId}"] img`);
      bannerImageElement.src = downloadURL;
    } else {
      // If no new banner image, update other fields directly
      await updateDoc(bannerDoc, updatedBannerData);
    }

    fetchBanners();
    alert("Banner updated successfully");
  } catch (error) {
    console.log("Error in updating Banner: ", error);
  }
};
