package com.kostya_zinoviev.whatsap0p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button agreeButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        agreeButton = findViewById(R.id.agreeButton);
        agreeButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.agreeButton:
                Intent loginIntent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null){
            Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(mainIntent);
        }
    }
}
