package com.example.registros;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.registros.ModuleProducts.EditProductActivity;
import com.example.registros.ModuleProducts.ListAdapterProducts;
import com.example.registros.ModuleProducts.ListElementProducts;
import com.example.registros.ModuleProducts.TypeProducts.TypeProductActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    LayoutInflater inflater;
    List<ListElementProducts> elements;
    ListAdapterProducts listAdapter;
    ArrayMap<String, Integer> arrayTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        getSupportActionBar().setElevation(0);
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadElements();
        GenerateTab();
    }
    //show search button on toolbar and menu items
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        return true;
    }
    //fill the elements from data base
    public void loadElements(){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        arrayTypes = admin.GetTypeProducts();
        arrayTypes.put("Sin Clasificar", 0);
        List<ListElementProducts> currentElements = admin.GetAllProducts();
        orderElementsByType(currentElements);

        List<Boolean> list = new ArrayList<>();
        int typeState = 0;

        for (ListElementProducts element:elements){
            if (typeState != element.getType_products()){
                typeState = element.getType_products();
                list.add(true);
            }else {
                list.add(false);
            }
        }

        listAdapter = new ListAdapterProducts(this, list, elements, arrayTypes, (item, view) -> {
            //open popup from product item selected
            PopupMenu menuProduct = new PopupMenu(this, view);
            menuProduct.getMenuInflater().inflate(R.menu.menu_item_product, menuProduct.getMenu());
            menuProduct.setForceShowIcon(true);
            menuProduct.setOnMenuItemClickListener(item1 -> {
                if (item1.getItemId() == R.id.itemProductEdit){
                    Intent intent = new Intent(MainActivity.this, EditProductActivity.class);
                    intent.putExtra("productData", item);
                    startActivity(intent);
                }else if (item1.getItemId() == R.id.itemProductDelete){
                    checkDeleteProduct(item.get_id());
                }
                return false;
            });
            menuProduct.show();
        });


    }
    //order the elements of list
    private void orderElementsByType(List<ListElementProducts> currentElements){
        List<ListElementProducts> orderElements = new ArrayList<>();
        for (Integer type:arrayTypes.values()){
            if (type != 0) orderElements.addAll(
                    currentElements.stream().filter(element->element.getType_products()==type).collect(Collectors.toList())
            );
        }
        orderElements.addAll(
                currentElements.stream().filter(element->element.getType_products()==0).collect(Collectors.toList())
        );
        elements = orderElements;
    }
    //method to delete with alertDialog
    public void checkDeleteProduct(int id){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Confirma para eliminar el producto");
        alert.setPositiveButton("Confirmar", (dialog, which) -> {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
            if (admin.DeleteProduct(String.valueOf(id)) > 0){
                Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                onStart();
            }else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Cancelar", (dialog, which) -> {});
        alert.create().show();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem){
        if (menuItem.getItemId() == R.id.createType){
            Intent intent = new Intent(this, TypeProductActivity.class);
            startActivity(intent);
        }
        return true;
    }
    //switch on system tab whit viewPager
    private void GenerateTab(){
        inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View products_layout = inflater.inflate(R.layout.products_layout, null);
        View sales_layout = inflater.inflate(R.layout.sales_layout, null);
        View reports_layout = inflater.inflate(R.layout.reports_layout, null);
        ArrayList<View> list = new ArrayList<>();
        list.add(products_layout);
        list.add(sales_layout);
        list.add(reports_layout);
        loadViewProducts(products_layout);
        pageAdapter = new PageAdapter(list,this);
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    public void loadViewProducts(View view){
        ImageButton button_add_product = view.findViewById(R.id.button_add_product);
        button_add_product.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditProductActivity.class);
            startActivity(intent);
        });
        RecyclerView recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setHasFixedSize(true);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducts.setAdapter(listAdapter);

    }
}