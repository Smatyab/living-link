package com.nothing.secad

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.nothing.secad.Adapter.UserTransactionAdapter
import com.nothing.secad.databinding.ActivityUserTransactionShowBinding
import com.nothing.secad.model.userTransaction
import java.util.*

class UserTransactionShowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserTransactionShowBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserTransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserTransactionShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.RVUserTransactionShow
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dummyData = generateDummyData()
        adapter = UserTransactionAdapter(dummyData.toMutableList())
        recyclerView.adapter = adapter

        val firestore = Firebase.firestore
        val userId = Firebase.auth.currentUser?.uid

        var transactions: MutableList<userTransaction> = mutableListOf()


        var searchView= binding.transactionFetchToUserSearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("Search", newText.toString())
                adapter.updateQuery(newText.orEmpty()) // Call updateQuery method
                return true
            }
        })


        firestore.collection("member").whereEqualTo("societyId", userId).get()
            .addOnCompleteListener { result ->
                for (document in result.result) {
                    val memberId = document.id
                    firestore.collection("member").document(memberId).collection("transactions").get()
                        .addOnCompleteListener { result2 ->
                            for (document2 in result2.result) {
                                val data = document2.data
                                transactions.add(
                                    userTransaction(
                                        (data["date"] as Timestamp).toDate(),
                                        (data["amount"] as Long).toInt(),
                                        data["completed"] as Boolean,
                                        document.data["userHouseNo"] as String,
                                        document2.id
                                    )
                                )

                                Log.d("UserTransactionShowActivity", "Transaction: $transactions")
                            }
                            adapter.updateTransactions(transactions)
//                            TODO: run this function on toggle:: true -> ONly unpaid, false all

                            binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (isChecked) {
                                    adapter.showOnlyUnpaidTransactions(true) // Show only unpaid transactions
                                } else {
                                    adapter.showOnlyUnpaidTransactions(false) // Show all transactions
                                }
                            }


                        }
                }}
    }

    private fun generateDummyData(): List<userTransaction> {
        val dummyList = ArrayList<userTransaction>()
        // Add more dummy data if needed
        return dummyList
    }
}
