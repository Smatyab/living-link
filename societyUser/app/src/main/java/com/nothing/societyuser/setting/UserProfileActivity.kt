package com.nothing.societyuser.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nothing.societyuser.R
import com.nothing.societyuser.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "User Profile"
        UserDataFetch()

        binding.btnUpdateProfileNext.setOnClickListener {
            var intent = Intent(this, updateUserProfile::class.java)
            startActivity(intent)

        }


    }

    fun UserDataFetch() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            db.collection("member").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Access the fields you need
                        val societyId = document.getString("societyId")!!
                        val userEmail = document.getString("userEmail")
                        val userHouseNo = document.getString("userHouseNo")
                        val userName = document.getString("userName")
                        val userImage = document.getString("userImage")

                        // Update the UI on the main thread
                        runOnUiThread {
                            if ( userName != null && userEmail != null && userHouseNo != null) {
                                Toast.makeText(this@UserProfileActivity, "Profile", Toast.LENGTH_SHORT).show()
//                                binding.profileSocNameTxt.text = societyId

//                                Toast.makeText(this,societyId.toString(),Toast.LENGTH_SHORT).show()

                                //start socity name fetch
//                                val uid = FirebaseAuth.getInstance().currentUser?.uid
//                                val db = FirebaseFirestore.getInstance()
                                db.collection("societies").document(societyId).get()
                                    .addOnSuccessListener { document ->
                                        if (document != null) {
                                            // Access the fields you need
                                            val societyName = document.getString("name")

                                            // Update the UI on the main thread
                                            runOnUiThread {
                                                if (societyName != null) {
                                                    binding.profileSocNameTxt.text = societyName
                                                }
                                            }
                                        } else {
                                            println("No such document")
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        println("Error getting documents: $exception")
                                    }

                                //end



                                binding.profileUserNameTxt.text = userName
                                binding.profileEmailTxt.text = userEmail
                                binding.profileHouseNoTxt.text = userHouseNo

                                Glide.with(this)
                                    .load(userImage)
                                    .placeholder(R.drawable.logo_black_primary) // Optional placeholder image while loading
                                    .error(R.drawable.logo_black_primary) // Optional error image if loading fails
                                    .into(binding.imageUserShow)
                            }
                        }
                    } else {
                        println("No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                }
        }
    }





}