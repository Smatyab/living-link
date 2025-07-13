package com.nothing.secad.simple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nothing.secad.R
import com.nothing.secad.databinding.ActivityInformationBinding
import com.nothing.secad.setting.CustomerCare

class information_activity : AppCompatActivity() {

    private lateinit var binding: ActivityInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "❕ Information"

        binding.meetingInfo.setOnClickListener(){

            var intent = Intent(information_activity@this,webView_info::class.java)
            intent.putExtra("value","meeting")
            startActivity(intent)
        }


        binding.appConcept.setOnClickListener(){

            var intent = Intent(information_activity@this,webView_info::class.java)
            intent.putExtra("value","appConcept")
            startActivity(intent)
        }

        binding.chating.setOnClickListener(){

            var intent = Intent(information_activity@this,webView_info::class.java)
            intent.putExtra("value","chat")
            startActivity(intent)
        }

        binding.payment.setOnClickListener(){

            var intent = Intent(information_activity@this,webView_info::class.java)
            intent.putExtra("value","payment")
            startActivity(intent)
        }

        binding.wallet.setOnClickListener(){

            var intent = Intent(information_activity@this,webView_info::class.java)
            intent.putExtra("value","wallet")
            startActivity(intent)
        }

        binding.complainInfo.setOnClickListener(){

            var intent = Intent(information_activity@this,webView_info::class.java)
            intent.putExtra("value","complain")
            startActivity(intent)
        }

        binding.notesInfo.setOnClickListener(){

            var intent = Intent(this,webView_info::class.java)
            intent.putExtra("value","note")
            startActivity(intent)
        }

        binding.customerInfoCare.setOnClickListener(){

            var intent = Intent(this,webView_info::class.java)
            intent.putExtra("value","customerCare")
            startActivity(intent)
        }

        binding.privatePolicyInfo.setOnClickListener(){

            var intent = Intent(this,webView_info::class.java)
            intent.putExtra("value","privatePolicy")
            startActivity(intent)
        }

        binding.customereCateBtn.setOnClickListener(){


            var intent = Intent(information_activity@this,CustomerCare::class.java)

            startActivity(intent)
        }


    }
}