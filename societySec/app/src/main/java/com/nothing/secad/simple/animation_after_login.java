package com.nothing.secad.simple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Button;

import com.nothing.secad.HomeActivity;
import com.nothing.secad.R;

public class animation_after_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_after_login);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(animation_after_login.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000); // Delay in milliseconds (1200 ms = 1.2 seconds)

        Button btn = (Button) findViewById(R.id.animation_login_to_dashbord);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(animation_after_login.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });




    }
}