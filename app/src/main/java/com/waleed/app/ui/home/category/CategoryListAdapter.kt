package com.waleed.app.ui.home.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.CategoryData
import com.waleed.app.business.data.newdata.CategoryListResponse
import com.waleed.app.util.LangUtils
import com.waleed.app.util.loadImg
import kotlinx.android.synthetic.main.category_single_layout.view.*

class CategoryListAdapter(
    private var listData: ArrayList<CategoryListResponse.CategoryListData>,
    private var context: Context
) : RecyclerView.Adapter<CategoryListAdapter.CategoryItemVH>() {
    var onItemClick: ((CategoryListResponse.CategoryListData ) -> Unit)? = null

    inner class CategoryItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: CategoryListResponse.CategoryListData) {
            itemView.img_category.loadImg(data.image)
            if (LangUtils.isCurrentLangArabic()) {
                itemView.tv_category_name.text = data.categoryNameAr
            } else {
                itemView.tv_category_name.text = data.categoryName
            }
            itemView.setOnClickListener {
                onItemClick?.invoke(data)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.category_single_layout, parent, false)
        return CategoryItemVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: CategoryItemVH, position: Int) {
        holder.bind(listData[position])
    }
}