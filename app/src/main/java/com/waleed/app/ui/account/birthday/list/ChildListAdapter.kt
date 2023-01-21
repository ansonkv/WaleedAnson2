package com.waleed.app.ui.account.birthday.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.ChildListResponse
import kotlinx.android.synthetic.main.layout_child_list_single_item.view.*

class ChildListAdapter(
    private var listData: ArrayList<ChildListResponse.ChildItem>,
    private var context: Context
) :
    RecyclerView.Adapter<ChildListAdapter.AddressVH>() {

    var defaultItemPosition: Int = -1
     var onEditClick: ((ChildListResponse.ChildItem) -> Unit)? = null
    var onDeleteClick: ((ChildListResponse.ChildItem) -> Unit)? = null

    inner class AddressVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: ChildListResponse.ChildItem) {
            itemView.tv_child_name.text = data.childName
            itemView.tv_dob.text = data.birthDate


            itemView.btnEdit.setOnClickListener {
                onEditClick?.invoke(data)
            }
            itemView.btnDelete.setOnClickListener {
                onDeleteClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_child_list_single_item, parent, false)
        return AddressVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: AddressVH, position: Int) {
        holder.bind(listData[position])
    }
}