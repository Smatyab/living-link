package com.nothing.secad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nothing.secad.databinding.ActivityMeetingUserSecBothWayBinding
import com.nothing.secad.meeting.ZoomMainActivity
import com.nothing.secad.meeting.user.UseMeetingMainActivity

class MeetingUserSecBothWayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMeetingUserSecBothWayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetingUserSecBothWayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.connectWithUserMeeting.setOnClickListener {
            var intent = Intent(this, UseMeetingMainActivity::class.java)
            startActivity(intent)
        }

        binding.connectWithSecMeeting.setOnClickListener(){
            var intent = Intent(this, ZoomMainActivity::class.java)
            startActivity(intent)
        }
    }
}