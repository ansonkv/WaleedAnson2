package com.waleed.app.ui.productlist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.business.data.newdata.getProductName
import com.waleed.app.business.data.newdata.getProductPrice
import com.waleed.app.business.data.newdata.getUnitPrice
import com.waleed.app.util.loadImg
import com.waleed.app.util.makeGone
import kotlinx.android.synthetic.main.product_single_item.view.*

class ProductListAdapter(
    private var listData: ArrayList<ProductItemData>
) : RecyclerView.Adapter<ProductListAdapter.ProductListVH>() {
    var onItemClick: ((ProductItemData) -> Unit)? = null
    var onFavItemClick: ((productData: ProductItemData, position: Int) -> Unit)? = null
    var onCartClick: ((productData: ProductItemData) -> Unit)? = null

    inner class ProductListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ProductItemData) {
            itemView.img_product.loadImg(data.thumbImage)
            itemView.tv_product_name.text = data.getProductName()
            itemView.tv_product_price.text = data.getProductPrice()
            if (data.unitPrice != data.discountUnitPrice) {
                itemView.tv_product_discount_price.text=data.getUnitPrice()
                itemView.tv_product_discount_price.paintFlags = itemView.tv_product_discount_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG;
            }else{
                itemView.tv_product_discount_price.makeGone()
            }
            if (data.unitsAvailable <= 0) {
                itemView.img_cart_btn.setImageResource(R.drawable.ic_cart_normal)
                itemView.img_cart_btn.isEnabled = false
            }
            itemView.img_fav_btn.isSelected = data.favorite == 1
            itemView.img_product.setOnClickListener {
                onItemClick?.invoke(data)
            }
            itemView.img_fav_btn.setOnClickListener {
                onFavItemClick?.invoke(data, adapterPosition)
            }
            itemView.img_cart_btn.setOnClickListener {
                onCartClick?.invoke(data)
            }
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

        }
    }

    fun updateItem(data: ProductItemData, position: Int) {
        listData[position] = data
        notifyItemChanged(position)
    }
}