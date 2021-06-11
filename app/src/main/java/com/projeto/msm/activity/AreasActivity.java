package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projeto.msm.R;
import com.projeto.msm.adapter.APICall;
import com.projeto.msm.adapter.Arcas_ListAdapter;
import com.projeto.msm.adapter.TreeColumn_ListAdapter;
import com.projeto.msm.model.Area;
import com.projeto.msm.model.Temperatura;
import com.projeto.msm.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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

        getAreas();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ComponentesActivity.class);
                intent.putExtra("user", current_user);
                intent.putExtra("area", areaList.get(position));
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Selected: " + areaList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAreas() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        Call<ArrayList<Area>> call = apiInterface.getAreas();

        call.enqueue(new Callback<ArrayList<Area>>() {
            @Override
            public void onResponse(Call<ArrayList<Area>> call, Response<ArrayList<Area>> response) {
                if (response.isSuccessful()) {
                    try{
                        areaList = new ArrayList<>();
                        Log.e("Tag", "Array: " + response.body());
                        areaList = response.body();

                        Arcas_ListAdapter adapter = new Arcas_ListAdapter(getApplicationContext(), R.layout.arca_list_adapter, areaList);
                        listView.setAdapter(adapter);

                    }catch (NullPointerException npe){
                        npe.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    try {
                        //Toast.makeText(MainActivity.this, getString(R.string.scanner_dialog_error_generic), Toast.LENGTH_SHORT).show();
                        Log.e("Tag", "error1");
                    } catch (Exception e) {
                        Log.e("Tag", "error2" + e);
                        //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Area>> call, Throwable t) {
                Log.e("Tag", "error" + t.toString());
                //Toast.makeText(MainActivity.this, getString(R.string.scanner_dialog_error_send), Toast.LENGTH_SHORT).show();
            }
        });
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


    public void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent.putExtra("user", current_user));
        finish();
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