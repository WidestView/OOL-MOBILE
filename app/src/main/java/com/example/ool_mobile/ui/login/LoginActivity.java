package com.example.ool_mobile.ui.login;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ool_mobile.R;

public class LoginActivity extends AppCompatActivity {

    ConstraintLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layout = findViewById(R.id.layout_login);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            layout.setForceDarkAllowed(false);
        }





    }

    public void logIn(@NonNull View view)
    {

    }
}