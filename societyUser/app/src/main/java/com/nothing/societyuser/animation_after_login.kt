package com.nothing.societyuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nothing.societyuser.databinding.ActivityAnimationAfterLoginBinding
import com.nothing.societyuser.databinding.ActivityAnimationAfterSignUpBinding
import com.nothing.societyuser.fragment.BottomActivity
import org.checkerframework.common.subtyping.qual.Bottom

class animation_after_login : AppCompatActivity() {
    private  lateinit var  binding: ActivityAnimationAfterLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationAfterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationLoginToDashbord.setOnClickListener(){

            var intent = Intent(this, BottomActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}