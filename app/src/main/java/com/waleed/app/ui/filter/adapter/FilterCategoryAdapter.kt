package com.waleed.app.ui.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.SearchFilterCategoriesData
import kotlinx.android.synthetic.main.search_filter_single_layout.view.*

class FilterCategoryAdapter(
    private var listDataSearchCategory: ArrayList<SearchFilterCategoriesData>,
    private var context: Context
) : RecyclerView.Adapter<FilterCategoryAdapter.FilterCategoryVH>() {

    var defaultItemPosition: Int = 0
    var previousPosition = 0
    var onItemClick: ((Int, SearchFilterCategoriesData) -> Unit)? = null

    inner class FilterCategoryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(searchFilterCategory: SearchFilterCategoriesData) {
            if (searchFilterCategory.count!! >= 1) {
                itemView.tv_filter_category_name.text =
                    searchFilterCategory.name + " (" + searchFilterCategory.count + ")"
            } else {
                itemView.tv_filter_category_name.text = searchFilterCategory.name
            }
            if (defaultItemPosition == adapterPosition){
                itemView.tv_filter_category_name.isSelected =true
                onItemClick?.invoke(adapterPosition, searchFilterCategory)
            }else{
                itemView.tv_filter_category_name.isSelected =false
            }

            itemView.tv_filter_category_name.setOnClickListener {
                previousPosition = defaultItemPosition
                defaultItemPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(defaultItemPosition)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCategoryVH {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_filter_single_layout, parent, false)
        return FilterCategoryVH(view)
    }

    override fun getItemCount(): Int {
        return listDataSearchCategory.size
    }

    override fun onBindViewHolder(holder: FilterCategoryVH, position: Int) {
        holder.bind(listDataSearchCategory[position])
    }
}