package com.waleed.app.ui.cart.store

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.ProductStore
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.store_single_item.view.*

class CartStoreAdapter(
    private var listData: MutableList<ProductStore>,
    private var context: Context,
    private var selectedStoreID: Int
) :
    RecyclerView.Adapter<CartStoreAdapter.AddressVH>() {

    var selectedStoreId = selectedStoreID
    var defaultItemPosition: Int = -1
    var onItemClick: ((ProductStore, Int) -> Unit)? = null
    var onCallButtonClick: ((ProductStore) -> Unit)? = null
    var onLocationButtonClick: ((ProductStore) -> Unit)? = null

    inner class AddressVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ProductStore) {
            itemView.tv_store_name.setLanguageString(data.area, data.areaAr)
            itemView.tv_store_address.setLanguageString(data.storeAddress, data.storeAddressAr)

            if (data.storeStatus == "Open Now") {
                itemView.tv_store_status.setLanguageString(
                    data.storeStatus, data.storeStatusAr
                )
                itemView.tv_store_status.setTextColor(context.resources.getColor(R.color.colorGreen))
                itemView.tv_store_open_time.text =
                    context.getString(R.string.will_close_string, data.closingTime)

            } else {
                itemView.tv_store_status.setLanguageString(
                    data.storeStatus, data.storeStatusAr
                )
                itemView.tv_store_status.setTextColor(context.resources.getColor(R.color.colorRed))
                itemView.tv_store_open_time.text =
                    context.getString(R.string.will_open_string, data.closingTime)
            }

            if (selectedStoreId == data.storeID) {
                defaultItemPosition = adapterPosition
                itemView.img_store_selector.isSelected = true
                listData[adapterPosition].isStoreSelected = true
            }else{
                itemView.img_store_selector.isSelected = false
                listData[adapterPosition].isStoreSelected = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.store_single_item, parent, false)
        return AddressVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: AddressVH, position: Int) {
        holder.bind(listData[position])

        holder.itemView.img_store_selector.setOnClickListener {
            listData[position].isStoreSelected = true
            selectedStoreId = listData[position].storeID
            if (defaultItemPosition != -1) {
                listData[defaultItemPosition].isStoreSelected = false
            }
            notifyItemChanged(defaultItemPosition)
            defaultItemPosition = position
            notifyItemChanged(position)
            onItemClick?.invoke(listData[position], position)
        }
        holder.itemView.img_msg_shop.setOnClickListener {
            onLocationButtonClick?.invoke(listData[position])
        }
        holder.itemView.btnCall.setOnClickListener {
            onCallButtonClick?.invoke(listData[position])
        }
    }
}