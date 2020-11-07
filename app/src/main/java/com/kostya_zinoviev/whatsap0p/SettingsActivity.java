package com.kostya_zinoviev.whatsap0p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView circleImageView;
    private Button savedInfo;
    private EditText userName,userStatus;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

        savedInfo.setOnClickListener(this);
        userName.setVisibility(View.INVISIBLE);
    }

    private void init() {
        circleImageView = findViewById(R.id.profile_image);
        savedInfo = findViewById(R.id.save_info_user);
        userName = findViewById(R.id.user_name);
        userStatus = findViewById(R.id.user_status);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.save_info_user:
                updateUserInfo();
                getInfoUser();
                break;
        }
    }
        private void updateUserInfo() {
            String name = userName.getText().toString().trim();
            String status = userStatus.getText().toString().trim();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(status)){
                HashMap<String,Object> profileMap = new HashMap<>();
                profileMap.put("uid",currentUserId);
                profileMap.put("name",name);
                profileMap.put("status",status);
                //Вставка дынных в DataBasREeference
                rootRef.child("User").child(currentUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(SettingsActivity.this, "Информация обновлена!", Toast.LENGTH_SHORT).show();

                            Intent mainIntent = new Intent(SettingsActivity.this,MainActivity.class);
                            startActivity(mainIntent);


                        }else {
                            Toast.makeText(SettingsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                Toast.makeText(this, "Введите имя и статус!", Toast.LENGTH_SHORT).show();
            }

    }
   private void getInfoUser(){
        //Получем данные из бд из папки User под папки current.UserId
        rootRef.child("User").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Если  у нас существует запись внутри
                        //И существует подПапка name dataSnapshot.hasChild("name")
                        if (dataSnapshot.exists() && dataSnapshot.hasChild("name")){
                            //Получаем имя из бд , под папки name.getValue и приводим к типу String
                            //Так же и статус полчаем
                            String getUserName = dataSnapshot.child("name").getValue().toString();
                            String getUserStatus = dataSnapshot.child("status").getValue().toString();

                            userName.setText(getUserName);
                            userStatus.setText(getUserStatus);
                        } else {
                            //А если имя не заполеное и не существует записи name,то нужно
                            //Предложить пользователю его заполнить
                            userName.setVisibility(View.INVISIBLE);
                            Toast.makeText(SettingsActivity.this, "Введите свое имя", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}


