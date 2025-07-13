import { db, collection, doc, addDoc, getDoc, getDocs, deleteDoc, updateDoc, getStorage, getDownloadURL, uploadBytesResumable, deleteObject, ref,setDoc } from "../src/FireBaseConfig.js";
import { checkAdmin, logout } from "../src/loginCheck.js";
const storage = getStorage();
const downloadURL = "";

window.onload = function () {
  checkAdmin();
}

const nav = document.querySelector(".toggle-btn");
nav.addEventListener("click", () => {
  document.querySelector("#sidebar").classList.toggle("expand");
});

const addEvent = async (event) => {
  try {
    // Check if the event date and time conflict with existing events
    const existingEvents = await checkEventConflicts(event.eventDatetime);
    if (existingEvents.length > 0) {
      alert("Another event is already scheduled at the same date and time.");
      return;
    }

    const storageRef = ref(storage, `eventBanners/${event.eventImage.name}`);
    const uploadTask = uploadBytesResumable(storageRef, event.eventImage);

    uploadTask.on("state_changed", (snapshot) => {
      // Handle upload progress and errors here
    });

    await uploadTask; // Wait for the upload to finish

    const downloadURL = await getDownloadURL(storageRef);

    // Check if an event already exists in the Firestore collection
    const eventsCollection = collection(db, "ADMIN/XqHXv5Ebd8aUXngVzSXq/events");
    const querySnapshot = await getDocs(eventsCollection);

    if (querySnapshot.size > 0) {
      // Update the existing event document
      const eventDoc = querySnapshot.docs[0].ref;
      await updateDoc(eventDoc, {
        eventImage: downloadURL,
        title: event.eventName,
        eventDescription: event.eventDescription,
        bookingDate: event.bookingDate,
        eventDatetime: event.eventDatetime,
      });
      alert("Event updated successfully");
    } else {
      // Add a new event document if no event exists
      await addDoc(eventsCollection, {
        eventImage: downloadURL,
        title: event.eventName,
        eventDescription: event.eventDescription,
        bookingDate: event.bookingDate,
        eventDatetime: event.eventDatetime,
      });
      alert("Event added successfully");
    }

    fetchEvents();
    location.reload();
  } catch (error) {
    alert("Please upload an event banner");
  }
};


// Function to check for event conflicts
const checkEventConflicts = async (dateTime) => {
  const eventsCollection = collection(db, "ADMIN/XqHXv5Ebd8aUXngVzSXq/events");
  const querySnapshot = await getDocs(eventsCollection);
  const existingEvents = [];

  querySnapshot.forEach((doc) => {
    const eventDateTime = doc.data().eventDatetime;
    if (eventDateTime === dateTime) {
      existingEvents.push(doc.data());
    }
  });

  return existingEvents;
};

// Function to fetch events and display in table
const fetchEvents = async () => {
  const eventCollection = collection(db, "ADMIN/XqHXv5Ebd8aUXngVzSXq/events");
  const eventSnapshot = await getDocs(eventCollection);

  const eventList = document.querySelector(".table tbody");
  eventList.innerHTML = "";

  eventSnapshot.forEach((doc) => {
    const data = doc.data();
    if (Object.keys(data).length > 0) {
      const imageUrl = data.eventImage;
      const eventRow = `
        <tr data-eventid="${doc.id}">
          <td><img src="${imageUrl}" /></td>
          <td>${data.title || ''}</td>
          <td>${data.eventDescription || ''}</td>
          <td>${data.bookingDate || ''}</td>
          <td>${data.eventDatetime || ''}</td>
          <td>
            <button type="button" class="btn btn-secondary">Edit</button>
            <button type="button" class="btn btn-danger">Delete</button>
          </td>
        </tr>
      `;
      eventList.insertAdjacentHTML("beforeend", eventRow);
    }
  });
};

// Add event on click of add event button
document.querySelector(".btn-primary").addEventListener("click", async (e) => {
  e.preventDefault();
  const loading = document.getElementById('loading');
  loading.style.display = 'block';

  const eventImage = document.querySelector("#eventImage").files[0];
  const eventName = document.querySelector("#eventName").value;
  const eventDescription = document.querySelector("#eventDescription").value;
  const bookingDate = document.querySelector("#bookingDate").value;
  const eventDatetime = document.querySelector("#eventDate").value;

  const currentDate = new Date();
  if (new Date(bookingDate) < currentDate.getTime() && new Date(eventDatetime) < currentDate.getTime()) {
    alert(bookingDate + " , " + eventDatetime + " Event date and time can not be in past");
    loading.style.display = 'none';
    return;
  }

  if (new Date(bookingDate) >= new Date(eventDatetime)) {
    alert("Booking always start before Event...");
    return;
  }

  const eventData = {
    eventImage,
    eventName,
    eventDescription,
    bookingDate,
    eventDatetime,
  };
  try {
    if (eventName !== "" && eventDescription !== "" && eventDatetime !== "" && bookingDate !== "") {
      await addEvent(eventData);
    } else {
      alert("please fill all the fields");
    }

  }
  catch (error) {
    alert("error occured");
  }
  finally {
    loading.style.display = 'none';
  }

  fetchEvents();
});

// Fetch events on page load
fetchEvents();


const deleteEvent = async (eventId) => {
  try {
    // Find the event document in Firestore
    const eventDocRef = doc(db, "ADMIN/XqHXv5Ebd8aUXngVzSXq/events", eventId);
    
    // Set the event document data to an empty object
    await setDoc(eventDocRef, {});

    alert("Event deleted successfully");
    fetchEvents();
  } catch (error) {
    console.log("Error in deleting event : ", error);
  }
};

// Add delete event on click of delete button
document.querySelector(".table tbody").addEventListener("click", async (e) => {
  if (e.target.tagName === "BUTTON") {
    const targetTr = e.target.closest("tr");
    if (!targetTr) {
      console.error("Event target is not within a table row element.");
      return;
    }

    const eventId = targetTr.dataset.eventid;

    if (e.target.classList.contains("btn-danger")) {
      deleteEvent(eventId);
    }
    else if (e.target.classList.contains("btn-secondary")) {
      const eventData = await fetchEventById(eventId);
      populateFormFields(eventData);
      const btnUpdate = document.querySelector(".btn-success");
      btnUpdate.style.display = "block";
      document.querySelector(".btn-primary").style.display = "none";

      //for updating
      btnUpdate.addEventListener("click", async (e) => {
        e.preventDefault();

        const updatedEventImage = document.querySelector("#eventImage").files[0];
        const updatedEventName = document.querySelector("#eventName").value;
        const updatedEventDescription = document.querySelector("#eventDescription").value;
        const updatedEventBookingDate = document.querySelector("#bookingDate").value;
        const updatedEventDatetime = document.querySelector("#eventDate").value;


        // Validate the updated event data
        if (!validateEventData(updatedEventName, updatedEventDescription, updatedEventBookingDate, updatedEventDatetime)) {
          return;
        }

        const updatedEventData = {
          title: updatedEventName,
          eventDescription: updatedEventDescription,
          bookingDate: updatedEventBookingDate,
          eventDatetime: updatedEventDatetime,
        };

        const eventDoc = doc(db, "ADMIN/XqHXv5Ebd8aUXngVzSXq/events", eventId);
        const eventSnapshot = await getDoc(eventDoc);
        const eventData = eventSnapshot.data();

        // If there is a new event image
        if (updatedEventImage) {
          const storageRef = ref(storage, `eventBanners/${updatedEventImage.name}`);
          const uploadTask = uploadBytesResumable(storageRef, updatedEventImage);

          uploadTask.on("state_changed", (snapshot) => {
            // Handle upload progress and errors here
          });

          await uploadTask; // Wait for the upload to finish

          const downloadURL = await getDownloadURL(storageRef);
          updatedEventData.eventImage = downloadURL;

          // Delete the old event image from Firebase Storage
          if (eventData.eventImage) {
            const oldStorageRef = ref(storage, eventData.eventImage);
            await deleteObject(oldStorageRef);
          }
        }

        // Update the event document in Firestore
        await updateDoc(eventDoc, updatedEventData);

        fetchEvents();
        alert("Event updated successfully");
        location.reload();
      });
    }
  }
});

// Function to fetch event data by id
const fetchEventById = async (eventId) => {
  const eventDoc = doc(db, "ADMIN/XqHXv5Ebd8aUXngVzSXq/events", eventId);
  const eventSnapshot = await getDoc(eventDoc);
  return eventSnapshot.data();
};

// Function to populate form fields with event data
const populateFormFields = (eventData) => {
  document.querySelector("#eventImage").src = eventData.eventImage;
  document.querySelector("#eventName").value = eventData.title;
  document.querySelector("#eventDescription").value = eventData.eventDescription;
  document.querySelector("#bookingDate").value = eventData.bookingDate;
  document.querySelector("#eventDate").value = eventData.eventDatetime;
};

document.querySelector(".logout").addEventListener("click", () => {
  logout();
})





// Function to validate event data
const validateEventData = (eventName, eventDescription, bookingDate, eventDatetime) => {
  if (!eventName || !eventDescription || !bookingDate || !eventDatetime) {
    alert("Please fill in all the fields");
    return false;
  }

  const currentDate = new Date();
  if (new Date(bookingDate) < currentDate.getTime() && new Date(eventDatetime) < currentDate.getTime()) {
    alert("Event date and time cannot be in the past");
    return false;
  }

  const splitString = eventDatetime.split('T');

  if (new Date(bookingDate) >= new Date(eventDate.value)) {
    alert("Booking always start before Event...");
    return false;
  }
  return true;
};
