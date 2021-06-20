package com.projeto.msm.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projeto.msm.R;
import com.projeto.msm.adapter.APICall;
import com.projeto.msm.model.AreaFrigorifica;
import com.projeto.msm.model.Rastreabilidade;
import com.projeto.msm.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegistoActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView userName;

    private static User current_user;
    private ArrayList<AreaFrigorifica> list;
    int reqCodeRastreabilidade = 1;
    int reqCodeValidade = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);
        current_user = (User) getIntent().getSerializableExtra("user");
        userName = findViewById(R.id.user_name);
        userName.setText(current_user.getName()+" - "+ current_user.getnumInterno());

        Log.e("Tag", "Registo: " + current_user.toString());
        list = new ArrayList<>();
        getAreaFrigorifica();

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void ClickAddTemp(View view){
        //Building dialog
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.add_temp_form, (ViewGroup) findViewById(R.id.layout_root));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);


        //Update spinner with areafrigorifica
        final EditText number = (EditText) layout.findViewById(R.id.numero);
        final Spinner spinner = (Spinner) layout.findViewById(R.id.arca_id);
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            arrayList.add(list.get(i).getNumero()+" - "+list.get(i).getDesignacao());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!isEmpty(number)){
                    String[] aux = spinner.getSelectedItem().toString().split(" - ");
                    sendAreaFrigorificaTemp(number.getText().toString(), aux[0]);
                }else{
                    Toast.makeText(RegistoActivity.this, R.string.scanner_dialog_error, Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        builder.show();
    }

    public void ClickAddLimpezaArca(View view){
        //Building dialog
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.add_limpeza_form, (ViewGroup) findViewById(R.id.layout_root));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);

        //Update spinner with areafrigorifica
        final Spinner spinner = (Spinner) layout.findViewById(R.id.arca_id);
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            arrayList.add(list.get(i).getNumero()+" - "+list.get(i).getDesignacao());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] aux = spinner.getSelectedItem().toString().split(" - ");
                sendAreaFrigorificaLimepza(aux[0]);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        builder.show();
    }

    public void ClickAddRastreabilidade(View view){
        Intent i = new Intent(getApplicationContext(), RastreabilidadeRegistoActivity.class);
        startActivityForResult(i, reqCodeRastreabilidade);
    }

    public void ClickAddValidade(View view){
        Intent i = new Intent(getApplicationContext(), ValidadeActivity.class);
        i.putExtra("user", current_user);
        startActivityForResult(i, reqCodeValidade);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == reqCodeRastreabilidade) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                if(b != null){
                    sendRastreabilidade((Rastreabilidade) b.getSerializable("new_rastreabilidade"), String.valueOf(current_user.getId()));

                }
            }
        }
        if (requestCode == reqCodeValidade) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                if(b != null){
                    //sendRastreabilidade((Rastreabilidade) b.getSerializable("new_rastreabilidade"), String.valueOf(current_user.getId()));
                    //TO:DO SEND VALIDADE DO BD
                }
            }
        }
    }

    public void ClickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        if(current_user.getTipo() == 0){
            redirectActivity(this, MainActivity.class);
        }else{
            redirectActivity(this, MainActivityAdmin.class);
        }

    }

    public void ClickAreas(View view){
        redirectActivity(this, AreasActivity.class);
    }

    public void ClickRegistos(View view){
        recreate();
    }

    public void ClickLogout(View view){
        current_user = null;
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
        finish();
    }

    public void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent.putExtra("user", current_user));
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
        //Save to sharedPrefs
        if(current_user != null){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("num_interno",current_user.getnumInterno());
            editor.putString("password",current_user.getPassword());
            editor.apply();
        }
    }

    private void sendRastreabilidade(Rastreabilidade rastreabilidade, String id){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("user_id", id);
            paramObject.put("lote", String.valueOf(rastreabilidade.getLote()));
            paramObject.put("produto_id", String.valueOf(rastreabilidade.getNum_interno()));
            paramObject.put("origem", rastreabilidade.getOrigem());
            paramObject.put("fornecedor_id", rastreabilidade.getFornecedor());

            Call<String> rastCall = apiInterface.sendRastreabilidade(current_user.getToken(), paramObject.toString());
            rastCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        try{
                            String res = response.body();
                            if(res != null){
                                if(res.equalsIgnoreCase("\"err_forn\"")){
                                    Toast.makeText(RegistoActivity.this, "O fornecedor não existe!!!", Toast.LENGTH_SHORT).show();
                                }else if(res.equalsIgnoreCase("\"err_prod\"")){
                                    Toast.makeText(RegistoActivity.this, "O produto não existe!!!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(RegistoActivity.this, getString(R.string.registo_dialog_ok_send_rastreabilidade), Toast.LENGTH_SHORT).show();
                                }
                            }
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
                        Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_generic), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(RegistoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Tag", "error" + t.toString());
                    Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_server), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendAreaFrigorificaTemp(String temp, String id){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("user_id", String.valueOf(current_user.getId()));
            paramObject.put("temperatura", temp);
            paramObject.put("area_frigorifica_id", id);

            Call<String> frigCall = apiInterface.areafrigtempsend(current_user.getToken(), paramObject.toString());
            frigCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        try{
                            String id = response.body();
                            Toast.makeText(RegistoActivity.this, getString(R.string.registo_dialog_ok_send), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_generic), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(RegistoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Tag", "error" + t.toString());
                    Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_server), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendAreaFrigorificaLimepza(String id){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("user_limpeza", String.valueOf(current_user.getnumInterno()));

            Call<String> frigCall = apiInterface.areafriglimpeza(current_user.getToken(), id.toString(),paramObject.toString());
            frigCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        try{
                            String id = response.body();
                            Toast.makeText(RegistoActivity.this, getString(R.string.registo_limpeza_dialog_ok_send), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_generic), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(RegistoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Tag", "error" + t.toString());
                    Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_server), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAreaFrigorifica(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        Call<ArrayList<AreaFrigorifica>> call = apiInterface.areafrigtemp(current_user.getToken());
        call.enqueue(new Callback<ArrayList<AreaFrigorifica>>() {
            @Override
            public void onResponse(Call<ArrayList<AreaFrigorifica>> call, Response<ArrayList<AreaFrigorifica>> response) {
                if (response.isSuccessful()) {
                    try{
                        Log.e("Tag", "Array: " + response.body());
                        list = response.body();
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
                    Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_generic), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(RegistoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<AreaFrigorifica>> call, Throwable t) {
                Log.e("Tag", "error" + t.toString());
                Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_server), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }
}