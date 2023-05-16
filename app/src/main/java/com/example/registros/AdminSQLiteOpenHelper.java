package com.example.registros;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.registros.ModuleProducts.ListElementProducts;
import com.example.registros.ModuleProducts.ProductsContract.ProductsEntry;

import java.util.ArrayList;
import java.util.List;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "registros_app";
    public static final String TABLE_TYPE_PRODUCT_NAME = "type_products";

    public AdminSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ProductsEntry.TABLE_NAME + " ("
                + ProductsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ProductsEntry.NAME_PRODUCT + " TEXT NOT NULL,"
                + ProductsEntry.PRICE_PRODUCT + " INTEGER NOT NULL,"
                + ProductsEntry.TYPE_PRODUCT + " INTEGER NOT NULL,"
                + ProductsEntry.ICON_PRODUCT + " TEXT NOT NULL,"
                + ProductsEntry.DETAILS_PRODUCT + " TEXT,"
                + ProductsEntry.ENABLE + " INTEGER DEFAULT 0);");

        Log.e("sqlite", "create table type");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TYPE_PRODUCT_NAME + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "text_type TEXT NOT NULL, color_type INTEGER NOT NULL," +
                "UNIQUE(color_type));");
    }

    public long AddNewTypeProduct(String textType, int colorType){
        ContentValues values = new ContentValues();
        SQLiteDatabase dataBase = getWritableDatabase();
        values.put("text_type", textType);
        values.put("color_type", colorType);
        return dataBase.insert(TABLE_TYPE_PRODUCT_NAME, null, values);
    }

    public int DeleteTypeProduct(String color_type){
        SQLiteDatabase dataBase = getWritableDatabase();
        return dataBase.delete(TABLE_TYPE_PRODUCT_NAME, "color_type = " + color_type, null);
    }

    public ArrayMap<String, Integer> GetTypeProducts(){
        ArrayMap<String, Integer> arrayMap = new ArrayMap<>();
        SQLiteDatabase dataBase = getWritableDatabase();
        Cursor cursor = dataBase.rawQuery("SELECT * FROM " + TABLE_TYPE_PRODUCT_NAME,null);
        while (cursor.moveToNext()){
            arrayMap.put(cursor.getString(1), cursor.getInt(2));
        }
        return arrayMap;
    }

    public long AddNewProduct(ListElementProducts listElementProducts){
        SQLiteDatabase dataBase = getWritableDatabase();
        return dataBase.insert(ProductsEntry.TABLE_NAME, null, listElementProducts.toContentValues());
    }

    public int UpdateProduct(String id, ContentValues values){
        SQLiteDatabase database = getWritableDatabase();
        return  database.update(ProductsEntry.TABLE_NAME, values,
                ProductsEntry._ID + " = " + id, null);
    }

    public List<ListElementProducts> GetAllProducts(){
        List<ListElementProducts> elements = new ArrayList<>();
        SQLiteDatabase dataBase = getWritableDatabase();
        Cursor cursor = dataBase.rawQuery("SELECT * FROM products",null);
        while (cursor.moveToNext()){
            //Log.e("valor", "de " + cursor.getString(6) + " a " + (cursor.getInt(6)==0?0:1));
            elements.add(new ListElementProducts(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6)!=0));
        }
        return elements;
    }

    public int DeleteProduct(String id){
        SQLiteDatabase dataBase = getWritableDatabase();
        return dataBase.delete(ProductsEntry.TABLE_NAME, ProductsEntry._ID + " = " + id, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
