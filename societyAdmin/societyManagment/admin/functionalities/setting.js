import { auth, sendPasswordResetEmail } from '../src/FireBaseConfig.js';
import { checkAdmin, logout } from "../src/loginCheck.js";

const nav = document.querySelector(".toggle-btn");
nav.addEventListener("click", () => {
  document.querySelector("#sidebar").classList.toggle("expand");
});

window.onload = function () {
  checkAdmin();
}

document.querySelector(".logout").addEventListener("click", () => {
  logout();
})

document.getElementById('forgetPasswordBtn').addEventListener('click', () => {

  // Send password reset email
  sendPasswordResetEmail(auth, "livinglink01@gmail.com")
    .then(() => {
      console.log(auth);
      // Password reset email sent successfully
      alert("Password reset email sent successfully!");
    })
    .catch((error) => {
      // Handle errors
      console.error("Error sending password reset email:", error.message);
      alert("Error sending password reset email. Please try again later.");
    });
});