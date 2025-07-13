package com.nothing.societyuser

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.nothing.societyuser.Model.complainModel
import com.nothing.societyuser.complain.ComplainRaiseHistory
import com.nothing.societyuser.databinding.FragmentComplainBinding
import java.util.UUID

class ComplainFragment : Fragment() {

    private var _binding: FragmentComplainBinding? = null
    private val binding get() = _binding!!
    private var selectedComplaintType: String? = null
    private var societyNameSend:String? = null

    private val complainModel = complainModel()

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                // Image Uri will not be null for RESULT_OK
                val fileUri = data?.data ?: return@registerForActivityResult
                uploadImage(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), "Error: ${ImagePicker.getError(data)}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Image empty, try again", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComplainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserDataFetch()
        // Select image
        binding.complainImage.setOnClickListener {
            ImagePicker.with(requireActivity())
                .crop()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        // Handle history button click
        binding.complainHistoryBtn.setOnClickListener {
            intentFun(ComplainRaiseHistory::class.java)
        }

        // Handle submit button click
        binding.submitComplainBtn.setOnClickListener {
            selectedComplaintType = binding.complainSelectSpinnerBtn.text.toString()

            if (selectedComplaintType.isNullOrEmpty() || selectedComplaintType == "--select no of--") {
                binding.complainSelectSpinnerBtn.error = "Please select Complain Type."
                return@setOnClickListener
            }

            if (binding.editTextComplainTitle.text.isNullOrBlank()) {
                binding.editTextComplainTitle.error = "Title is required."
                return@setOnClickListener
            }

            if (binding.editTextIssueDescription.text.isNullOrBlank()) {
                binding.editTextIssueDescription.error = "Description is required."
                return@setOnClickListener
            }

            if (complainModel.imageUrl.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please select Complain Image.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            submitComplain()
        }
    }

    private fun uploadImage(fileUri: Uri) {
        binding.complainImageLl.visibility = View.GONE
        binding.complainInfoLl.visibility = View.GONE
        binding.submitComplainBtn.visibility = View.GONE
        binding.complainRootLl.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_white))
        binding.spinKit.visibility = View.VISIBLE

        Firebase.storage.reference.child("complainImage/${UUID.randomUUID()}")
            .putFile(fileUri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    complainModel.imageUrl = uri.toString()
                    displayUploadedImage(fileUri)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error uploading image: ${it.message}", Toast.LENGTH_SHORT).show()
                resetUI()
            }
    }

    private fun displayUploadedImage(fileUri: Uri) {
        binding.complainImageLl.visibility = View.VISIBLE
        binding.complainInfoLl.visibility = View.VISIBLE
        binding.submitComplainBtn.visibility = View.VISIBLE
        binding.spinKit.visibility = View.GONE
        binding.complainRootLl.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.complainImage.setImageURI(fileUri)
    }

    private fun submitComplain() {
        complainModel.type = selectedComplaintType ?: "Default value or handle null case"
        complainModel.title = binding.editTextComplainTitle.text.toString()
        complainModel.description = binding.editTextIssueDescription.text.toString()
        complainModel.societyName = societyNameSend.toString()
        complainModel.upload(requireContext())
        toastFun("Complaint submitted successfully.")
        intentFun(Complain_success::class.java)

        binding.editTextComplainTitle.text.clear()
        binding.editTextIssueDescription.text.clear()
        binding.complainSelectSpinnerBtn.selectItemByIndex(0)
    }

    // Intent function
    private fun intentFun(destination: Class<*>) {
        val intent = Intent(requireContext(), destination)
        startActivity(intent)
    }

    // Toast function
    private fun toastFun(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun resetUI() {
        binding.complainImageLl.visibility = View.VISIBLE
        binding.complainInfoLl.visibility = View.VISIBLE
        binding.submitComplainBtn.visibility = View.VISIBLE
        binding.spinKit.visibility = View.GONE
        binding.complainRootLl.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    //start
    fun UserDataFetch() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            db.collection("member").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Access the fields you need
                        val societyIdFetch = document.getString("societyId")!!
                        db.collection("societies").document(societyIdFetch).get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    // Access the fields you need
                                    val societyName = document.getString("name")
//                                    Toast.makeText(
//                                        requireContext(),
//                                        "Society Name: $societyName",
//                                        Toast.LENGTH_SHORT
//                                    ).show()

                                    // Update the UI on the main thread
                                        activity?.runOnUiThread {
                                        if (societyName != null) {
                                            // Access views using the fragment's layout
                                            // For example:
                                            // val profileSocNameTxt = fragment.requireView().findViewById<TextView>(R.id.profileSocNameTxt)
                                            // profileSocNameTxt.text = societyName
                                            societyNameSend = societyName
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

                    }
                }
        } else {
            println("No such document")
        }
    }

    //end



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
