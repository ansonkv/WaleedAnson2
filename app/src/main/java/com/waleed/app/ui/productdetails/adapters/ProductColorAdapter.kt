package com.waleed.app.ui.productdetails.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.ProductSizeCombinationResponse
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.product_color_single_item.view.*

class ProductColorAdapter(
    private var listData: ArrayList<ProductSizeCombinationResponse>,
    private var context: Context
) : RecyclerView.Adapter<ProductColorAdapter.ProductColorVH>() {

    var defaultItemPosition: Int = 0
    var previousPosition = 0
    var onItemClick: ((ProductSizeCombinationResponse) -> Unit)? = null

    inner class ProductColorVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("Range")
        fun bind(data: ProductSizeCombinationResponse) {


            if (defaultItemPosition == adapterPosition) {
                val shape = GradientDrawable()
                shape.cornerRadius = 48f
                shape.setStroke(4, Color.WHITE)
                var colorCode = data
                shape.setColor(Color.parseColor(data.colorCode))
                itemView.view_color_normal.background = shape
                itemView.view_color_selected.background = shape
                itemView.view_color_normal.makeGone()
                itemView.view_color_selected.makeVisible()
                onItemClick?.invoke(listData[adapterPosition])
            } else {
                val shape = GradientDrawable()
                shape.cornerRadius = 48f
                var colorCode = data
                shape.setColor(Color.parseColor(data.colorCode))
                itemView.view_color_normal.background = shape
                itemView.view_color_selected.background = shape
                itemView.view_color_normal.makeVisible()
                itemView.view_color_selected.makeGone()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductColorVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_color_single_item, parent, false)
        return ProductColorVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ProductColorVH, position: Int) {
        holder.bind(listData[position])

        holder.itemView.setOnClickListener {

            previousPosition = defaultItemPosition
            defaultItemPosition = position

            notifyItemChanged(previousPosition)
            notifyItemChanged(position)
        }
    }
}