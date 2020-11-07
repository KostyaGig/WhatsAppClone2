package com.kostya_zinoviev.whatsap0p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.SnackbarContentLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.security.AuthProvider;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginTitleButton, loginNextButton, loginVerificationButton, loginRegisterEmailButton;
    private EditText loginEditText, loginVerificationEditText;
    private TextView smsTextView, plataTextView;
    //Для верификации
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        //OnclickButton

        loginTitleButton.setOnClickListener(this);
        loginNextButton.setOnClickListener(this);
        loginVerificationButton.setOnClickListener(this);
        loginRegisterEmailButton.setOnClickListener(this);

    }

    private void init() {
        loginTitleButton = findViewById(R.id.loginTitleButton);
        loginNextButton = findViewById(R.id.loginNextButton);
        loginVerificationButton = findViewById(R.id.loginVerificationButton);
        loginRegisterEmailButton = findViewById(R.id.loginRegisterEmailButton);

        loginEditText = findViewById(R.id.loginEditText);
        loginVerificationEditText = findViewById(R.id.loginVerificationEditText);

        smsTextView = findViewById(R.id.textView1);
        plataTextView = findViewById(R.id.textView2);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Загрузка");
        progressDialog.setMessage("Пожалуйста,подождите...");
        progressDialog.setCancelable(false);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //При успешной верификации кода
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                //Иначе
                progressDialog.dismiss();
                loginNextButton.setVisibility(View.VISIBLE);
                loginVerificationButton.setVisibility(View.INVISIBLE);
                loginEditText.setVisibility(View.VISIBLE);
                loginVerificationEditText.setVisibility(View.INVISIBLE);
                plataTextView.setVisibility(View.VISIBLE);
                smsTextView.setVisibility(View.INVISIBLE);

                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                //При отправвке кода

                mVerificationId = verificationId;
                mResendToken = token;
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "На ваш номер был отправлен код", Toast.LENGTH_SHORT).show();

                loginNextButton.setVisibility(View.INVISIBLE);
                loginVerificationButton.setVisibility(View.VISIBLE);
                loginEditText.setVisibility(View.INVISIBLE);
                loginVerificationEditText.setVisibility(View.VISIBLE);
                plataTextView.setVisibility(View.INVISIBLE);
                smsTextView.setVisibility(View.VISIBLE);


            }
        };

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.loginTitleButton:
                //ХЗ зачем он)
                break;
            case R.id.loginNextButton:
                //По нажатию на Далее
                progressDialog.show();
                String phoneNumber = loginEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneNumber)){

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            LoginActivity.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show();
                    return;
                }


                break;
            case R.id.loginVerificationButton:
                progressDialog.show();
                //По нажатию на Подтвердить
                String verificationCode = loginVerificationEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(verificationCode)){
                    //Проверка кода смс

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationCode);
                    signInWithPhoneAuthCredential(credential);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Введите код", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.loginRegisterEmailButton:
                Intent registerEmailIntent = new Intent(LoginActivity.this,RegisterActivtity.class);
                startActivity(registerEmailIntent);
                break;
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Успешно!", Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Ошибка!", Toast.LENGTH_SHORT).show();
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
