package com.nothing.societyuser.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nothing.societyuser.databinding.ActivityWelcomeSignUpLoginBinding

class WelcomeSignUpLogin : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeSignUpLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeSignUpLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO:animation onClick AgainRun
        //login button intent
        binding.btnLoginWelcome.setOnClickListener {
            btnIntent(LoginActivity::class.java)
            finish()
        }
        //signUp button intent
        binding.btnRegWelcome.setOnClickListener {
            btnIntent(RegistrationActivity::class.java)
            finish()
        }

    }



    //intent function
    fun btnIntent(destination : Class<*>){
        var intent = Intent(this, destination)
        startActivity(intent)
    }


}