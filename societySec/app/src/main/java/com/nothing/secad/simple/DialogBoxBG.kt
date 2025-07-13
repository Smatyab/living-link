package com.nothing.secad.simple

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.nothing.secad.R

class DialogBoxBG : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_box_bg)

        showCustomDialog()

    }

    private fun showCustomDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.form_before_dialogbox, null)
        builder.setView(dialogView)

        val positiveButton: Button = dialogView.findViewById(R.id.btn_dialogBuilding)
        val negativeButton: Button = dialogView.findViewById(R.id.btn_dialogRowHouse)

        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        

        positiveButton.setOnClickListener {
            Toast.makeText(this@DialogBoxBG,"building", Toast.LENGTH_LONG).show();
            val intent = Intent(this, BuildingForm::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        negativeButton.setOnClickListener {
            // Perform the desired action when Cancel is clicked
            Toast.makeText(this@DialogBoxBG,"row house", Toast.LENGTH_LONG).show();
            val intent = Intent(this, BuildingForm::class.java)
            intent.putExtra("check", "abc")
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }
}