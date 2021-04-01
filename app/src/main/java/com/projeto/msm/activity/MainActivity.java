package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.projeto.msm.R;
import com.projeto.msm.model.User;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    private User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        TextView label = (TextView) findViewById (R.id.homeText);
        current_user = (User) getIntent().getSerializableExtra("user");
        Log.e("Tag", "User" + current_user.getName());
        label.setText(current_user.getName());
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }

    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
}