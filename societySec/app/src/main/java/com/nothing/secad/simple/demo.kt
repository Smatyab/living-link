package com.nothing.secad.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.nothing.secad.R
import com.nothing.secad.databinding.ActivityDemoBinding
import com.nothing.secad.fragments.TransactionFragment

class demo : AppCompatActivity() {
    private lateinit var binding: ActivityDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_demo)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Retrieve values from the intent



        // Set values to the TextView


//        // Start TransactionFragment without passing parameters
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.fragmentContainer, TransactionFragment())
//        fragmentTransaction.addToBackStack(null)  // Optional: Add to back stack if needed
//        fragmentTransaction.commit()
    }
}

