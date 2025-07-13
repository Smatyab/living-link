package com.nothing.secad.simple

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nothing.secad.HomeActivity
import com.nothing.secad.R
import com.nothing.secad.databinding.ActivityDemoBinding
import com.nothing.secad.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var passwordVisible:Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = Firebase.auth


        //back button
        binding.btnBack.setOnClickListener(){
            var intent =  Intent(this@LoginActivity, welcome_signUp_login::class.java)
            startActivity(intent)
            finish()
        }
        //login to registration
        binding.loginToRegButton.setOnClickListener(){
             var intent =  Intent(this@LoginActivity, DialogBoxBG::class.java)
             startActivity(intent)
             finish()
        }
        //google button
        binding.loginGoogleBtn.setOnClickListener(){
            Toast.makeText(this, "tmp not available", Toast.LENGTH_SHORT).show()

        }

        binding.loginBtn.setOnClickListener(){

            if (validateInput()){
                val email = binding.emailLoginEdt.text.toString()
                val password = binding.passwordLoginEdt.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser

                            var intent = Intent(this@LoginActivity,animation_after_login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
            }
        }
        //forgot password
        binding.btnTxtForgot.setOnClickListener(){
            forgotPass()
        }


        //hide and show password
        binding.passwordLoginEdt.setOnTouchListener { v, event ->

            val Right = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.passwordLoginEdt.right - binding.passwordLoginEdt.compoundDrawables[Right].bounds.width()) {
                    val selection = binding.passwordLoginEdt.selectionEnd
                    if (passwordVisible) {
                        binding.passwordLoginEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                        binding.passwordLoginEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                        passwordVisible = false
                    } else {
                        binding.passwordLoginEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                        binding.passwordLoginEdt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        passwordVisible = true
                    }
                    binding.passwordLoginEdt.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false // Consume the touch event
        }

    }

    //start

    // Function to validate input fields
    private fun validateInput(): Boolean {
        var isValid = true

        val email = binding.emailLoginEdt.text.toString().trim()
        val password = binding.passwordLoginEdt.text.toString().trim()


        // Add your validation logic here


        if (email.isEmpty() || !isValidEmailFormat(email)) {
            binding.emailLoginEdt.setError("Invalid email format")
            isValid = false
        }

        if (password.isEmpty()) {
            binding.passwordLoginEdt.setError("Password is required")
            isValid = false
        }


        return isValid
    }
    private fun isValidEmailFormat(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$"
        return email.matches(emailRegex.toRegex())
    }

    // Function to clear errors on all EditText widgets
    private fun clearErrors() {

        binding.emailLoginEdt.error = null
        binding.passwordLoginEdt.error = null

    }
    //forgot password

    fun forgotPass() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogView: View = layoutInflater.inflate(R.layout.activity_dialog_forgot_password, null)
        val editText: EditText = dialogView.findViewById(R.id.emailBox)
        builder.setView(dialogView)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        dialogView.findViewById<Button>(R.id.btnReset).setOnClickListener {
            val userEmail: String = editText.text.toString()

            if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(this@LoginActivity, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val auth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Check your email", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this@LoginActivity, "Unable to send, please try again", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Cancel
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawableResource(R.color.light_white)
        }
    }

    //end
}