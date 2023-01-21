package com.waleed.app.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.CartDataForWrap
import com.waleed.app.business.data.newdata.CartListDataResponse
import com.waleed.app.util.*
import kotlinx.android.synthetic.main.cart_single_item.view.*

class CartAdapter(
    private var listData: ArrayList<CartListDataResponse.CartItem>,
    private var context: Context
) :
    RecyclerView.Adapter<CartAdapter.CartVH>() {

    private var selectedItemPositions = arrayListOf<Int>()
    private var selectedItems = arrayListOf<CartListDataResponse.CartItem>()
    var onItemIncreaseClicked: ((CartListDataResponse.CartItem) -> Unit)? = null
    var onItemDecreaseClicked: ((CartListDataResponse.CartItem) -> Unit)? = null
    var onDeleteClicked: ((CartListDataResponse.CartItem) -> Unit)? = null
    var onCheckClicked: ((ArrayList<Int>, CartListDataResponse.CartItem) -> Unit)? = null
    var onDeliveryChangeClicked: ((Int) -> Unit)? = null
    var onWrapperCancelClick: ((CartListDataResponse.CartItem) -> Unit)? = null
    var onMultipleStoreForWrapping: ((Int) -> Unit)? = null
    var onUnitsUnavailable: ((CartListDataResponse.CartItem) -> Unit)? = null
    var onItemOutOfStock: ((CartListDataResponse.CartItem) -> Unit)? = null
    inner class CartVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var increaseButton: ImageButton = itemView.btn_increase
        var decreaseButton: ImageButton = itemView.btn_decrease
        var deleteButton: ImageButton = itemView.btn_delete
        var tvQuantity: TextView = itemView.tv_item_count
        var checkButton: ImageView = itemView.img_store_selector
        var changeDeliveryButton: TextView = itemView.tv_change_delivery_type
        var cancelWrapButton: TextView = itemView.tv_cancel
        var wrapLayout: LinearLayout = itemView.ll_wrap
        var checkBoxLayout: LinearLayout = itemView.ll_check_box
        fun bindData(data: CartListDataResponse.CartItem) {
            itemView.img_product.loadImg(data.thumbImage)
            itemView.tv_product_name.setLanguageString(
                data.productName, data.productNameAr
            )
            if (data.selectedQuantity > data.unitsAvailable) {
                if (data.unitsAvailable <= 0)
                    onItemOutOfStock?.invoke(data)
                else
                    onUnitsUnavailable?.invoke(data)
            }
            itemView.tv_item_count.text = data.selectedQuantity.toString()
            itemView.tv_product_price.setLanguageString(
                data.unitPrice, data.unitPriceAr
            )
            if (data.storeID == 0) {
                itemView.tv_delivery_type.text =
                    context.getString(R.string.home_delivery_string)
            } else {
                itemView.tv_delivery_type.text =
                    context.getString(
                        R.string.collect_from_store_string_name,
                        LangUtils.getLanguageString(data.storeName, data.storeNameAr)
                    )
            }
            if (data.paperID != 0) {
                itemView.ll_wrap.makeVisible()
                itemView.ll_check_box.makeGone()
                itemView.img_wrap.loadImg(data.paperImage!!)

            } else {
                itemView.ll_wrap.makeGone()
                itemView.ll_check_box.makeVisible()
                itemView.img_store_selector.isSelected =
                    selectedItemPositions.contains(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_single_item, parent, false)
        return CartVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: CartVH, position: Int) {
        holder.bindData(listData[position])
        holder.deleteButton.setOnClickListener {
//            listData.removeAt(position)
//            notifyDataSetChanged()
            onDeleteClicked?.invoke(listData[position])
        }
        holder.decreaseButton.setOnClickListener {
            if (listData[position].selectedQuantity >= 2) {
                onItemDecreaseClicked?.invoke(listData[position])
            }
        }
        holder.increaseButton.setOnClickListener {
            onItemIncreaseClicked?.invoke(listData[position])
        }
        holder.checkButton.setOnClickListener {
            if (selectedItemPositions.contains(position)) {
                holder.checkButton.isSelected = false
                selectedItemPositions.remove(position)
                selectedItems.remove(listData[position])
                onCheckClicked?.invoke(selectedItemPositions, listData[position])
            } else {

                if (selectedItems.size > 0) {
                    var isHaveMultipleStore = false
                    if (listData[position].storeID != 0) {
                        for (item in selectedItems) {
                            if (item.storeID != 0) {
                                if (item.storeID != listData[position].storeID) {
                                    isHaveMultipleStore = true
                                    break
                                }
                            } else {
                                isHaveMultipleStore = false
                            }
                        }
                    } else {
                        isHaveMultipleStore = false
                    }
                    if (isHaveMultipleStore) {
                        onMultipleStoreForWrapping?.invoke(0)

                    } else {
                        holder.checkButton.isSelected = true
                        selectedItems.add(listData[position])
                        selectedItemPositions.add(position)
                        onCheckClicked?.invoke(selectedItemPositions, listData[position])
                    }

                } else {
                    holder.checkButton.isSelected = true
                    selectedItems.add(listData[position])
                    selectedItemPositions.add(position)
                    onCheckClicked?.invoke(selectedItemPositions, listData[position])
                }
            }
        }
        holder.changeDeliveryButton.setOnClickListener {
            onDeliveryChangeClicked?.invoke(position)
        }
        holder.cancelWrapButton.setOnClickListener {
            holder.wrapLayout.makeGone()
            holder.checkBoxLayout.makeVisible()
            listData[position].paperID = 0
            notifyItemChanged(position)
            onWrapperCancelClick?.invoke(listData[position])

        }
    }

    fun changeDeliverOption(position: Int, deliveryType: Int, storeName: String?) {
//        if (deliveryType == 1) {
//            listData[position].deliveryType = deliveryType
//            listData[position].storeName = storeName
//        } else {
//            listData[position].deliveryType = deliveryType
//        }
        notifyItemChanged(position)
    }

    fun updateWraper(wrapData: ArrayList<CartDataForWrap>, imageUrl: String) {
        selectedItemPositions.clear()
//        for (data in wrapData) {
//            for (listItem in listData) {
//                if (data.id == listItem.cartId) {
//                    listItem.wrapImageUrl = imageUrl
//                }
//            }
//        }
        notifyDataSetChanged()
    }
}