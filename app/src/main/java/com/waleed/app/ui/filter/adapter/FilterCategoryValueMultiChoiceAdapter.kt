package com.waleed.app.ui.filter.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.SearchFilterCategoryValues
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeInvisible
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.search_filter_value_single_layout.view.*

class FilterCategoryValueMultiChoiceAdapter(
    private var categoryVlueList: List<SearchFilterCategoryValues>,
    private var context: Context
) : RecyclerView.Adapter<FilterCategoryValueMultiChoiceAdapter.FilterCategoryValueVH>() {

    var selectedPositions = mutableListOf<Int>()
    var onMultiChooserClick: ((MutableList<Int>) -> Unit)? = null

    inner class FilterCategoryValueVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: SearchFilterCategoryValues) {
            itemView.tv_category_value_name.text = data.valueName
            itemView.tv_category_value_name_selected.text = data.valueName

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
        return categoryVlueList.size
    }

    override fun onBindViewHolder(holder: FilterCategoryValueVH, position: Int) {
        holder.bind(categoryVlueList[position])
        holder.itemView.setOnClickListener {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position)
                notifyItemChanged(position)
            } else {
                selectedPositions.add(position)
                notifyItemChanged(position)
            }

            onMultiChooserClick?.invoke(selectedPositions)
        }
    }

    public fun setSelectedPositionList(selectedPositionList: MutableList<Int>) {
        selectedPositions = selectedPositionList
        notifyDataSetChanged()
    }
}