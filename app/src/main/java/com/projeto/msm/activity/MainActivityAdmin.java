package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.projeto.msm.R;
import com.projeto.msm.adapter.APICall;
import com.projeto.msm.model.AreaFrigorifica;
import com.projeto.msm.model.Capture;
import com.projeto.msm.model.User;
import com.projeto.msm.fragment.ScannedDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivityAdmin extends AppCompatActivity {

    DrawerLayout drawerLayout;
    private ImageButton qrcodescanner;

    private static User current_user;
    private AreaFrigorifica areaFrigorifica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        inicializarComponentes();


        TextView label = (TextView) findViewById (R.id.homeText);
        current_user = (User) getIntent().getSerializableExtra("user");
        Log.e("Tag", "User on Main (admin): " + current_user.toString());
        label.setText(current_user.getName());

        qrcodescanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivityAdmin.this);
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

    private void inicializarComponentes(){
        drawerLayout = findViewById(R.id.drawer_layout);
        qrcodescanner = findViewById(R.id.qrcode_scanner);
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }

    public void ClickLogout(View view){
        current_user = null;
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void ClickRegistos(View view){
        redirectActivity(this, RegistoActivity.class);
    }

    public void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent.putExtra("user", current_user));
        finish();
    }

    private void ScannerDialogCall(String codeContent){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.scanner_result, (ViewGroup) findViewById(R.id.layout_root));

        final EditText numBox = (EditText) layout.findViewById(R.id.numero);
        final EditText desigBox = (EditText) layout.findViewById(R.id.designacao);
        final EditText fabBox = (EditText) layout.findViewById(R.id.fabricante);
        final EditText tminBox = (EditText) layout.findViewById(R.id.temmin);
        final EditText tmaxBox = (EditText) layout.findViewById(R.id.temMax);

        //Format Content
        String[] keys = {"numero:","designacao:","fabricante:","tem_min:","tem_max:"};
        boolean flag = false;
        for(int i = 0; i < keys.length; i++){
            String split[] = codeContent.split(keys[i]);
            if(split.length > 1){
                String match[] = split[1].split("\n");
                switch(i) {
                    case 0:
                        numBox.setText(match[0]);
                        break;
                    case 1:
                        desigBox.setText(match[0]);
                        break;
                    case 2:
                        fabBox.setText(match[0]);
                        break;
                    case 3:
                        tminBox.setText(match[0]);
                        break;
                    case 4:
                        tmaxBox.setText(match[0]);
                        break;
                }
            }else flag = true;
        }
        if(flag)Toast.makeText(this, R.string.scanner_dialog_error, Toast.LENGTH_LONG).show();
        //Building dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    private void sendAreaFrigorifica(AreaFrigorifica areaFrigorifica){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("numero", String.valueOf(areaFrigorifica.getNumero()));
            paramObject.put("designacao", areaFrigorifica.getDesignacao());
            paramObject.put("fabricante", areaFrigorifica.getFabricante());
            paramObject.put("tem_min", String.valueOf(areaFrigorifica.getTem_max()));
            paramObject.put("tem_max", String.valueOf(areaFrigorifica.getTem_max()));

            Call<String> frigCall = apiInterface.areafrig(current_user.getToken(), paramObject.toString());
            frigCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        try{
                            String id = response.body();
                            Toast.makeText(MainActivityAdmin.this, getString(R.string.scanner_dialog_ok_send), Toast.LENGTH_SHORT).show();
                        }catch (NullPointerException npe){
                            npe.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else if(response.code() == 403){
                        Toast.makeText(MainActivityAdmin.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            Toast.makeText(MainActivityAdmin.this, getString(R.string.scanner_dialog_error_generic), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(MainActivityAdmin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Tag", "error" + t.toString());
                    Toast.makeText(MainActivityAdmin.this, getString(R.string.scanner_dialog_error_send), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
}