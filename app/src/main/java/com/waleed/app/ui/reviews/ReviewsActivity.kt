package com.waleed.app.ui.reviews

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.ReviewData
import com.waleed.app.business.data.newdata.ReviewListData
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.productdetails.adapters.ProductReviewAdapter
import com.waleed.app.ui.productdetails.adapters.ReviewImageAdapter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.activity_reviews.*
import kotlinx.android.synthetic.main.activity_reviews.rating_bar_two
import kotlinx.android.synthetic.main.activity_reviews.rv_review_images
import kotlinx.android.synthetic.main.activity_reviews.rv_review_list
import kotlinx.android.synthetic.main.activity_reviews.tv_rating
import kotlinx.android.synthetic.main.activity_reviews.tv_review_count_two
import kotlinx.android.synthetic.main.product_details_activity.*
import kotlinx.android.synthetic.main.toolbar_product_details.*
import java.math.RoundingMode
import javax.inject.Inject

class ReviewsActivity : BaseActivity(), ReviewsView {

    @Inject
    lateinit var presenter: ReviewsPresenter
    lateinit var reviewList: ArrayList<ReviewListData>
    lateinit var reviewImageList: ArrayList<ReviewListData.ReviewImage>


    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_reviews)
        initViews()

        var reviewCount = intent.getIntExtra(AppConstants.BUNDLE_REVIEW_COUNT, 0)
        var rating = intent.getStringExtra(AppConstants.BUNDLE_REVIEW_RATING)
        reviewList =
            intent.getSerializableExtra(AppConstants.BUNDLE_REVIEW_LIST)
                    as ArrayList<ReviewListData>

        reviewImageList = intent.getSerializableExtra(AppConstants.BUNDLE_REVIEW_IMAGE_LIST)
                as ArrayList<ReviewListData.ReviewImage>
        tv_review_count_two.makeVisible()
        rating_bar_two.makeVisible()
        tv_rating.makeVisible()

        rating_bar_two.rating = 3f
        tv_rating.text = rating!!.toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
            .toString() + "/5"
        tv_review_count_two.text = getString(
            R.string.review_count_string, reviewCount
        )
        rating_bar_two.rating = rating!!.toBigDecimal()
            .setScale(1, RoundingMode.UP).toFloat()
        displayReviewImageList(reviewImageList)
        displayReviewList(reviewList)

    }

    private fun displayReviewList(reviewList: java.util.ArrayList<ReviewListData>) {
        rv_review_list.makeVisible()
        rv_review_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_review_list.adapter = ProductReviewAdapter(reviewList, this)
    }

    private fun displayReviewImageList(reviewImageList: java.util.ArrayList<ReviewListData.ReviewImage>) {
        rv_review_images.makeVisible()
        rv_review_images.layoutManager = LinearLayoutManager(
            this, RecyclerView.HORIZONTAL, false
        )
        rv_review_images.adapter = ReviewImageAdapter(
            reviewImageList, this
        )
    }

    private fun initViews() {
        btn_cart.makeGone()
        toolbar_page_title_tv.text = getString(R.string.reviews_string)
        btn_close.setOnClickListener { onBackPressed() }
    }

   /* override fun showReviewList(reviewDataList: List<ReviewData>) {   }

    override fun showReviewImages(reviewImageList: List<String>) {
        tv_review_count_two.makeVisible()
        rating_bar_two.makeVisible()
        tv_rating.makeVisible()
        tv_rating.text = "3/5"
        tv_review_count_two.text = "Reviews (10)"
        rating_bar_two.rating = 3f
        if (reviewImageList.isNotEmpty()) {
            rv_review_images.makeVisible()
            rv_review_images.layoutManager = LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false
            )
//            rv_review_images.adapter = ReviewImageAdapter(reviewImageList, this)
        }

    }*/
}