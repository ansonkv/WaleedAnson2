package com.waleed.app.ui.orders.list.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.MyOrderResponse
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.orders_single_item.view.*

class OrderListAdapter(
    private var listData: List<MyOrderResponse.OrderItem>,
    private var context: Context
) :
    RecyclerView.Adapter<OrderListAdapter.OrderItemVH>() {

    var onItemClick: ((MyOrderResponse.OrderItem) -> Unit)? = null
    lateinit var orderProductAdapter: OrderProductAdapter

    inner class OrderItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productList: RecyclerView = itemView.rv_order_product_list
        fun bindData(data: MyOrderResponse.OrderItem) {
            itemView.tv_order_date.text = data.orderDate
            itemView.tv_order_id.text =
                context.getString(R.string.order_id_value, data.uniqueOrderID)
            itemView.rv_order_product_list.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            orderProductAdapter =
                OrderProductAdapter(
                    data.orderedProductItems, context, data.orderStatusID == 3,
                    data.orderDate, data.uniqueOrderID
                )
            itemView.rv_order_product_list.adapter = orderProductAdapter
            itemView.tv_order_status.setLanguageString(
                data.orderStatusEN, data.orderStatusAR
            )
            when (data.orderStatusID) {

                1 -> {
                    itemView.rl_products.isSelected = false
                    itemView.tv_order_status.setTextColor(context.resources.getColor(R.color.colorAccent))
                }
                3 -> {
                    itemView.tv_order_status.setTextColor(context.resources.getColor(R.color.colorGreen))
                    itemView.rl_products.isSelected = true
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.orders_single_item, parent, false)
        return OrderItemVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: OrderItemVH, position: Int) {
        holder.bindData(listData[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(listData[position])
        }
    }
}