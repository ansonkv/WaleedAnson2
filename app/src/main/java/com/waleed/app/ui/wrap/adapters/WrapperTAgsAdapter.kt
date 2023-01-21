package com.waleed.app.ui.wrap.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.WrapData
import com.waleed.app.business.data.WrapperData
import com.waleed.app.business.data.newdata.WrapperResponse
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.product_tags_single_item.view.*

class WrapperTAgsAdapter(private var listData: ArrayList<WrapperResponse.Tag>, private var context: Context) :
    RecyclerView.Adapter<WrapperTAgsAdapter.WrapperTagVH>() {

    var checkedPosition = 0
    var previousPosition=0
    var onItemClick: ((WrapperResponse.Tag) -> Unit)? = null

    inner class WrapperTagVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: WrapperResponse.Tag) {
            itemView.tv_product_tag.setLanguageString(
                data.tagName,data.tagNameAr
            )
            itemView.tv_product_tag.isSelected = checkedPosition == adapterPosition
            if(checkedPosition == adapterPosition){
                onItemClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WrapperTagVH {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.wrapper_tags_single_item, parent, false)
        return WrapperTagVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: WrapperTagVH, position: Int) {
        holder.bind(listData[position])
        holder.itemView.setOnClickListener {
            previousPosition=checkedPosition
            checkedPosition=position
            notifyItemChanged(previousPosition)
            notifyItemChanged(checkedPosition)
//            onItemClick?.invoke(listData[position].wrapperList)
        }
    }

    fun setDefault() {
        checkedPosition = -1
        notifyDataSetChanged()
    }
}