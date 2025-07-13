package com.nothing.secad.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nothing.secad.Building_profile
import com.nothing.secad.ChangePasswordActivity
import com.nothing.secad.R
import com.nothing.secad.databinding.FragmentSettingBinding
import com.nothing.secad.setting.CustomerCare
import com.nothing.secad.simple.LoginActivity
import com.nothing.secad.simple.information_activity
import com.nothing.secad.simple.welcome_signUp_login
import com.zego.ve.Log


class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserDataFetch()

        // You can interact with the views in the layout here
        binding.llChangePassword.setOnClickListener(){
            var intent = Intent(context, ChangePasswordActivity::class.java)
            startActivity(intent)

        }

        binding.llInfo.setOnClickListener(){

            var intent = Intent(context, information_activity::class.java)
            startActivity(intent)
        }
        //customer care
        binding.customerCareLl.setOnClickListener(){
            var intent = Intent(context, CustomerCare::class.java)
            startActivity(intent)
        }

        //logOut
        binding.logOutBtn.setOnClickListener() {
            val firebaseAuth = FirebaseAuth.getInstance()
            //TODO:DialogBox
            firebaseAuth.signOut()
            var intent = Intent(requireActivity(), welcome_signUp_login::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        //profile
        binding.profileSoc.setOnClickListener(){
            var intent = Intent(context, Building_profile::class.java)
            startActivity(intent)

        }
        binding.imgButtonEditProfile.setOnClickListener(){
            var intent = Intent(context, Building_profile::class.java)
            startActivity(intent)

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
                        val userName = document.getString("name")
                        val userImage = document.getString("userImage")
//                        Toast.makeText(requireContext(), userName.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("@@@","${userName}")
                        // Update the UI on the main thread
                        activity?.runOnUiThread {
                            if (userName != null && binding != null) {
                                var finalUserName:String = "Hello, "+userName
                                binding!!.socName.text = finalUserName
                                binding!!.socNamesub.text = "Don’t tell anyone, but I’m $userName ."
                                Glide.with(this)
                                    .load(userImage)
//                                    .placeholder(R.drawable.user_image_place_holder) // Optional placeholder image while loading
//                                    .error(R.drawable.user_image_place_holder) // Optional error image if loading fails
                                    .centerCrop()
                                    .into(binding!!.socImage)

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
        // Clean up any resources here
    }
}
