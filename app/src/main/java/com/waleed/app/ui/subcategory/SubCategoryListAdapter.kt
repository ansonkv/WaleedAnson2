package com.waleed.app.ui.subcategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.SubCategoryListResponse
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.layout_sub_category_single_item.view.*

class SubCategoryListAdapter(
    private var listData: ArrayList<SubCategoryListResponse.SubCategoryData>
) : RecyclerView.Adapter<SubCategoryListAdapter.CategoryItemVH>() {
    var onItemClick: ((SubCategoryListResponse.SubCategoryData) -> Unit)? = null

    inner class CategoryItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: SubCategoryListResponse.SubCategoryData) {
            itemView.tv_sub_category_name.setLanguageString(
                    data.subCategoryName,
                    data.subCategoryNameAr
                )
            itemView.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_sub_category_single_item, parent, false)
        return CategoryItemVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: CategoryItemVH, position: Int) {
        holder.bind(listData[position])
    }
}