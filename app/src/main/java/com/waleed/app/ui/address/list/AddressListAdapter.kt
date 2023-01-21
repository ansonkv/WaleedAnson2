package com.waleed.app.ui.address.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.AddressListResponse
import com.waleed.app.util.LangUtils
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.address_single_item.view.*

class AddressListAdapter(
    private var listData: ArrayList<AddressListResponse.AddressItem>,
    private var context: Context,
    private var fromProfilePage: Boolean
) :
    RecyclerView.Adapter<AddressListAdapter.AddressVH>() {

    var defaultItemPosition: Int = -1
    var onItemClick: ((AddressListResponse.AddressItem) -> Unit)? = null
    var onEditClick: ((AddressListResponse.AddressItem) -> Unit)? = null
    var onDeleteClick: ((AddressListResponse.AddressItem) -> Unit)? = null
    inner class AddressVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var selectBtn: TextView = itemView.tv_address_select
        var editBtn:ImageButton=itemView.imgBtnEdit
        var deleteBtn:ImageButton=itemView.imgBtnDelete
        fun bind(data: AddressListResponse.AddressItem) {
            itemView.tv_address_title.text = data.title
            itemView.tv_address_name.text =
                LangUtils.getLanguageString(data.govState, data.govStateAr) + ", " +
                        LangUtils.getLanguageString(data.areaCity, data.areaCityAr) + ", " +

                        context.getString(R.string.block_string) + " " + data.block + ", " +
                        context.getString(R.string.Street_string) + " " + data.street + ", " +
                        context.getString(R.string.building_no_string) + " " + data.building + ", " +
                        context.getString(R.string.floor_no_string) + " " + data.floor + ", " +
                        context.getString(R.string.flat_no_string) + " " + data.flatNo


            itemView.tv_direction.text =
                context.getString(R.string.direction_string) + " " + data.direction
            if (fromProfilePage) {
                itemView.tv_address_select.text = context.getString(R.string.edit_string)
                itemView.tv_address_select.makeGone()
                itemView.imgBtnEdit.makeVisible()
                itemView.tv_address_title.makeVisible()
                itemView.imgBtnDelete.makeVisible()
            } else {
                itemView.tv_address_select.text = context.getText(R.string.select_text)
                itemView.tv_address_title.makeVisible()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressVH {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.address_single_item, parent, false)
        return AddressVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: AddressVH, position: Int) {
        holder.bind(listData[position])

        holder.selectBtn.setOnClickListener {
            onItemClick?.invoke(listData[position])
        }
        holder.editBtn.setOnClickListener {
            onEditClick?.invoke(listData[position])
        }
        holder.deleteBtn.setOnClickListener {
            onDeleteClick?.invoke(listData[position])
        }
    }
}