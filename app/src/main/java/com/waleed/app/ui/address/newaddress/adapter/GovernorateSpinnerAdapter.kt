package com.waleed.app.ui.address.newaddress.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.GovStateListResponse
import com.waleed.app.util.setLanguageString

class GovernorateSpinnerAdapter(
    val context: Context,
    var listGovernorate: ArrayList<GovStateListResponse.GovState>
) : BaseAdapter() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.spinner_single_item, parent, false)
            vh =
                ItemRowHolder(
                    view
                )
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }
        vh.label.setLanguageString(
            listGovernorate[position].govStateName,
            listGovernorate[position].govStateNameAr
        )

        return view
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return listGovernorate.size
    }

    private class ItemRowHolder(row: View?) {

        val label: TextView

        init {
            this.label = row?.findViewById(R.id.tv_spinner) as TextView
        }
    }

}