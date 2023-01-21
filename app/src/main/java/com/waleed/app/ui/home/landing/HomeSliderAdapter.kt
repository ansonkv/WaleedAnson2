package com.waleed.app.ui.home.landing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.waleed.app.R
import com.waleed.app.business.data.newdata.HomeSliderDataResponse
import com.waleed.app.business.data.newdata.SearchFilterCategoriesData
import com.waleed.app.util.loadImg
import kotlinx.android.synthetic.main.home_slider_single_item.view.*


class HomeSliderAdapter(
    private var list: List<HomeSliderDataResponse.HomeSliderData>,
    private var context: Context
) : PagerAdapter() {
    var onItemClick: ((HomeSliderDataResponse.HomeSliderData) -> Unit)? = null
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = inflater.inflate(R.layout.home_slider_single_item, container, false)
        itemView.img_offer.loadImg(list[position].imageMobile)
        itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }
        (container as ViewPager).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as LinearLayout)
    }

    override fun getCount(): Int {
        return list.size
    }
}