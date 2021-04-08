package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projeto.msm.R;
import com.projeto.msm.adapter.APICall;
import com.projeto.msm.model.AreaFrigorifica;
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


    private static User current_user;
    private ArrayList<AreaFrigorifica> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);
        current_user = (User) getIntent().getSerializableExtra("user");
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

        Log.e("Tag", "Array222: " + list.toString());
        Log.e("Tag", "FFFFFFFFF: " + list.get(1));
        //TODO:Update spinner

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                /*
                if(!isEmpty(numBox) && !isEmpty(desigBox) && !isEmpty(fabBox) && !isEmpty(tminBox) && !isEmpty(tmaxBox)){
                    areaFrigorifica = new AreaFrigorifica();
                    areaFrigorifica.setNumero(Integer.parseInt(numBox.getText().toString()));
                    areaFrigorifica.setDesignacao(desigBox.getText().toString());
                    areaFrigorifica.setFabricante(fabBox.getText().toString());
                    areaFrigorifica.setTem_min(Integer.parseInt(tminBox.getText().toString()));
                    areaFrigorifica.setTem_max(Integer.parseInt(tmaxBox.getText().toString()));
                    sendAreaFrigorifica(areaFrigorifica);
                    dialog.dismiss();
                }else{
                    Toast.makeText(MainActivityAdmin.this, R.string.scanner_dialog_error, Toast.LENGTH_LONG).show();
                    ScannerDialogCall(codeContent);
                }
                //TODO:Save info
                */
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

    private void getAreaFrigorifica(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        Call<ArrayList<AreaFrigorifica>> call = apiInterface.areafrigtemp();
        call.enqueue(new Callback<ArrayList<AreaFrigorifica>>() {
            @Override
            public void onResponse(Call<ArrayList<AreaFrigorifica>> call, Response<ArrayList<AreaFrigorifica>> response) {
                if (response.isSuccessful()) {
                    try{
                        //Log.e("Tag", "Array: " + response.body());
                        list = response.body();
                    }catch (NullPointerException npe){
                        npe.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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
                Toast.makeText(RegistoActivity.this, getString(R.string.scanner_dialog_error_send), Toast.LENGTH_SHORT).show();
            }
        });
    }
}