package com.nothing.secad.simple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nothing.secad.R
import com.nothing.secad.databinding.ActivityDemoBinding
import com.nothing.secad.databinding.ActivityPaymentSendReceiveBinding

class PaymentSendReceiveActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentSendReceiveBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentSendReceiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendPaymentToAdminBtn.setOnClickListener(){
            var intent = Intent(this@PaymentSendReceiveActivity,SendPaymentAdmin::class.java);
            startActivity(intent);
        }
        binding.recivePaymentToUserBtn.setOnClickListener(){
            var intent = Intent(this@PaymentSendReceiveActivity,ReceivePaymentUser::class.java);
            startActivity(intent);
        }

    }
}