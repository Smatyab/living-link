package com.nothing.societyuser.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nothing.societyuser.R
import com.nothing.societyuser.fragment.BottomActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //splash screen delay time
        delaySpashScreen();
        //TODO: Add progress Bar

    }

    //Run splash screen
    fun delaySpashScreen() {
        //TODO:check Internet Connectivity

        Handler().postDelayed({
            var currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                intentFun(BottomActivity::class.java)
                finish()
            }else{
                intentFun(WelcomeSignUpLogin::class.java)
            }
        }, 1200)
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
}