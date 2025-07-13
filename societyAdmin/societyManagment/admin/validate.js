import { auth, signInWithEmailAndPassword } from "./src/FireBaseConfig.js";

var email = "admin@admin.com";
var password = "admin123";
let isloggedIn = false;

window.onload=function(){
  getCookieData();
}

//for set cookies
function setCookie() {
  var inputEmail = document.getElementById("email").value;
  var inputPassword = document.getElementById("password").value;
  var d = new Date();
  //set cookies for 24 hour
  d.setTime(d.getTime() + (24 * 60 * 60 * 1000));
  var expires = "; expires=" + d.toString();

  document.cookie = "myEmail=" + inputEmail + expires + ";path=./index.html";
  document.cookie = "myPassword=" + inputPassword + expires + ";path=./index.html";
}
document.getElementById("rm").addEventListener("click",()=>{
  setCookie();
})

//for get cookie
function getCookie(key) {
  var name = key + "=";
  var decodeCookie = decodeURIComponent(document.cookie);
  var strSplit = decodeCookie.split(';');
  for (var i = 0; i < strSplit.length; i++) {
    var val = strSplit[i];
    while (val.charAt(0) == ' ') {
      val = val.substring(1);
    }
    if (val.indexOf(name) == 0) {
      return val.substring(name.length, val.length);
    }
  }
  return "";
}


//for get cookies data in input
function getCookieData() {
  document.getElementById("email").value = getCookie("myEmail");
  document.getElementById("password").value = getCookie("myPassword");
  //console.log(document.cookie);
}

const loginForm = document.querySelector(".login");
loginForm.addEventListener("submit", (event) => {
  event.preventDefault();
  validate();
});


function validate() {
  var inputEmail = document.getElementById("email").value;
  var inputPassword = document.getElementById("password").value;

  signInWithEmailAndPassword(auth, inputEmail, inputPassword)
    .then((userCredential) => {
      const user = userCredential.user;

      // Check admin UID
      if (user.uid === "DZYTwpvpO7dMADfdpPJPwxFIweA2") {
        isloggedIn = true;
        localStorage.setItem("isloggedIn", isloggedIn);
        window.location.href = "./home.html";
        alert("Login Successful");
      } else {
        auth.signOut();
        alert("You are not authorized to access this panel.");
      }
    })
    .catch((error) => {
      const errorMessage = error.message;
      alert(errorMessage);
    });
}




// if (inputEmail == email) {
//   if (inputPassword == password) {
//     isloggedIn = true;
//     localStorage.setItem("isloggedIn", isloggedIn);
//     window.location.href = "./home.html";
//     alert("Login Succsessful");
//   } else {
//     alert("Password is Invalid");
//   }
// } else {
//   alert("Email address in Invalid");
// }