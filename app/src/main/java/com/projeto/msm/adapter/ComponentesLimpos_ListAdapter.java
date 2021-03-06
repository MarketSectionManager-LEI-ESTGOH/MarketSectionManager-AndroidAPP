package com.projeto.msm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projeto.msm.R;
import com.projeto.msm.model.ComponentesLimpos;

import java.util.ArrayList;

public class ComponentesLimpos_ListAdapter extends ArrayAdapter<ComponentesLimpos> {

        private LayoutInflater mInflator;
        private ArrayList<ComponentesLimpos> componentes;
        private int mViewResourceId;

public ComponentesLimpos_ListAdapter(Context context, int textViewResourceId, ArrayList<ComponentesLimpos> componentes){
        super(context, textViewResourceId, componentes);
        this.componentes = componentes;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mViewResourceId = textViewResourceId;
        }

public View getView(int position, View converterView, ViewGroup parents){
        converterView = mInflator.inflate(mViewResourceId, null);

        ComponentesLimpos componentesLimpos = componentes.get(position);
        //Log.e("Tag", "INSIDE COM ADP: " + componentesLimpos.getComponente());

        if(componentesLimpos != null){
                TextView comp_designacao = (TextView) converterView.findViewById(R.id.comp_designacao);
                TextView designacao = (TextView) converterView.findViewById(R.id.comp_area_designacao);
                TextView date = (TextView) converterView.findViewById(R.id.list_last_date);

                if(comp_designacao != null){
                        /*
                        String aux = String.valueOf(componentesLimpos.getComponente());
                        String arr[] = aux.split(" ");
                        if(arr.length > 1){
                                aux = "";
                                for(int i = 0; i < arr.length; i++){
                                        if(i == arr.length-1){
                                                aux = aux + arr[i];
                                        }else{
                                                aux = aux + arr[i]+'\n';
                                        }

                                }
                        }else{
                                aux = arr[0];
                        }

                         */
                        comp_designacao.setText(componentesLimpos.getComponente());
                        //comp_designacao.setText(aux);
                }
                if(designacao != null){
                        /*
                        String aux = String.valueOf(componentesLimpos.getArea());
                        String arr[] = aux.split(" ");
                        if(arr.length > 1){
                                aux = "";
                                for(int i = 0; i < arr.length; i++){
                                        if(i == arr.length-1){
                                                aux = aux + arr[i];
                                        }else{
                                                aux = aux + arr[i]+'\n';
                                        }

                                }
                        }else{
                                aux = arr[0];
                        }
                        */

                        designacao.setText(String.valueOf(componentesLimpos.getArea()));
                        //designacao.setText(aux);
                }
                if(date != null){
                        date.setText(String.valueOf(componentesLimpos.getData()));
                }
        }
        return converterView;
        }

}
