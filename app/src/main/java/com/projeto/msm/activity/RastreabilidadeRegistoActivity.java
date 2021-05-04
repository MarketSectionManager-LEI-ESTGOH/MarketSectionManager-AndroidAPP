package com.projeto.msm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.projeto.msm.R;
import com.projeto.msm.model.AreaFrigorifica;
import com.projeto.msm.model.Rastreabilidade;

public class RastreabilidadeRegistoActivity extends AppCompatActivity {

    EditText text_lote, text_fornecedor, text_produto, text_origem;

    private Rastreabilidade rastreabilidade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rastreabilidade_registo);

        text_lote = (EditText) findViewById(R.id.rastreabilidadeLote);
        text_fornecedor = (EditText) findViewById(R.id.rastreabilidadeFornecedor);
        text_produto = (EditText) findViewById(R.id.rastreabilidadeProduto);
        text_origem = (EditText) findViewById(R.id.rastreabilidadeOrigem);

    }

    public void onClickAceitar(View view){
        if(!isEmpty(text_lote) && !isEmpty(text_fornecedor) && !isEmpty(text_produto) && !isEmpty(text_origem)){
            rastreabilidade = new Rastreabilidade(Integer.parseInt(text_lote.getText().toString()), text_fornecedor.getText().toString(), text_origem.getText().toString(), Integer.parseInt(text_produto.getText().toString()));
            Intent i = new Intent();
            Bundle b = new Bundle();
            b.putSerializable("new_rastreabilidade", rastreabilidade);
            i.putExtras(b);
            setResult(RESULT_OK, i);
            finish();
        }else{
            Toast.makeText(RastreabilidadeRegistoActivity.this, R.string.scanner_dialog_error, Toast.LENGTH_LONG).show();
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