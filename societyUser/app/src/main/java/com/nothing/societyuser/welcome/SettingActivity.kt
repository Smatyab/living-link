package com.nothing.societyuser.welcome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nothing.societyuser.databinding.ActivitySettingBinding


class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set the Toolbar as the support action bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Setting"
            setDisplayHomeAsUpEnabled(true) // Enable the back arrow
        }

    }


}