package com.nothing.secad.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nothing.secad.R
import com.nothing.secad.model.userTransaction
import java.text.SimpleDateFormat
import java.util.*

class UserTransactionAdapter(private var transactions: MutableList<userTransaction>) : RecyclerView.Adapter<UserTransactionAdapter.UserTransactionViewHolder>() {

    var transactionsMaster = transactions.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_item_user_transaction_show, parent, false)
        return UserTransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserTransactionViewHolder, position: Int) {
        val currentItem = transactions[position]

        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        holder.transactionDate.text = "Date: ${dateFormat.format(currentItem.date)}"
        holder.transactionAmount.text = "Amount: ${currentItem.amount}"
        holder.transactionStatus.text = "Status: ${if (currentItem.status) "Paid" else "Unpaid"}"
        holder.homeNo.text = "Home No: ${currentItem.homeNo}"

        holder.fastPayButton.setOnClickListener {
            // Handle button click here
        }
    }

    override fun getItemCount() = transactions.size

    class UserTransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionDate: TextView = itemView.findViewById(R.id.transaction_date)
        val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount)
        val transactionStatus: TextView = itemView.findViewById(R.id.transaction_status)
        val homeNo: TextView = itemView.findViewById(R.id.home_no)
        val fastPayButton: Button = itemView.findViewById(R.id.transaction_to_pay_btn)
    }

    fun updateTransactions(newTransactions: MutableList<userTransaction>) {
        this.transactions = newTransactions
        this.transactionsMaster = newTransactions
        notifyDataSetChanged()
    }
    fun updateQuery(query: String) {
        transactions = if (query.isNotBlank()) {
            transactionsMaster.filter { transaction ->
                transaction.amount.toString().contains(query, ignoreCase = true) ||
                        transaction.homeNo.toString().contains(query, ignoreCase = true) ||
                        transaction.status.toString().contains(query, ignoreCase = true)||
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(transaction.date).contains(query, ignoreCase = true)

            }.toMutableList()
        } else {
            transactionsMaster.toMutableList()
            //ok
        }
        notifyDataSetChanged()
    }

    fun showOnlyUnpaidTransactions(onlyUnpaid : Boolean) {
        Log.d("UserTransactionAdapter", "showOnlyUnpaidTransactions: $transactions all $transactionsMaster")
        if (onlyUnpaid) {
            transactions = transactions.filter { !it.status }.toMutableList()
        } else {
            transactions = transactionsMaster
        }
        notifyDataSetChanged()
    }




}
