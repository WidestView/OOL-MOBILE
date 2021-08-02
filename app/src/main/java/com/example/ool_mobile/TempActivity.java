package com.example.ool_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ool_mobile.ui.add_photo_shoot.AddPhotoShootActivity;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, AddPhotoShootActivity.class);
        startActivity(intent);
    }
}