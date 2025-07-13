package com.nothing.secad.simple

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Log.*
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nothing.secad.R
import com.nothing.secad.Society
import com.nothing.secad.databinding.ActivitySignUpBinding


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth;
    private var totalAmount : Int = 0;
    var perHomePrice : Int = 0;
    var passwordVisibleMain:Boolean = false;
    var passwordVisible:Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = Firebase.auth;

        //come with intent data to form
        val selectedParking = intent.getStringExtra("selectedParking")
        val selectedElevator = intent.getStringExtra("selectedElevator")
        val selectedWatchman = intent.getStringExtra("selectedWatchman")
        val selectedGarden = intent.getStringExtra("selectedGarden")
        val selectedTemple = intent.getStringExtra("selectedTemple")
        val selectedWaterTank = intent.getStringExtra("selectedWaterTank")
        val selectedTotalHome = intent.getStringExtra("selectedTotalHome")
        totalAmount = intent.getStringExtra("totalAmount")!!.toInt()
     //   Toast.makeText(this, "$selectedParking + $selectedElevator + $selectedWatchman + $selectedGarden + $selectedTemple + $selectedWaterTank + $selectedTotalHome ,$totalAmount", Toast.LENGTH_SHORT).show()
             perHomePrice = totalAmount!!.toInt()/selectedTotalHome!!.toInt()
            Toast.makeText(this, "Total amount: $perHomePrice", Toast.LENGTH_SHORT).show()
        customDialog()


        var name: String;
        var email: String;
        var password: String;
        var address: String;


        // TODO: add data to intent
        var intentNew = Intent(this, FirebaseUpload::class.java)

        intentNew.putExtra("selectedParking", selectedParking)
        intentNew.putExtra("selectedElevator", selectedElevator)
        intentNew.putExtra("selectedWatchman", selectedWatchman)
        intentNew.putExtra("selectedGarden", selectedGarden)
        intentNew.putExtra("selectedTemple", selectedTemple)
        intentNew.putExtra("selectedWaterTank", selectedWaterTank)
        intentNew.putExtra("selectedTotalHome", selectedTotalHome)




        // Initialize your views
        val registerButton = binding.btnRegisterWelcome

        // Set click listener for the registerButton
        registerButton.setOnClickListener {


            name = binding.editTextTextSocietyName.text.toString()
            email = binding.editTextTextEmailAddress.text.toString()
            password =binding.editTextPassword.text.toString()
            address = binding.editTextSocAddress.text.toString()


            intentNew.putExtra("societyName", name)
            intentNew.putExtra("emailAddress", email)
            intentNew.putExtra("password", password)
            intentNew.putExtra("address", address)
            clearErrors() // Clear previous errors


            binding.editTextPassword.setOnTouchListener { v, event ->

                val Right = 2
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= binding.editTextPassword.right - binding.editTextPassword.compoundDrawables[Right].bounds.width()) {
                        val selection = binding.editTextPassword.selectionEnd
                        if (passwordVisibleMain) {
                            binding.editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                            binding.editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                            passwordVisibleMain = false
                        } else {
                            binding.editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                            binding.editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                            passwordVisibleMain = true
                        }
                        binding.editTextPassword.setSelection(selection)
                        return@setOnTouchListener true
                    }
                }
                false // Consume the touch event
            }

            //hide and show conform password
            binding.editTextConformPassword.setOnTouchListener { v, event ->

                val Right = 2
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= binding.editTextConformPassword.right - binding.editTextConformPassword.compoundDrawables[Right].bounds.width()) {
                        val selection = binding.editTextConformPassword.selectionEnd
                        if (passwordVisible) {
                            binding.editTextConformPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                            binding.editTextConformPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                            passwordVisible = false
                        } else {
                            binding.editTextConformPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0)
                            binding.editTextConformPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                            passwordVisible = true
                        }
                        binding.editTextConformPassword.setSelection(selection)
                        return@setOnTouchListener true
                    }
                }
                false // Consume the touch event
            }



            if (validateInput()) {

                Log.d(TAG, "Email is:" + binding.editTextTextEmailAddress.text)
                createUser(binding.editTextTextEmailAddress.text.toString(), binding.editTextPassword.text.toString(), intentNew)
                val intent = Intent(this,FirebaseUpload::class.java)

                // Transfer Data
//                val society: Society = Society (
//                    name = binding.editTextTextSocietyName.text.toString(),
//                    email = binding.editTextTextEmailAddress.text.toString(),
//                    password =binding.editTextPassword.text.toString(),

                    // TODO: take building
//                    isBuilding = true,
//
//                    parking = selectedParking!!.toInt(),
//                    elevators = selectedElevator!!.toInt(),
//                    watchMan = selectedWatchman!!.toInt(),
//                    garden = selectedGarden!!.toInt(),
//                    temple = selectedTemple!!.toInt(),
//                    waterTank = selectedWaterTank!!.toInt(),
//                    totalHouses = selectedTotalHome!!.toInt(),
//
//                    // TODO: calculate price and add
//                    expectedPricePerHouse = 1000
//
//                )
//                intent.putExtra("society", society);




            }
        }

        //back Button
        binding.btnBackReg.setOnClickListener(){
            val intent = Intent(this, welcome_signUp_login::class.java)
            startActivity(intent)
            finish()
        }

        //regi to login button
        binding.regToLoginBtn.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }


    // dialog box

    private fun customDialog() {
        binding.signUpRoot.background = ContextCompat.getDrawable(this, R.color.primary_color)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_animation)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        var tvTotalAmount = dialog.findViewById<TextView>(R.id.total_amountDialog)
        var tvPerHomeAmount = dialog.findViewById<TextView>(R.id.per_home_amountDialog)
        var btnNextToDialog = dialog.findViewById<TextView>(R.id.after_btn_amountDialog)

        tvTotalAmount.text = "Total Amount : $totalAmount"
        tvPerHomeAmount.text = "Per Home Amount : $perHomePrice"
        btnNextToDialog.setOnClickListener {
            dialog.dismiss()
            binding.signUpRoot.background = ContextCompat.getDrawable(this, R.color.white)
        }
        dialog.show()

    }

    // Function to validate input fields
    private fun validateInput(): Boolean {
        var isValid = true

        val userName = binding.editTextTextSocietyName.text.toString().trim()
        val email = binding.editTextTextEmailAddress.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val confirmPassword = binding.editTextConformPassword.text.toString().trim()
        val address = binding.editTextSocAddress.text.toString().trim()

        // Add your validation logic here
        if (userName.isEmpty()) {
            binding.editTextTextSocietyName.setError("Username is required")
            isValid = false
        }

        if (email.isEmpty() || !isValidEmailFormat(email)) {
            binding.editTextTextEmailAddress.setError("Invalid email format")
            isValid = false
        }

        if (password.isEmpty()) {
            binding.editTextPassword.setError("Password is required")
            isValid = false
        }

        if (address.isEmpty() || address.length <  20) {
            binding.editTextSocAddress.setError("Address is required or more than 20 char")
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.editTextConformPassword.setError("Confirm Password is required")
            isValid = false
        }

        if (password != confirmPassword) {
            binding.editTextConformPassword.setError("Passwords do not match")
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
        binding.editTextTextSocietyName.error = null
        binding.editTextTextEmailAddress.error = null
        binding.editTextPassword.error = null
        binding.editTextConformPassword.error = null
    }

    private fun createUser(email: String, password: String, intent: Intent) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed." + task.exception,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }


}
