package com.nothing.secad.meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nothing.secad.R;

import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class ZoomMainActivity extends AppCompatActivity {
    Button joinButton;
    EditText name, meetingId, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_activity_main);
//        eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBLZXkiOiI4Vm5Db25GN1I1S2hOY0o3MlJ3WFF3IiwiaWF0IjoxNzA4ODU2Mzg0LCJleHAiOjE4MDg4NTYzODQsInRva2VuRXhwIjoxODA4ODU2Mzg0fQ.rMTYAjjYXUhKKxnJT90_hqv27TN7jpszw4IB2hdgi4Y
        name = findViewById(R.id.meeting_name);
        meetingId = findViewById(R.id.meeting_Id);
        password = findViewById(R.id.meeting_password);
        joinButton = findViewById(R.id.btn);

        initializeZoomSdk(ZoomMainActivity.this);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = name.getText().toString().trim();
                String meeting1 = "4331480358";
                String password1 = "123";

                if (name1.length()>0){

                    startMeeting(name1,meeting1,password1, ZoomMainActivity.this);

                }else {
                    Toast.makeText(ZoomMainActivity.this, "Invalid details", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void startMeeting(String name1, String meeting1, String password1, Context context) {

        MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
        JoinMeetingOptions joinMeetingOptions = new JoinMeetingOptions();
        JoinMeetingParams params = new JoinMeetingParams();
        params.displayName = name1;
        params.password= password1;
        params.meetingNo = meeting1;
        meetingService.joinMeetingWithParams(context,params,joinMeetingOptions);


    }

    private void initializeZoomSdk(Context context) {
        ZoomSDK sdk = ZoomSDK.getInstance();
        ZoomSDKInitParams params = new ZoomSDKInitParams();
//        params.appkey = "";
//        params.appSecret = "";
        params.jwtToken ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBLZXkiOiI4Vm5Db25GN1I1S2hOY0o3MlJ3WFF3IiwiaWF0IjoxNzA4ODU2Mzg0LCJleHAiOjE4MDg4NTYzODQsInRva2VuRXhwIjoxODA4ODU2Mzg0fQ.rMTYAjjYXUhKKxnJT90_hqv27TN7jpszw4IB2hdgi4Y";
        //params.jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBLZXkiOiJjUkloS2JoV1NNYVE3YWIxdnVsRWt3IiwiaWF0IjoxNzA4ODU2Mzg0LCJleHAiOjE4MDg4NTYzODQsInRva2VuRXhwIjoxODA4ODU2Mzg0fQ.T_hPyOqIYYyb_MfCgow_k6rrH_IHaRxYYL80bNJ2Hec";
        // params.jwtToken ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJCSlZvN3BFU1I3S1QzV0hNVW9OU1pRIiwiZXhwIjoxNzQwNDQ5OTk1fQ.SqOayGD1faNCviCi9JPC2rNis4tfoOpzBa8r4DsgZcE";
        params.domain = "zoom.us";
        params.enableLog = false;
        ZoomSDKInitializeListener zoomSDKInitializeListener = new ZoomSDKInitializeListener() {
            @Override
            public void onZoomSDKInitializeResult(int i, int i1) {

            }

            @Override
            public void onZoomAuthIdentityExpired() {

            }
        };
        sdk.initialize(context,zoomSDKInitializeListener,params);

    }
}