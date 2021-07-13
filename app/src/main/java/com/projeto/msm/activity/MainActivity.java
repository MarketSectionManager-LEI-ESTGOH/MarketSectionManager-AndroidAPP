package com.projeto.msm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projeto.msm.R;
import com.projeto.msm.adapter.APICall;
import com.projeto.msm.adapter.ComponentesLimpos_ListAdapter;
import com.projeto.msm.adapter.TreeColumn_ListAdapter;
import com.projeto.msm.model.ComponentesLimpos;
import com.projeto.msm.model.Temperatura;
import com.projeto.msm.model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Temperatura> temperaturaList;
    private ArrayList<ComponentesLimpos> ComponetesLimposList;

    DrawerLayout drawerLayout;
    ListView listView;
    TextView userName;

    private LinearLayout temp_layout;
    private LinearLayout limp_comp_layout;
    private TextView limp_comp_layout1;
    private TextView limp_comp_layout2;
    private TextView limp_comp_layout3;
    private LinearLayout rast_layout;
    private View temp_View;
    private View limp_comp_View;
    private View rast_View;
    private TextView title;

    private int menu_option = 0;
    private static User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        userName = findViewById(R.id.user_name);

        listView = (ListView) findViewById(R.id.listview);

        title = (TextView) findViewById(R.id.textView6);
        temp_layout = (LinearLayout) findViewById(R.id.linearLayout_temp);
        limp_comp_layout = (LinearLayout) findViewById(R.id.linearLayout_limpeza);
        limp_comp_layout1 = (TextView) findViewById(R.id.textView_limp1);
        limp_comp_layout2 = (TextView) findViewById(R.id.textView_limp2);
        limp_comp_layout3 = (TextView) findViewById(R.id.textView_limp4);

        //LinearLayout rast_layout = (LinearLayout) findViewById(R.id.linearLayout_rast);
        temp_View = (View) findViewById(R.id.view2);
        limp_comp_View = (View) findViewById(R.id.view3);
        //View rast_View = (View) findViewById(R.id.view4);

        current_user = (User) getIntent().getSerializableExtra("user");
        userName.setText(current_user.getName()+" - "+ current_user.getnumInterno());

        temp_layout.setVisibility(View.INVISIBLE);
        limp_comp_layout.setVisibility(View.INVISIBLE);
        //rast_layout.setVisibility(View.INVISIBLE);

        temp_View.setVisibility(View.VISIBLE);
        limp_comp_View.setVisibility(View.INVISIBLE);
        //rast_View.setVisibility(View.INVISIBLE);
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
        Log.e("Tag", "ID: " + String.valueOf(id));
        Call<ArrayList<Temperatura>> call = apiInterface.areafrigtempByuser(String.valueOf(id), current_user.getToken());

        call.enqueue(new Callback<ArrayList<Temperatura>>() {
            @Override
            public void onResponse(Call<ArrayList<Temperatura>> call, Response<ArrayList<Temperatura>> response) {
                if (response.isSuccessful()) {
                    try{
                        temperaturaList = new ArrayList<>();
                        Log.e("Tag", "Array: " + response.body());
                        temperaturaList = response.body();

                        TreeColumn_ListAdapter adapter = new TreeColumn_ListAdapter(getApplicationContext(), R.layout.list_adapter, temperaturaList);
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
                    Toast.makeText(MainActivity.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_LONG).show();
                        Log.e("Tag", "error11");
                    } catch (Exception e) {
                        Log.e("Tag", "error2" + e);
                        //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Temperatura>> call, Throwable t) {
                Log.e("Tag", "error" + t.toString());
                Toast.makeText(MainActivity.this, getString(R.string.scanner_dialog_error_server), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserLastInputsCompoentes(int id) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APICall.Base_URL).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        APICall apiInterface = retrofit.create(APICall.class);
        Log.e("Tag", "ID: " + String.valueOf(id));
        Call<ArrayList<ComponentesLimpos>> call = apiInterface.getUserLastInputsCompoentes(String.valueOf(id), current_user.getToken());

        call.enqueue(new Callback<ArrayList<ComponentesLimpos>>() {
            @Override
            public void onResponse(Call<ArrayList<ComponentesLimpos>> call, Response<ArrayList<ComponentesLimpos>> response) {
                if (response.isSuccessful()) {
                    try{
                        ComponetesLimposList = new ArrayList<>();
                        Log.e("Tag", "Array: " + response.body());
                        ComponetesLimposList = response.body();

                        ComponentesLimpos_ListAdapter adapter = new ComponentesLimpos_ListAdapter(getApplicationContext(), R.layout.list_adapter_componentes, ComponetesLimposList);
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
                    Toast.makeText(MainActivity.this, getString(R.string.scanner_dialog_error_forbidden), Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_LONG).show();
                        Log.e("Tag", "error11");
                    } catch (Exception e) {
                        Log.e("Tag", "error2" + e);
                        //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ComponentesLimpos>> call, Throwable t) {
                Log.e("Tag", "error" + t.toString());
                Toast.makeText(MainActivity.this, getString(R.string.scanner_dialog_error_server), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public void SideMenu(View view){
        //Building dialog
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.main_menu_selection, (ViewGroup) findViewById(R.id.layout_root));

        //CHECK WHAT BTT IS SELECTED AND MAKE IT SELECTED
        RadioGroup radioButtonGroup = (RadioGroup) layout.findViewById(R.id.radioGroup);
        switch (menu_option){
            case 0:
                radioButtonGroup.check(R.id.radioButton_temp);
                break;
            case 1:
                radioButtonGroup.check(R.id.radioButton_limp);
                break;
            default:
                menu_option = 0;
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
                View radioButton = radioButtonGroup.findViewById(radioButtonID);
                int idx = radioButtonGroup.indexOfChild(radioButton);
                menu_option = idx;
                //Toast.makeText(MainActivity.this, "OK" + idx, Toast.LENGTH_SHORT).show();
                ConstraintSet set = new ConstraintSet();
                ConstraintLayout main_const_layout = (ConstraintLayout) findViewById(R.id.main_const_layout);
                set.clone(main_const_layout);

                switch (menu_option){
                    case 0:
                        /*TODO
                           -TURN OFF PREV INDEX
                           -TURN ON NEW INDEX
                           -UPDATE TABLE ADAPTER
                           -UPDATE INFO
                        */
                        temp_layout.setVisibility(View.VISIBLE);
                        limp_comp_layout.setVisibility(View.INVISIBLE);
                        //rast_layout.setVisibility(View.VISIBLE);

                        temp_View.setVisibility(View.VISIBLE);
                        limp_comp_View.setVisibility(View.INVISIBLE);
                        //rast_View.setVisibility(View.INVISIBLE);

                        temp_layout.invalidate();
                        temp_View.invalidate();

                        title.setText("Registos de Temperatura");

                        set.connect(listView.getId(), ConstraintSet.TOP,
                                temp_View.getId(), ConstraintSet.BOTTOM);
                        set.applyTo(main_const_layout);

                        getUserLastInputs(current_user.getId());
                        break;
                    case 1:

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                temp_layout.setVisibility(View.INVISIBLE);
                                limp_comp_layout.setVisibility(View.VISIBLE);
                                limp_comp_layout.bringToFront();
                                limp_comp_layout1.setVisibility(View.VISIBLE);
                                limp_comp_layout2.setVisibility(View.VISIBLE);
                                limp_comp_layout3.setVisibility(View.VISIBLE);
                            }
                        });
                        //rast_layout.setVisibility(View.INVISIBLE);

                        temp_View.setVisibility(View.INVISIBLE);
                        limp_comp_View.setVisibility(View.VISIBLE);
                        //rast_View.setVisibility(View.INVISIBLE);

                        limp_comp_View.invalidate();

                        title.setText("Registos de Limpeza");

                        set.connect(listView.getId(), ConstraintSet.TOP,
                                limp_comp_View.getId(), ConstraintSet.BOTTOM);
                        set.applyTo(main_const_layout);

                        getUserLastInputsCompoentes(current_user.getId());
                        break;
                    default:
                        menu_option = 0;
                        break;
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

    public void ClickAreas(View view){
        redirectActivity(this, AreasActivity.class);
    }

    public void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent.putExtra("user", current_user));
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
}