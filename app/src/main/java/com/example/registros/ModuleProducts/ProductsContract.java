package com.example.registros.ModuleProducts;

import android.provider.BaseColumns;

public class ProductsContract {
    public static abstract class ProductsEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String PRICE_PRODUCT = "price_product";
        public static final String NAME_PRODUCT = "name_product";
        public static final String TYPE_PRODUCT = "type_product";
        public static final String ICON_PRODUCT = "icon_product";
        public static final String DETAILS_PRODUCT = "details_product";
        public static final String ENABLE = "enable";
    }
}
