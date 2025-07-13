package com.nothing.societyuser.setting

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nothing.societyuser.R
import com.nothing.societyuser.databinding.ActivityChangePasswordBinding
import com.nothing.societyuser.databinding.FragmentHomeBinding
import com.nothing.societyuser.welcome.LoginActivity

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var auth: FirebaseAuth
    var passwordVisibleNew:Boolean = false;
    var passwordVisibleOld:Boolean = false;
    var passwordVisibleCon:Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Change Password"

        auth = FirebaseAuth.getInstance()
        binding.btnChangePassword.setOnClickListener {
            changePassword()
            finish()
        }


        //new password show
        binding.edtCurrentPassword.setOnTouchListener { v, event ->

            val Right = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.edtCurrentPassword.right - binding.edtCurrentPassword.compoundDrawables[Right].bounds.width()) {
                    val selection = binding.edtCurrentPassword.selectionEnd
                    if (passwordVisibleNew) {
                        binding.edtCurrentPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                        binding.edtCurrentPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        passwordVisibleNew = false
                    } else {
                        binding.edtCurrentPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                        binding.edtCurrentPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        passwordVisibleNew = true
                    }
                    binding.edtCurrentPassword.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false // Consume the touch event
        }

        //old password show
        binding.edtNewPassword.setOnTouchListener { v, event ->

            val Right = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.edtNewPassword.right - binding.edtNewPassword.compoundDrawables[Right].bounds.width()) {
                    val selection = binding.edtNewPassword.selectionEnd
                    if (passwordVisibleOld) {
                        binding.edtNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                        binding.edtNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        passwordVisibleOld = false
                    } else {
                        binding.edtNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                        binding.edtNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        passwordVisibleOld = true
                    }
                    binding.edtNewPassword.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false // Consume the touch event
        }

        //confirm password show
        binding.edtConformPassword.setOnTouchListener { v, event ->

            val Right = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.edtConformPassword.right - binding.edtConformPassword.compoundDrawables[Right].bounds.width()) {
                    val selection = binding.edtConformPassword.selectionEnd
                    if (passwordVisibleCon) {
                        binding.edtConformPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                        binding.edtConformPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        passwordVisibleCon = false
                    } else {
                        binding.edtConformPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                        binding.edtConformPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        passwordVisibleCon = true
                    }
                    binding.edtConformPassword.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false // Consume the touch event
        }


    }
    private fun changePassword() {
        val oldPassword = binding.edtCurrentPassword.text.toString()
        val newPassword = binding.edtNewPassword.text.toString()
        val confirmPassword = binding.edtConformPassword.text.toString()

        if (oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if(newPassword.equals(confirmPassword)){
                var user: FirebaseUser? = auth.currentUser
                if (user != null && user.email != null){

                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, oldPassword)

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Re-Authentication success.", Toast.LENGTH_SHORT).show()

                                user!!.updatePassword(newPassword)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            //TODO:SET dialogBox
                                            //TODO: change UI
                                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                            auth.signOut()
//                                            startActivity(Intent(this, LoginActivity::class.java))
                                            var intent = Intent(this, LoginActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }
                            }else{
                                Toast.makeText(this, "Password change failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                }else{
                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }else{
                Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show()
            }

        }else{
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else if (newPassword != confirmPassword) {
                Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show()
                } else {
                Log.i("TAG","ok")
                }
        }

    }




}