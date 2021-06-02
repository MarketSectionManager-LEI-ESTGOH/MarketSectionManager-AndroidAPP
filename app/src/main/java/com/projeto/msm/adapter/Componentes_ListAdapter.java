package com.projeto.msm.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.projeto.msm.R;
import com.projeto.msm.activity.ComponentesActivity;
import com.projeto.msm.model.Area;
import com.projeto.msm.model.Componente;

import java.util.ArrayList;

public class Componentes_ListAdapter extends ArrayAdapter<Componente>{

    private LayoutInflater mInflator;
    private ArrayList<Componente> componentes;
    private int mViewResourceId;
    public SparseBooleanArray mCheckStates;

    public Componentes_ListAdapter(Context context, int textViewResourceId, ArrayList<Componente> componentes){
        super(context, textViewResourceId, componentes);
        this.componentes = componentes;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mViewResourceId = textViewResourceId;
        mCheckStates = new SparseBooleanArray(componentes.size());
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    public View getView(int position, View converterView, ViewGroup parents){
        ViewHolder holder = null;
        converterView = mInflator.inflate(mViewResourceId, null);
        Componente componente_value = componentes.get(position);

        if(componente_value != null){
            holder = new ViewHolder();
            TextView data = (TextView) converterView.findViewById(R.id.componente_data);
            TextView nome = (TextView) converterView.findViewById(R.id.componente_nome);
            holder.code = nome;
            holder.name = (CheckBox) converterView.findViewById(R.id.componente_checked);
            converterView.setTag(holder);
            holder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    if(cb.isChecked()){
                        ComponentesActivity.checkedcomponentes.add(componente_value);
                    }else if(ComponentesActivity.checkedcomponentes.contains(componente_value)){
                        ComponentesActivity.checkedcomponentes.remove(componente_value);
                    }
                    //Toast.makeText(getContext(), "Clicked on Checkbox: " + position + " is " + cb.isChecked(), Toast.LENGTH_SHORT).show();
                }
            });
            if(data != null){
                data.setText(componente_value.getData());
            }else{
                CharSequence cs = "-";
                data.setText(cs);
            }
            if(nome != null){
                nome.setText(componente_value.getDesignacao());
            }
        }
        return converterView;
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);

    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));

    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        mCheckStates.put((Integer) buttonView.getTag(), isChecked);

    }

}
