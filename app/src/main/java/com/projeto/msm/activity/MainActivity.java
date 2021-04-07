package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.projeto.msm.R;
import com.projeto.msm.model.User;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    private static User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        TextView label = (TextView) findViewById (R.id.homeText);
        current_user = (User) getIntent().getSerializableExtra("user");
        //Log.e("Tag", "User on Main (non admin): " + current_user.toString());
        label.setText(current_user.getName());
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }

    public void ClickRegistos(View view){
        redirectActivity(this, RegistoActivity.class);
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent.putExtra("user", current_user));
    }

    public void ClickLogout(View view){
        current_user = null;
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
}