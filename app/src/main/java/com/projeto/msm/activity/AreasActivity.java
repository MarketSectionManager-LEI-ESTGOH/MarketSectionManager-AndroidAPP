package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.projeto.msm.R;
import com.projeto.msm.adapter.Arcas_ListAdapter;
import com.projeto.msm.adapter.TreeColumn_ListAdapter;
import com.projeto.msm.model.Area;
import com.projeto.msm.model.User;

import java.util.ArrayList;

import static com.projeto.msm.activity.MainActivity.closeDrawer;

public class AreasActivity extends AppCompatActivity {

    private ArrayList<Area> areaList;

    DrawerLayout drawerLayout;
    ListView listView;

    private static User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);

        drawerLayout = findViewById(R.id.drawer_layout);

        listView = (ListView) findViewById(R.id.areas_listView);

        current_user = (User) getIntent().getSerializableExtra("user");

        areaList = new ArrayList<>();
        areaList.add(new Area());
        areaList.add(new Area());

        Arcas_ListAdapter adapter = new Arcas_ListAdapter(getApplicationContext(), R.layout.arca_list_adapter, areaList);
        Log.e("Tag", "Array: " + areaList);
        listView.setAdapter(adapter);
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
        redirectActivity(this, MainActivity.class);
    }

    public void ClickRegistos(View view){
        redirectActivity(this, RegistoActivity.class);
    }

    public void ClickAreas(View view){
        recreate();
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