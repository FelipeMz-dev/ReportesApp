package com.example.registros.ModuleProducts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registros.ModuleProducts.ProductsContract.ProductsEntry;
import com.example.registros.AdminSQLiteOpenHelper;
import com.example.registros.MainActivity;
import com.example.registros.ModuleProducts.TypeProducts.AdapterSpinnerTypeProduct;
import com.example.registros.R;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiUtils;

import java.text.DecimalFormat;
import java.util.Objects;

public class EditProductActivity extends AppCompatActivity {

    EditText edtRegisterCode, edtRegisterName,edtRegisterPrice, edtRegisterDetails, edtRegisterIcon;
    TextView txtRegisterIcon;
    Spinner spinnerRegisterType;
    CheckBox checkBoxProductEnable;
    ArrayMap<String, Integer> currentTypes = new ArrayMap<>();
    private boolean edit = false;
    final static String spinnerEmpty = "NO HAY TIPOS DE PROSUCTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        edtRegisterCode = findViewById(R.id.edtRegisterCode);
        edtRegisterName = findViewById(R.id.edtRegisterName);
        edtRegisterPrice = findViewById(R.id.edtRegisterPrice);
        edtRegisterDetails = findViewById(R.id.edtRegisterDetails);
        spinnerRegisterType = findViewById(R.id.spinnerRegisterType);
        txtRegisterIcon = findViewById(R.id.txtRegisterIcon);
        edtRegisterIcon = findViewById(R.id.edtRegisterIcon);
        checkBoxProductEnable = findViewById(R.id.checkBoxProductEnable);
        edtRegisterName.requestFocus();
        //show button to return
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //load the support to open the emoji keyboard
        loadEmojiKeyboard();

        loadSpinnerItems();
        if (savedInstanceState == null){
            Bundle extra = getIntent().getExtras();
            if (extra != null){
                fillDataProduct((ListElementProducts) extra.get("productData"));
                this.edit = true;
            }
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        if (edtRegisterCode.getText().toString().length() < 3){
            String code;
            for (int i=edtRegisterCode.getText().toString().length();i<4;i++) {
                code = "0" + edtRegisterCode.getText().toString();
                edtRegisterCode.setText(code);
            }
        }
        spinnerRegisterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    checkBoxProductEnable.setChecked(true);
                    checkBoxProductEnable.setEnabled(true);
                }else {
                    checkBoxProductEnable.setChecked(false);
                    checkBoxProductEnable.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edtRegisterPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                DecimalFormat format = new DecimalFormat("###,###.##");
                if (!s.toString().isEmpty()){
                    int numberPrice = Integer.parseInt(edtRegisterPrice.getText().toString().replace(".",""));
                    String textPrice = format.format(numberPrice).replace(',','.');
                    if (!textPrice.equals(edtRegisterPrice.getText().toString())) {
                        edtRegisterPrice.setText(textPrice);
                        edtRegisterPrice.setSelection(textPrice.length());
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void loadSpinnerItems(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        currentTypes = admin.GetTypeProducts(); //load types created
        if (currentTypes.size() == 0){
            String [] spinnerString = {spinnerEmpty};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerString
            );
            spinnerRegisterType.setAdapter(adapter);
            spinnerRegisterType.setEnabled(false);
            checkBoxProductEnable.setChecked(false);
            checkBoxProductEnable.setEnabled(false);
        }else {
            AdapterSpinnerTypeProduct adapterTypesAvailable = new AdapterSpinnerTypeProduct(
                    this , currentTypes);
            spinnerRegisterType.setAdapter(adapterTypesAvailable);
        }
    }

    private void fillDataProduct(ListElementProducts element){
        DecimalFormat format = new DecimalFormat("###,###.##");
        String price = format.format(element.getPrice_product()).replace(',','.');
        edtRegisterCode.setText(String.valueOf(element.get_id()));
        edtRegisterName.setText(element.getName_product());
        edtRegisterPrice.setText(price);
        txtRegisterIcon.setText(element.getIcon_product());
        edtRegisterDetails.setText(element.getDetails_product());
        checkBoxProductEnable.setChecked(element.isEnable());
        Button btnRegisterProduct = findViewById(R.id.btnRegisterProduct);
        btnRegisterProduct.setText(getString(R.string.strUpdateProduct));
        int typePosition = currentTypes.indexOfValue(element.getType_products());

        spinnerRegisterType.setSelection(typePosition+1);

    }

    public void OnButtonClick(View view){
        boolean checkEditText = edtRegisterName.getText().toString().isEmpty() ||
                                edtRegisterPrice.getText().toString().isEmpty();
        if (checkEditText){
            Toast.makeText(this, "Hay campos vacios", Toast.LENGTH_SHORT).show();
        }else {
            if (edit) {
                if (updateProduct() > 0){
                    Toast.makeText(this, "Producto ACTUALIZADO", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(this, MainActivity.class));
                    finish();
                    onStart();
                } else {
                    Toast.makeText(this, "Error al Actualizar", Toast.LENGTH_SHORT).show();
                }
            }else {
                if (saveProduct() > 0) {
                    Toast.makeText(this, "Producto REGISTRADO", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(this, MainActivity.class));
                    finish();
                    onStart();
                } else {
                    Toast.makeText(this, "Error al Registrar", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private int updateProduct(){
        final String ID = edtRegisterCode.getText().toString();
        int typeSelected = 0;
        int spinnerPosition = spinnerRegisterType.getSelectedItemPosition();
        if (spinnerPosition > 0) {
            typeSelected = currentTypes.valueAt(spinnerPosition-1);
        }
        int numberValuePrice = Integer.parseInt(edtRegisterPrice.getText().toString().replace(".",""));

        ContentValues values = new ContentValues();
        values.put(ProductsEntry.NAME_PRODUCT, edtRegisterName.getText().toString().toLowerCase().trim());
        values.put(ProductsEntry.PRICE_PRODUCT, numberValuePrice);
        values.put(ProductsEntry.TYPE_PRODUCT, typeSelected);
        values.put(ProductsEntry.ICON_PRODUCT, txtRegisterIcon.getText().toString());
        values.put(ProductsEntry.DETAILS_PRODUCT, edtRegisterDetails.getText().toString());
        values.put(ProductsEntry.ENABLE, checkBoxProductEnable.isChecked()?1:0);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        return admin.UpdateProduct(ID, values);
    }

    private long saveProduct(){
        int typeSelected = 0;
        int spinnerPosition = spinnerRegisterType.getSelectedItemPosition();
        if (spinnerPosition > 0) {
            typeSelected = currentTypes.valueAt(spinnerPosition-1);
        }
        int numberValuePrice = Integer.parseInt(edtRegisterPrice.getText().toString().replace(".",""));

        ListElementProducts elements = new ListElementProducts(
                null,
                edtRegisterName.getText().toString().toLowerCase().trim(),
                numberValuePrice,
                typeSelected,
                txtRegisterIcon.getText().toString(),
                edtRegisterDetails.getText().toString(),
                checkBoxProductEnable.isChecked());
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        return admin.AddNewProduct(elements);
    }

    private void loadEmojiKeyboard(){
        EmojiPopup popup = EmojiPopup.Builder.fromRootView(
                findViewById(R.id.root_view)
        ).build(edtRegisterIcon);

        edtRegisterIcon.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textEmoji = s.toString();
                if (EmojiUtils.isOnlyEmojis(textEmoji)) txtRegisterIcon.setText(textEmoji);
                if (!textEmoji.isEmpty()) edtRegisterIcon.setText("");
            }
            public void afterTextChanged(Editable s) {
            }
        });

        txtRegisterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtRegisterIcon.requestFocus();
            }
        });

        edtRegisterIcon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //popup.toggle();
                if (!hasFocus) {
                    txtRegisterIcon.setBackground(getDrawable(R.drawable.background_edit_text));
                    popup.dismiss();
                }else {
                    txtRegisterIcon.getBackground().setTint(getColor(R.color.color_app_light));
                    popup.show();
                }
            }
        });
    }
}