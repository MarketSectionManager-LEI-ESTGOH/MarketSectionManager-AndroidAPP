package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projeto.msm.R;
import com.projeto.msm.adapter.APICall;
import com.projeto.msm.adapter.Arcas_ListAdapter;
import com.projeto.msm.adapter.Componentes_ListAdapter;
import com.projeto.msm.model.Area;
import com.projeto.msm.model.Componente;
import com.projeto.msm.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ComponentesActivity extends AppCompatActivity {

    private ArrayList<Componente> componenteList;
    public static ArrayList<Componente> checkedcomponentes;
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
        checkedcomponentes = new ArrayList<>();
        getComponentesArea(area.getNumero());

        nameView.setText(area.getDesignacao());

        Componentes_ListAdapter adapter = new Componentes_ListAdapter(getApplicationContext(), R.layout.componentes_list_adapter, componenteList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringBuilder result = new StringBuilder();
                for(int i=0;i<adapter.mCheckStates.size();i++)
                {
                    if(adapter.mCheckStates.get(i)==true)
                    {
                        result.append("CheckBox: " + position);
                        result.append("\n");
                    }

                }
                //Toast.makeText(ComponentesActivity.this, result, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "Selected: " + areaList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void guardaChecked(View view){
        String aux = "";
        for(int i=0; i < checkedcomponentes.size(); i++){
            aux = aux+" "+checkedcomponentes.get(i).toString();
        }
        Toast.makeText(ComponentesActivity.this, aux, Toast.LENGTH_LONG).show();
    }

    private void getComponentesArea(int area_num){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);

        Call<ArrayList<Componente>> call = apiInterface.getComponentesArea(String.valueOf(area_num));
        call.enqueue(new Callback<ArrayList<Componente>>() {
            @Override
            public void onResponse(Call<ArrayList<Componente>> call, Response<ArrayList<Componente>> response) {
                if (response.isSuccessful()) {
                    try{
                        componenteList = new ArrayList<>();
                        Log.e("Tag", "Array: " + response.body());
                        componenteList = response.body();

                        Componentes_ListAdapter adapter = new Componentes_ListAdapter(getApplicationContext(), R.layout.componentes_list_adapter, componenteList);
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
            public void onFailure(Call<ArrayList<Componente>> call, Throwable t) {
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