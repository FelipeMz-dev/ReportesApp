package com.example.registros.ModuleProducts;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.PopupMenuCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registros.MainActivity;
import com.example.registros.R;

import java.text.DecimalFormat;
import java.util.List;

public class ListAdapterProducts extends RecyclerView.Adapter<ListAdapterProducts.ViewHolder> {
    private List<ListElementProducts> elementData;
    private ArrayMap<String, Integer> headElements;
    private final Context context;
    final ListAdapterProducts.onItemClickListener listener;
    private int typeIndex = 0;
    List<Boolean> isHead;

    public ListAdapterProducts(Context context, List<Boolean> isHead,
                               List<ListElementProducts> itemList,
                               ArrayMap<String, Integer> headElements,
                               ListAdapterProducts.onItemClickListener listener){
        this.context = context;
        this.isHead = isHead;
        this.elementData = itemList;
        this.headElements = headElements;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {return elementData.size();}

    @Override
    public int getItemViewType(int position) {
        if (isHead.get(elementData.indexOf(elementData.get(position)))) {
            return 1;
        }
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (viewType == 1){
            View view = layoutInflater.inflate(R.layout.products_type_list_item, parent,false);
            return new ViewHolder(view);
        }else {
            View viewCard = layoutInflater.inflate(R.layout.products_card, parent,false);
            return new ViewHolder(viewCard);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.bindData(elementData.get(position));
    }

    public interface onItemClickListener{
        void onItemClick(ListElementProducts item, CardView cardView);
    }

    //public void setItems(List<ListElementProducts> items){elementData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView productCard;
        TextView txtIconTypeProduct, cardNameProduct, cardPriceProduct;
        View viewColorType;
        ViewHolder(View itemView){
            super(itemView);
            productCard = itemView.findViewById(R.id.productCard);
            txtIconTypeProduct = itemView.findViewById(R.id.txtIconTypeProduct);
            cardNameProduct = itemView.findViewById(R.id.cardNameProduct);
            cardPriceProduct = itemView.findViewById(R.id.cardPriceProduct);
            viewColorType = itemView.findViewById(R.id.viewColorType);
        }
        void bindData(final ListElementProducts item){
            int typeItem = item.getType_products();
            int position = elementData.indexOf(item);
            if (isHead.get(position)) {
                TextView txtItemTypeProduct = itemView.findViewById(R.id.txtItemTypeProduct);
                txtItemTypeProduct.setVisibility(View.VISIBLE);
                txtItemTypeProduct.setText(headElements.keyAt(headElements.indexOfValue(typeItem)));
                txtItemTypeProduct.getBackground().setTint(typeItem);

                int color;
                if (elementData.get(position).getType_products() == 0) {
                    color = itemView.getResources().getColor(R.color.color_app_night_light);
                }else {
                    color = itemView.getResources().getColor(R.color.white);
                }
                txtItemTypeProduct.setTextColor(color);
            }
            productCard.setOnClickListener(v -> listener.onItemClick(item, productCard));
            Drawable foregroundColor = itemView.getResources().getDrawable(R.color.foreground_card_disable);
            if(item.isEnable()){
                foregroundColor = itemView.getResources().getDrawable(R.color.foreground_card_enable);
            }
            viewColorType.setBackgroundColor(item.getType_products());
            cardNameProduct.setText(item.getName_product());
            productCard.setForeground(foregroundColor);
            DecimalFormat format = new DecimalFormat("###,###.##");
            cardPriceProduct.setText(format.format(item.getPrice_product()).replace(',','.'));
            txtIconTypeProduct.setText(item.getIcon_product());
        }
    }
}
