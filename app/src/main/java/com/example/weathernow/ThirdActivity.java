package com.example.weathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        backToMain();
    }

    private void backToMain() {
        ImageView backBtn = findViewById(R.id.homeButton);
        backBtn.setOnClickListener(view -> startActivity(new Intent(ThirdActivity.this, MainActivity.class)));
    }
}