package com.projeto.msm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projeto.msm.R;
import com.projeto.msm.model.Area;

import java.util.ArrayList;

public class Arcas_ListAdapter extends ArrayAdapter<Area> {

    private LayoutInflater mInflator;
    private ArrayList<Area> areas;
    private int mViewResourceId;

    public Arcas_ListAdapter(Context context, int textViewResourceId, ArrayList<Area> areas){
        super(context, textViewResourceId, areas);
        this.areas = areas;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View converterView, ViewGroup parents){
        converterView = mInflator.inflate(mViewResourceId, null);
        Area areas_value = areas.get(position);

        if(areas_value != null){
            TextView numero = (TextView) converterView.findViewById(R.id.area_numero);
            TextView nome = (TextView) converterView.findViewById(R.id.area_designacao);

            if(numero != null){
                numero.setText(String.valueOf(areas_value.getNumero()));
            }
            if(nome != null){
                nome.setText(areas_value.getDesignacao());
            }
        }
        return converterView;
    }

}
