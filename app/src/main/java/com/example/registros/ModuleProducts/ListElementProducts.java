package com.example.registros.ModuleProducts;

import android.content.ContentValues;

import java.io.Serializable;
import com.example.registros.ModuleProducts.ProductsContract.ProductsEntry;

public class ListElementProducts implements Serializable{

    private final Integer _id;
    private int type_products, price_product;
    private String name_product, icon_product, details_product;
    private boolean enable;

    public ListElementProducts(Integer _id,
                               String name_product,
                               int price_product,
                               int type_products,
                               String icon_product,
                               String details_product,
                               boolean enable){
        this._id = _id;
        this.price_product = price_product;
        this.name_product = name_product;
        this.type_products = type_products;
        this.icon_product = icon_product;
        this.details_product = details_product;
        this.enable = enable;
    }

    public Integer get_id() {
        return _id;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getPrice_product() {
        return price_product;
    }

    public void setPrice_product(int price_product) {
        this.price_product = price_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public int getType_products() {
        return type_products;
    }

    public void setType_products(int type_products) {
        this.type_products = type_products;
    }

    public String getIcon_product() {
        return icon_product;
    }

    public void setIcon_product(String icon_product) {
        this.icon_product = icon_product;
    }

    public String getDetails_product() {
        return details_product;
    }

    public void setDetails_product(String details_product) {
        this.details_product = details_product;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ProductsEntry.NAME_PRODUCT, name_product);
        values.put(ProductsEntry.PRICE_PRODUCT, price_product);
        values.put(ProductsEntry.TYPE_PRODUCT, type_products);
        values.put(ProductsEntry.ICON_PRODUCT, icon_product);
        values.put(ProductsEntry.DETAILS_PRODUCT, details_product);
        values.put(ProductsEntry.ENABLE, enable);
        return values;
    }

}
