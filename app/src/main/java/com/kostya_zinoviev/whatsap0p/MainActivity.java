package com.kostya_zinoviev.whatsap0p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentAdapterViewPager adapter;;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        propertyToolbar();

        viewPager.setAdapter(adapter);

        //Add View Pager for Tablayout
        tabLayout.setupWithViewPager(viewPager);


    }

    private void propertyToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("WhatsApp");
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new FragmentAdapterViewPager(getSupportFragmentManager());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.find_people_menu:
                //Найти людей
                break;

            case R.id.create_group_menu:
                //Создание группы
                break;

            case R.id.settings_menu:
                //Настройки
                Intent settingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            case R.id.log_out_menu:
                //Выйти
                //Так мы выходим из аккаунта mAuth.signOut()
                mAuth.signOut();

                Intent loginActivity = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(loginActivity);
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null){
            Intent welcomeActivity = new Intent(MainActivity.this,WelcomeActivity.class);
            startActivity(welcomeActivity);
        } else {
           verifyUser();
        }
    }

    private void verifyUser() {

        String currentUserId = mAuth.getCurrentUser().getUid();

        rootRef.child("User").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Если в наше бд папка "name " не пустая,о человек уже заходил в наше приложение
                //И мы сразу будем перенаправлять его в наше MainAcivity
                if (dataSnapshot.child("name").exists()){
                    Toast.makeText(MainActivity.this, "Привет", Toast.LENGTH_SHORT).show();
                } else {
                    //Направляем в сетинг активити
                    Intent settingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(settingsIntent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
