package com.nothing.societyuser.wallet

import TransactionHistoryAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nothing.societyuser.Model.TransactionHistoryModel
import com.nothing.societyuser.Model.createTransactionHistoryModel
import com.nothing.societyuser.databasehandler.Transaction
import com.nothing.societyuser.databinding.ActivityTransactionHistoryBinding
import java.util.Date

class TransactionHistory : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionHistoryBinding
    private lateinit var transactionAdapter: TransactionHistoryAdapter
    var adapter: TransactionHistoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize and set up the RecyclerView and Adapter
        transactionAdapter = TransactionHistoryAdapter(mutableListOf())
        binding.transactionHistoryRv.adapter = transactionAdapter
        binding.transactionHistoryRv.layoutManager = LinearLayoutManager(this)



//        simulateDataUpdateAfterDelay()


        var searchView = binding.transactionSearchView

        Log.d("Search", searchView.toString())
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("Search", newText.toString())
                transactionAdapter?.updateQuery(newText!!)
                return true
            }
        })

        createDummyTransactions()

    }

    private fun createDummyTransactions() {
        val dummyList = ArrayList<TransactionHistoryModel>()



        val fireStore = Firebase.firestore
        val user = Firebase.auth.currentUser

        fireStore.collection("member").document(user?.uid ?: "C5r6cqwiemodtmaudeAm")
            .collection("transactions").get()
            .addOnSuccessListener { result ->
//                this.transactions = document.toObject(Member::class.java)?.transactions ?: ArrayList()
                for (document in result) {
                    Log.d("Doc", document.id)

                    var date = document.get("date") as Timestamp
                    dummyList.add(
                        createTransactionHistoryModel(
                            date.toDate(),
                            document.get("amount").toString().toInt(),
                            document.get("completed").toString().toBoolean(),
                            document.id
                        )
                    )
                }
                Log.d("Member", "Transactions: $dummyList")
                Log.d("Member", "UID: ${user?.uid ?: "C5r6cqwiemodtmaudeAm"}")

                updateAdapterWithData(dummyList)
//                payTransaction(dummyList.get(0), this)

//                for (transaction in transactions) {
//                    Log.d("Transaction History", transaction.date.toString())
//                    dummyList.add(
//                        TransactionHistoryModel(
//                            transaction.date,
//                            transaction.amount.toInt(),
//                            transaction.completed,
//                            transaction.id
//                        )
//                    )
//                }
            }


        // Adding dummy transactions
//        dummyList.add(TransactionHistoryModel(Date(), 1000, true))
//        dummyList.add(TransactionHistoryModel(Date(), 1500, false))
//        dummyList.add(TransactionHistoryModel(Date(), 800, true))
        // Add more dummy transactions as needed
    }

    // Example of simulating data update after some delay
//    private fun simulateDataUpdateAfterDelay() {
//        // Simulate fetching new data after a delay (e.g., network request)
//        val newData = createDummyTransactions()
//        val handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            // Update the adapter with the new data
//            updateAdapterWithData(newData)
//        }, 2000) // Simulating a 2-second delay, replace with your actual logic
//
//    }

    // Example of updating data in the adapter
    private fun updateAdapterWithData(newData: List<TransactionHistoryModel>) {
        Log.d("Adapter", "Updating adapter with data: $newData")
        transactionAdapter.updateData(newData)
        transactionAdapter.notifyDataSetChanged()
    }

    //padding and complete transaction show


}
