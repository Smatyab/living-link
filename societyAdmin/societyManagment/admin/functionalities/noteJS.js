import { checkAdmin, logout } from "../src/loginCheck.js";

const nav = document.querySelector(".toggle-btn");
nav.addEventListener("click", () => {
  document.querySelector("#sidebar").classList.toggle("expand");
});


// Initialize an empty array to store notes
let notes = [];

// Function to add a new note
function addNote(title, note) {
  // Create a new note object
  const newNote = {
    title,
    note,
    id: Date.now(), // Generate a unique ID for the note
  };

  // Add the new note to the notes array
  notes.push(newNote);

  // Save the notes in local storage
  localStorage.setItem("savedNotes", JSON.stringify(notes));

  // Display the saved notes
  displayNotes();
}






function displayNotes() {
  const listContainer = document.querySelector(".list-group");

  // Clear the list container before adding new notes
  listContainer.innerHTML = "";

  // Check if there are any saved notes
  if (notes.length === 0) {
    listContainer.innerHTML = "<p class='text-muted'>No saved notes yet.</p>";
    return;
  }

  // Loop through the notes array and create list items for each note
  notes.forEach((note) => {
    const listItem = document.createElement("div");
    listItem.classList.add("list-group-item", "note-item", "border");

    // Create a div element to hold the note title and delete button
    const noteInfo = document.createElement("div");
    noteInfo.classList.add("d-flex", "align-items-center");

    // Create a span element to display the note title
    const noteTitle = document.createElement("h5");
    noteTitle.classList.add("note-title", "mb-0","flex-grow-1");
    noteTitle.textContent = note.title;

    // Create a button element to delete the note
    const deleteBtn = document.createElement("button");
    deleteBtn.classList.add("btn", "btn-delete", "btn-danger");
    deleteBtn.textContent = "Delete";
    deleteBtn.addEventListener("click", () => deleteNote(note.id));

    // Create a button element for updating the note
    const updateBtn = document.createElement("button");
    updateBtn.classList.add("btn", "btn-update", "btn-secondary", "mx-2");
    updateBtn.textContent = "Update";
    updateBtn.addEventListener("click", () => populateFormForUpdate(note.id));

    // Add the update button before the delete button in the note info div
    noteInfo.appendChild(noteTitle);
    noteInfo.appendChild(updateBtn);
    noteInfo.appendChild(deleteBtn);


    // Create a paragraph element to display the note content
    const noteContent = document.createElement("p");
    noteContent.classList.add("note-content", "mb-0");

    // Replace newline characters with <br> tags to preserve new lines
    const formattedContent = note.note.replace(/\n/g, "<br>");
    noteContent.innerHTML = formattedContent;

    // Add the note info and note content to the list item
    listItem.appendChild(noteInfo);
    listItem.appendChild(noteContent);

    // Add the list item to the list container
    listContainer.appendChild(listItem);
  });
}

// Function to delete a note
function deleteNote(id) {
  // Find the index of the note to be deleted
  const noteIndex = notes.findIndex((note) => note.id === id);

  if (noteIndex !== -1) {
    // Remove the note from the notes array
    notes.splice(noteIndex, 1);

    // Save the updated notes in local storage
    localStorage.setItem("savedNotes", JSON.stringify(notes));

    // Display the updated list of notes
    displayNotes();
  }
}

// Load saved notes from local storage on page load
window.addEventListener("DOMContentLoaded", () => {
  checkAdmin();
  const savedNotes = localStorage.getItem("savedNotes");
  if (savedNotes) {
    notes = JSON.parse(savedNotes);
  }
  displayNotes();
});

// Get references to the form elements
const titleInput = document.getElementById("title");
const noteInput = document.getElementById("note");
const addNoteBtn = document.getElementById("addNoteBtn");

// Add event listener to the add note button
addNoteBtn.addEventListener("click", handleAddNote);

// Function to populate form fields with note data for updating
function populateFormForUpdate(id) {
  // Find the note with the given id
  const noteToUpdate = notes.find((note) => note.id === id);

  // Populate the form fields with the note data
  titleInput.value = noteToUpdate.title;
  noteInput.value = noteToUpdate.note;

  // Change the button text to "Update Note"
  addNoteBtn.textContent = "Update Note";

  // Remove the existing event listener from the "Update Note" button
  addNoteBtn.removeEventListener("click", handleAddNote);

  // Add a new event listener for updating the note
  addNoteBtn.addEventListener("click", () => handleUpdateNote(id));
}

// Function to handle updating the note
function handleUpdateNote(id) {
  const title = titleInput.value.trim();
  const note = noteInput.value.trim();

  // Find the index of the note to be updated
  const noteIndex = notes.findIndex((note) => note.id === id);

  if (noteIndex !== -1) {
    // Update the note with new data
    notes[noteIndex].title = title;
    notes[noteIndex].note = note;

    // Save the updated notes in local storage
    localStorage.setItem("savedNotes", JSON.stringify(notes));
    alert("note updated Successfully...");

    // Display the updated list of notes
    displayNotes();

    // Reset form fields and button text
    titleInput.value = "";
    noteInput.value = "";
    addNoteBtn.textContent = "Add Note";

    // Reattach the event listener for adding a new note
    addNoteBtn.removeEventListener("click", handleUpdateNote);
    addNoteBtn.addEventListener("click", handleAddNote);
  }
}

// Function to handle adding a new note
function handleAddNote(event) {
  event.preventDefault(); // Prevent default form submission behavior

  const title = titleInput.value.trim();
  const note = noteInput.value.trim();

  // Check if both title and note are filled
  if (title && note) {
    addNote(title, note);
    titleInput.value = "";
    noteInput.value = "";
  } else {
    alert("Please enter both title and note!");
  }
}

document.querySelector(".logout").addEventListener("click", () => {
  logout();
})