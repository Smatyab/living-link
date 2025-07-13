package com.nothing.societyuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nothing.societyuser.chat.ConversationActivity;
import com.zegocloud.zimkit.services.ZIMKit;

import im.zego.zim.enums.ZIMErrorCode;

public class login_mock extends AppCompatActivity {
    EditText userIdInput;

    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mock);
        userDataFetch();

//        userIdInput = findViewById(R.id.userid_input);
//        loginButton = findViewById(R.id.loginBtn);

        //Toast.makeText(login_mock.this, "Document data: "  + " " + userName + " " + uid, Toast.LENGTH_SHORT).show();
        // Update the UI on the main thread

//        loginButton.setOnClickListener(v -> {
//            String userId = userIdInput.getText().toString();
//
//            connectUser(userId,userId,"");
//            //firebase data
//
//        });
    }

    public void connectUser(String userId, String userName,String userAvatar) {
        // Logs in.
        ZIMKit.connectUser(userId,userName,userAvatar, errorInfo -> {
            if (errorInfo.code == ZIMErrorCode.SUCCESS) {
                // Operation after successful login. You will be redirected to other modules only after successful login. In this sample code, you will be redirected to the conversation module.
                toConversationActivity();
            } else {

            }
        });
    }
    private void toConversationActivity() {
        // Redirect to the conversation list (Activity) you created.
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
        finish();
    }


    private void userDataFetch() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("member").document(uid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {

                                String userName = documentSnapshot.getString("userName");
                                String socId = documentSnapshot.getString("societyId");
                                String uid = socId+userName;

                                Log.d("tag", "Document data: "  + " " + userName + " " + uid);
                                connectUser(uid,userName,"");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(login_mock.this, "Profile Update", Toast.LENGTH_SHORT).show();
//                                        Glide.with(login_mock.this)
//                                                .load(userImage)
//                                                .placeholder(R.drawable.logo_black_primary) // Optional placeholder image while loading
//                                                .error(R.drawable.logo_black_primary) // Optional error image if loading fails
//                                                .into(binding.updateProfileImageBtn);
                                    }
                                });
                            } else {
                                Log.d("tag", "No such document");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "Error getting documents: " + e.getMessage());
                        }
                    });
        }
    }

}