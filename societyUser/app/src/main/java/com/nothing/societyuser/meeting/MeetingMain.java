package com.nothing.societyuser.meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nothing.societyuser.R;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;

public class MeetingMain extends AppCompatActivity {

    EditText meetingIdInput, nameInput;
    MaterialButton joinBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_main);

        meetingIdInput = findViewById(R.id.meeting_id_input);
        nameInput = findViewById(R.id.meeting_name_input);
        joinBtn = findViewById(R.id.join_btn);


        joinBtn.setOnClickListener((v -> {
            String meetingId = meetingIdInput.getText().toString();
            if (meetingId.length() != 10) {
                meetingIdInput.setError("Invalid Meeting Id");
                meetingIdInput.requestFocus();
                return;
            }

            String name = nameInput.getText().toString();
            if (name.isEmpty()) {
                nameInput.setError("Name is required to join the meeting");
                meetingIdInput.requestFocus();
                return;
            }
            startMeeting(meetingId, name);

        }));

    }

    void startMeeting(String meetingId, String name) {
        String userID = UUID.randomUUID().toString();
        Intent intent = new Intent(this, MeetingConsforanceActivity.class);
        intent.putExtra("meeting_ID",meetingId);
        intent.putExtra("user_ID", userID);
        intent.putExtra("name", name);
        startActivity(intent);

    }

    String getRandomMeeting(){

        StringBuilder id = new StringBuilder();
        while (id.length() < 10) {
            int random = new Random().nextInt(10);
            id.append(random);
        }
        return id.toString();


    }
}