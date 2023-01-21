package com.waleed.app.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.SearchResponse
import com.waleed.app.util.loadImg
import kotlinx.android.synthetic.main.layout_search.view.*

class SearchAdapter(
    private var listData: ArrayList<SearchResponse.SearchItem>
) : RecyclerView.Adapter<SearchAdapter.SearchItemVH>() {
    var onItemClick: ((SearchResponse.SearchItem) -> Unit)? = null

    inner class SearchItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: SearchResponse.SearchItem) {

            itemView.item_label.text = data.item
            itemView.item_label.isSelected=true
            itemView.img_product.loadImg(data.imageUrl)
            itemView.item_label.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_search, parent, false)
        return SearchItemVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }


    override fun onBindViewHolder(holder: SearchItemVH, position: Int) {
        holder.bind(listData[position])

    }

    fun updateItem(updatedListData: ArrayList<SearchResponse.SearchItem>) {
        listData.clear()
        listData.addAll(updatedListData)
        notifyDataSetChanged()
    }
}