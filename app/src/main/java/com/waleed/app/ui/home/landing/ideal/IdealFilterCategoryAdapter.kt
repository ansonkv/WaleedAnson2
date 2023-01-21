package com.waleed.app.ui.home.landing.ideal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.GenderTypesData
import kotlinx.android.synthetic.main.layout_agegroup_single_item.view.*

class IdealFilterCategoryAdapter(
    private var listData: List<GenderTypesData>
) : RecyclerView.Adapter<IdealFilterCategoryAdapter.FilterCategoryVH>() {
    var checkedPosition = 0
    var onItemClick: ((filterId: Int) -> Unit)? = null

    inner class FilterCategoryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: GenderTypesData) {
            itemView.img_category.setImageResource(data.imageUrl)
            itemView.tv_cat_name.text = data.name
            if (checkedPosition == adapterPosition) {
                itemView.isSelected = true
                onItemClick?.invoke(data.id)
            } else {
                itemView.isSelected = false
            }

            itemView.setOnClickListener {
                itemView.isSelected = true
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
                onItemClick?.invoke(data.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCategoryVH {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_agegroup_single_item, parent, false)
        return FilterCategoryVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: FilterCategoryVH, position: Int) {
        holder.bind(listData[position])
    }

    fun setDefault(){
        checkedPosition=-1
        notifyDataSetChanged()
    }
}