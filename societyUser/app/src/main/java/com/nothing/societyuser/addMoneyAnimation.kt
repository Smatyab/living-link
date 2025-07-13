package com.nothing.societyuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nothing.societyuser.databinding.ActivityAddMoneyAnimationBinding
import com.nothing.societyuser.databinding.ActivityDabitCardBinding
import com.nothing.societyuser.fragment.BottomActivity
import com.nothing.societyuser.wallet.TransactionHistory

class addMoneyAnimation : AppCompatActivity() {

    private lateinit var binding: ActivityAddMoneyAnimationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMoneyAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationAddAmount.setOnClickListener {
            var intent = Intent(this@addMoneyAnimation, BottomActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}