import { db, collection, doc, addDoc, getDoc, getDocs, deleteDoc, updateDoc, getStorage, getDownloadURL, uploadBytesResumable, deleteObject, ref } from "../src/FireBaseConfig.js";
import { checkAdmin, logout } from "../src/loginCheck.js";

const nav = document.querySelector(".toggle-btn");
nav.addEventListener("click", () => {
    document.querySelector("#sidebar").classList.toggle("expand");
});

window.onload = function () {
    checkAdmin();
    displayComplaints();
}

const formatDate = (timestamp) => {
    const date = new Date(timestamp * 1000); // Convert Firestore timestamp (in seconds) to milliseconds
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${day}/${month}/${year} | ${hours}:${minutes}`;
};

const societiesCollection = collection(db, 'societies');

const toggleComplaints = async () => {
    const toggleButton = document.querySelector(".toggle");
    const buttonText = toggleButton.textContent;

    if (buttonText === "Show Completed Complaints") {
        await displayCompletedComplaints();
        toggleButton.textContent = "Show Pending Complaints";
    } else {
        await displayComplaints();
        toggleButton.textContent = "Show Completed Complaints";
    }
};

document.querySelector(".toggle").addEventListener("click", toggleComplaints);

const displayCompletedComplaints = async () => {
    document.querySelector('.form-control').value = null;
    const searchButton = document.getElementById('searchButton');
    searchButton.textContent = 'Search';
    searchButton.classList.remove('cancelButton');

    const societiesSnapshot = await getDocs(societiesCollection);
    const complaintList = document.querySelector("#complaintTableBody");
    complaintList.innerHTML = "";

    societiesSnapshot.forEach(async (societyDoc) => {

        let address=societyDoc.data().address;
        const complaintsCollection = collection(societyDoc.ref, 'complains');
        const complaintsSnapshot = await getDocs(complaintsCollection);

        complaintsSnapshot.forEach((doc) => {
            const data = doc.data();
            if (data.resolved && data.approved) {
                const complaintsHTML = `
                    <tr data-eventid="${doc.id}">
                        <td><img src="${data.imageUrl}" style="max-width: 150px; max-height: 150px; border-radius:10px;"></td>
                        <td>${data.title}</td>
                        <td>${data.description}</td>
                        <td>${data.societyName}</td>
                        <td>${data.type}</td>
                        <td>${formatDate(data.timestamp)}</td>
                        <td>${address}</td>
                        <td><button class="btn btn-secondary markPending" data-id="${doc.id}">Mark Pending</button></td>
                    </tr>
                `;
                complaintList.insertAdjacentHTML("beforeend", complaintsHTML);
            }
        });
    });
};

const displayComplaints = async () => {
    document.querySelector('.form-control').value = null;
    const searchButton = document.getElementById('searchButton');
    searchButton.textContent = 'Search';
    searchButton.classList.remove('cancelButton');

    const societiesSnapshot = await getDocs(societiesCollection);

    const complaintList = document.querySelector(".table tbody");
    complaintList.innerHTML = "";

    societiesSnapshot.forEach(async (societyDoc) => {

        let address=societyDoc.data().address;
        const complaintsCollection = collection(societyDoc.ref, 'complains');
        const complaintsSnapshot = await getDocs(complaintsCollection);

        complaintsSnapshot.forEach((doc) => {
            const data = doc.data();
            if (!data.resolved && data.approved) { // Only display complaints that are not completed
                const complaintsHTML = `
                    <tr data-eventid="${doc.id}">
                    <td><img src="${data.imageUrl}" style="max-height: 100px; border-radius:10px;"></td>
                    <td>${data.title}</td>
                    <td>${data.description}</td>
                    <td>${data.societyName}</td>
                    <td>${data.type}</td>
                    <td>${formatDate(data.timestamp)}</td>
                    <td>${address}</td>
                    <td><button class="btn btn-secondary markDone" data-id="${doc.id}">Mark Done</button></td>
                    </tr>
                `;
                complaintList.insertAdjacentHTML("beforeend", complaintsHTML);
            }
        });
    });
};



// Function to get the document IDs from the 'socities' collection
const getDocumentIds = async () => {
    const snapshot = await getDocs(societiesCollection);
    return snapshot.docs.map(doc => doc.id);
};

document.querySelector(".table tbody").addEventListener('click', async (e) => {
    
    const target = e.target;

    if (target.classList.contains('markDone')) {
        const complaintId = target.dataset.id;
        // Get the document IDs from the 'socities' collection
        const socitiesDocumentIds = await getDocumentIds();
        // Loop through the document IDs to find the correct document
        for (const socitiesDocumentId of socitiesDocumentIds) {
            const docRef = doc(db, 'societies', socitiesDocumentId, 'complains', complaintId);
            const docSnap = await getDoc(docRef);
            if (docSnap.exists()) {
                await updateDoc(docRef, {
                    resolved: true
                });
                alert("Complaint Marked As Done...");
                displayComplaints();
                break; // Exit loop once the document is found and updated
            }
        }
    }

    if (target.classList.contains('markPending')) {
        const complaintId = target.dataset.id;
        // Get the document IDs from the 'socities' collection
        const socitiesDocumentIds = await getDocumentIds();
        // Loop through the document IDs to find the correct document
        for (const socitiesDocumentId of socitiesDocumentIds) {
            const docRef = doc(db, 'societies', socitiesDocumentId, 'complains', complaintId);
            const docSnap = await getDoc(docRef);
            if (docSnap.exists()) {
                await updateDoc(docRef, {
                    resolved: false,
                    complaint_status: 'pending'
                });
                alert("Complaint Marked As pending...");
                displayCompletedComplaints();
                break; // Exit loop once the document is found and updated
            }
        }
    }
});

const searchRecord = (searchText) => {
    const complaintRows = document.querySelectorAll(".table tbody tr");

    complaintRows.forEach((row) => {
        const societyName = row.cells[4].textContent.toLowerCase(); // Index 5 corresponds to the "Society Name" column
        if (!societyName.includes(searchText.toLowerCase())) {
            row.classList.add("hidden"); // Hide row if societyName does not contain searchText
        } else {
            row.classList.remove("hidden"); // Show row if societyName contains searchText
        }
    });
};

document.getElementById('searchButton').addEventListener('click', () => {
    const searchText = document.querySelector('.form-control').value;
    const searchButton = document.getElementById('searchButton');

    if (searchText.trim() !== '') {
        searchRecord(searchText);

        // Change button text and class to Cancel
        if (searchButton.textContent === 'Search') {
            searchButton.textContent = 'Cancel';
            searchButton.classList.add('cancelButton');
        } else {
            // If the button text is already Cancel, it means the user wants to cancel the search
            document.querySelector('.form-control').value = ''; // Clear search input
            searchRecord(''); // Show all rows
            // Change button text and class back to Search
            searchButton.textContent = 'Search';
            searchButton.classList.remove('cancelButton');
        }
    }
});

document.querySelector(".logout").addEventListener("click", () => {
    logout();
});
