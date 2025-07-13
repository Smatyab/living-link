package com.nothing.societyuser.welcome

import HomeCategoryAdapter
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.nothing.societyuser.Model.HomeCategoryModel
import com.nothing.societyuser.R
import com.nothing.societyuser.chat.ChatMainActivity
import com.nothing.societyuser.complain.ComplainRaiseActivity
import com.nothing.societyuser.complain.ComplainRaiseHistory
import com.nothing.societyuser.databinding.ActivityMoreCategotyBinding
import com.nothing.societyuser.fragment.BottomActivity
import com.nothing.societyuser.meeting.MeetingMain
import com.nothing.societyuser.note.NotesMainActivity
import com.nothing.societyuser.setting.ChangePasswordActivity
import com.nothing.societyuser.setting.UserProfileActivity
import com.nothing.societyuser.setting.cutomer_care
import com.nothing.societyuser.setting.informationAcctivity
import com.nothing.societyuser.wallet.TransactionHistory
import com.nothing.societyuser.wallet.WalletActivity

class MoreCategotyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoreCategotyBinding
    private lateinit var homeCategoryAdapter: HomeCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreCategotyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.show()
        supportActionBar?.title = "Categories"

        // Assuming you have a RecyclerView in your layout with the id "recyclerView"


        // Create dummy data for testing (replace this with your actual data)
        val dummyDataList = ArrayList<HomeCategoryModel>()



        dummyDataList.add(HomeCategoryModel(R.drawable.pay_vector, "Pay"))
        dummyDataList.add(HomeCategoryModel(R.drawable.baseline_add_complain_24, "complain"))
        dummyDataList.add(HomeCategoryModel(R.drawable.baseline_meeting_24, "Meeting"))
        dummyDataList.add(HomeCategoryModel(R.drawable.baseline_chat_24, "Chat"))
        dummyDataList.add(HomeCategoryModel(R.drawable.baseline_event_note_24,"Notes"))
        dummyDataList.add(HomeCategoryModel(R.drawable.event, "Event"))
        dummyDataList.add(HomeCategoryModel(R.drawable.wallet_vector, "Wallet"))
        dummyDataList.add(HomeCategoryModel(R.drawable.ic_home_black_24dp, "Home"))
        dummyDataList.add(HomeCategoryModel(R.drawable.pay_history_vector, "Pay History"))
        dummyDataList.add(HomeCategoryModel(R.drawable.complain_history_vector, "Complain History"))
        dummyDataList.add(HomeCategoryModel(R.drawable.outline_profile_24, "profile"))
        dummyDataList.add(HomeCategoryModel(R.drawable.baseline_notifications_none_24, "Notify"))
        dummyDataList.add(HomeCategoryModel(R.drawable.baseline_change_password_24, "Change Password"))
        dummyDataList.add(HomeCategoryModel(R.drawable.baseline_customer_care_24, "Customer Care"))
        dummyDataList.add(HomeCategoryModel(R.drawable.outline_info_24, "Info"))
        // Add more categories as needed\




        homeCategoryAdapter = HomeCategoryAdapter(dummyDataList)
        binding.connectUserRv.layoutManager = GridLayoutManager(this, 4)
        binding.connectUserRv.adapter = homeCategoryAdapter



        homeCategoryAdapter.onItemClickListener = object : HomeCategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        intentFun(WalletActivity::class.java)
                    }
                    1->{

                        intentFun(ComplainRaiseActivity::class.java)
                    }
                    2->{
                        intentFun(MeetingMain::class.java)
                    }
                    3->{
                        intentFun(ChatMainActivity::class.java)
                    }
                    4->{
                        intentFun(NotesMainActivity::class.java)
                    }
                    5->{
                        intentFun(EventActivity::class.java)
                    }
                    6->{
                        intentFun(WalletActivity::class.java)
                    }
                    7->{

                        intentFun(BottomActivity::class.java)
                        finish()
                    }
                    8->{
                        intentFun(TransactionHistory::class.java)
                    }
                    9->{
                        intentFun(ComplainRaiseHistory::class.java)
                    }
                    10->{
                        intentFun(UserProfileActivity::class.java)
                    }
                    11->{
                        Toast.makeText(this@MoreCategotyActivity, "tmp not available", Toast.LENGTH_SHORT).show()
                    }
                    12->{
                        intentFun(ChangePasswordActivity::class.java)
                    }
                    13->{
                        intentFun(cutomer_care::class.java)
                    }
                    14->{

                        intentFun(informationAcctivity::class.java)
                    }


                    else -> {
                        Toast.makeText(this@MoreCategotyActivity, "tmp not available", Toast.LENGTH_SHORT).show()
                        // Handle other positions if necessary
                    }
                }
            }
        }

        binding.connectUserRv.adapter=homeCategoryAdapter


    }

    fun intentFun(destination: Class<*>) {
        val intent = Intent(this@MoreCategotyActivity, destination)
        startActivity(intent)
    }
}
