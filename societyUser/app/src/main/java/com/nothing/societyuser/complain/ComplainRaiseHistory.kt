package com.nothing.societyuser.complain

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.nothing.societyuser.Adapter.ComplainHistoryAdapter
import com.nothing.societyuser.Model.complainHistoryModel
import com.nothing.societyuser.databinding.ActivityComplainRaiseHistoryBinding

class ComplainRaiseHistory : AppCompatActivity() {
    // Your existing code...
    var searchList: List<complainHistoryModel> = listOf()
    private  lateinit var binding: ActivityComplainRaiseHistoryBinding
    var adapter: ComplainHistoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplainRaiseHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dummy data for testing
        var dummyData: List<complainHistoryModel> = listOf()

        var searchView = binding.complainSearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.updateQuery(newText!!)
                return true
            }
        })

        // Initialize RecyclerView and set up the adapter
        binding.complainRaiseHistoryRv.layoutManager = LinearLayoutManager(this)
       adapter = ComplainHistoryAdapter(this,dummyData.toMutableList())
        binding.complainRaiseHistoryRv.adapter = adapter

        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        db.collection("member").document(user!!.uid).get()
            .addOnSuccessListener { document ->
                val societyId = document.data!!["societyId"]
                Log.d("societyId", societyId.toString())
                if (societyId != null) {
                    db.collection("societies").document(societyId.toString()).collection("complains")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val data = document.data

                                val url:String? = data["imageUrl"] as? String // Get image URL
//                                Toast.makeText(this@ComplainRaiseHistory, url.toString(), Toast.LENGTH_SHORT).show()

                                var status = "Pending"
                                if (data["approved"] == true)
                                    status = "Approved!"
                                else if (data["resolved"] == true) {
                                    status = "Resolved!"
                                } else if (data["rejected"] == true) {
                                    status = "Rejected!"
                                }

                                val timestamp = data["timestamp"] as Timestamp
                                dummyData = dummyData + listOf(
                                    complainHistoryModel(
                                        url!!, // Pass image URL here
                                        data["type"] as String,
                                        data["title"] as String,
                                        timestamp.toDate(),
                                        status,
                                        data["description"] as String,
                                        data["userHouseNo"] as String
                                    )
                                )
                            }

                            adapter!!.updateData(dummyData)
                        }
                }
            }

    }


}
