package com.waleed.app.ui.home.landing.ideal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.AgeGroupListResponse
import com.waleed.app.util.loadImg
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.layout_agegroup_single_item.view.*

class IdealAgeGroupAdapter(
    private var categoryList: ArrayList<AgeGroupListResponse.AgeGroupData>
) : RecyclerView.Adapter<IdealAgeGroupAdapter.CategoryVH>() {

    var defaultItemPosition: Int = 0
    var onItemClick: ((AgeGroupListResponse.AgeGroupData, Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_agegroup_single_item, parent, false)
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
            data: AgeGroupListResponse.AgeGroupData,
            position: Int
        ) {
            if (position == 0) {
                itemView.img_category.setImageResource(data.ageGroupID)
                itemView.tv_cat_name.text = data.ageGroupName
            } else {
                itemView.img_category.loadImg(data.image)
                itemView.tv_cat_name.setLanguageString(data.ageGroupName, data.ageGroupNameAr)
            }
            itemView.setOnClickListener {
                notifyItemChanged(defaultItemPosition)
                defaultItemPosition = position
                notifyItemChanged(defaultItemPosition)
                onItemClick?.invoke(data, position)
            }
            itemView.isSelected = defaultItemPosition == position
        }
    }
    fun addData(mList: ArrayList<AgeGroupListResponse.AgeGroupData>){
        categoryList.clear()
        categoryList.addAll(mList)
        notifyDataSetChanged()
    }
}