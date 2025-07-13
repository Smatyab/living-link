package com.nothing.societyuser.easyWork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nothing.societyuser.R

class EaseWorkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ease_work)


    }
    fun intentFun(destination : Class<*>){
        var intent = Intent(this, destination)
        startActivity(intent)

    }

    fun toastFun(message : String){
        var toast = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }
}