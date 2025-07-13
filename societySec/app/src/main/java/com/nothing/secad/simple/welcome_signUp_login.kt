package com.nothing.secad.simple

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nothing.secad.databinding.ActivityWelcomeSignUpLoginBinding


class welcome_signUp_login : AppCompatActivity() {

    private val binding by lazy {
        ActivityWelcomeSignUpLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegWelcome.setOnClickListener(){

            val intent = Intent(this, DialogBoxBG::class.java)
            startActivity(intent)
        }
        binding.btnLoginWelcome.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }




    }

}