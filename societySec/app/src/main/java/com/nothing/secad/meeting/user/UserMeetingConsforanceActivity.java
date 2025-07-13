package com.nothing.secad.meeting.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nothing.secad.R;
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceConfig;
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceFragment;

public class UserMeetingConsforanceActivity extends AppCompatActivity {
    TextView meetingIdText;
    ImageView shareBtn;
    String meetingID,UserID,name;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_meeting_consforance);

        sharedPreferences = getSharedPreferences("name_pref",MODE_PRIVATE);

        meetingIdText = findViewById(R.id.meeting_id_textView);
        shareBtn = findViewById(R.id.share_btn);


        meetingID = getIntent().getStringExtra("meeting_ID");
        UserID = getIntent().getStringExtra("user_ID");
        name = getIntent().getStringExtra("name");



        meetingIdText.setText("Meeting ID : "+meetingID);
        addFragment();

        shareBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Join LL society this meeting : "+meetingID);
            startActivity(Intent.createChooser(intent,"Share Via"));
        });
    }

    public void addFragment() {
        long appID = 1841163551;
        String appSign = "2f8df112c2c8434d52a43a1a3053110bfd8b6ce3257149736684c869ec95ced4";



        ZegoUIKitPrebuiltVideoConferenceConfig config = new ZegoUIKitPrebuiltVideoConferenceConfig();
        ZegoUIKitPrebuiltVideoConferenceFragment fragment = ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(appID, appSign,UserID, name,meetingID,config);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }

}