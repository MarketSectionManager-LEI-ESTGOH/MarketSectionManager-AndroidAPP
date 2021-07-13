package com.projeto.msm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projeto.msm.R;
import com.projeto.msm.model.Temperatura;

import java.util.ArrayList;

public class TreeColumn_ListAdapter extends ArrayAdapter<Temperatura> {

    private LayoutInflater mInflator;
    private ArrayList<Temperatura> temperaturas;
    private int mViewResourceId;

    public TreeColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<Temperatura> temperaturas){
        super(context, textViewResourceId, temperaturas);
        this.temperaturas = temperaturas;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View converterView, ViewGroup parents){
        converterView = mInflator.inflate(mViewResourceId, null);

        Temperatura temperatura_value = temperaturas.get(position);

        if(temperatura_value != null){
            TextView hora = (TextView) converterView.findViewById(R.id.list_hora);
            TextView temperatura = (TextView) converterView.findViewById(R.id.list_temmperatura);
            TextView arca = (TextView) converterView.findViewById(R.id.list_arca);

            if(hora != null){
                hora.setText(temperatura_value.getData_hora());
            }
            if(temperatura != null){
                temperatura.setText(String.valueOf(temperatura_value.getTemperatura())+" ºC");
            }
            if(arca != null){
                arca.setText(String.valueOf(temperatura_value.getDesignacao()));
            }
        }
        return converterView;
    }

}
