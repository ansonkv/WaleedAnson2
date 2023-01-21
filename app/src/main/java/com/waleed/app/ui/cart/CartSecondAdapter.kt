package com.waleed.app.ui.cart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.CartData
import com.waleed.app.business.data.StoreData
import kotlinx.android.synthetic.main.store_single_item.view.*

class CartSecondAdapter(
    private var listData: List<CartData>,
    private var context: Context
) :
    RecyclerView.Adapter<CartSecondAdapter.AddressVH>() {

    var defaultItemPosition: Int = -1
    var onItemClick: ((StoreData, Int) -> Unit)? = null

    inner class AddressVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: CartData) {
//            itemView.tv_store_name.text = data.storeName
//            itemView.tv_store_address.text = data.storeAddress
//            if (data.isStoreOpen){
//                itemView.tv_store_status.text="Open Now"
//                itemView.tv_store_status.setTextColor( context.resources.getColor(R.color.colorGreen))
//                itemView.tv_store_open_time.text="Will close at "+data.time
//
//            }else{
//                itemView.tv_store_status.text="Closed"
//                itemView.tv_store_status.setTextColor( context.resources.getColor(R.color.colorRed))
//                itemView.tv_store_open_time.text="Will open at "+data.time
//            }
//            itemView.tv_store_status.text
//            if (data.isStoreSelected) {
//                defaultItemPosition = adapterPosition
//                itemView.img_store_selector.isSelected = true
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_single_item, parent, false)
        return AddressVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: AddressVH, position: Int) {
        holder.bind(listData[position])


    }
}