package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.projeto.msm.R;
import com.projeto.msm.adapter.APICall;
import com.projeto.msm.adapter.Arcas_ListAdapter;
import com.projeto.msm.model.Area;
import com.projeto.msm.model.AreaFrigorifica;
import com.projeto.msm.model.Capture;
import com.projeto.msm.model.Produto;
import com.projeto.msm.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ValidadeActivity extends AppCompatActivity {

    private ImageButton qrcodescanner;
    private DatePickerDialog picker;
    private EditText date;
    private TextView produto;
    private TextView produto_subtext;
    private EditText ean_text;
    private LinearLayout layout_nome;

    private User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validade);

        qrcodescanner = findViewById(R.id.imageButton);
        ean_text = findViewById(R.id.rastreabilidadeLote);
        produto =  findViewById(R.id.validadeProduto);
        produto_subtext = findViewById(R.id.produto_subtext);
        layout_nome = findViewById(R.id.linearLayout6);
        layout_nome.setVisibility(View.GONE);

        current_user = (User) getIntent().getSerializableExtra("user");

        date =  findViewById(R.id.data_validade);
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int year = cldr.get(Calendar.YEAR);
                int month = cldr.get(Calendar.MONTH);
                int mDay = cldr.get(Calendar.DAY_OF_MONTH);
                // time picker dialog
                picker = new DatePickerDialog(ValidadeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                date.setText(year + "-" + month+1 + "-" + dayOfMonth);
                            }
                        }, year, month, mDay);
                picker.show();
            }
        });


        qrcodescanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(ValidadeActivity.this);
                intentIntegrator.setPrompt("Faça scan ao código");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
            } else {
                ScannerDialogCall(result.getContents());
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void ScannerDialogCall(String codeContent){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_validade, (ViewGroup) findViewById(R.id.layout_root));
        ean_text.setText(codeContent);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        Call<ArrayList<Produto>> call = apiInterface.getProdutoByEAN(current_user.getToken() ,codeContent);

        call.enqueue(new Callback<ArrayList<Produto>>() {
            @Override
            public void onResponse(Call<ArrayList<Produto>> call, Response<ArrayList<Produto>> response) {
                if (response.isSuccessful()) {
                    try{
                        ArrayList<Produto> produtoList = new ArrayList<>();
                        Log.e("Tag", "Array: " + response.body());
                        produtoList = response.body();
                        if(produtoList.size() > 0){
                            Log.e("Tag", "PRODUTO: " + produtoList.get(0).getN_interno());
                            produto.setText("Nº"+String.valueOf(produtoList.get(0).getN_interno()));
                            layout_nome.setVisibility(View.VISIBLE);
                            produto_subtext.setText(produtoList.get(0).getNome() + '(' + produtoList.get(0).getMarca() + ')');
                        }else{
                            Toast.makeText(ValidadeActivity.this, "Não existem produtos registados no sistema com esse EAN!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ValidadeActivity.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        produto.setText("");
                        produto_subtext.setText("");
                        layout_nome.setVisibility(View.GONE);
                        Toast.makeText(ValidadeActivity.this, "Não existem produtos registados no sistema com esse EAN!", Toast.LENGTH_SHORT).show();
                        Log.e("Tag", "error1");
                    } catch (Exception e) {
                        Log.e("Tag", "error2" + e);
                        //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Produto>> call, Throwable t) {
                Log.e("Tag", "error" + t.toString());
                Toast.makeText(ValidadeActivity.this, "Erro no servidor!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClickAceitar(View view){
        if(!isEmpty(ean_text) && !isEmpty(date)){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
            APICall apiInterface = retrofit.create(APICall.class);
            try {
                JSONObject paramObject = new JSONObject();
                paramObject.put("ean", ean_text.getText());
                paramObject.put("validade", String.valueOf(date.getText()));
                Log.e("Tag", "EEEEE" + paramObject.toString());

                Call<String> rastCall = apiInterface.putValidadeByProduto(current_user.getToken(), paramObject.toString());
                rastCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            try{
                                String res = response.body();
                                Log.e("Tag", "EEEEE" + response.body());
                                if(!res.equalsIgnoreCase("[]")){
                                    Toast.makeText(ValidadeActivity.this, "Validade registada com sucesso", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ValidadeActivity.this, "Erro a guardar a informação! (EAN ou data inválida)", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ValidadeActivity.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                Toast.makeText(ValidadeActivity.this, "Erro a guardar a informação! (EAN ou data inválida)", Toast.LENGTH_SHORT).show();
                                Log.e("Tag", "error1");
                            } catch (Exception e) {
                                Log.e("Tag", "error2" + e);
                                //Toast.makeText(ValidadeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("Tag", "error" + t.toString());
                        Toast.makeText(ValidadeActivity.this, getString(R.string.scanner_dialog_error_send), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(ValidadeActivity.this, R.string.scanner_dialog_error, Toast.LENGTH_LONG).show();
        }

    }

    protected void onPause(){
        super.onPause();
        //Save to sharedPrefs
        if(current_user != null){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("num_interno",current_user.getnumInterno());
            editor.putString("password",current_user.getPassword());
            editor.apply();
        }
    }

    public void onClickVoltar(View view){
        finish();
    }

    public void ClickBack(View view){
        finish();
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }
}