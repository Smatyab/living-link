package com.nothing.secad.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.nothing.secad.R
import com.nothing.secad.model.complainModel
import java.text.SimpleDateFormat
import java.util.*

class ComplainAdapter(private var complainList: MutableList<complainModel>, var context: Context) :
    RecyclerView.Adapter<ComplainAdapter.ComplainViewHolder>() {

    var complainMaster = complainList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplainViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_item_complain, parent, false)
        return ComplainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComplainViewHolder, position: Int) {
        val currentItem = complainList[position]
        holder.imgView.setImageResource(R.drawable.money_transfer_image)
        holder.typeTextView.text = "Type: ${currentItem.type}"
        holder.titleTextView.text = "Title: ${currentItem.title}"
        holder.dateTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(currentItem.date)
        holder.statusTextView.text = "Status: ${currentItem.status}"
        holder.descTextView.text = "Description: ${currentItem.description}"

        fun hideButtons() {
            holder.aproveButton.visibility = View.GONE
            holder.unAproveButton.visibility = View.GONE
        }

        fun updateStatus(status: String) {
            holder.statusTextView.text = "Status: $status"
        }
        // Implement button click listeners here if needed
        holder.aproveButton.setOnClickListener {
            updateComplain(currentItem.itemId, true)
            hideButtons()
            updateStatus("Approved!")
        }

        holder.unAproveButton.setOnClickListener {
            updateComplain(currentItem.itemId, false)
            hideButtons()
            updateStatus("Rejected!")
        }


        if (currentItem.status != "pending") {
            hideButtons()
        }

        Glide.with(context)
            .load(currentItem.imgUrl)
            .placeholder(R.drawable.logo_black_primary) // Optional placeholder image while loading
            .error(R.drawable.logo_black_primary) // Optional error image if loading fails
            .centerCrop()
            .into(holder.imgView)
    }

    override fun getItemCount() = complainList.size

    inner class ComplainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var imgView:ImageView = itemView.findViewById(R.id.complain_history_img)
        var typeTextView:TextView = itemView.findViewById(R.id.complain_history_type)
        var titleTextView:TextView = itemView.findViewById(R.id.complain_history_title)
        var dateTextView:TextView = itemView.findViewById(R.id.complain_history_date)
        var statusTextView:TextView = itemView.findViewById(R.id.complain_history_status)
        var descTextView:TextView = itemView.findViewById(R.id.complain_history_desc)
        var aproveButton:Button = itemView.findViewById(R.id.complain_aproove_btn)
        var unAproveButton:Button = itemView.findViewById(R.id.complain_un_aproove_btn)


    }

    fun addData(dummyData : List<complainModel>) {


        this.complainList = dummyData.toMutableList()
        this.complainMaster = dummyData.toMutableList()

        notifyDataSetChanged()
    }

    fun updateQuery(query: String) {



        complainList = if (query.isNotBlank()) {
            complainMaster.filter { complain ->
                complain.title.contains(query, ignoreCase = true)||complain.type.contains(query, ignoreCase = true)||complain.status.contains(query,ignoreCase = true) || complain.description.contains(query,ignoreCase = true)|| SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(complain.date).contains(query, ignoreCase = true)
            }.toMutableList()
        } else {
            complainMaster.toMutableList()
        }
        notifyDataSetChanged()
    }


}


fun updateComplain(complainId: String, approved: Boolean) {
    var db = Firebase.firestore
    val userid = Firebase.auth.currentUser!!.uid

    if (userid.isNullOrEmpty()) {
        return
    }
    if (approved) {
        db.collection("societies").document(userid).collection("complains").document(complainId)
            .update("approved", true)
    }
    else {
        db.collection("societies").document(userid).collection("complains").document(complainId)
            .update("rejected", true)
    }
}

