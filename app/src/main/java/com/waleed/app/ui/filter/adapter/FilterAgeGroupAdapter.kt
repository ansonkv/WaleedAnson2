package com.waleed.app.ui.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.FilterResponse
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeInvisible
import com.waleed.app.util.makeVisible
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.search_filter_value_single_layout.view.*

class FilterAgeGroupAdapter(
    private var brandValueList: List<FilterResponse.AgeGroupValue>,
    private var context: Context
) : RecyclerView.Adapter<FilterAgeGroupAdapter.FilterCategoryValueVH>() {

    var selectedPositions = mutableListOf<Int>()
    var selectedValues = mutableListOf<Int>()
    var onMultiChooserClick: ((MutableList<Int>,MutableList<Int>) -> Unit)? = null

    inner class FilterCategoryValueVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: FilterResponse.AgeGroupValue) {
            itemView.tv_category_value_name.setLanguageString(
                data.name,data.nameAr
            )
            itemView.tv_category_value_name_selected.setLanguageString(
                data.name,data.nameAr
            )
            if (selectedPositions.contains(adapterPosition)) {
                itemView.img_check.makeVisible()
                itemView.tv_category_value_name_selected.makeVisible()
                itemView.tv_category_value_name.makeGone()
            } else {
                itemView.img_check.makeInvisible()
                itemView.tv_category_value_name_selected.makeGone()
                itemView.tv_category_value_name.makeVisible()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCategoryValueVH {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_filter_value_single_layout, parent, false)
        return FilterCategoryValueVH(view)
    }

    override fun getItemCount(): Int {
        return brandValueList.size
    }

    override fun onBindViewHolder(holder: FilterCategoryValueVH, position: Int) {
        holder.bind(brandValueList[position])
        holder.itemView.setOnClickListener {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position)
                selectedValues.remove(brandValueList[position].ageGroupID)
                notifyItemChanged(position)
            } else {
                selectedPositions.add(position)
                selectedValues.add(brandValueList[position].ageGroupID)
                notifyItemChanged(position)
            }

            onMultiChooserClick?.invoke(selectedPositions,selectedValues)
        }
    }

    public fun setSelectedPositionList(selectedPositionList: MutableList<Int>,values: MutableList<Int>) {
        selectedPositions = selectedPositionList
        selectedValues=values
        notifyDataSetChanged()
    }
}