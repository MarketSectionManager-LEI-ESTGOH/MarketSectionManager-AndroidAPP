package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.projeto.msm.R;
import com.projeto.msm.model.AreaFrigorifica;
import com.projeto.msm.model.Capture;

import java.util.Calendar;

public class ValidadeActivity extends AppCompatActivity {

    private ImageButton qrcodescanner;
    private DatePickerDialog picker;
    private EditText date;
    private EditText ean_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validade);

        qrcodescanner = findViewById(R.id.imageButton);
        ean_text = findViewById(R.id.rastreabilidadeLote);
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
                                date.setText(dayOfMonth + "-" + month + "-" + year);
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
    }

    public void onClickAceitar(View view){
        Toast.makeText(this, "-> " + "KKK", Toast.LENGTH_LONG).show();
    }

    public void onClickVoltar(View view){
        finish();
    }

    public void ClickBack(View view){
        finish();
    }
}