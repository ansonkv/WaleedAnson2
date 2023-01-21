package com.waleed.app.ui.wrap.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.CartDataForWrap
import com.waleed.app.business.data.newdata.CartListDataResponse
import com.waleed.app.util.loadImg
import kotlinx.android.synthetic.main.wrap_product_single_item.view.*

class ProductListAdapter(
    private var cartDataList: ArrayList<CartListDataResponse.CartItem>,
    private var context: Context

) : RecyclerView.Adapter<ProductListAdapter.ProductListVH>() {

    var onDeleteClick: ((Int) -> Unit)? = null

    inner class ProductListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deleteButton: ImageButton = itemView.btn_delete
        fun bind(dataForWrap: CartListDataResponse.CartItem) {
            itemView.img_product.loadImg(dataForWrap.thumbImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListVH {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.wrap_product_single_item, parent, false)
        return ProductListVH(view)
    }

    override fun getItemCount(): Int {
        return cartDataList.size
    }

    override fun onBindViewHolder(holder: ProductListVH, position: Int) {
        holder.bind(cartDataList[position])
        holder.deleteButton.setOnClickListener {
            cartDataList.removeAt(position)
            onDeleteClick?.invoke(position)
            if (cartDataList.isNotEmpty()) notifyDataSetChanged()
        }
    }
}