package com.nothing.societyuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nothing.societyuser.databinding.ActivityAnimationAfterSignUpBinding
import com.nothing.societyuser.fragment.BottomActivity
import org.checkerframework.common.subtyping.qual.Bottom

class animation_after_sign_up : AppCompatActivity() {

    private lateinit var binding : ActivityAnimationAfterSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationAfterSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationSignUpToDashbord.setOnClickListener(){
            var intent = Intent(this,BottomActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}