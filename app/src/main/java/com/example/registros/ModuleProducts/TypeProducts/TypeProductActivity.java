package com.example.registros.ModuleProducts.TypeProducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registros.AdminSQLiteOpenHelper;
import com.example.registros.MainActivity;
import com.example.registros.ModuleProducts.ListElementProducts;
import com.example.registros.R;

import java.util.List;

public class TypeProductActivity extends AppCompatActivity {

    private boolean isEdited = false;
    Spinner spinnerRegisterTypeColor;
    EditText edtRegisterTypeProduct;
    ArrayMap<String, Integer> arrayTypesCreated, arrayTypesAvailable = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_product);

        spinnerRegisterTypeColor = findViewById(R.id.spinnerRegisterTypeColor);
        edtRegisterTypeProduct = findViewById(R.id.edtRegisterTypeProduct);

    }

    @Override
    public void onStart() {
        super.onStart();

        loadColorAvailable();

        ListAdapterTypesCreated adapter = new ListAdapterTypesCreated();
        RecyclerView recyclerViewTypes = findViewById(R.id.recyclerViewTypes);
        recyclerViewTypes.setHasFixedSize(true);
        recyclerViewTypes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTypes.setAdapter(adapter);

    }

    public void onBackPressed(){
        if (isEdited){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            finish();
        }
    }

    private void loadColorAvailable() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        arrayTypesCreated = admin.GetTypeProducts(); //load types created
        //write all colors
        ArrayMap<String, Integer> arrayMap = new ArrayMap<>();
        arrayMap.put("Color 1", getColor(R.color.color1));
        arrayMap.put("Color 2", getColor(R.color.color2));
        arrayMap.put("Color 3", getColor(R.color.color3));
        arrayMap.put("Color 4", getColor(R.color.color4));
        arrayMap.put("Color 5", getColor(R.color.color5));
        arrayMap.put("Color 6", getColor(R.color.color6));
        arrayMap.put("Color 7", getColor(R.color.color7));
        arrayMap.put("Color 8", getColor(R.color.color8));
        arrayMap.put("Color 9", getColor(R.color.color9));
        //write available colors
        arrayTypesAvailable.clear();
        for (Integer colorType:arrayMap.values()){
            if (!arrayTypesCreated.containsValue(colorType)) {
                String keyIndex = arrayMap.keyAt(arrayMap.indexOfValue(colorType));
                arrayTypesAvailable.put(keyIndex, colorType);
            }
        }
        AdapterSpinnerTypeProduct adapterTypesAvailable = new AdapterSpinnerTypeProduct(
                this ,arrayTypesAvailable);
        spinnerRegisterTypeColor.setAdapter(adapterTypesAvailable);
        spinnerRegisterTypeColor.setSelection(0);
    }

    public void deleteTypeProduct(int colorType){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        if (admin.DeleteTypeProduct(String.valueOf(colorType)) > 0){
            List<ListElementProducts> elements = admin.GetAllProducts();
            //update elements from type is delete
            for (ListElementProducts element:elements){
                if (element.getType_products() == colorType){
                    element.setType_products(0);
                    element.setEnable(false);
                    ContentValues values = element.toContentValues();
                    admin.UpdateProduct(String.valueOf(element.get_id()), values);
                }
            }
            Toast.makeText(this, "Tipo de producto BORRADO", Toast.LENGTH_SHORT).show();
            isEdited = true;
            onStart();
        }
    }

    public void onButtonSumType(View view){
        String textType = edtRegisterTypeProduct.getText().toString().toLowerCase().trim();
        int spinnerItemType = spinnerRegisterTypeColor.getSelectedItemPosition();
        if (!textType.isEmpty() && spinnerItemType != 0) {
            if (!arrayTypesCreated.containsKey(textType)) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
                if (admin.AddNewTypeProduct(textType, arrayTypesAvailable.valueAt(spinnerItemType-1)) > 0){
                    Toast.makeText(this, "Tipo de producto CREADO", Toast.LENGTH_SHORT).show();
                    onStart();
                }
            }else {
                Toast.makeText(this, "Tipo de producto ya existe", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Faltan datos por llenar", Toast.LENGTH_SHORT).show();
        }
    }
    //clas to create adapter for typeProduct RecyclerView
    class ListAdapterTypesCreated extends RecyclerView.Adapter<ListAdapterTypesCreated.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(TypeProductActivity.this);
            View view = layoutInflater.inflate(R.layout.type_product_listview_item, parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bindData(arrayTypesCreated.keyAt(position), arrayTypesCreated.valueAt(position));
        }

        @Override
        public int getItemCount() {
            return arrayTypesCreated.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textItemTypeName;
            View viewTypeColor;
            ImageView btnDeleteTypeProduct;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                viewTypeColor = itemView.findViewById(R.id.viewTypeColor);
                textItemTypeName = itemView.findViewById(R.id.textItemTypeName);
                btnDeleteTypeProduct = itemView.findViewById(R.id.btnDeleteTypeProduct);
            }
            void bindData(final String textData, final int colorData){
                viewTypeColor.setBackgroundColor(colorData);
                textItemTypeName.setText(textData.toUpperCase());
                btnDeleteTypeProduct.setOnClickListener(v -> {
                    TypeProductActivity.this.deleteTypeProduct(colorData);
                });
            }
        }
    }
}