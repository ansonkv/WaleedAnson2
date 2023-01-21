package com.waleed.app.ui.productdetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.ProductTags
import kotlinx.android.synthetic.main.product_tags_single_item.view.*

class ProductTagsAdapter(private var listData:List<String>, private var context: Context) :RecyclerView.Adapter<ProductTagsAdapter.ProductTagsVH>(){

    var checkedPosition = -1
    var onItemClick: ((Int) -> Unit)? = null

    inner class ProductTagsVH(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(data: String){
            itemView.tv_product_tag.text = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductTagsVH {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.product_tags_single_item, parent, false)
        return ProductTagsVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ProductTagsVH, position: Int) {
        holder.bind(listData[position])
    }
    fun setDefault(){
        checkedPosition=-1
        notifyDataSetChanged()
    }
}