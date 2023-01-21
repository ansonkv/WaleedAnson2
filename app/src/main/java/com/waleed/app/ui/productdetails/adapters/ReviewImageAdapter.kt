package com.waleed.app.ui.productdetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.business.data.newdata.ProductDetailsResponse
import com.waleed.app.business.data.newdata.ReviewListData
import com.waleed.app.util.loadImg
import kotlinx.android.synthetic.main.product_review_image_single_item.view.*

class ReviewImageAdapter(
    private var imageList: List<ReviewListData.ReviewImage>,
    private var context: Context
) : RecyclerView.Adapter<ReviewImageAdapter.ImageViewVH>() {
    var onItemClick: ((ReviewListData.ReviewImage,Int) -> Unit)? = null

    inner class ImageViewVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(imageUrl: String) {
            itemView.img_review.loadImg(imageUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewVH {
        val view: View =
            LayoutInflater.from(context)
                .inflate(R.layout.product_review_image_single_item, parent, false)
        return ImageViewVH(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewVH, position: Int) {
        holder.bind(imageList[position].image)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(imageList[position],position)
        }
    }

}