package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.projeto.msm.R;
import com.projeto.msm.model.User;

public class RegistoActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    private static User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);
        current_user = (User) getIntent().getSerializableExtra("user");

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void ClickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    public void ClickRegistos(View view){
        recreate();
    }

    public void ClickLogout(View view){
        current_user = null;
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();;
    }

    @Override
    protected void onPause(){
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}