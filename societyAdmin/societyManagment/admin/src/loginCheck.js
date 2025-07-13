import { auth } from "./FireBaseConfig.js";

export function checkAdmin(){
    if(!localStorage.getItem("isloggedIn") || localStorage.getItem("isloggedIn")!=="true"){
      window.location.href="../index.html";
    }
  }

export function logout(){

  auth.signOut()
    .then(() => {
      localStorage.removeItem("isloggedIn");
      window.location.href = "../index.html";
    })
    .catch((error) => {
      console.error("Error signing out:", error);
      localStorage.removeItem("isloggedIn");
      window.location.href = "../index.html";
    });
}  


// onAuthStateChanged(auth, function(user) {
  //   if (!user) {
  //     window.location.href="./index.html";
  //   }
  // });



// signOut(auth).then(function() {
  //   console.log("Signed out successfully.");
  //   window.location.href = "./index.html";
  // }).catch(function(error) {
  //   console.log("An error occurred when signing out:", error);
  // });









