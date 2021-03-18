package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.projeto.msm.R;
import com.projeto.msm.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText textFieldNumero, textFieldPassword;
    private Button buttonLogin;
    private ProgressBar progessBarLogin;

    private User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializarComponentes();

        progessBarLogin.setVisibility(View.GONE);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(textFieldNumero)){
                    if(!isEmpty(textFieldPassword)){
                        current_user = new User();
                        current_user.setNum(Integer.parseInt(textFieldNumero.getText().toString()));
                        current_user.setPassword(textFieldPassword.getText().toString());
                        validarLogin(current_user);
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

    private void validarLogin(User user_a_validar){
        progessBarLogin.setVisibility(View.VISIBLE);
    }
}