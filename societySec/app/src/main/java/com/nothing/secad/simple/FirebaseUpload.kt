package com.nothing.secad.simple

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.nothing.secad.HomeActivity
import com.nothing.secad.Society
import com.nothing.secad.animation_after_sign_up
import com.nothing.secad.databinding.ActivityFirebaseUploadBinding

class FirebaseUpload : AppCompatActivity() {
    private lateinit var binding: ActivityFirebaseUploadBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var auth = Firebase.auth;



        // TODO: take data from intent
        val selectedParking = intent.getStringExtra("selectedParking")
        val selectedElevator = intent.getStringExtra("selectedElevator")
        val selectedWatchman = intent.getStringExtra("selectedWatchman")
        val selectedGarden = intent.getStringExtra("selectedGarden")
        val selectedTemple = intent.getStringExtra("selectedTemple")
        val selectedWaterTank = intent.getStringExtra("selectedWaterTank")
        val selectedTotalHome = intent.getStringExtra("selectedTotalHome")

        val societyName = intent.getStringExtra("societyName")
        val emailAddress = intent.getStringExtra("emailAddress")
        val password = intent.getStringExtra("password")
        val address= intent.getStringExtra("address")

        var elevators = 0;
        if (selectedElevator != null){
            elevators = selectedElevator.toInt()
        }

        val society: Society = Society (
                    name = societyName!!,
                    email = emailAddress!!,
                    password =password!!,
                    address = address!!,
                    uid = auth.currentUser!!.uid ,
//
//             // TODO: take building
                    isBuilding = true,
//
                    parking = selectedParking!!.toInt(),
                    elevators = elevators,


                    watchMan = selectedWatchman!!.toInt(),
                    garden = selectedGarden!!.toInt(),
                    temple = selectedTemple!!.toInt(),
                    waterTank = selectedWaterTank!!.toInt(),
                    totalHouses = selectedTotalHome!!.toInt(),

                    // TODO: calculate price and add
                    expectedPricePerHouse = 1000,
                    memberIDs = ArrayList<String>(),
                    complains = ArrayList()

        )

        Log.d(TAG, "onCreate: ${society.uid}")


        // TODO: temp next activity
        val nextIntent = Intent(this, animation_after_sign_up::class.java)

        // Init database
        val db = Firebase.firestore;
        // Upload data
        db.collection("societies").document(society.uid.toString())
            .set(society)
            // Success
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                startActivity(nextIntent)
                finish()
            }
            // Failure
            .addOnFailureListener {
                e -> Log.w(TAG, "Error writing document", e)
                Toast.makeText(this@FirebaseUpload, e.toString(), Toast.LENGTH_LONG).show()

                val signUpIntent = Intent(this, SignupActivity::class.java)
                startActivity(signUpIntent)

            }

    }
}
