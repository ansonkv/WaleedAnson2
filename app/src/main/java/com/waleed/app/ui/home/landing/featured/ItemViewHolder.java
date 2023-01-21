package com.waleed.app.ui.home.landing.featured;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waleed.app.R;

final class ItemViewHolder extends RecyclerView.ViewHolder {

    final View rootView;
    final ImageView productImage;
    final TextView productName;
    final TextView discountPrice;
    final TextView productPrice;
    final ImageView favButton;
    final ImageView cartButton;

    ItemViewHolder(@NonNull final View view) {
        super(view);
        rootView = view;
        productImage = view.findViewById(R.id.img_product);
        productName = view.findViewById(R.id.tv_product_name);
        productPrice = view.findViewById(R.id.tv_product_price);
        discountPrice= view.findViewById(R.id.tv_product_discount_price);
        favButton = view.findViewById(R.id.img_fav_btn);
        cartButton=view.findViewById(R.id.img_cart_btn);

    }
}
