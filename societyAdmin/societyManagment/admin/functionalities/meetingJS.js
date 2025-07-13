import { db } from '../src/FireBaseConfig.js';
import { collection, doc, addDoc, getDocs, getDoc, updateDoc, deleteDoc, } from '../src/FireBaseConfig.js';
import { checkAdmin, logout } from "../src/loginCheck.js";

const nav = document.querySelector(".toggle-btn");
nav.addEventListener("click", () => {
  document.querySelector("#sidebar").classList.toggle("expand");
});


// Fetch and display scheduled meetings on page load
window.addEventListener("load", async () => {
  checkAdmin();
  displayScheduledMeetings();
});


// Function to fetch and display scheduled meetings
async function displayScheduledMeetings() {

  try {
    const adminDocRef = doc(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq");
    const querySnapshot = await getDocs(collection(adminDocRef, "meetings"));
    const meetingsDiv = document.getElementById("scheduledMeetings");

    // Clear existing content
    meetingsDiv.innerHTML = "";

    querySnapshot.forEach((res) => {
      const meeting = res.data();
      const meetingID = res.id;

      // Convert the meeting date and time to a Date object
      const meetingDate = new Date(meeting.meetingDate);
      const meetingTime = new Date(meeting.meetingTime);

      // Set the meeting time to the local time zone
      meetingTime.setMinutes(meetingTime.getMinutes() - meetingTime.getTimezoneOffset());

      // Check if the meeting is in the past
      if (meetingDate < new Date() || (meetingDate === new Date() && meetingTime < new Date())) {
        // If the meeting is in the past, delete it
        deleteDoc(doc(adminDocRef, "meetings", meetingID));
      }

      const meetingInfo = createMeetingInfoElement(meeting, meetingID);
      meetingsDiv.appendChild(meetingInfo);
    });
  } catch (error) {
    console.log("Error fetching meetings: ", error);
  }
}


function createMeetingInfoElement(meeting, meetingID) {
  const meetingInfo = document.createElement("div");
  meetingInfo.classList.add("meeting-info");
  meetingInfo.classList.add("mx-5");
  meetingInfo.classList.add("shadow-lg");
  meetingInfo.classList.add("bg-body-tertiary");

  const topic = document.createElement("h2");
  topic.textContent = `Topic: ${meeting.meetingTopic}`;
  meetingInfo.appendChild(topic);

  const description = document.createElement("p");
  description.textContent = `Description: ${meeting.meetingDescription}`;
  meetingInfo.appendChild(description);

  const dateTime = document.createElement("p");
  dateTime.textContent = `Date: ${meeting.meetingDate} | Time: ${meeting.meetingTime}`;
  meetingInfo.appendChild(dateTime);

  const duration = document.createElement("p");
  duration.textContent = `Duration: ${meeting.meetingDuration} minutes`;
  meetingInfo.appendChild(duration);

  const link = document.createElement("p");
  link.textContent = `Link: ${meeting.meetingLink}`;
  meetingInfo.appendChild(link);

  const passCode = document.createElement("p");
  passCode.textContent = `Passcode: ${meeting.meetingPassCode}`;
  meetingInfo.appendChild(passCode);

  const actionBtnDiv = document.createElement("div");
  actionBtnDiv.classList.add("action-btn-div");

  const updateBtn = document.createElement("button");
  updateBtn.textContent = "Update";
  updateBtn.classList.add("update-btn");
  updateBtn.classList.add("mx-3");
  updateBtn.classList.add("my-2");
  updateBtn.classList.add("btn");
  updateBtn.classList.add("btn-dark");
  updateBtn.classList.add("rounded-pill");
  updateBtn.addEventListener("click", () => updateMeeting(meetingID, meeting));
  actionBtnDiv.appendChild(updateBtn);

  const deleteBtn = document.createElement("button");
  deleteBtn.textContent = "Delete";
  deleteBtn.classList.add("delete-btn");
  deleteBtn.classList.add("mx-3");
  deleteBtn.classList.add("my-2");
  deleteBtn.classList.add("btn");
  deleteBtn.classList.add("btn-secondary");
  deleteBtn.classList.add("rounded-pill");
  deleteBtn.addEventListener("click", () => deleteMeeting(meetingID));
  actionBtnDiv.appendChild(deleteBtn);

  meetingInfo.appendChild(actionBtnDiv);

  return meetingInfo;
}

// // Function to add a new meeting
// Function to add a new meeting
async function addMeetingToFirestore(meetingData) {
  const adminDocRef = doc(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq");

  // Extract date and time from meetingData
  const meetingDate = meetingData.meetingDate;
  const meetingTime = meetingData.meetingTime;

  // Combine date and time strings into a single datetime string
  const meetingDateTimeString = `${meetingDate}T${meetingTime}`;
  
  // Check if the meeting datetime is in the past
  const meetingDateTime = new Date(meetingDateTimeString);
  const currentDateTime = new Date();
  if (meetingDateTime < currentDateTime) {
    alert("Meeting date and time cannot be in the past.");
    return;
  }

  // Check if there's already a meeting scheduled at the inserted date and time
  const querySnapshot = await getDocs(collection(adminDocRef, "meetings"));
  const existingMeetings = querySnapshot.docs.filter(doc => {
    const existingMeetingData = doc.data();
    const existingMeetingDate = existingMeetingData.meetingDate;
    const existingMeetingTime = existingMeetingData.meetingTime;
    const existingMeetingDateTimeString = `${existingMeetingDate}T${existingMeetingTime}`;
    const existingMeetingDateTime = new Date(existingMeetingDateTimeString);
    return existingMeetingDateTime.getTime() === meetingDateTime.getTime();
  });

  if (existingMeetings.length > 0) {
    alert("Another meeting is already scheduled at the same date and time.");
    return;
  }

  try {
    // If validations pass, add the meeting
    await addDoc(collection(adminDocRef, "meetings"), meetingData);
    alert("Meeting Scheduled Successfully! ");
    location.reload();
  } catch (error) {
    alert("Some error occurred: " + error);
  }
}


// async function addMeetingToFirestore(meetingData) {
//   const adminDocRef = doc(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq");
//   await addDoc(collection(adminDocRef, "meetings"), meetingData);
  
// }

let meetingTopic = document.querySelector("#meetingTopic");
let meetingDescription = document.querySelector("#meetingDescription");
let date = document.querySelector("#date");
let time = document.querySelector("#time");
let duration = document.querySelector("#duration");
let meetingLink = document.querySelector("#meetingLink");
let meetingPassCode = document.querySelector("#meetingPassCode");

let submitBtn = document.querySelector("#submitBtn");
submitBtn.dataset.action = "add";

submitBtn.addEventListener("click", async function (ev) {
  ev.preventDefault();
  console.log("clicked");
  if (submitBtn.dataset.action === "add") {
    const meetingData = {
      meetingTopic: meetingTopic.value,
      meetingDescription: meetingDescription.value,
      meetingDate: date.value,
      meetingTime: time.value,
      meetingDuration: duration.value,
      meetingLink: meetingLink.value,
      meetingPassCode: meetingPassCode.value,
    };

    await addMeetingToFirestore(meetingData)
      .then(() => {
        location.reload();
      })
      .catch((error) => {
        alert("Some error occurred: " + error);
      });
  } else if (submitBtn.dataset.action === "update") {
    console.log("update");
    // Your existing update meeting logic here
    await updateDoc(doc(db, "meetings", submitBtn.dataset.meetingId), {
      meetingTopic: meetingTopic.value,
      meetingDescription: meetingDescription.value,
      meetingDate: date.value,
      meetingTime: time.value,
      meetingDuration: duration.value,
      meetingLink: meetingLink.value,
      meetingPassCode: meetingPassCode.value,
    })
      .then(() => {
        alert("Meeting Updated Successfully! ");
        location.reload();
      })
      .catch((error) => {
        alert("Some error occurred: " + error);
      });

  }
});


async function updateMeeting(meetingID, meeting) {

  let submitBtn = document.querySelector("#submitBtn");
  submitBtn.textContent = "Update Meeting";
  submitBtn.dataset.action = "update";
  meetingTopic.value = meeting.meetingTopic;
  meetingDescription.value = meeting.meetingDescription;
  date.value = meeting.meetingDate;
  time.value = meeting.meetingTime;
  duration.value = meeting.meetingDuration;
  meetingLink.value = meeting.meetingLink;
  meetingPassCode.value = meeting.meetingPassCode;
  submitBtn.addEventListener("click", async (ev) => {
    ev.preventDefault();

    try {
      const adminDocRef = doc(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq");
      const meetingRef = doc(adminDocRef, "meetings", meetingID);

      await updateDoc(meetingRef, {
        meetingTopic: meetingTopic.value,
        meetingDescription: meetingDescription.value,
        meetingDate: date.value,
        meetingTime: time.value,
        meetingDuration: duration.value,
        meetingLink: meetingLink.value,
        meetingPassCode: meetingPassCode.value,
      });

      alert("Meeting updated successfully!");
      location.reload();
    } catch (error) {
      alert("Some error occurred: " + error);
      console.error("Error updating meeting:", error);
    }

  });
}


async function deleteMeeting(meetingID) {
  try {
    alert("Meeting Deleted Successfully..");
    const meetingRef = doc(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq", "meetings", meetingID);
    await deleteDoc(meetingRef);
    displayScheduledMeetings();
  } catch (error) {
    console.error("Error deleting meeting:", error);
    alert("An error occurred while deleting the meeting.");
  }
}

document.querySelector(".logout").addEventListener("click", () => {
  logout();
})