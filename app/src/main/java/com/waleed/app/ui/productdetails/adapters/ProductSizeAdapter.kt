package com.waleed.app.ui.productdetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.ProductSizeCombinationResponse
import com.waleed.app.util.LangUtils
import kotlinx.android.synthetic.main.product_size_single_item.view.*

class ProductSizeAdapter(
    private var listData: List<ProductSizeCombinationResponse.Size>,
    private var context: Context
) : RecyclerView.Adapter<ProductSizeAdapter.ProductSizeVH>() {

    var defaultItemPosition: Int = 0
    var previousPosition = 0
    var onItemClick: ((ProductSizeCombinationResponse.Size) -> Unit)? = null

    inner class ProductSizeVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ProductSizeCombinationResponse.Size) {
            itemView.tv_product_size.text=LangUtils.getLanguageString(
                data.sizeName,data.sizeNameAr
            )

            if (defaultItemPosition == adapterPosition) {
                itemView.tv_product_size.isSelected = true
                onItemClick?.invoke(listData[adapterPosition])
            }else{
                itemView.tv_product_size.isSelected = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSizeVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_size_single_item, parent, false)
        return ProductSizeVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ProductSizeVH, position: Int) {
        holder.bind(listData[position])

        holder.itemView.setOnClickListener {

            previousPosition = defaultItemPosition
            defaultItemPosition = position

            notifyItemChanged(previousPosition)
            notifyItemChanged(position)
        }
    }
}