package com.example.registros.ModuleProducts.TypeProducts;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.registros.R;

public class AdapterSpinnerTypeProduct extends BaseAdapter {

    ArrayMap<String, Integer> arrayMap;
    Context context;

    public AdapterSpinnerTypeProduct(Context context, ArrayMap<String, Integer> arrayMap) {
        this.context = context;
        this.arrayMap = arrayMap;
    }

    @Override
    public int getCount() {
        return arrayMap.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return arrayMap.keyAt(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.spinner_item_type_color, null);
        View viewItemTypeColor = convertView.findViewById(R.id.viewItemTypeColor);
        TextView textItemSpinnerTypeColor = convertView.findViewById(R.id.textItemSpinnerTypeColor);
        if (position == 0){ //position without product type
            textItemSpinnerTypeColor.setText("Seleccionar");
            viewItemTypeColor.setBackgroundColor(0);
        }else {
            viewItemTypeColor.setBackgroundColor(arrayMap.valueAt(position-1));
            textItemSpinnerTypeColor.setText(arrayMap.keyAt(position-1));
        }
        return convertView;
    }
}