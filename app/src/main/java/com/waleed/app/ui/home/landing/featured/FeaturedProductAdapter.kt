package com.waleed.app.ui.home.landing.featured

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.ProductListData
import com.waleed.app.ui.productdetails.ProductDetailsActivity
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.loadImg
import kotlinx.android.synthetic.main.product_single_item.view.*

class FeaturedProductAdapter(
    private var listData: List<ProductListData>,
    private var context: Context
) : RecyclerView.Adapter<FeaturedProductAdapter.ProductListVH>() {

    inner class ProductListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ProductListData) {
            itemView.img_product.loadImg(data.imageUrl)
            itemView.tv_product_name.text = data.productName
            itemView.tv_product_price.text = data.price.toString() + " KWD"
            itemView.img_fav_btn.isSelected = data.isFav
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_single_item, parent, false)
        return ProductListVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ProductListVH, position: Int) {
        holder.bind(listData[position])
        holder.itemView.setOnClickListener {
            var bundle=Bundle()
            ActivitiesManager.goTOAnotherActivityWithBundle(context,ProductDetailsActivity::class.java,bundle)
        }
    }
}