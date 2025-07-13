import { db } from '../src/FireBaseConfig.js';
import { collection, doc, getDocs, deleteDoc } from '../src/FireBaseConfig.js';
import {checkAdmin,logout} from "../src/loginCheck.js";


const membersCollection = collection(db, 'member');


const displayMembers = async () => {
    const membersSnapshot = await getDocs(membersCollection);
    let membersHTML = '';

    membersSnapshot.forEach(member => {
        const data = member.data();
        const userProfileImage = data.userImage ? data.userImage : "https://firebasestorage.googleapis.com/v0/b/society-agency.appspot.com/o/profileImage%2Fistockphoto-1495088043-612x612.jpg?alt=media&token=e42355d4-f250-49c4-9f36-72d0036472f9";
        const userHouseNo = data.userHouseNo ? data.userHouseNo : "-"; // Display "-" if house number is undefined

        membersHTML += `
            <tr>
                <td><img src="${userProfileImage}" onerror="this.src='https://firebasestorage.googleapis.com/v0/b/society-agency.appspot.com/o/profileImage%2Fistockphoto-1495088043-612x612.jpg?alt=media&token=e42355d4-f250-49c4-9f36-72d0036472f9'" style="border-radius: 50%; width: 50px; height: 50px;"></td>
                <td>${data.userName}</td>
                <td>${data.userEmail}</td>
                <td>${userHouseNo}</td>
                <td><button class="btn btn-secondary deactivateMember" data-id="${member.id}">Deactivate</button></td>
            </tr>
        `;
    });

    document.getElementById('memberTableBody').innerHTML = membersHTML;
};

window.onload = function () {
    checkAdmin();
    displayMembers();
}

document.getElementById('memberTableBody').addEventListener('click', async (e) => {
    const target = e.target;

    if (target.classList.contains('deactivateMember')) {
        const memberId = target.dataset.id;
        await deleteDoc(doc(db, 'member', memberId));
        alert("Member deactivated successfully");
        displayMembers();
    }
});




const nav = document.querySelector(".toggle-btn");
nav.addEventListener("click", () => {
    document.querySelector("#sidebar").classList.toggle("expand");
});

window.onload = function () {
    checkAdmin();
    displayMembers();
}