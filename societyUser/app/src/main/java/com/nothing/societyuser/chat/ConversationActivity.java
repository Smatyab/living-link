package com.nothing.societyuser.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nothing.societyuser.R;
import com.nothing.societyuser.login_mock;
import com.zegocloud.zimkit.common.ZIMKitRouter;
import com.zegocloud.zimkit.common.enums.ZIMKitConversationType;

public class ConversationActivity extends AppCompatActivity {
    FloatingActionButton actionButton;
    String uid2="";
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        actionButton = findViewById(R.id.floating_button);
        userDataFetch();
        actionButton.setOnClickListener(v -> {
            showPopUpMenu();
        });

        imageView = findViewById(R.id.share_btn_chat);

        imageView.setOnClickListener(v -> {
            Toast.makeText(ConversationActivity.this,"ok",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Below number share with your friend and connect with your friend");
            intent.putExtra(Intent.EXTRA_TEXT, "My chat : "+uid2);
            startActivity(Intent.createChooser(intent,"Share Via"));
        });
    }

    void showPopUpMenu() {
        PopupMenu popmenu = new PopupMenu(this, actionButton);
        popmenu.getMenuInflater().inflate(R.menu.menu, popmenu.getMenu());

        popmenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.new_chat) {
                showNewChatDialog();
            }

            return false;
        });
        popmenu.show();


    }



    void showNewChatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Chat");

        EditText editText = new EditText(this);
        editText.setHint("User ID");
        builder.setView(editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ZIMKitRouter.toMessageActivity(ConversationActivity.this,editText.getText().toString(), ZIMKitConversationType.ZIMKitConversationTypePeer);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
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
                                uid2 = uid;

                                Log.d("tag", "Document data: "  + " " + userName + " " + uid);
                                //connectUser(uid,userName,"");
                                Toast.makeText(ConversationActivity.this,""+uid,Toast.LENGTH_SHORT).show();
                                TextView tv = findViewById(R.id.chat_id_textView);
                                tv.setText("Your Id:" + uid);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       // Toast.makeText(login_mock.this, "Profile Update", Toast.LENGTH_SHORT).show();
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