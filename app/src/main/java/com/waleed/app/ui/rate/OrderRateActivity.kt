package com.waleed.app.ui.rate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.aminography.choosephotohelper.ChoosePhotoHelper
import com.aminography.choosephotohelper.callback.ChoosePhotoCallback
import com.bumptech.glide.Glide
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.MyOrderResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.*
import com.waleed.app.util.AppConstants.BUNDLE_ORDER_DATE
import com.waleed.app.util.AppConstants.BUNDLE_ORDER_ID
import kotlinx.android.synthetic.main.activity_rate_product.*
import kotlinx.android.synthetic.main.toolbar_product_details.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class OrderRateActivity : BaseActivity(), OrderRateView {
    @Inject
    lateinit var presenter: OrderRatePresenter

    private lateinit var choosePhotoHelper: ChoosePhotoHelper

    private lateinit var productDetails: MyOrderResponse.OrderItem.OrderedProductItem

    private var uriList: ArrayList<Uri> = arrayListOf()
    private var fileList: ArrayList<File> = arrayListOf()
    private var selectedReviewImagePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_rate_product)

        initViews()
    }

    private fun initViews() {
        productDetails = intent.getSerializableExtra(AppConstants.BUNDLE_REVIEW_PRODUCT)
                as MyOrderResponse.OrderItem.OrderedProductItem
        toolbar_page_title_tv.text = getText(R.string.rate_order_string)
        btn_cart.makeGone()
        btn_close.setOnClickListener { onBackPressed() }

        tv_product_date.text = intent.getStringExtra(BUNDLE_ORDER_DATE)
        tv_order_id.text = intent.getStringExtra(BUNDLE_ORDER_ID).toString()
        img_product.loadImg(productDetails.thumbImage)
        tv_product_name.setLanguageString(productDetails.productName, productDetails.productNameAr)
        tv_product_quantity.text =
            getString(
                R.string.quanity_value_string,
                productDetails.quantity.toString()
            )
        tv_product_price.text =
            getString(
                R.string.unit_price_string,
                (productDetails.unitPrice.getAmount() * productDetails.quantity).toString()
            )
        btn_submit.setOnClickListener {
            if (et_review_message.text.isNullOrEmpty())
                showPop(getString(R.string.please_eneter_your_review))
            else {
                val imageList = arrayListOf<MultipartBody.Part>()
                if (fileList.isNotEmpty()) {
                    for ((index, file) in fileList.withIndex()) {
                        val requestFile1 = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val body1 = MultipartBody.Part.createFormData(
                            "ImageFile" + (index + 1),
                            file.name, requestFile1
                        )
                        imageList.add(body1)
                    }
                }

                presenter.submitReview(
                    productDetails.productID,
                    rating_bar.rating.toString(), et_review_message.text.toString(),
                    imageList
                )
            }
        }
        btn_upload_image1.setOnClickListener {
            selectedReviewImagePosition = 0
            choosePhotoHelper.showChooser()
        }
        btn_upload_image2.setOnClickListener {
            selectedReviewImagePosition = 1
            choosePhotoHelper.showChooser()
        }
        btn_upload_image3.setOnClickListener {
            selectedReviewImagePosition = 2
            choosePhotoHelper.showChooser()
        }
        btn_upload_image4.setOnClickListener {
            selectedReviewImagePosition = 3
            choosePhotoHelper.showChooser()
        }

        choosePhotoHelper = ChoosePhotoHelper.with(this)
            .asFilePath()
            .build(ChoosePhotoCallback {
                if (it == null) {
                    btn_upload_image1.setImageResource(R.drawable.ic_upload_image)
                } else {
                    var file = File(it)
                    fileList.add(selectedReviewImagePosition, file)
                    val imageUri: Uri = Uri.fromFile(file)
                    uriList.add(selectedReviewImagePosition, imageUri)
                    handleReviewImage()
                }
            })

        val mTextEditorWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) { //This sets a textView to the current length
                tv_char_count.text = s.length.toString() + "/150"
            }

            override fun afterTextChanged(s: Editable) {}
        }
        et_review_message.addTextChangedListener(mTextEditorWatcher)
    }

    private fun handleReviewImage() {
        when (uriList.size) {
            1 -> {

                Glide.with(this)
                    .load(uriList[0])
                    .into(btn_upload_image1)
                btn_upload_image1.makeVisible()

                btn_upload_image2.makeVisible()
            }
            2 -> {

                Glide.with(this)
                    .load(uriList[0])
                    .into(btn_upload_image1)
                Glide.with(this)
                    .load(uriList[1])
                    .into(btn_upload_image2)
                btn_upload_image1.makeVisible()
                btn_upload_image2.makeVisible()
                btn_upload_image3.makeVisible()
            }
            3 -> {

                Glide.with(this)
                    .load(uriList[0])
                    .into(btn_upload_image1)
                Glide.with(this)
                    .load(uriList[1])
                    .into(btn_upload_image2)
                Glide.with(this)
                    .load(uriList[2])
                    .into(btn_upload_image2)
                btn_upload_image1.makeVisible()
                btn_upload_image2.makeVisible()
                btn_upload_image3.makeVisible()
                btn_upload_image4.makeVisible()
            }
            4 -> {

                Glide.with(this)
                    .load(uriList[0])
                    .into(btn_upload_image1)
                Glide.with(this)
                    .load(uriList[1])
                    .into(btn_upload_image2)
                Glide.with(this)
                    .load(uriList[2])
                    .into(btn_upload_image2)
                Glide.with(this)
                    .load(uriList[3])
                    .into(btn_upload_image2)
                btn_upload_image1.makeVisible()
                btn_upload_image2.makeVisible()
                btn_upload_image3.makeVisible()
                btn_upload_image4.makeVisible()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        choosePhotoHelper.onActivityResult(requestCode, resultCode, data)

    }

    override fun onSuccessReviewSubmission() {
        Dialogs.alertDialog(
            this, getString(R.string.review_submited_success_string), getString(R.string.oke_text)
        ) { _, _ -> onBackPressed() }
    }

}