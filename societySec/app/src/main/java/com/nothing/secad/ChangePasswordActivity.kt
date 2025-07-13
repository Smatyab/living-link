package com.nothing.secad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nothing.secad.databinding.ActivityChangePasswordBinding
import com.nothing.secad.simple.LoginActivity


class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Change Password"

        auth = FirebaseAuth.getInstance()
        binding.btnChangePassword.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        val oldPassword = binding.edtCurrentPassword.text.toString()
        val newPassword = binding.edtNewPassword.text.toString()
        val confirmPassword = binding.edtConformPassword.text.toString()

        if (oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if(newPassword == confirmPassword){
                val user: FirebaseUser? = auth.currentUser
                if (user != null && user.email != null){
                    val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

                    user.reauthenticate(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Re-Authentication success.", Toast.LENGTH_SHORT).show()

                            user.updatePassword(newPassword).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Password changed successfully
                                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                    auth.signOut()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                } else {
                                    Toast.makeText(this, "Password change failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Password change failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
