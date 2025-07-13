package com.nothing.secad.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import android.window.SplashScreen
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codebyashish.autoimageslider.AutoImageSlider
import com.codebyashish.autoimageslider.Enums.ImageScaleType
import com.codebyashish.autoimageslider.Models.ImageSlidesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nothing.secad.Adapter.CategoryAdapter
import com.nothing.secad.Complain
import com.nothing.secad.EventActivity
import com.nothing.secad.MeetingUserSecBothWayActivity
import com.nothing.secad.MoreCategoryActivity
import com.nothing.secad.R
import com.nothing.secad.complain.ComplainActivity
import com.nothing.secad.databinding.ActivityMeetingUserSecBothWayBinding
import com.nothing.secad.model.CategoryModel
import com.nothing.secad.databinding.FragmentDashbordBinding
import com.nothing.secad.meeting.ZoomMainActivity
import com.nothing.secad.note.NotesMainActivity
import com.nothing.secad.simple.PaymentSendReceiveActivity
import org.w3c.dom.Text
import java.util.Calendar

class DashbordFragment : Fragment() {

    private var binding: FragmentDashbordBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashbordBinding.inflate(inflater, container, false)
        boxChangeOnDate()


        binding!!.box2.setOnClickListener(){
            var intent = Intent(context, ComplainActivity::class.java)
            startActivity(intent)
        }
        binding!!.notification.setOnClickListener(){
            Toast.makeText(requireContext(), "tmp not available", Toast.LENGTH_SHORT).show()
        }

        binding!!.moreCategoryTxt.setOnClickListener(){
            var intent = Intent(context, MoreCategoryActivity::class.java)
            startActivity(intent)
        }

        return binding?.root


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Call your function here
        fireslider()
        UserDataFetch()

        // Dummy data for categories
        val categoryList = listOf(
            CategoryModel(R.drawable.pay_vector, "payment"),
            CategoryModel(R.drawable.baseline_add_complain_24, "complain"),
            CategoryModel(R.drawable.baseline_meeting_24, "meeting"),
            CategoryModel(R.drawable.baseline_event_note_24, "Notes"),
            CategoryModel(R.drawable.event, "Event"),
            CategoryModel(us.zoom.videomeetings.R.drawable.ic_more, "more")
        )

        // Create and set up the CategoryAdapter
        val categoryAdapter = CategoryAdapter(categoryList)

        // Set the item click listener for the adapter
        categoryAdapter.onItemClickListener = object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        intentFun(PaymentSendReceiveActivity::class.java)
                    }
                    1 -> {
                        // Replace the current fragment with XYZFragment
                        intentFun(ComplainActivity::class.java)

                    }
                    2 -> {
                        intentFun(MeetingUserSecBothWayActivity::class.java)
                    }
                    3->{

                        intentFun(NotesMainActivity::class.java)
                    }
                    4 -> {
                        intentFun(EventActivity::class.java)
                    }
                    5 -> {
                        intentFun(MoreCategoryActivity::class.java)
                    }

                    else -> {
                        Toast.makeText(context,"rmp not available",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Set up the RecyclerView with a GridLayoutManager
        binding?.categoryRv?.apply {
            layoutManager = GridLayoutManager(context, 3) // 3 is the number of columns
            adapter = categoryAdapter
        }
    }

    //dialog box for show amount





    fun intentFun(destination: Class<*>) {
        val intent = Intent(requireContext(), destination)
        startActivity(intent)

    }

    // ... Rest of your code

    private fun fireslider() {
        val imageSlider: AutoImageSlider = binding?.autoImageSlider ?: return
        val sliderModels = ArrayList<ImageSlidesModel>()
        val db = FirebaseFirestore.getInstance()
        db.collection("ADMIN").document("XqHXv5Ebd8aUXngVzSXq").collection("banners").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val imageUrl = document.getString("bannerImage")
                        val title = document.getString("bannerTitle")
                        if (!imageUrl.isNullOrEmpty()) {
                            val imageModel = ImageSlidesModel(imageUrl, ImageScaleType.FIT)
                            // Set title if available

                            sliderModels.add(imageModel)
                        }
                    }
                    imageSlider.setImageList(sliderModels, ImageScaleType.FIT)
                } else {
                    // Handle failures
                    val exception = task.exception
                    exception?.printStackTrace()
                }
            }
            .addOnFailureListener { e ->
                // Handle failure separately, if needed
                e.printStackTrace()
            }
    }

    fun boxChangeOnDate(){
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        // Determine which box to show based on the current date
        if (currentDay in 1..5) {
            // Show the first box
            binding?.box1?.visibility = View.VISIBLE
            binding?.box2?.visibility = View.GONE
        } else if (currentDay in 6..31) {
            // Show the second box
            binding?.box1?.visibility = View.GONE
            binding?.box2?.visibility = View.VISIBLE
        }
    }

    fun UserDataFetch() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            db.collection("societies").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Access the fields you need
                        val userName = document.getString("name")
                        val userImage = document.getString("userImage")

                        // Update the UI on the main thread
                        activity?.runOnUiThread {
                            if (userName != null && binding != null) {
                                var finalUserName:String = " $userName"
                                binding!!.helloUserNameTxtHome.text = finalUserName
                                Glide.with(this)
                                    .load(userImage)
//                                    .placeholder(R.drawable.user_image_place_holder) // Optional placeholder image while loading
//                                    .error(R.drawable.user_image_place_holder) // Optional error image if loading fails
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
