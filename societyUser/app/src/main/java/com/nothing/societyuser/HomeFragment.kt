package com.nothing.societyuser

import HomeCategoryAdapter
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codebyashish.autoimageslider.Enums.ImageScaleType
import com.codebyashish.autoimageslider.Models.ImageSlidesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nothing.societyuser.Model.HomeCategoryModel
import com.nothing.societyuser.chat.ChatMainActivity
import com.nothing.societyuser.complain.ComplainRaiseActivity
import com.nothing.societyuser.databinding.FragmentHomeBinding
import com.nothing.societyuser.meeting.MeetingMain
import com.nothing.societyuser.note.NotesMainActivity
import com.nothing.societyuser.wallet.TransactionHistory
import com.nothing.societyuser.wallet.WalletActivity
import com.nothing.societyuser.welcome.HomeActivity
import com.nothing.societyuser.welcome.MoreCategotyActivity


class HomeFragment : Fragment() {



    private var binding: FragmentHomeBinding? = null
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter: HomeCategoryAdapter
    private lateinit var categoryList: ArrayList<HomeCategoryModel>
    private var societyId:String = ""
    private lateinit var slider:ArrayList<ImageSlidesModel>
    var database  = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UserDataFetch()
        dataInitialize()
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        recyclerView = view.findViewById(R.id.category_rv)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter= HomeCategoryAdapter(categoryList)
        //slider call
        fireslider()

        //pay
        binding!!.payBtnHomeBig.setOnClickListener(){
            intentFun(TransactionHistory::class.java)
        }
        binding!!.claimBtnHomeBig.setOnClickListener(){
            intentFun(ComplainRaiseActivity::class.java)
        }

        //under line
        binding!!.moreTxtButton.setPaintFlags(binding!!.moreTxtButton.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding!!.moreTxtButton.setOnClickListener(){

            intentFun(MoreCategotyActivity::class.java)
        }



        adapter.onItemClickListener = object : HomeCategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        intentFun(WalletActivity::class.java)
                    }
                    1 ->{
                        // Replace the current fragment with XYZFragment
                        intentFun(ComplainRaiseActivity::class.java)

                    }
                    2 ->{
                        intentFun(MeetingMain::class.java)

                    }
                    3->{
                        intentFun(ChatMainActivity::class.java)
                    }
                    4->{
                        intentFun(WalletActivity::class.java)
                    }
                    5->{
                        intentFun(NotesMainActivity::class.java)

                    }
                    6->{
                        intentFun(MoreCategotyActivity::class.java)
                    }

                    else -> {
                        // Handle other positions if necessary
                    }
                }
            }
        }

        recyclerView.adapter=adapter

    }

    private fun fireslider(){


        // create an imageArrayList which extend ImageSlideModel class
        val autoImageList : ArrayList<ImageSlidesModel> = ArrayList()

        // find and initialize ImageSlider


        // add some imagees or titles (text) inside the imagesArrayList
        autoImageList.add(ImageSlidesModel("https://firebasestorage.googleapis.com/v0/b/society-agency.appspot.com/o/UserBanners%2FScreenshot%202024-04-04%20113606.png?alt=media&token=45ef1735-a91c-44ac-8c28-e7cf317e8f1c"))
        autoImageList.add(ImageSlidesModel("https://firebasestorage.googleapis.com/v0/b/society-agency.appspot.com/o/UserBanners%2FScreenshot%202024-04-04%20111736.png?alt=media&token=65b5b109-1778-4e25-83e1-c5256539d3dc"))
        autoImageList.add(ImageSlidesModel("https://firebasestorage.googleapis.com/v0/b/society-agency.appspot.com/o/UserBanners%2FScreenshot%202024-04-04%20113913.png?alt=media&token=348cb7d2-6ac1-4d07-9ca6-7e666de072c5"))
        autoImageList.add(ImageSlidesModel("https://firebasestorage.googleapis.com/v0/b/society-agency.appspot.com/o/UserBanners%2FScreenshot%202024-04-04%20114111.png?alt=media&token=83e72906-3592-4755-9552-12ab152c3b9e"))

        // set the added images inside the AutoImageSlider
        binding?.autoImageSlider?.setImageList(autoImageList, ImageScaleType.FIT)

        // set any default animation or custom animation (setSlideAnimation(ImageAnimationTypes.ZOOM_IN))
        binding?.autoImageSlider?.setDefaultAnimation()

        binding!!.imgNotification.setOnClickListener(){
            Toast.makeText(requireContext(), "Notification tmp not available", Toast.LENGTH_SHORT).show()
        }
    }



//    private fun fireslider() {
//        // Initialize slider ArrayList
//        slider = ArrayList()
//
//        // Call socId() with a callback to ensure it's finished before querying Firestore
//        socId { societyId ->
//            if (societyId.isNotEmpty()) {
//                Toast.makeText(requireContext(), societyId.toString(), Toast.LENGTH_SHORT).show()
//                database.collection("societies").document(societyId).collection("imageSlider").get()
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            for (document in task.result!!) {
//                                // Ensure slider ArrayList is initialized before adding items
//                                slider.add(document.getString("url")!!)
//                            }
//                            // Set image list only once after adding all items
//                            binding?.autoImageSlider?.setImageList(slider)
//                        } else {
//                            Toast.makeText(requireContext(), "can't load image", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    .addOnFailureListener { exception ->
//                        // Handle failure
//                    }
//            }
//        }
//    }


    private fun socId(callback: (String) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("member")
            .document(uid!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val societyId = documentSnapshot.getString("societyId") ?: ""
                callback(societyId)
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }

    private fun dataInitialize() {
        categoryList = ArrayList()

        categoryList.add(HomeCategoryModel(R.drawable.pay_vector, "Pay"))
        categoryList.add(HomeCategoryModel(R.drawable.baseline_add_complain_24, "complain"))
        categoryList.add(HomeCategoryModel(R.drawable.baseline_meeting_24, "meeting"))
        categoryList.add(HomeCategoryModel(R.drawable.baseline_chat_24, "chat"))
        categoryList.add(HomeCategoryModel(R.drawable.wallet_vector, "wallet"))
        categoryList.add(HomeCategoryModel(R.drawable.baseline_event_note_24, "Note"))
        categoryList.add(HomeCategoryModel(R.drawable.baseline_more_forward_ios_24, "More"))


    }
    fun intentFun(destination: Class<*>) {
        val intent = Intent(requireContext(), destination)
        startActivity(intent)
    }

    fun UserDataFetch() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            db.collection("member").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Access the fields you need
                        val userName = document.getString("userName")
                        val userImage = document.getString("userImage")

                        // Update the UI on the main thread
                        activity?.runOnUiThread {
                            if (userName != null && binding != null) {
                                var finalUserName:String = "Hello, "+userName
                                binding!!.helloUserNameTxtHome.text = finalUserName
                                Glide.with(this)
                                    .load(userImage)
                                    .placeholder(R.drawable.user_image_place_holder) // Optional placeholder image while loading
                                    .error(R.drawable.user_image_place_holder) // Optional error image if loading fails
                                    .centerCrop()
                                    .into(binding!!.userImageHomefragment)

                            }
                        }
                    } else {
                        println("No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                }
        }
    }



    private fun startHomeActivity() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }


}