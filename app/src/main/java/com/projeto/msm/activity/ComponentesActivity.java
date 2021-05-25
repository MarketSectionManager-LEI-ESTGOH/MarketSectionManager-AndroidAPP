package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.projeto.msm.R;
import com.projeto.msm.adapter.Componentes_ListAdapter;
import com.projeto.msm.model.Area;
import com.projeto.msm.model.Componente;
import com.projeto.msm.model.User;

import java.util.ArrayList;

public class ComponentesActivity extends AppCompatActivity {

    private ArrayList<Componente> componenteList;
    private Area area;

    DrawerLayout drawerLayout;
    ListView listView;
    TextView nameView;

    private static User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_componentes);

        drawerLayout = findViewById(R.id.drawer_layout);

        listView = (ListView) findViewById(R.id.componentes_listView);
        nameView = (TextView) findViewById(R.id.textArea);

        current_user = (User) getIntent().getSerializableExtra("user");
        area = (Area) getIntent().getSerializableExtra("area");



        componenteList = new ArrayList<>();
        componenteList.add(new Componente("Desig 1", 1));
        componenteList.add(new Componente("Desig 2", 2));

        nameView.setText(area.getDesignacao());

        Componentes_ListAdapter adapter = new Componentes_ListAdapter(getApplicationContext(), R.layout.componentes_list_adapter, componenteList);
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
        redirectActivity(this, AreasActivity.class);
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