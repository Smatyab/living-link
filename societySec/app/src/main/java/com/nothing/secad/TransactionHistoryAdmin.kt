package com.nothing.secad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.nothing.secad.Adapter.TransactionHistoryAdapter
import com.nothing.secad.databinding.ActivityTransactionHistoryAdminBinding
import com.nothing.secad.model.TransactionHistoryModel
import java.util.*

class TransactionHistoryAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionHistoryAdminBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionArrayList: MutableList<TransactionHistoryModel>
    private lateinit var myAdapter: TransactionHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionHistoryAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.transactionHistoryRv
        recyclerView.layoutManager = LinearLayoutManager(this)
        transactionArrayList = mutableListOf()
        myAdapter = TransactionHistoryAdapter(transactionArrayList)
        recyclerView.adapter = myAdapter


        var searchView= binding.transactionSearchViewAdmin
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("Search", newText.toString())
                myAdapter.updateQuery(newText.orEmpty()) // Call updateQuery method
                return true
            }
        })

        // Call function to add default hardcoded data
        addDefaultData()
    }

    private fun addDefaultData() {
        Log.d("TransactionHistory", "addDefaultData: Adding default data")

        Firebase.firestore.collection("societies").document(Firebase.auth.currentUser?.uid!!).collection(
            "transactions"
        ).get() .addOnCompleteListener { result ->
            for (document in result.result) {
                val transactionId = document.id
                val data = document.data

                transactionArrayList.add(
                    TransactionHistoryModel(
                        (data["date"] as Timestamp).toDate(),
                        (data["amount"] as Long).toInt(),
                        data["completed"] as Boolean,
                        transactionId
                ))
            }

        }
        myAdapter.updateTransactionList(transactionArrayList)
    }
}


