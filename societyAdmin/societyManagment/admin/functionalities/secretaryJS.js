// import { db } from '../src/FireBaseConfig.js';
// import { collection, doc, getDocs, deleteDoc } from '../src/FireBaseConfig.js';
// import {checkAdmin,logout} from "../src/loginCheck.js";

// const nav = document.querySelector(".toggle-btn");
// nav.addEventListener("click",()=>{
//     document.querySelector("#sidebar").classList.toggle("expand");
// });


// window.onload=function(){
//     checkAdmin();
//     displaySecretaries();
// }


// const secretariesCollection = collection(db, 'secretaries');

// const displaySecretaries = async () => {
//     const secretariesSnapshot = await getDocs(secretariesCollection);
//     let secretariesHTML = '';

//     secretariesSnapshot.forEach(secretary => {
//         const data = secretary.data();
//         secretariesHTML += `
//             <tr>
//                 <td>${data.secretaryName}</td>
//                 <td>${data.secretaryEmail}</td>
//                 <td>${data.secretaryContactNo}</td>
//                 <td>${data.secretarySocietyName}</td>
//                 <td>${data.societyAddress}</td>
//                 <td><button class="btn btn-secondary deleteSecretary" data-id="${secretary.id}">Deactivate</button></td>
//             </tr>
//         `;
//     });
//     //<td><button class="btn btn-success chatSecretary" data-id="${secretary.id}">Message</button></td>
//     document.getElementById('secretaryTableBody').innerHTML = secretariesHTML;
// };

// document.getElementById('secretaryTableBody').addEventListener('click', async (e) => {
//     const target = e.target;

//     if (target.classList.contains('chatSecretary')) {
//         // chat with secretary logic here
//     } 
//     else if (target.classList.contains('deleteSecretary')) {
//         const secretaryId = target.dataset.id;
//         await deleteDoc(doc(db, 'secretaries', secretaryId));
//         alert("secretary deactivated successfully");
//         displaySecretaries();
//     }
// });


// document.querySelector(".logout").addEventListener("click",()=>{
//     logout();
//   })

import { db } from '../src/FireBaseConfig.js';
import { collection, doc, getDocs, deleteDoc, addDoc, serverTimestamp, getDoc } from '../src/FireBaseConfig.js';
import { checkAdmin, logout } from "../src/loginCheck.js";

const nav = document.querySelector(".toggle-btn");
nav.addEventListener("click", () => {
    document.querySelector("#sidebar").classList.toggle("expand");
});

window.onload = function () {
    checkAdmin();
    displaySocieties();
}

const societiesCollection = collection(db, 'societies');

const displaySocieties = async () => {
    const societiesSnapshot = await getDocs(societiesCollection);
    let societiesHTML = '';

    societiesSnapshot.forEach(society => {
        const data = society.data();
        const isChecked = data.isBuilding ? 'checked' : '';
        const totalMembers = data.memberIDs ? data.memberIDs.length : 0; // Check if membersIds exists
        societiesHTML += `
            <tr>
                <td>${data.name}</td>
                <td>${data.email}</td>
                <td>${data.expectedPricePerHouse}</td>
                <td>${data.totalHouses}</td>
                <td><input type="checkbox" class="form-check-input" ${isChecked} disabled></td>
                <td>${totalMembers}</td>
                <td><button class="btn btn-secondary deactivateSociety" data-id="${society.id}">Deactivate</button></td>
            </tr>
        `;
    });

    document.getElementById('societyTableBody').innerHTML = societiesHTML;
};

// document.getElementById('societyTableBody').addEventListener('click', async (e) => {
//     const target = e.target;
//     if (target.classList.contains('deactivateSociety')) {
//         const societyId = target.dataset.id;
//         await deleteDoc(doc(db, 'societies', societyId));
//         alert("Society deactivated successfully");
//         displaySocieties();
//     }
// });

//deleting
// Function to delete a society and its associated member documents
const deleteSociety = async (societyId) => {
    // Fetch the society document
    const societyDoc = doc(db, 'societies', societyId);
    const societySnapshot = await getDoc(societyDoc);
    const societyData = societySnapshot.data();

    // Check if there are any members associated with the society
    if (societyData.memberIDs && societyData.memberIDs.length > 0) {
        // Iterate over member IDs and delete each member document
        for (const memberId of societyData.memberIDs) {
            const memberDoc = doc(db, 'member', memberId);
            await deleteDoc(memberDoc);
        }
    }

    // Delete the society document
    await deleteDoc(societyDoc);
};

// Event listener for deactivating a society
document.getElementById('societyTableBody').addEventListener('click', async (e) => {
    const target = e.target;
    if (target.classList.contains('deactivateSociety')) {
        const societyId = target.dataset.id;

        // Call the deleteSociety function to delete the society and its members
        await deleteSociety(societyId);

        alert("Society and its members have been deactivated successfully");
        displaySocieties();
    }
});




// document.getElementById('societyTableBody').addEventListener('click', async (e) => {
//     const target = e.target;
//     if (target.classList.contains('deactivateSociety')) {
//         const societyId = target.dataset.id;

//         // Fetch the society document
//         const societyDoc = doc(db, 'societies', societyId);
//         const societySnapshot = await getDoc(societyDoc);
//         const societyData = societySnapshot.data();

//         // Check if there are any members associated with the society
//         if (societyData.memberIDs && societyData.memberIDs.length > 0) {
//             // Loop through the memberIDs and delete each member document
//             societyData.memberIDs.forEach(async (memberId) => {
//                 const memberDoc = doc(db, 'members', memberId);
//                 console.log(memberDoc);
//                 await deleteDoc(memberDoc);
//             });
//         }

//         // Delete the society document
//         //await deleteDoc(societyDoc);

//         alert("Society and its members have been deactivated successfully");
//         displaySocieties();
//     }
// });


const btnSendPaymentRequest = document.getElementById('btnSendPaymentRequest');
document.getElementById('btnSendPaymentRequest').addEventListener('click', () => {
    btnClick();
});


const btnClick = async () => {
    const societiesCollectionRef = collection(db, "societies");
    const querySnapshot = await getDocs(societiesCollectionRef);
    querySnapshot.forEach(async (doc) => {
        const societyData = doc.data();
        const totalHouses = societyData.totalHouses;
        const expectedPricePerHouse = societyData.expectedPricePerHouse;
        const societyName = societyData.name;
        const societyUid = societyData.uid;

        const amount = totalHouses * expectedPricePerHouse;
        const id = new Date().getTime();
        const date = serverTimestamp();
        const completed = false;

        // Add a new document to the "transactions" collection within the current society
        try {
            //console.log(date);
            await addDoc(collection(doc.ref, 'transactions'), {
                amount: amount,
                date: date,
                id: id,
                completed: completed,
                societyName: societyName,
                societyId: societyUid
            });
        } catch (error) {
            console.error("Error adding transaction:", error);
        }
    });
    alert("Transaction Request Send Successfully...");
};


document.querySelector(".logout").addEventListener("click", () => {
    logout();
})

