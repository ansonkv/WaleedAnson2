package com.waleed.app.ui.checkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.AgeGroupListResponse
import com.waleed.app.business.data.newdata.PaymentMethodsResponse
import com.waleed.app.util.loadImg
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.layout_agegroup_single_item.view.*
import kotlinx.android.synthetic.main.payment_method_single_item.view.*

class PaymentListAdapter(
    private var categoryList: ArrayList<PaymentMethodsResponse.PaymentMethod>
) : RecyclerView.Adapter<PaymentListAdapter.CategoryVH>() {

    var defaultItemPosition: Int = 0
    var previousPosition = 0
    var onItemClick: ((PaymentMethodsResponse.PaymentMethod, Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.payment_method_single_item, parent, false)
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
            data: PaymentMethodsResponse.PaymentMethod,
            position: Int
        ) {

            itemView.imgPaymentMethod.loadImg(data.image)
            itemView.tvMethodName.setLanguageString(data.name, data.nameAr)
            itemView.rb_knet.isChecked = defaultItemPosition == position
            itemView.setOnClickListener {
                if (position == defaultItemPosition) {
                    previousPosition = defaultItemPosition
                    defaultItemPosition =0
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(defaultItemPosition)
                } else {
                    previousPosition = defaultItemPosition
                    defaultItemPosition = position
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(defaultItemPosition)
                }
                onItemClick?.invoke(data,defaultItemPosition)
            }
            itemView.rb_knet.setOnClickListener {
                if (position == defaultItemPosition) {
                    previousPosition = defaultItemPosition
                    defaultItemPosition =0
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(defaultItemPosition)
                } else {
                    previousPosition = defaultItemPosition
                    defaultItemPosition = position
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(defaultItemPosition)
                }
                onItemClick?.invoke(data,defaultItemPosition)
            }

        }
    }


}