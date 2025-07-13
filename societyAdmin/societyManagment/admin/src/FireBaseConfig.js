// // Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.7.2/firebase-app.js";
import { getFirestore, collection, doc, addDoc, getDocs, query,where,getDoc, updateDoc, deleteDoc, onSnapshot,serverTimestamp,setDoc } from "https://www.gstatic.com/firebasejs/10.7.2/firebase-firestore.js";
import { getStorage, ref, uploadBytesResumable, getDownloadURL, deleteObject} from "https://www.gstatic.com/firebasejs/10.7.2/firebase-storage.js";
import { getAuth,signInWithEmailAndPassword,signOut,onAuthStateChanged,sendPasswordResetEmail } from "https://www.gstatic.com/firebasejs/10.7.2/firebase-auth.js";

// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyATRFlnPJp_Jvk14Tv92RiHu41GkHuawuY",
  authDomain: "society-agency.firebaseapp.com",
  projectId: "society-agency",
  storageBucket: "society-agency.appspot.com",
  messagingSenderId: "1087023213756",
  appId: "1:1087023213756:web:e104ac707438140a35f4b9",
  measurementId: "G-SLXV8GXCDN"
};


// Initialize Firebase
const app = initializeApp(firebaseConfig);
const db=getFirestore(app);
const auth = getAuth();

const fieldValue = db.FieldValue;

export {db, collection, doc, addDoc, getDocs, query, where,getDoc, updateDoc, deleteDoc,onSnapshot,serverTimestamp,setDoc};
export{getStorage, ref, uploadBytesResumable, getDownloadURL, deleteObject};
export {auth,onAuthStateChanged,signInWithEmailAndPassword,signOut,sendPasswordResetEmail};

