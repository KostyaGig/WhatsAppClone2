package com.kostya_zinoviev.whatsap0p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivtity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText registerEmailEditText, registerPasswordEditText;
    private Button registerButton,sinInButton;
    private ProgressDialog progressDialog;

    private TextView textViewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activtity);
        init();

        registerButton.setOnClickListener(this);
        sinInButton.setOnClickListener(this);
        textViewAccount.setOnClickListener(this);
    }

    private void init() {

        registerEmailEditText = findViewById(R.id.registerEmailEditText);
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        sinInButton = findViewById(R.id.signInButton);

        textViewAccount = findViewById(R.id.textViewAccount);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Загрузка");
        progressDialog.setMessage("Пожалуйста,подождите...");
        progressDialog.setCancelable(false);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String email  = registerEmailEditText.getText().toString().trim();
        String password  = registerPasswordEditText.getText().toString().trim();
        switch (id) {
            case R.id.registerButton:
                //Кнопка регистрации

                progressDialog.show();


                //Если почта и пароль не пустые,то...
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    //TODO ProgressDIalog dismiss!!!!!!!!!!
                    createAccoundWithEmailAndPassword(email,password);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Заполните пустые поля", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.signInButton:
                //Кнопка входа
                progressDialog.show();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    signInAccountWithEmailAndPassword(email,password);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Заполните пустые поля", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.textViewAccount:
                //Текст "Уже есть аккаунт"
                sinInButton.setVisibility(View.VISIBLE);
                textViewAccount.setVisibility(View.INVISIBLE);
                registerButton.setVisibility(View.INVISIBLE);
                break;

        }
    }

    private void signInAccountWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Intent mainIntent = new Intent(RegisterActivtity.this,MainActivity.class);
                    startActivity(mainIntent);

                    progressDialog.dismiss();

                    Toast.makeText(RegisterActivtity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivtity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createAccoundWithEmailAndPassword(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Intent mainIntent = new Intent(RegisterActivtity.this,MainActivity.class);
                    startActivity(mainIntent);

                    progressDialog.dismiss();

                    Toast.makeText(RegisterActivtity.this, "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();



                }else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivtity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
