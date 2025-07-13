package com.nothing.societyuser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.nothing.societyuser.databinding.ActivityUpdateUserProfileBinding
import com.nothing.societyuser.databinding.FragmentSettingBinding
import com.nothing.societyuser.setting.ChangePasswordActivity
import com.nothing.societyuser.setting.UserProfileActivity
import com.nothing.societyuser.setting.cutomer_care
import com.nothing.societyuser.setting.informationAcctivity
import com.nothing.societyuser.setting.updateUserProfile
import com.nothing.societyuser.welcome.LoginActivity

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserDataFetch()

        // Your initialization logic goes here
        binding.llChangePassword.setOnClickListener(){

            val intent = Intent(requireActivity(), ChangePasswordActivity::class.java)
            startActivity(intent)

        }
        binding.llCustomerCare.setOnClickListener(){
            //TODO:activity name change
            val intent = Intent(requireActivity(), cutomer_care::class.java)
            startActivity(intent)
        }

        binding.editProfileExtraBtn.setOnClickListener(){
            val intent = Intent(requireActivity(), updateUserProfile::class.java)
            startActivity(intent)
        }

        binding.notification.setOnClickListener(){
            Toast.makeText(requireContext(), "tmp not available Notifications", Toast.LENGTH_SHORT).show()
        }

        binding.llInformation.setOnClickListener(){
            val intent = Intent(requireActivity(), informationAcctivity::class.java)
            startActivity(intent)
        }

        binding.llProfileAccount.setOnClickListener(){
            val intent = Intent(requireActivity(), UserProfileActivity::class.java)
            startActivity(intent)
        }

        //logOut
        binding.logOutBtn.setOnClickListener() {
            val firebaseAuth = FirebaseAuth.getInstance()
            //TODO:DialogBox
            firebaseAuth.signOut()
            var intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }



    }

    //data fetch to firebase
    fun UserDataFetch() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            db.collection("member").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Access the fields you need
                        val userName = document.getString("userName")
                        val userImage = document.getString("userImage")

                        // Update the UI on the main thread
                        activity?.runOnUiThread {
                            if (userName != null && binding != null) {
                                var finalUserName:String = "Hello, "+userName
                                binding!!.userProfileTxtSetting.text = finalUserName
                                binding!!.attitudeUserName.text =
                                    "Don’t tell anyone, but I’m $userName ."
                                Glide.with(this)
                                    .load(userImage)
                                    .placeholder(R.drawable.user_image_place_holder) // Optional placeholder image while loading
                                    .error(R.drawable.user_image_place_holder) // Optional error image if loading fails
                                    .centerCrop()
                                    .into(binding!!.userProfileImageSetting)

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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
