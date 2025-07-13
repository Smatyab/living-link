// Import Firebase SDKs
import { db, collection, getDocs, getDoc,auth, doc, query, where } from "./src/FireBaseConfig.js";

function checkAdmin() {
  if (!localStorage.getItem("isloggedIn") || localStorage.getItem("isloggedIn") !== "true") {
    window.location.href = "./index.html";
  }
}

document.getElementById("logoutBtn").addEventListener("click", () => {
  logout();
})

function logout() {
  auth.signOut()
    .then(() => {
      localStorage.removeItem("isloggedIn");
      window.location.href = "./index.html";
    })
    .catch((error) => {
      console.error("Error signing out:", error);
      localStorage.removeItem("isloggedIn");
      window.location.href = "./index.html";
    });
}
const nav = document.querySelector(".toggle-btn");
nav.addEventListener("click", () => {
  document.querySelector("#sidebar").classList.toggle("expand");
});

let adminBalance=0;
let societiesBalance=0;
let membersBalance=0;

// Function to show loading spinner and backdrop
function showLoadingSpinner() {
  document.querySelector('.backdrop').style.display = 'block';
  document.querySelector('.spinner-container').style.display = 'block';
}

// Function to hide loading spinner and backdrop
function hideLoadingSpinner() {
  document.querySelector('.backdrop').style.display = 'none';
  document.querySelector('.spinner-container').style.display = 'none';
}

// Main function to fetch all data and update UI
async function fetchDataAndUpdateUI() {
  try {
    // Show loading spinner
    showLoadingSpinner();

    // Fetch data for Slide 1
    const allComplaints = await fetchAllComplaints();

    // Fetch data for Slide 2
    const allBanners = await fetchAllBanners();

    // Update Slide 1
    populateSlide1(allComplaints);

    // Update Slide 2
    populateSlide2(allBanners);

    //Update counts
    updateEventsCount();
    updateComplaintsCount(allComplaints);
    updateMeetingsCount();
    updateSecretaryCount();
    updateMembersCount();
    updateBannersCount(allBanners);
    updateNotesCount();

    // Create charts and calendar
    createComplaintsChart(allComplaints);
    createCalendar();

    //balance
    

    // Hide loading spinner after data is loaded
    hideLoadingSpinner();
  } catch (error) {
    console.error("Error fetching and updating data:", error);
    // Hide loading spinner in case of error
    hideLoadingSpinner();
  }
}


// Function to fetch all complaints
async function fetchAllComplaints() {
  const allComplaints = [];
  try {
    const societiesSnapshot = await getDocs(collection(db, "societies"));
    let amount=0;
    societiesSnapshot.forEach((doc) => {
      const currentAmount = parseInt(doc.data().currentAmount || 0);
      amount += currentAmount;
    });
    document.querySelector(".count_amount_society").innerHTML="₹ "+amount;

    for (const societyDoc of societiesSnapshot.docs) {
      const complaintsCollection = collection(societyDoc.ref, "complains");
      const allComplaintsDataRef = query(complaintsCollection,
        where("approved", "==", true)
      );

      const querySnapshot = await getDocs(allComplaintsDataRef);
      const complaints = querySnapshot.docs.map(doc => doc.data());
      allComplaints.push(...complaints);
    }
  } catch (error) {
    console.error("Error fetching complaints:", error);
  }
  return allComplaints;
}

// Function to fetch all banners
async function fetchAllBanners() {
  const allBanners = [];
  try {
    const bannerCollection = collection(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq", "banners");
    const bannersSnapshot = await getDocs(bannerCollection);
    allBanners.push(...bannersSnapshot.docs.map(doc => doc.data()));
  } catch (error) {
    console.error("Error fetching banners:", error);
  }
  return allBanners;
}

// Call the main function to fetch data and update UI when the page loads
window.addEventListener("DOMContentLoaded", () => {
  checkAdmin();
  fetchDataAndUpdateUI();
});

async function populateSlide1(allComplaints) {
  let complaintIndex = 0;

  // Filter complaints where resolved is false
  const unresolvedComplaints = allComplaints.filter(complaint => !complaint.resolved);


  // Function to update slide content
  function updateSlide() {
    if (unresolvedComplaints.length === 0) {
      console.error("No complaints found.");
      return;
    }
    const complaint = unresolvedComplaints[complaintIndex];
    const slide1Image = document.getElementById("slide1-image");
    const slide1Title = document.getElementById("slide1-title");
    const slide1Subtitle = document.getElementById("slide1-subtitle");

    // Update slide content with complaint data
    slide1Image.src = complaint.imageUrl;
    slide1Title.textContent = complaint.title;
    slide1Subtitle.textContent = "Type: " + complaint.type;

    complaintIndex = (complaintIndex + 1) % unresolvedComplaints.length; // Move to the next complaint

    if (complaintIndex === 0) {
      // If all complaints have been displayed, restart from the beginning
      shuffleArray(unresolvedComplaints); // Optional: Shuffle the complaints array to change the order
    }
  }

  // Initial update
  updateSlide();

  // Set interval to update slide every 3 seconds
  setInterval(updateSlide, 3000);
}
// Function to shuffle an array (Fisher-Yates algorithm)
function shuffleArray(array) {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
}


async function populateSlide2(allBanners) {
  let bannerIndex = 0;

  // Function to update slide content
  function updateSlide2() {
    if (allBanners.length === 0) {
      console.error("No banners found.");
      return;
    }

    const banner = allBanners[bannerIndex];
    const slide2Image = document.getElementById("slide2-image");
    const slide2Title = document.getElementById("slide2-title");
    const slide2Subtitle = document.getElementById("slide2-subtitle");

    slide2Image.src = banner.bannerImage;
    slide2Title.textContent = banner.bannerTitle;
    slide2Subtitle.textContent = "Date: " + banner.bannerAddDate;

    bannerIndex = (bannerIndex + 1) % allBanners.length; // Move to the next banner
  }

  // Initial update
  updateSlide2();

  // Set interval to update slide every 3 seconds
  setInterval(updateSlide2, 3000);
}

async function updateComplaintsCount(allComplaints) {
  const countComplaintsSpan = document.querySelector(".count-complaint");
  if (countComplaintsSpan) {
    const totalComplaintsCount = allComplaints.length;
    countComplaintsSpan.textContent = totalComplaintsCount;
  }
}

async function updateBannersCount(allBanners) {
  const countBannerSpan = document.querySelector(".count-banner");
  if (countBannerSpan) {
    const totalBannerCount ='Total Count : <span class="card-title">'+ allBanners.length +"</span>";
    countBannerSpan.innerHTML = totalBannerCount;
  }
}


// Function to get the total count of events from Firestore
async function getTotalEventsCount() {
  try {
    const eventsCollection = collection(db, "ADMIN/XqHXv5Ebd8aUXngVzSXq/events");
    const querySnapshot = await getDocs(eventsCollection);
    return querySnapshot.size; // Returns the total number of documents in the collection
  } catch (error) {
    console.error("Error getting events count: ", error);
    return 0; // Return 0 if there's an error
  }
}

// Function to update the count in the HTML
async function updateEventsCount() {
  const countEventsSpan = document.querySelector(".count-event");
  if (countEventsSpan) {
    const totalEventsCount = await getTotalEventsCount();
    countEventsSpan.textContent = totalEventsCount; // Update the count in the span
  }
}

async function getTotalMeetingsCount() {
  try {
    const meetingsCollection = collection(db, "ADMIN", "XqHXv5Ebd8aUXngVzSXq", "meetings");
    const querySnapshot = await getDocs(meetingsCollection);
    return querySnapshot.size;
  } catch (error) {
    console.error("Error getting meetings count: ", error);
    return 0;
  }
}

// Function to update the meetings count in the HTML
async function updateMeetingsCount() {
  const countMeetingsSpan = document.querySelector(".count-meeting");
  if (countMeetingsSpan) {
    const totalMeetingsCount = await getTotalMeetingsCount();
    countMeetingsSpan.textContent = totalMeetingsCount;
  }
}

// Function to get the total count of secretary from Firestore
async function getTotalSecretaryCount() {
  try {
    const secretaryCollection = collection(db, "societies");
    const querySnapshot = await getDocs(secretaryCollection);
    return querySnapshot.size;
  } catch (error) {
    console.error("Error getting secretary count: ", error);
    return 0;
  }
}

// Function to update the secretary count in the HTML
async function updateSecretaryCount() {
  const countSecretarySpan = document.querySelector(".count-secretary");
  if (countSecretarySpan) {
    const totalSecretaryCount ='Total Count : <span class="card-title">'+await getTotalSecretaryCount()+"</span>";
    countSecretarySpan.innerHTML = totalSecretaryCount;
  }
}

// Function to get the total count of members from Firestore
async function getTotalMembersCount() {
  try {
    const memberCollection = collection(db, "member");
    const querySnapshot = await getDocs(memberCollection);

    let amount=0;
    querySnapshot.forEach((doc) => {
      const currentAmount = parseInt(doc.data().currentAmount || 0);
      amount += currentAmount;
    });
    document.querySelector(".count_amount_member").innerHTML="₹ "+amount;

    return querySnapshot.size;
  } catch (error) {
    console.error("Error getting member count: ", error);
    return 0;
  }
}

// Function to update the members count in the HTML
async function updateMembersCount() {
  const countMemberSpan = document.querySelector(".count-member");
  if (countMemberSpan) {
    const totalMemberCount ='Total Count : <span class="card-title">'+ await getTotalMembersCount()+"</span>";
    countMemberSpan.innerHTML = totalMemberCount;
  }
}

// Function to get the total count of notes
function getTotalNotesCount() {
    const noteCount=JSON.parse(localStorage.getItem("savedNotes")).length;
    return noteCount;
}

// Function to update the notes count in the HTML
function updateNotesCount() {
  const countNoteSpan = document.querySelector(".count-note");
  if (countNoteSpan) {
    const totalNoteCount ='Total Count : <span class="card-title">'+ getTotalNotesCount()+"</span>";
    countNoteSpan.innerHTML = totalNoteCount;
  }
}


//charts
async function createComplaintsChart(allComplaints) {
  try {
    // Initialize counts
    let resolvedCounts = 0;
    let unresolvedCounts = 0;

    // Count complaints
    allComplaints.forEach((complaintData) => {
      if (complaintData.approved && complaintData.resolved) {
        resolvedCounts++;
      } else if (complaintData.approved && !complaintData.resolved) {
        unresolvedCounts++;
      }
    });

    // Create the line chart after counting all complaints
    createLineChart(resolvedCounts, unresolvedCounts);
  } catch (error) {
    console.error("Error creating complaints chart: ", error);
  }
}


// Function to create the line chart
function createLineChart(resolvedCounts, unresolvedCounts) {
  // Ensure the canvas is not already in use
  const existingChart = Chart.getChart("complaintsChart");
  if (existingChart) {
    existingChart.destroy();
  }

  // Create a line chart
  const ctx = document.getElementById('complaintsChart').getContext('2d');
  const complaintsChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: ['resolved', 'unresolved'],
      datasets: [{
        label: 'Complaints',
        data: [resolvedCounts, unresolvedCounts],
        backgroundColor: [
          'rgba(54, 162, 235, 0.2)', // Resolved complaints color
          'rgba(255, 99, 132, 0.2)' // Unresolved complaints color
        ],
        borderColor: [
          'rgba(54, 162, 235, 1)', // Resolved complaints border color
          'rgba(255, 99, 132, 1)' // Unresolved complaints border color
        ],
        borderWidth: 3 // Increase border width for thicker line
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}


// Function to create the calendar
function createCalendar() {
  // Get current date
  var currentDate = new Date();
  var currentMonth = currentDate.getMonth();
  var currentYear = currentDate.getFullYear();

  // Get the number of days in the current month
  var daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();

  // Get the day of the week of the first day of the month
  var firstDayOfWeek = new Date(currentYear, currentMonth, 1).getDay();

  // Reference to the table body
  var eventsBody = document.getElementById('events-body');

  // Fetch event dates from Firebase and update the calendar
  fetchEventDates(currentYear, currentMonth + 1).then(eventDates => {
    // Generate table rows and cells
    var row = document.createElement('tr');
    for (var i = 0; i < firstDayOfWeek; i++) {
      var cell = document.createElement('td');
      row.appendChild(cell);
    }
    for (var day = 1; day <= daysInMonth; day++) {
      var cell = document.createElement('td');
      cell.textContent = day;
      if (eventDates.includes(day)) {
        cell.style.backgroundColor = '#007bff';
        cell.style.color = '#ffffff';
         // Set background color to dark blue for event dates
      }
      row.appendChild(cell);
      if ((firstDayOfWeek + day - 1) % 7 === 6 || day === daysInMonth) {
        eventsBody.appendChild(row);
        row = document.createElement('tr');
      }
    }
  });



  // Reference to the table body for meetings
  var meetingsBody = document.getElementById('meetings-body');

  // Fetch meeting dates from Firebase and update the calendar
  fetchMeetingDates(currentYear, currentMonth + 1).then(meetingDates => {
    // Generate table rows and cells
    var row = document.createElement('tr');
    for (var i = 0; i < firstDayOfWeek; i++) {
      var cell = document.createElement('td');
      row.appendChild(cell);
    }
    for (var day = 1; day <= daysInMonth; day++) {
      var cell = document.createElement('td');
      cell.textContent = day;
      if (meetingDates.includes(day)) {
        cell.style.backgroundColor = '#007bff';
        cell.style.color = '#ffffff'; 
      }
      row.appendChild(cell);
      if ((firstDayOfWeek + day - 1) % 7 === 6 || day === daysInMonth) {
        meetingsBody.appendChild(row);
        row = document.createElement('tr');
      }
    }
  });

}

// Function to fetch event dates from Firebase
async function fetchEventDates(year, month) {
  const eventDates = [];
  const querySnapshot = await getDocs(collection(db, 'ADMIN/XqHXv5Ebd8aUXngVzSXq/events'));
  querySnapshot.forEach(doc => {
    const eventDateTime = new Date(doc.data().eventDatetime);
    if (eventDateTime.getFullYear() === year && eventDateTime.getMonth() + 1 === month) {
      eventDates.push(eventDateTime.getDate());
    }
  });
  return eventDates;
}

// Function to fetch meeting dates from Firebase
async function fetchMeetingDates(year, month) {
  const meetingDates = [];
  try {
    // Fetch the "ADMIN" document with a specific ID
    const adminDocRef = doc(db, 'ADMIN', 'XqHXv5Ebd8aUXngVzSXq');
    const adminDocSnapshot = await getDoc(adminDocRef);

    // Check if the "ADMIN" document exists
    if (adminDocSnapshot.exists()) {
      const adminData = adminDocSnapshot.data();
      adminBalance =await adminData.balance;
      document.querySelector(".count_amount_admin").innerHTML="₹ "+adminBalance;

      // Access the "meetings" collection under the "ADMIN" document
      const meetingsCollection = collection(adminDocRef, 'meetings');
      const querySnapshot = await getDocs(meetingsCollection);

      // Iterate over each meeting document
      querySnapshot.forEach(doc => {
        const meetingDate = doc.data().meetingDate;
        const [meetingYear, meetingMonth, meetingDay] = meetingDate.split('-').map(Number);

        // Check if the meeting date matches the specified year and month
        if (meetingYear === year && meetingMonth === month) {
          meetingDates.push(meetingDay);
        }
      });
    }
  } catch (error) {
    console.error("Error fetching meeting dates: ", error);
  }
  return meetingDates;
}