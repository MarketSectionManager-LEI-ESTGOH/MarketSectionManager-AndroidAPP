package com.projeto.msm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projeto.msm.R;
import com.projeto.msm.model.Area;
import com.projeto.msm.model.Componente;

import java.util.ArrayList;

public class Componentes_ListAdapter extends ArrayAdapter<Componente>{

    private LayoutInflater mInflator;
    private ArrayList<Componente> componentes;
    private int mViewResourceId;

    public Componentes_ListAdapter(Context context, int textViewResourceId, ArrayList<Componente> componentes){
        super(context, textViewResourceId, componentes);
        this.componentes = componentes;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View converterView, ViewGroup parents){
        converterView = mInflator.inflate(mViewResourceId, null);
        Componente componente_value = componentes.get(position);

        if(componente_value != null){
            TextView id = (TextView) converterView.findViewById(R.id.componente_id);
            TextView nome = (TextView) converterView.findViewById(R.id.componente_nome);

            if(id != null){
                id.setText(String.valueOf(componente_value.getId()));
            }
            if(nome != null){
                nome.setText(componente_value.getDesignacao());
            }
        }
        return converterView;
    }
}
