package com.nothing.secad.meeting.user

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.nothing.secad.R
import com.nothing.secad.databinding.ActivityUseMeetingMainBinding
import java.util.Random
import java.util.UUID

class UseMeetingMainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUseMeetingMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUseMeetingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var name =  findViewById<EditText>(R.id.meeting_name_input)



        binding.createBtn.setOnClickListener(View.OnClickListener { v: View? ->
            val name: String = binding.meetingNameInput.getText().toString()
            if (name.isEmpty()) {
                binding.meetingNameInput.setError("Name is required to join the meeting")
                binding.meetingIdInput.requestFocus()
                return@OnClickListener
            }
            startMeeting(getRandomMeeting(), name)
        })
    }
    fun startMeeting(meetingId: String?, name: String?) {

        val userID = UUID.randomUUID().toString()
        val intent = Intent(this, UserMeetingConsforanceActivity::class.java)
        intent.putExtra("meeting_ID", meetingId)
        intent.putExtra("user_ID", userID)
        intent.putExtra("name", name)
        startActivity(intent)
    }

    fun getRandomMeeting(): String? {
        val id = StringBuilder()
        while (id.length < 10) {
            val random = Random().nextInt(10)
            id.append(random)
        }
        return id.toString()
    }
}