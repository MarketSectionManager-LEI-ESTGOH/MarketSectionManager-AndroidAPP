package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    TextView userName;

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

        userName = findViewById(R.id.user_name);
        userName.setText(current_user.getName()+" - "+ current_user.getnumInterno());

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
        /*
        String aux = "";
        for(int i=0; i < checkedcomponentes.size(); i++){
            aux = aux+" "+checkedcomponentes.get(i).toString();
        }
        Toast.makeText(ComponentesActivity.this, aux, Toast.LENGTH_LONG).show();
        */
        if(checkedcomponentes.size() == 0){
            Toast.makeText(ComponentesActivity.this, getString(R.string.scanner_dialog_error_no_componetes), Toast.LENGTH_SHORT).show();
        }else{
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
            APICall apiInterface = retrofit.create(APICall.class);
            try {
                JSONObject paramObject = new JSONObject();
                //Log.e("Tag", "EEEEE" + checkedcomponentes.toString());
                int count = 0;
                for(int i = 0; i < checkedcomponentes.size(); i++){
                    paramObject.put(String.valueOf(count), String.valueOf(area.getId()));
                    paramObject.put(String.valueOf(count+1), String.valueOf(checkedcomponentes.get(i).getId()));
                    count = count+2;
                }
                Log.e("Tag", "EEEEE" + paramObject.toString());

                Call<String> rastCall = apiInterface.putLimpezaComponentesArea(current_user.getToken(), String.valueOf(current_user.getId()) , paramObject.toString());
                rastCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            try{
                                String res = response.body();
                                Log.e("Tag", "EEEEE" + response.body());
                                if(!res.equalsIgnoreCase("[]")){
                                    Toast.makeText(ComponentesActivity.this, getString(R.string.registo_limpeza_dialog_ok_send), Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(ComponentesActivity.this, getString(R.string.scanner_dialog_error_generic), Toast.LENGTH_SHORT).show();
                                }
                            }catch (NullPointerException npe){
                                npe.printStackTrace();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else if(response.code() == 403){
                            current_user = null;
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                            Toast.makeText(ComponentesActivity.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                Toast.makeText(ComponentesActivity.this, getString(R.string.scanner_dialog_error_generic), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(ComponentesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("Tag", "error" + t.toString());
                        Toast.makeText(ComponentesActivity.this, getString(R.string.scanner_dialog_error_send), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getComponentesArea(int area_num){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);

        Call<ArrayList<Componente>> call = apiInterface.getComponentesArea(current_user.getToken(), String.valueOf(area_num));
        call.enqueue(new Callback<ArrayList<Componente>>() {
            @Override
            public void onResponse(Call<ArrayList<Componente>> call, Response<ArrayList<Componente>> response) {
                if (response.isSuccessful()) {
                    try{
                        componenteList = new ArrayList<>();
                        componenteList = response.body();
                        Log.e("Tag", "Array->: " + componenteList.toString());
                        /*
                        ArrayList<String> comp_names = new ArrayList<>();
                        if(componenteList.size() > 0){
                            for(int i = 0; i < componenteList.size(); i++){
                                String c_name = componenteList.get(i).getDesignacao();
                                if(!comp_names.contains(c_name)){
                                    comp_names.add(c_name);
                                }
                            }
                            if(comp_names.size() > 0){
                                for(int i = 0; i < comp_names.size(); i++){
                                    Componente num1 = new Componente();
                                    num1.setDesignacao(comp_names.get(i));
                                    num1.setData("1999-01-01");
                                    for(int x = 0; x < componenteList.size(); x++){
                                        if(componenteList.get(x).getDesignacao().equalsIgnoreCase(comp_names.get(i))){
                                            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                                            Date d1 = sdformat.parse(num1.getData());
                                            Date d2 = sdformat.parse(componenteList.get(x).getData());
                                            if(d1.compareTo(d2) < 0) {
                                                num1.setData(componenteList.get(x).getData());
                                                System.out.println("Date 1 occurs before Date 2");
                                            }else{

                                            }
                                        }
                                    }
                                }
                            }
                            Log.e("Tag", "Array: " + comp_names.toString());
                        }
                        */

                        Componentes_ListAdapter adapter = new Componentes_ListAdapter(getApplicationContext(), R.layout.componentes_list_adapter, componenteList);
                        listView.setAdapter(adapter);

                    }catch (NullPointerException npe){
                        npe.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(response.code() == 403){
                    current_user = null;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear().commit();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                    Toast.makeText(ComponentesActivity.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        Toast.makeText(ComponentesActivity.this, getString(R.string.scanner_dialog_error_server), Toast.LENGTH_SHORT).show();
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

    public void SideMenu(View view){ }

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


    public void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent.putExtra("user", current_user));
        finish();
    }

    public void ClickLogout(View view){
        current_user = null;
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
        //Save to sharedPrefs
        if(current_user != null){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("num_interno",current_user.getnumInterno());
            editor.putString("password",current_user.getPassword());
            editor.apply();
        }
    }
}