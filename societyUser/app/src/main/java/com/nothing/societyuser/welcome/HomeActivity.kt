package com.nothing.societyuser.welcome

import HomeCategoryAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nothing.societyuser.Model.HomeCategoryModel
import com.nothing.societyuser.R
import com.nothing.societyuser.databinding.ActivityHomeBinding
import com.nothing.societyuser.wallet.WalletActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample data list (replace this with your actual data)
        val dataList = ArrayList<HomeCategoryModel>()
        dataList.add(HomeCategoryModel(R.drawable.pay_icon, "Pay"))
        dataList.add(HomeCategoryModel(R.drawable.baseline_add_complain_24, "complain"))
        dataList.add(HomeCategoryModel(R.drawable.baseline_meeting_24, "meeting"))
        dataList.add(HomeCategoryModel(R.drawable.baseline_chat_24, "chat"))
        dataList.add(HomeCategoryModel(R.drawable.wallet_icon, "wallet"))
        dataList.add(HomeCategoryModel(R.drawable.baseline_more_forward_ios_24, "More"))

        val adapter = HomeCategoryAdapter(dataList)
        adapter.onItemClickListener = object : HomeCategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> intentFun(WalletActivity::class.java)
                    1 -> intentFun(SplashActivity::class.java)
                    // Add more cases as needed for other positions
                    else -> {
                        // Handle other positions if necessary
                    }
                }
            }
        }

        val rv: RecyclerView = findViewById(R.id.category_rv)
        binding.categoryRv.adapter = adapter
        binding.categoryRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    fun intentFun(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
    }

    //toast
    fun toastFun(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
