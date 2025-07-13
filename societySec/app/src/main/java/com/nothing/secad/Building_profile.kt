package com.nothing.secad

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import com.nothing.secad.databinding.ActivityBuildingProfileBinding
import com.nothing.secad.databinding.NoteLayoutBinding
import java.util.UUID

class Building_profile : AppCompatActivity() {

    private lateinit var binding: ActivityBuildingProfileBinding
    private  var  updateImgUrlDownload =""

    //start

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            binding.imageUserShow.visibility = View.GONE
            binding.updateUserProfileRoot.setBackgroundColor(Color.LTGRAY)
            binding.updateUserProfileRoot.gravity = Gravity.CENTER
            binding.spinKit.visibility = View.VISIBLE
            val fileUri = data?.data

            if (fileUri != null) {
                val imageRef = Firebase.storage.reference.child("profileImage/${UUID.randomUUID()}")

                imageRef.putFile(fileUri)
                    .addOnSuccessListener { uploadTask ->
                        uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()

                            // Update the userImage field for the user document
                            val uid = FirebaseAuth.getInstance().currentUser?.uid
                            val db = FirebaseFirestore.getInstance()
                            uid?.let { uid ->
                                val userRef = db.collection("societies").document(uid)
                                userRef.update("userImage", imageUrl)
                                    .addOnSuccessListener {

                                        println("User image updated successfully")

                                    }
                                    .addOnFailureListener { e ->
                                        // If the document does not exist or there's an error updating, create the field and set the value
                                        userRef.set(mapOf("userImage" to imageUrl))
                                            .addOnSuccessListener {
                                                println("User image field created successfully")
                                            }
                                            .addOnFailureListener { e ->
                                                println("Error creating user image field: $e")
                                            }
                                    }
                            }

                            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                            binding.imageUserShow.setImageURI(fileUri)
                            binding.updateUserProfileScroll.visibility = View.VISIBLE
                            binding.updateUserProfileRoot.gravity = Gravity.TOP
                            binding.spinKit.visibility = View.GONE
                            binding.updateUserProfileRoot.setBackgroundColor(Color.WHITE)
                            UserDataFetchImage()
                        }
                    }
                    .addOnFailureListener { e ->
                        println("Error uploading image: $e")
                        Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
                    }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    //end
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildingProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Profile"
        UserDataFetch()
        UserDataFetchImage()


        binding.imageUserShow.setOnClickListener(){
            ImagePicker.with(this)
                .crop()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
            UserDataFetchImage()

        }


    }
    fun UserDataFetch() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            db.collection("societies").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Access the fields you need
                        val societyname = document.getString("name")!!
                        val socEmail = document.getString("email")
                        val totalHouse = document.getLong("totalHouses")?.toString() // Retrieve as Long and then convert to String
                        val maintenanceTotal = "0"
//                        val socImage = document.getString("userImage")
                        val perHomeAmount = document.getLong("expectedPricePerHouse")?.toString()


                        // Update the UI on the main thread
                        runOnUiThread {
                            if ( societyname != null && socEmail != null && totalHouse != null && maintenanceTotal != null && totalHouse != null && perHomeAmount != null) {
                                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
//

                                binding.profileSocNameTxt.text =   "society Name: ${societyname}"
                                binding.profileEmailTxt.text = "society Email: ${socEmail}"
                                binding.profileHouseNoTxt.text =  "Total House: ${totalHouse}"
                                binding.totalMaintenance.text = "Maintenance Total: ${totalHouse.toInt()*perHomeAmount.toInt()}"
                                binding.perHomePayAbleAmount.text =  "Home Payable Amount: ${perHomeAmount}"
//
//                                Glide.with(this)
//                                    .load(userImage)
//                                    .placeholder(R.drawable.logo_black_primary) // Optional placeholder image while loading
//                                    .error(R.drawable.logo_black_primary) // Optional error image if loading fails
//                                    .into(binding.imageUserShow)
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

    fun UserDataFetchImage() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            db.collection("societies").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val userImage = document.getString("userImage")

                        // Update the UI on the main thread
                        runOnUiThread {
                            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
//

                            Glide.with(this)
                                .load(userImage)
                                .placeholder(R.drawable.logo_black_primary) // Optional placeholder image while loading
                                .error(R.drawable.logo_black_primary) // Optional error image if loading fails
                                .into(binding.imageUserShow)

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