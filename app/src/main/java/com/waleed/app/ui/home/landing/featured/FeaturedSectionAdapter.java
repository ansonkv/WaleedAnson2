package com.waleed.app.ui.home.landing.featured;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.waleed.app.R;
import com.waleed.app.business.data.newdata.ProductItemData;
import com.waleed.app.util.LangUtils;
import com.androidessence.lib.RichTextView;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

final class FeaturedSectionAdapter extends Section {

    private final String title;
    private final List<ProductItemData> list;
    private final ClickListener clickListener;
    private final Context context;

    FeaturedSectionAdapter(@NonNull final String title, @NonNull final List<ProductItemData> list,
                           @NonNull final ClickListener clickListener,
                           @NonNull final Context context) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.product_single_item)
                .headerResourceId(R.layout.layout_featured_list_header)
                .build());

        this.title = title;
        this.list = list;
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(final View view) {
        return new ItemViewHolder(view);
    }

    public void updateProduct(Integer position, ProductItemData data) {

    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        final ProductItemData data = list.get(position);

        itemHolder.productName.setText(LangUtils.getLanguageString(
                list.get(position).getProductName(), list.get(position).getProductNameAr()
        ));
        itemHolder.productPrice.setText(
                LangUtils.getLanguageString(
                        list.get(position).getDiscountUnitPrice(),
                        list.get(position).getDiscountUnitPriceAr()
                )
        );
        if (!list.get(position).getDiscountUnitPrice().equals(list.get(position).getUnitPrice())) {
            itemHolder.discountPrice.setVisibility(View.VISIBLE);
            itemHolder.discountPrice.setText(
                    LangUtils.getLanguageString(
                            list.get(position).getUnitPrice(), list.get(position).getUnitPriceAr()
                    )
            );
            itemHolder.discountPrice.setPaintFlags(itemHolder.discountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }else {
            itemHolder.discountPrice.setVisibility(View.INVISIBLE);
        }
        if (data.getUnitsAvailable() <= 0) {
            itemHolder.cartButton.setImageResource(R.drawable.ic_cart_normal);
            itemHolder.cartButton.setEnabled(false);
        }

        Glide.with(context).load(data.getThumbImage()).into(itemHolder.productImage);
        itemHolder.favButton.setSelected(data.getFavorite() == 1);
        itemHolder.productImage.setOnClickListener(v ->
                this.clickListener.onItemRootViewClicked(itemHolder.getAdapterPosition(), data)
        );
        itemHolder.favButton.setOnClickListener(v ->
                this.clickListener.onFavButtonClicked(itemHolder.getAdapterPosition(), data)
        );
        itemHolder.cartButton.setOnClickListener(v ->
                this.clickListener.onCartButtonClicked(data)
        );
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(final View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);

//        headerHolder.btnMore.setOnClickListener(v ->
//                clickListener.onHeaderMoreButtonClicked(this, headerHolder.getAdapterPosition())
//        );
    }

    interface ClickListener {

//        void onHeaderMoreButtonClicked(  int itemAdapterPosition);

        void onFavButtonClicked(final int itemAdapterPosition, ProductItemData data);

        void onItemRootViewClicked(final int itemAdapterPosition, ProductItemData data);

        void onCartButtonClicked(ProductItemData data);
    }
}
