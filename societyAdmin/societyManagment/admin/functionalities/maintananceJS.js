import { db } from '../src/FireBaseConfig.js';
import { collection, doc, getDocs, deleteDoc, addDoc, serverTimestamp,updateDoc } from '../src/FireBaseConfig.js';
import { checkAdmin, logout } from "../src/loginCheck.js";

const nav = document.querySelector(".toggle-btn");
nav.addEventListener("click", () => {
    document.querySelector("#sidebar").classList.toggle("expand");
});

window.onload = function () {
    checkAdmin();
}


const tableSwitch = document.getElementById('tableSwitch');

tableSwitch.addEventListener('change', function () {
    const unpaidTable = document.getElementById('unpaid');
    const paidTable = document.getElementById('paid');

    if (this.checked) {
        unpaidTable.style.display = 'none';
        paidTable.style.display = 'table';
    } else {
        unpaidTable.style.display = 'table';
        paidTable.style.display = 'none';
    }
});



//show data in table
const societiesCollection = collection(db, "societies");

// Function to render the table based on Firestore data
function renderTable() {
    const unpaidTableBody = document.getElementById("unpaid").querySelector("tbody");
    const paidTableBody = document.getElementById("paid").querySelector("tbody");

    // Clear existing table rows
    unpaidTableBody.innerHTML = "";
    paidTableBody.innerHTML = "";

    // Fetch data from Firestore
    getDocs(societiesCollection).then((querySnapshot) => {
        querySnapshot.forEach((doc) => {
            const transactionsCollection = collection(doc.ref, "transactions");

            getDocs(transactionsCollection).then((querySnapshot) => {
                querySnapshot.forEach((doc) => {
                    const data = doc.data();
                    const societyName = data.societyName;
                    const date = data.date;
                    const amount = data.amount;
                    const completed = data.completed;

                    // Determine which table body to append to
                    const tableBody = completed ? paidTableBody : unpaidTableBody;

                    // Create table row
                    const row = document.createElement("tr");

                    // Populate table cells
                    const societyNameCell = document.createElement("td");
                    societyNameCell.textContent = societyName;
                    row.appendChild(societyNameCell);

                    const dateCell = document.createElement("td");
                    const dateObj = new Date(data.date.toDate());
                    const dateString = dateObj.toLocaleDateString("en-GB", { day: "2-digit", month: "2-digit", year: "numeric" });
                    const timeString = dateObj.toLocaleTimeString("en-GB", { hour: "2-digit", minute: "2-digit" });
                    dateCell.textContent = `${dateString} | ${timeString}`;
                    row.appendChild(dateCell);

                    const amountCell = document.createElement("td");
                    amountCell.textContent = amount;
                    row.appendChild(amountCell);

                    const statusCell = document.createElement("td");
                    statusCell.textContent = completed ? "Paid" : "Unpaid";
                    row.appendChild(statusCell);

                    const actionCell = document.createElement("td");
                    const actionButton = document.createElement("button");
                    actionButton.classList.add("btn");
                    actionButton.classList.add("btn-secondary");
                    actionButton.textContent = completed ? "Unpaid" : "Paid";
                    actionButton.addEventListener("click", () => {
                        // Toggle the 'completed' status
                        const newValue = !completed;
                        // Update the 'completed' field in Firestore
                        updateDoc(doc.ref, { completed: newValue }).then(() => {
                            // Re-render the table
                            renderTable();
                        }).catch((error) => {
                            console.error("Error updating document: ", error);
                        });
                    });
                    actionCell.appendChild(actionButton);
                    row.appendChild(actionCell);

                    // Append the row to the appropriate table body
                    tableBody.appendChild(row);
                });
            });
        });
    }).catch((error) => {
        console.log("Error getting documents: ", error);
    });
}

// Call renderTable function on page load
window.onload = function () {
    checkAdmin();
    renderTable();
}
