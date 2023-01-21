package com.waleed.app.ui.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.DeliveryTypeResponse
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.layout_delivery_type.view.*

class DeliveryTypeListAdapter(
    private var categoryList: ArrayList<DeliveryTypeResponse.DeliveryType>,
    private var context: Context
) :
    RecyclerView.Adapter<DeliveryTypeListAdapter.CategoryVH>() {

    var defaultItemPosition: Int = 0
    var onItemClick: ((DeliveryTypeResponse.DeliveryType, Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_delivery_type, parent, false)
        return CategoryVH(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.bind(categoryList[position], position)
    }

    inner class CategoryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            data: DeliveryTypeResponse.DeliveryType,
            position: Int
        ) {

            itemView.tv_regular.setLanguageString(data.name, data.nameAr)
            if (data.deliveryPrice == "0.000 KD") {
                itemView.tv_delivery_amount.text=context.getString(R.string.free_string)
            } else {
                itemView.tv_delivery_amount.setLanguageString(
                    data.deliveryPrice,
                    data.deliveryPriceAr
                )
            }
            itemView.tv_free_dec.setLanguageString(data.description, data.descriptionAr)

            itemView.rb_regular.setOnClickListener {
                notifyItemChanged(defaultItemPosition)
                defaultItemPosition = position
                notifyItemChanged(defaultItemPosition)
                onItemClick?.invoke(data, position)
            }
            itemView.ll_free.setOnClickListener {
                notifyItemChanged(defaultItemPosition)
                defaultItemPosition = position
                notifyItemChanged(defaultItemPosition)

            }
            if (defaultItemPosition == position) {
                itemView.rb_regular.isChecked = true
                onItemClick?.invoke(data, position)
            } else
                itemView.rb_regular.isChecked = false
        }
    }

}