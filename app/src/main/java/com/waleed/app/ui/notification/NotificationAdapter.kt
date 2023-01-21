package com.waleed.app.ui.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.NotificationData
import com.waleed.app.business.data.newdata.NotificationResponse
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.notification_single_item.view.*

class NotificationAdapter(
    private var list: ArrayList<NotificationResponse.NotificationItem>,
    private var context: Context
) : RecyclerView.Adapter<NotificationAdapter.NotificationVH>() {

    var onRAteClick: ((NotificationResponse.NotificationItem) -> Unit)? = null
    var onItemClick: ((NotificationResponse.NotificationItem ) -> Unit)? = null
    inner class NotificationVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: NotificationResponse.NotificationItem) {
              itemView.tv_notification_msg.text = data.message
            if (data.showReview==1) {
                itemView.tv_view_more.makeVisible()
                itemView.tv_view_more.setOnClickListener {
                    onRAteClick?.invoke(data)
                }
            } else {
                itemView.tv_view_more.makeGone()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_single_item, parent, false)
        return NotificationVH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationVH, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }
    }
}