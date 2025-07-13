package com.nothing.societyuser.wallet


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nothing.societyuser.R
import com.nothing.societyuser.databinding.ActivityWalletBinding

class WalletActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //TODO: Show Total amount to firebase
        fetchCurrentAmount()

        //add money editText
        var editTextTAddWalletMoney = binding.editTextTAddWalletMoney.text.toString().trim()
        //var edtAddMoneyWallet = editTextTAddWalletMoney.toIntOrNull() ?: 0

        //Intent for Pay Button
        binding.FastPayBtnWallet.setOnClickListener() {
            val intent = Intent(this, TransactionHistory::class.java)
            startActivity(intent)
        }

        //TODO: Intent for history button
        binding.historyBtnWallet.setOnClickListener() {
            val intent = Intent(this, TransactionHistory::class.java)
            startActivity(intent)
        }

        //PromoCode Intent
        binding.ll.setOnClickListener() {
            showDialog("Temporary not working")
        }

        //Add money via button 100,200,500
        //100
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
                val intent = Intent(this@WalletActivity, DabitCardActivity::class.java)
                intent.putExtra("amount", amount)
                startActivity(intent)
            } else {
                // If the EditText is empty, show an error message or handle it accordingly
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //intent
    fun intentFun(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
    }

    //toast


    fun amountIncrease(addAmount: Int) {
        val getAmountToEditText = binding.editTextTAddWalletMoney.text.toString().trim()
        // If the editText is empty, default to 0
        var amountAsInteger: Int = if (getAmountToEditText.isNotEmpty()) getAmountToEditText.toInt() else 0
        var newAmount: Int = amountAsInteger + addAmount
        binding.editTextTAddWalletMoney.text.clear()
        binding.editTextTAddWalletMoney.setText(newAmount.toString())
    }

    //show dialog box function
    private fun showDialog(message: String, positiveButtonText: String = "OK") {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("!!!Information!!!")
        builder.setMessage(message)
        builder.setPositiveButton(positiveButtonText) { dialog, which ->
            // Handle the OK button click if needed
            dialog.dismiss()
        }
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }
    //next 2 function for current amount fetch
    private fun fetchCurrentAmount() {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val uid = user.uid
            val memberDocRef = firestore.collection("member").document(uid)

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

}
