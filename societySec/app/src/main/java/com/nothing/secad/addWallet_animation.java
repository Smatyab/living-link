package com.nothing.secad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import com.nothing.secad.simple.SendPaymentAdmin;

public class addWallet_animation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet_animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(addWallet_animation.this, SendPaymentAdmin.class);
                startActivity(intent);
                finish();
            }
        }, 4000); // Delay in milliseconds (1200 ms = 1.2 seconds)

        Button btn = (Button) findViewById(R.id.animation_add_amount);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(addWallet_animation.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}