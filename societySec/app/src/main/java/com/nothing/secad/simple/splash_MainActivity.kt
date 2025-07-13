package com.nothing.secad.simple

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nothing.secad.HomeActivity
import com.nothing.secad.R

class splash_MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_main)

        // Check internet connectivity
        if (isNetworkAvailable()) {
            // If internet is available, start the main activity after the splash timeout
            Handler().postDelayed({
                var currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    intentFun(HomeActivity::class.java)
                    finish()
                }else{
                    intentFun(welcome_signUp_login::class.java)
                    finish()
                }
            }, 1200)
        } else {
            // Handle the case when there is no internet connectivity
            // You can show a message or take appropriate action here
            // For example, display a message and close the app
//            finish()
            Toast.makeText(this@splash_MainActivity,"offline mode",Toast.LENGTH_LONG).show();

        }

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun intentFun(destination : Class<*>){
        var intent = Intent(this, destination)
        startActivity(intent)

    }
    //toast
    fun toastFun(message : String){
        var toast = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }
}