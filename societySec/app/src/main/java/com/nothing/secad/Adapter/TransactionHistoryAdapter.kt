package com.nothing.secad.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nothing.secad.R
import com.nothing.secad.model.TransactionHistoryModel
import com.nothing.secad.simple.PayToAdmin
import java.text.SimpleDateFormat
import java.util.*

class TransactionHistoryAdapter(private var transactionList: MutableList<TransactionHistoryModel>) :
    RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>() {

        var transactionListMaster = transactionList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_history_single_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.bind(transaction)


        if (transaction.status){
            holder.transactionStatus.text = "Status: Complete"
        }else{
            holder.transactionStatus.text = "Status: pending"
        }

        if (transaction.status) { // Assuming transaction.status is a boolean
            holder.transactionToPayBtn.visibility = View.GONE
        } else {
            holder.transactionToPayBtn.visibility = View.VISIBLE
            holder.transactionToPayBtn.setOnClickListener {
                val intent = Intent(it.context, PayToAdmin::class.java)
                intent.putExtra("transactionId", transaction.id)
                intent.putExtra("transactionDate", transaction.date)
                intent.putExtra("transactionAmount", transaction.amount)
                intent.putExtra("transactionStatus", transaction.status)
                it.context.startActivity(intent)
            }
        }


    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val transactionDate: TextView = itemView.findViewById(R.id.transaction_date)
        private val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount)
        val transactionStatus: TextView = itemView.findViewById(R.id.transaction_status)
        val transactionToPayBtn: Button = itemView.findViewById(R.id.transaction_to_pay_btn)


        fun bind(transaction: TransactionHistoryModel) {
            val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            val formattedDate = dateFormat.format(transaction.date)
            transactionDate.text = "Date: $formattedDate"
            transactionAmount.text = "Amount: ${transaction.amount}"
            transactionStatus.text = "Status: ${if (transaction.status) "Paid" else "Pending"}"
            transactionToPayBtn.setOnClickListener {
                // Handle button click here
            }
        }
    }

    fun updateTransactionList(updatedList: MutableList<TransactionHistoryModel>) {
        transactionList = updatedList
        transactionListMaster = updatedList
        notifyDataSetChanged()
    }

    fun updateQuery(query: String) {
        transactionList = if (query.isNotBlank()) {
            transactionListMaster.filter { transaction ->
                transaction.amount.toString().contains(query,ignoreCase = true)|| transaction.status.toString().contains(query, ignoreCase = true)|| SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(transaction.date).contains(query, ignoreCase = true)
            }.toMutableList()
        } else {
            transactionListMaster.toMutableList()
        }
        notifyDataSetChanged()
    }
}
