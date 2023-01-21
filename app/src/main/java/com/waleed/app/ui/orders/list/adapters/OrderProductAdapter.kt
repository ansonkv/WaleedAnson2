package com.waleed.app.ui.orders.list.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.MyOrderResponse
import com.waleed.app.ui.rate.OrderRateActivity
import com.waleed.app.util.*
import com.waleed.app.util.AppConstants.BUNDLE_ORDER_DATE
import com.waleed.app.util.AppConstants.BUNDLE_ORDER_ID
import com.waleed.app.util.AppConstants.BUNDLE_REVIEW_PRODUCT
import kotlinx.android.synthetic.main.order_product_single_item.view.*

class OrderProductAdapter(
    private var listData: List<MyOrderResponse.OrderItem.OrderedProductItem>,
    private var context: Context, private var isDelivered: Boolean,
    private var orderDate: String, private var orderId: String
) : RecyclerView.Adapter<OrderProductAdapter.OrderProductVH>() {


    inner class OrderProductVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: MyOrderResponse.OrderItem.OrderedProductItem) {
            itemView.img_product.loadImg(data.thumbImage)
            itemView.tv_product_name.setLanguageString(data.productName, data.productNameAr)
            itemView.tv_product_quantity.text =
                context.getString(R.string.quanity_value_string, data.quantity.toString())
            itemView.tv_product_price.text =
                context.getString(
                    R.string.unit_price_string,
                    String.format("%.3f", (data.unitPrice.getAmount() * data.quantity))
                )
            if (data.paperID != 0) {
                itemView.img_wrap.loadImg(data.paperImage)
                itemView.img_wrap.makeVisible()
            }
            if (isDelivered) {
                itemView.btn_product_rate.makeVisible()
                itemView.btn_product_rate.setOnClickListener {
                    var bundle = Bundle()
                    bundle.putSerializable(BUNDLE_REVIEW_PRODUCT, data)
                    bundle.putString(BUNDLE_ORDER_DATE, orderDate)
                    bundle.putString(BUNDLE_ORDER_ID, orderId)
                    ActivitiesManager.goTOAnotherActivityWithBundle(
                        context, OrderRateActivity::class.java,
                        bundle
                    )
                }
            } else {
                itemView.btn_product_rate.makeGone()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductVH {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_product_single_item, parent, false)
        return OrderProductVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: OrderProductVH, position: Int) {
        holder.bind(listData[position])
    }
}