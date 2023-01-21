package com.waleed.app.ui.productdetails.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.ReviewData
import com.waleed.app.business.data.newdata.ReviewListData
import com.waleed.app.ui.productdetails.SingleImageActivity
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.AppConstants
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.product_review_single_item.view.*

class ProductReviewAdapter(
    private var listData: List<ReviewListData>,
    private var context: Context
) :
    RecyclerView.Adapter<ProductReviewAdapter.ReviewVH>() {
    inner class ReviewVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ReviewListData) {
            itemView.tv_review_name.text = data.customerName
            itemView.tv_review_date.text = data.reviewDate
            itemView.tv_review_value.text = data.review
            if (data.reviewImages != null) {
                if (data.reviewImages.isNotEmpty()) {
                    itemView.rv_review_image_upload.makeVisible()
                    itemView.rv_review_image_upload.layoutManager = LinearLayoutManager(
                        context, RecyclerView.HORIZONTAL, false
                    )
                    var imageAdapter=ReviewImageAdapter(data.reviewImages, context)
                    itemView.rv_review_image_upload.adapter =imageAdapter
                    imageAdapter.onItemClick={ reviewImage: ReviewListData.ReviewImage, position: Int ->
                        var bundle = Bundle()
                        bundle.putString(AppConstants.BUNDLE_ZOOM_URL, reviewImage.image)
                        ActivitiesManager.goTOAnotherActivityWithBundle(
                            context, SingleImageActivity::class.java, bundle
                        )
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewVH {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.product_review_single_item, parent, false)
        return ReviewVH(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ReviewVH, position: Int) {
        holder.bind(listData[position])
    }

}