package com.waleed.app.ui.wrap.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.WrapperData
import com.waleed.app.business.data.newdata.WrapperResponse
import com.waleed.app.util.loadImg
import kotlinx.android.synthetic.main.wrapper_paper_single_item.view.*

class WrapperPaperAdapter(
    private var listData: ArrayList<WrapperResponse.Paper>,
    private var context: Context
) : RecyclerView.Adapter<WrapperPaperAdapter.WrapperPaperVH>() {
    var checkedPosition = 0
    var previousPosition=0

    var onItemClick: ((WrapperResponse.Paper) -> Unit)? = null

    inner class WrapperPaperVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
         fun bind(data: WrapperResponse.Paper) {
            itemView.img_wrapper.loadImg(data.paperImage)
            itemView.img_wrapper.isSelected = checkedPosition == adapterPosition
            itemView.ll_wrapper.isSelected= checkedPosition == adapterPosition
            if(checkedPosition == adapterPosition){
                onItemClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WrapperPaperVH {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.wrapper_paper_single_item, parent, false)
        return WrapperPaperVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: WrapperPaperVH, position: Int) {
        holder.bind(listData[position])
        holder.itemView.setOnClickListener{
            previousPosition=checkedPosition
            checkedPosition=position
            notifyItemChanged(previousPosition)
            notifyItemChanged(checkedPosition)
            onItemClick?.invoke(listData[position])
        }
    }

    fun setDefault() {
        checkedPosition = -1
        notifyDataSetChanged()
    }
}