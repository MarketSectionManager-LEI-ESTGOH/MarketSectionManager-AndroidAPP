package com.projeto.msm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projeto.msm.R;
import com.projeto.msm.adapter.APICall;
import com.projeto.msm.adapter.TreeColumn_ListAdapter;
import com.projeto.msm.model.Temperatura;
import com.projeto.msm.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Temperatura> temperaturaList;

    DrawerLayout drawerLayout;
    ListView listView;

    private static User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        listView = (ListView) findViewById(R.id.listview);

        current_user = (User) getIntent().getSerializableExtra("user");
        getUserLastInputs(current_user.getId());
        //Log.e("Tag", "BRUH: " + temperaturaList.get(1).getArca_frigorifica_id());


        //Log.e("Tag", "User on Main (non admin): " + temperaturaList.get(0));
    }

    private void getUserLastInputs(int id) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        Call<ArrayList<Temperatura>> call = apiInterface.areafrigtempByuser(String.valueOf(id));

        call.enqueue(new Callback<ArrayList<Temperatura>>() {
            @Override
            public void onResponse(Call<ArrayList<Temperatura>> call, Response<ArrayList<Temperatura>> response) {
                if (response.isSuccessful()) {
                    try{
                        temperaturaList = new ArrayList<>();
                        //Log.e("Tag", "Array: " + response.body());
                        temperaturaList = response.body();

                        TreeColumn_ListAdapter adapter = new TreeColumn_ListAdapter(getApplicationContext(), R.layout.list_adapter, temperaturaList);
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
            public void onFailure(Call<ArrayList<Temperatura>> call, Throwable t) {
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