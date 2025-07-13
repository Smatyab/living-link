package com.nothing.societyuser.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.nothing.societyuser.R
import com.nothing.societyuser.animation_after_login
import com.nothing.societyuser.databinding.ActivityLoginBinding
import com.nothing.societyuser.fragment.BottomActivity

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    var passwordVisible:Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Login button Intent
        binding.loginBtn.setOnClickListener {
            loginUsser()

        }
        //back button intent
        binding.loginBackBtn.setOnClickListener{
            intentFun(WelcomeSignUpLogin::class.java)
            finish()
        }
        //Text Intent to registation
        binding.registationTextBtn.setOnClickListener {
            intentFun(RegistrationActivity::class.java)
            finish()
        }
        //forgot password
        binding.btnTxtForgot.setOnClickListener(){
            forgotPass()

        }

        //hide and show password
        binding.loginPasswordEdt.setOnTouchListener { v, event ->

            val Right = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.loginPasswordEdt.right - binding.loginPasswordEdt.compoundDrawables[Right].bounds.width()) {
                    val selection = binding.loginPasswordEdt.selectionEnd
                    if (passwordVisible) {
                        binding.loginPasswordEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                        binding.loginPasswordEdt.transformationMethod = PasswordTransformationMethod.getInstance()
                        passwordVisible = false
                    } else {
                        binding.loginPasswordEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                        binding.loginPasswordEdt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        passwordVisible = true
                    }
                    binding.loginPasswordEdt.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false // Consume the touch event
        }

        binding.googleLoginBtn.setOnClickListener(){
            Toast.makeText(this, "TMP Not Available", Toast.LENGTH_SHORT).show()
        }



    }

    private fun loginUsser() {
        val email = binding.loginEmailEdt.text.toString()
        val password = binding.loginPasswordEdt.text.toString()
        var isValidated:Boolean = validateRegistration(email,password)
        if (!isValidated) {
            return
        }

        loginAccountInFirebase(email,password)
    }

    private fun loginAccountInFirebase(email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(){
            changeProgress(false)
            if (it.isSuccessful){
                //login is success
                var intent = Intent(this, animation_after_login::class.java)
                startActivity(intent)
                finish()

            }else{
                toastFun("enter right password and email")
            }
        }

    }

    //progressBar on button
    fun changeProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.loginBtn.visibility = View.GONE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.loginBtn.visibility = View.VISIBLE
        }
    }

    //validation
    fun validateRegistration(userEmail: String, userPassword: String, ): Boolean {
        // empty format validate
        if (userEmail.isEmpty()) {
            binding.loginEmailEdt.error = "Email is required"
            return false
        }

        if (userPassword.isEmpty()) {
            binding.loginPasswordEdt.error = "Password is required"
            return false
        }

        // format Validate
        val emailRegex = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
        if (!emailRegex.matches(userEmail)) {
            binding.loginEmailEdt.error = "Invalid email address"
            return false
        }
        if (userPassword.length < 8) {
            binding.loginPasswordEdt.error = "Password should be at least 8 characters long"
            return false
        }
        return true

    }


    //helper function
    //intent
    fun intentFun(destination : Class<*>){
        var intent = Intent(this, destination)
        startActivity(intent)
        finish()

    }
    //toast
    fun toastFun(message : String){
        var toast = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }

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


}
