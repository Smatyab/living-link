package com.nothing.secad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.nothing.secad.Adapter.CategoryAdapter
import com.nothing.secad.complain.ComplainActivity
import com.nothing.secad.databinding.ActivityMoreCategoryBinding
import com.nothing.secad.meeting.ZoomMainActivity
import com.nothing.secad.meeting.user.UseMeetingMainActivity
import com.nothing.secad.model.CategoryModel
import com.nothing.secad.note.NotesMainActivity
import com.nothing.secad.setting.CustomerCare
import com.nothing.secad.simple.PaymentSendReceiveActivity
import com.nothing.secad.simple.ReceivePaymentUser
import com.nothing.secad.simple.SendPaymentAdmin

class MoreCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoreCategoryBinding
//    private lateinit var binding: ActivityMoreCategotyBinding
    private lateinit var homeCategoryAdapter: CategoryAdapter
    private lateinit var homeCategoryAdapter2: CategoryAdapter
    private lateinit var homeCategoryAdapter3: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //user
        val dummyDataList = ArrayList<CategoryModel>()
        dummyDataList.add(CategoryModel(R.drawable.pay_vector, "Pay"))
        dummyDataList.add(CategoryModel(R.drawable.wallet_vector, "Wallet"))
        dummyDataList.add(CategoryModel(R.drawable.baseline_add_complain_24, "complain"))
        dummyDataList.add(CategoryModel(R.drawable.baseline_meeting_24, "Meeting"))
        dummyDataList.add(CategoryModel(R.drawable.baseline_event_note_24, "Notes"))
        dummyDataList.add(CategoryModel(R.drawable.ic_notifications_black_24dp, "maintenance"))
        dummyDataList.add(CategoryModel(R.drawable.ic_home_black_24dp,"Home"))



        homeCategoryAdapter = CategoryAdapter(dummyDataList)
        binding.connectUserRv.layoutManager = GridLayoutManager(this, 3)
        binding.connectUserRv.adapter = homeCategoryAdapter

        homeCategoryAdapter.onItemClickListener = object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        intentFun(ReceivePaymentUser::class.java)
                    }
                    1->{

                        intentFun(ReceivePaymentUser::class.java)
                    }
                    2->{
                        intentFun(ComplainActivity::class.java)
                    }
                    3->{
                        intentFun(UseMeetingMainActivity::class.java)
                    }
                    4->{
                        intentFun(NotesMainActivity::class.java)
                    }
                    5->{
                        intentFun(ReceivePaymentUser::class.java)
                    }
                    6->{
                        intentFun(HomeActivity::class.java)
                    }


                    else -> {
                        Toast.makeText(this@MoreCategoryActivity, "tmp not available", Toast.LENGTH_SHORT).show()
                        // Handle other positions if necessary
                    }
                }
                binding.connectAdminRv.adapter = homeCategoryAdapter
            }


        }









        //admin
        val dummyDataList2 = ArrayList<CategoryModel>()
        dummyDataList2.add(CategoryModel(R.drawable.pay_vector, "Pay"))
        dummyDataList2.add(CategoryModel(R.drawable.wallet_vector, "Wallet"))
        dummyDataList2.add(CategoryModel(R.drawable.baseline_add_complain_24, "complain"))
        dummyDataList2.add(CategoryModel(R.drawable.baseline_meeting_24, "Meeting"))
        dummyDataList2.add(CategoryModel(R.drawable.baseline_event_note_24, "Notes"))
        dummyDataList2.add(CategoryModel(R.drawable.event, "Events"))
        dummyDataList2.add(CategoryModel(R.drawable.ic_home_black_24dp,"Home"))


        homeCategoryAdapter2 = CategoryAdapter(dummyDataList2)
        binding.connectAdminRv.layoutManager = GridLayoutManager(this, 3)
        binding.connectAdminRv.adapter = homeCategoryAdapter2
        homeCategoryAdapter2.onItemClickListener = object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        intentFun(TransactionHistoryAdmin::class.java)
                    }
                    1->{

                        intentFun(SendPaymentAdmin::class.java)
                    }
                    2->{
                        intentFun(ComplainActivity::class.java)
                    }
                    3->{
                        intentFun(ZoomMainActivity::class.java)
                    }
                    4->{
                        intentFun(NotesMainActivity::class.java)
                    }
                    5->{
                        intentFun(EventActivity::class.java)
                    }
                    6->{
                        intentFun(HomeActivity::class.java)
                    }


                    else -> {
                        Toast.makeText(this@MoreCategoryActivity, "tmp not available", Toast.LENGTH_SHORT).show()
                        // Handle other positions if necessary
                    }
                }
                binding.connectAdminRv.adapter = homeCategoryAdapter2
            }


        }

        //other
        val dummyDataList3 = ArrayList<CategoryModel>()
        dummyDataList3.add(CategoryModel(R.drawable.outline_profile_24, "Society Profile"))
        dummyDataList3.add(CategoryModel(R.drawable.baseline_change_password_24, "Change Password"))
//        dummyDataList2.add(CategoryModel(R.drawable.baseline_add_complain_24, "complain"))
        dummyDataList3.add(CategoryModel(R.drawable.baseline_event_note_24, "Note"))
        dummyDataList3.add(CategoryModel(R.drawable.baseline_customer_care_24, "Customer Care"))
        dummyDataList3.add(CategoryModel(R.drawable.outline_info_24, "Info"))
        dummyDataList3.add(CategoryModel(R.drawable.ic_notifications_black_24dp, "Alert"))


        homeCategoryAdapter3 = CategoryAdapter(dummyDataList3)
        binding.connectOtherRv.layoutManager = GridLayoutManager(this, 3)
        binding.connectOtherRv.adapter = homeCategoryAdapter3

        homeCategoryAdapter3.onItemClickListener = object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        intentFun(Building_profile::class.java)
                    }
                    1->{

                        intentFun(ChangePasswordActivity::class.java)
                    }
                    2->{
                        intentFun(NotesMainActivity::class.java)
                    }
                    3->{
                        intentFun(CustomerCare::class.java)
                    }
                    4->{
                        Toast.makeText(this@MoreCategoryActivity,"information",Toast.LENGTH_SHORT).show()
                    }
                    5->{
                        Toast.makeText(this@MoreCategoryActivity,"tmp not  available",Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this@MoreCategoryActivity, "tmp not available", Toast.LENGTH_SHORT).show()
                        // Handle other positions if necessary
                    }
                }
                binding.connectAdminRv.adapter = homeCategoryAdapter3
            }


        }




    }
    fun intentFun(destination: Class<*>) {
        val intent = Intent(this@MoreCategoryActivity, destination)
        startActivity(intent)
    }
}