package com.waleed.app.ui.home.landing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.IdealTypeListResponse
import com.waleed.app.util.loadImg
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.layout_ideal_category_single.view.*

class IdealCategoryListAdapter(
    private var categoryList: ArrayList<IdealTypeListResponse.IdealTypeData>
) :
    RecyclerView.Adapter<IdealCategoryListAdapter.CategoryVH>() {

    var defaultItemPosition: Int = 0
    var onItemClick: ((IdealTypeListResponse.IdealTypeData, Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_ideal_category_single, parent, false)
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
            data: IdealTypeListResponse.IdealTypeData,
            position: Int
        ) {
            if (position == 0) {
                itemView.tvIdealCategoryName.text = data.idealName
                itemView.imgIdealCategoryImage.setImageResource(data.idealID)
            } else {
                itemView.tvIdealCategoryName.setLanguageString(data.idealName, data.idealNameAr)
                itemView.imgIdealCategoryImage.loadImg(data.imageMobile)
            }
            when (adapterPosition % 4) {
                0 -> itemView.ll_ideal_category.setBackgroundResource(R.drawable.bg_home_ideal_featured_selector)
                1 -> itemView.ll_ideal_category.setBackgroundResource(R.drawable.bg_home_ideal_boys_selector)
                2 -> itemView.ll_ideal_category.setBackgroundResource(R.drawable.bg_home_category_item_selector)
                3 -> itemView.ll_ideal_category.setBackgroundResource(R.drawable.bg_home_ideal_unisex_selector)

            }
            itemView.ll_ideal_category.setOnClickListener {
                notifyItemChanged(defaultItemPosition)
                defaultItemPosition = position
                notifyItemChanged(defaultItemPosition)
                onItemClick?.invoke(data, position)
            }
            itemView.ll_ideal_category.isSelected = defaultItemPosition == position
        }
    }

}