package com.nothing.societyuser.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nothing.societyuser.R
import com.nothing.societyuser.login_mock
import com.zegocloud.zimkit.services.ZIMKit

class ChatMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)
        initZegocloud()

        val intent = Intent(this, login_mock::class.java)
        startActivity(intent)
        finish()


    }

    fun initZegocloud() {
        ZIMKit.initWith(this.application, KeyConstant.appID, KeyConstant.appSign)
        // Online notification for the initialization (use the following code if this is needed).
        ZIMKit.initNotifications()
    }
}