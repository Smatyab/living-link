package com.nothing.secad.simple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nothing.secad.DebitCard
import com.nothing.secad.R
import com.nothing.secad.TransactionHistoryAdmin
import com.nothing.secad.databinding.ActivitySendPaymentAdminBinding

class SendPaymentAdmin : AppCompatActivity() {
    private lateinit var binding: ActivitySendPaymentAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendPaymentAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //transaction button
        binding.historyAdminTransaction.setOnClickListener(){


            val intent = Intent(this, TransactionHistoryAdmin::class.java)
            startActivity(intent)
        }

        //get request to admin
        binding.RecivePaymentRequestToAdmin.setOnClickListener() {
            val intent = Intent(this, TransactionHistoryAdmin::class.java)
            startActivity(intent)
        }


        //show total amount
        fetchCurrentAmount()

        binding.historyAdminTransaction.setOnClickListener(){

            val intent = Intent(this, TransactionHistoryAdmin::class.java)
            startActivity(intent)
        }

        //increase amounnt
        binding.add100BtnWallet.setOnClickListener() {
            amountIncrease(100)
        }
        //200
        binding.add200BtnWallet.setOnClickListener() {
            amountIncrease(200)
        }
        //500
        binding.add500BtnWallet.setOnClickListener() {
            amountIncrease(500)
        }

        //addMoneyToWallet button
        binding.addBtnWallet.setOnClickListener() {

            var amountText = binding.editTextTAddWalletMoney.text.toString().trim()
            if (amountText.isNotEmpty()) {
                var amount = amountText
                val intent = Intent(this, DebitCard::class.java)
                intent.putExtra("amount", amount)
                startActivity(intent)
            } else {
                // If the EditText is empty, show an error message or handle it accordingly
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun fetchCurrentAmount() {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val uid = user.uid
            val memberDocRef = firestore.collection("societies").document(uid)

            // Add a snapshot listener to get real-time updates
            memberDocRef.addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    Toast.makeText(this, "Error fetching current amount", Toast.LENGTH_SHORT).show()
                    exception.printStackTrace()
                    return@addSnapshotListener
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val currentAmount = documentSnapshot.getLong("currentAmount")?.toDouble() ?: 0.0
                    // Handle the fetched currentAmount value
                    handleCurrentAmount(currentAmount)
                } else {
                    Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleCurrentAmount(currentAmount: Double) {
        // Handle the fetched currentAmount value
        // You can update UI elements, perform calculations, or any other operations with the currentAmount value
        // For example, you can display the currentAmount in a TextView
        val currentAmountTextView: TextView = findViewById(R.id.current_amount_text_view)
        currentAmountTextView.text = " ₹$currentAmount"
    }


    //button amount increase
    fun amountIncrease(addAmount: Int) {
        val getAmountToEditText = binding.editTextTAddWalletMoney.text.toString().trim()
        // If the editText is empty, default to 0
        var amountAsInteger: Int = if (getAmountToEditText.isNotEmpty()) getAmountToEditText.toInt() else 0
        var newAmount: Int = amountAsInteger + addAmount
        binding.editTextTAddWalletMoney.text.clear()
        binding.editTextTAddWalletMoney.setText(newAmount.toString())
    }
}