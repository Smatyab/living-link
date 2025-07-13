package com.nothing.societyuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.nothing.societyuser.fragment.BottomActivity

class Complain_success : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complain_success)

        var btn: Button = findViewById(R.id.animation_to_complain);

        btn.setOnClickListener(){
            var intent = Intent(this@Complain_success,BottomActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}