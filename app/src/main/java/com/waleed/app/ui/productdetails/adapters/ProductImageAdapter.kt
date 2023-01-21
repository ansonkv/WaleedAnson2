package com.waleed.app.ui.productdetails.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.waleed.app.R
import com.waleed.app.business.data.HomeOfferSliderData
import com.waleed.app.business.data.newdata.ProductDetailsResponse
import com.waleed.app.ui.productdetails.SingleImageActivity
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.AppConstants
import com.waleed.app.util.loadImg
import kotlinx.android.synthetic.main.home_slider_single_item.view.*


class ProductImageAdapter(
    private var list: ArrayList<ProductDetailsResponse.SliderImageData>,
    private var context: Context
) :    PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View =
            inflater.inflate(R.layout.product_details_image_slider, container, false)
        itemView.img_offer.loadImg(list[position].image)
        itemView.img_offer.setOnClickListener {
            var bundle = Bundle()
            bundle.putString(AppConstants.BUNDLE_ZOOM_URL, list[position].image)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context, SingleImageActivity::class.java, bundle
            )
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

    fun add(sliderImageList: ArrayList<ProductDetailsResponse.SliderImageData>) {
        list.clear()
        list.addAll(sliderImageList)
        notifyDataSetChanged()
    }
}