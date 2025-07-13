import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nothing.societyuser.Model.TransactionHistoryModel
import com.nothing.societyuser.wallet.PayAdmin
import com.nothing.societyuser.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionHistoryAdapter(private var transactionList: MutableList<TransactionHistoryModel>) :
    RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>() {

    var transactionMaster = transactionList

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.transaction_date)
        val amountTextView: TextView = itemView.findViewById(R.id.transaction_amount)
        val statusTextView: TextView = itemView.findViewById(R.id.transaction_sattus)
        val button : Button = itemView.findViewById(R.id.transaction_to_pay_btn)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_item_transaction_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactionList[position]

        val dateFormat = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        val formattedDate = dateFormat.format(transaction.date)



        holder.dateTextView.text = "Date: $formattedDate"
        holder.amountTextView.text = "Amount: ${transaction.amount}"
//        holder.statusTextView.text = "Status: ${transaction.status}"

        if (transaction.status == true){
            holder.statusTextView.text = "Status: Complete"
        }else{
            holder.statusTextView.text = "Status: pending"
        }
        if (transaction.status) { // Assuming transaction.status is a boolean
            holder.button.visibility = View.GONE
        } else {
            holder.button.visibility = View.VISIBLE
            holder.button.setOnClickListener {
                val intent = Intent(it.context, PayAdmin::class.java)
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

    // Update the data in the adapter
    fun updateData(newTransactionList: List<TransactionHistoryModel>) {

        this.transactionList = newTransactionList.toMutableList()
        this.transactionMaster = newTransactionList.toMutableList()

        notifyDataSetChanged()
    }

    fun updateQuery(query: String) {
        this.transactionList = mutableListOf()
        Log.d("query", query)

        for (transaction in transactionMaster) {
            Log.d("transaction", transaction.amount.toString())
            if (transaction.amount.toString().contains(query, ignoreCase = true) || SimpleDateFormat("dd MMMM YYYY", Locale.getDefault()).format(transaction.date).contains(query, ignoreCase = true)) {
                this.transactionList.add(transaction)
            }
        }

        notifyDataSetChanged()
    }


}
