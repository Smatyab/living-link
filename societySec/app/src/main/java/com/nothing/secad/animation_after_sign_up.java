package com.nothing.secad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import com.nothing.secad.simple.animation_after_login;

public class animation_after_sign_up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_after_sign_up);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(animation_after_sign_up.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000); // Delay in milliseconds (1200 ms = 1.2 seconds)

        Button btn = (Button) findViewById(R.id.animation_sign_up_to_dashbord);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(animation_after_sign_up.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}