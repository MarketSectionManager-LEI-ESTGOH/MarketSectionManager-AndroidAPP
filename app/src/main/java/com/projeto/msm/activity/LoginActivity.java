package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.projeto.msm.R;
import com.projeto.msm.adapter.APICall;
import com.projeto.msm.adapter.RetroClass;
import com.projeto.msm.model.Encryption;
import com.projeto.msm.model.JWTToken;
import com.projeto.msm.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity{

    private EditText textFieldNumero, textFieldPassword;
    private Button buttonLogin;
    private ProgressBar progessBarLogin;

    private User current_user;
    private Encryption encryption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_login);
        inicializarComponentes();
        progessBarLogin.setVisibility(View.GONE);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(textFieldNumero)){
                    if(!isEmpty(textFieldPassword)){
                        validarLogin();
                    }else{
                        Toast.makeText(LoginActivity.this, getString(R.string.no_password_warning), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, getString(R.string.no_num_warning), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inicializarComponentes(){
        textFieldNumero = findViewById(R.id.editLoginNum);
        textFieldPassword = findViewById(R.id.editLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        progessBarLogin = findViewById(R.id.progressBarLogin);
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    private void validarLogin(){
        //Create current user
        current_user = new User();
        current_user.setNum(Integer.parseInt(textFieldNumero.getText().toString()));
        current_user.setPassword(textFieldPassword.getText().toString());
        //Set bar visible
        progessBarLogin.setVisibility(View.VISIBLE);
        //Retrofit request
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("num_interno", String.valueOf(current_user.getnumInterno()));
            paramObject.put("password", encryption.encrypt(current_user.getPassword()));
            Log.e(" Infor => ", String.valueOf(current_user.getnumInterno())+" "+ encryption.encrypt(current_user.getPassword()));
            Call<User> userCall = apiInterface.login(paramObject.toString());
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        //Update user info
                        current_user = response.body();
                        Log.e(" Full json gson => ", response.body().toString());
                        if(encryption.validatePassword(textFieldPassword.getText().toString(),current_user.getPassword())){
                            try{
                                //Log.e(" Full json gson => ", response.body().toString());
                                progessBarLogin.setVisibility(View.GONE);

                                if(current_user.getTipo() == 1){
                                    startActivity(new Intent(getApplicationContext(), MainActivityAdmin.class).putExtra("user", current_user));
                                    finish();
                                }else{
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("user", current_user));
                                    finish();
                                }

                            }catch (NullPointerException npe){
                                npe.printStackTrace();
                                progessBarLogin.setVisibility(View.GONE);
                            }catch (Exception e){
                                e.printStackTrace();
                                progessBarLogin.setVisibility(View.GONE);
                            }
                        }else{
                            current_user = null;
                            progessBarLogin.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        try {
                            progessBarLogin.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Tag", "error" + t.toString());
                    Toast.makeText(LoginActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    progessBarLogin.setVisibility(View.GONE);
                    //TODO: Reset activity?;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}