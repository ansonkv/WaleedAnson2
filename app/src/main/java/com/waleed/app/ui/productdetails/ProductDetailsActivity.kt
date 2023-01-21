package com.waleed.app.ui.productdetails

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.*
import com.waleed.app.ui.account.register.RegisterActivity
import com.waleed.app.ui.account.signin.SignInActivity
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.cart.CartActivity
import com.waleed.app.ui.productdetails.adapters.*
import com.waleed.app.ui.reviews.ReviewsActivity
import com.waleed.app.ui.stores.StoresActivity
import com.waleed.app.util.*
import com.waleed.app.util.AppConstants.PREF_KEY_CART_COUNT
import com.waleed.app.util.AppConstants.PREF_LOCAL_CART
import com.waleed.app.util.AppConstants.REQUEST_CODE_CART_FROM_DETAILS
import com.waleed.app.util.AppConstants.STORE_REQUEST
import kotlinx.android.synthetic.main.dialogue_cart_message.*
import kotlinx.android.synthetic.main.dialogue_cart_message.img_close_dialogue
import kotlinx.android.synthetic.main.dialogue_replace_product.*
import kotlinx.android.synthetic.main.product_color_sheet.*
import kotlinx.android.synthetic.main.product_details_activity.*
import kotlinx.android.synthetic.main.toolbar_product_details.*
import java.math.RoundingMode
import javax.inject.Inject


class ProductDetailsActivity : BaseActivity(), ProductDetailsView, View.OnClickListener {

    @Inject
    lateinit var presenter: ProductDetailsPresenter
    private var isFav = false
    private var isColorLayoutVisible = false

    private lateinit var productData: ProductItemData
    private lateinit var productSliderAdapter: ProductImageAdapter
    private var productImageList = arrayListOf<ProductDetailsResponse.SliderImageData>()
    private var selectedStorePosition = -1
    private var selectedStoreId = 0
    private var itemInCartCount = 0
    private var totalCartCount = 0
    private var selectedCombinationId = 0
    private var combinationSelectedFirstTime = true
    private var combinationIDTemp = 0
    private lateinit var productDetails: ProductDetailsResponse
    private var fromSearch = false
    private var selectedProductPrice = 0.0
    private var selectedTempPrice = 0.0
    private lateinit var relatedProductsAdapter: RelatedProductListAdapter
    private lateinit var loginDialog: Dialog
    private lateinit var searchItem: SearchResponse.SearchItem
    private var isUnitAvailable = true
    private lateinit var favProductItemData: ProductItemData

    private var favPosition = 0
    private var favListFromRelatedList = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.product_details_activity)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                listenerCartCount, IntentFilter(AppConstants.ACTION_BROADCAST_CART_COUNT)
            )
        initViews()
    }

    private fun initViews() {
        if (intent.getBooleanExtra(AppConstants.BUNDLE_SEARCH_VALUE, false)) {
            fromSearch = true
            searchItem =
                intent.getSerializableExtra(AppConstants.KEY_PRODUCT_DATA) as SearchResponse.SearchItem
            presenter.getProductDetails(productId = searchItem.productID)
        } else {
            productData = intent.getSerializableExtra(AppConstants.KEY_PRODUCT_DATA) as
                    ProductItemData
            loadFromListData()
        }
        btn_close.setOnClickListener { onBackPressed() }

        btn_cart.setOnClickListener {
            var bundle = Bundle()
            ActivitiesManager.startActivityForResult(
                this, CartActivity::class.java, bundle, REQUEST_CODE_CART_FROM_DETAILS
            )
        }
        tv_view_more_review.setOnClickListener {
            val bundle = Bundle()
            ActivitiesManager.goTOAnotherActivityWithBundle(
                this, ReviewsActivity::class.java, bundle
            )
        }

        val cartCount = Waleed.appContext.getAppPref()
            .getInteger(PREF_KEY_CART_COUNT)
        if (cartCount!! >= 1) {
            tv_cart_count_toolbar.makeVisible()
            tv_cart_count_toolbar.text = cartCount.toString()
        } else
            tv_cart_count_toolbar.makeGone()

        ll_checkout_btn.setOnClickListener(this)
        /*   tv_cart_count.setOnClickListener(this)

           tv_cart_btn.setOnClickListener(this)*/
        tv_color.setOnClickListener(this)
        view_color.setOnClickListener(this)
        tv_product_size.setOnClickListener(this)
        ll_size.setOnClickListener(this)
        img_close_sheet.setOnClickListener(this)
//        btn_collect_store.setOnClickListener(this)
        btn_sheet_done.setOnClickListener(this)


    }

    private fun loadFromListData() {
        /**Setdata from intent*/
        tv_product_name.text = productData.getProductName()
        selectedProductPrice = productData.discountUnitPrice.getAmount()
        tv_product_price.text = productData.getProductPrice()
        productImageList.add(ProductDetailsResponse.SliderImageData(productData.thumbImage, 0))
        productSliderAdapter = ProductImageAdapter(productImageList, this)
        view_pager_product_image.adapter = productSliderAdapter
        indicator.setViewPager(view_pager_product_image)
        presenter.getProductDetails(productId = productData.productID)

        img_fav_btn.setOnClickListener {
            if (LoggedUser.getInstance().isUserLoggedIn()) {
                if (productData.favorite == 0) {
                    presenter.addToFavourite(productData.productID)
                } else {
                    presenter.removeFromFavourite(productData.productID)
                }
            } else {
                openLoginDialogue()
            }
        }
        if (productData.unitsAvailable <= 0) {
            tv_stock_status.text = getString(R.string.out_of_stock)
            tv_stock_status.setTextColor(resources.getColor(R.color.colorRed))
            isUnitAvailable = false
            btn_collect_store.text = getString(R.string.replace_product)
            rlAddToCart.makeGone()
        }
    }

    private val listenerCartCount = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                AppConstants.ACTION_BROADCAST_CART_COUNT -> {
                    checkInCart()
                    val cartCount = Waleed.appContext.getAppPref()
                        .getInteger(PREF_KEY_CART_COUNT)
                    if (cartCount!! >= 1) {
                        tv_cart_count_toolbar.makeVisible()
                        tv_cart_count_toolbar.text = cartCount.toString()
                    } else
                        tv_cart_count_toolbar.makeGone()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "Range")
    override fun showProductDetails(detailsResponse: ProductDetailsResponse) {
        productDetails = detailsResponse

        val productInfo = detailsResponse.product

        if (fromSearch) {

            tv_product_name.text = productInfo.getProductName()
            selectedProductPrice = productInfo.discountUnitPrice.getAmount()
            tv_product_price.text = productInfo.getProductPrice()
            productSliderAdapter = ProductImageAdapter(productDetails.sliderImageList, this)
            view_pager_product_image.adapter = productSliderAdapter
            indicator.setViewPager(view_pager_product_image)
            productData=ProductItemData(
                discountUnitPrice=productInfo.discountUnitPrice,
                discountUnitPriceAr = productInfo.discountUnitPriceAr,
                favorite = productInfo.favorite,
                points = productInfo.points,productID = productInfo.productID,
                productName = productInfo.productName,productNameAr = productInfo.productNameAr,
                thumbImage = productDetails.sliderImageList[0].image,
                unitPrice = productInfo.unitPrice,unitPriceAr = productInfo.discountUnitPriceAr,
                unitsAvailable = productInfo.unitsAvailable
            )
            if (productInfo.unitsAvailable == 0) {
                tv_stock_status.text = getString(R.string.out_of_stock)
                tv_stock_status.setTextColor(resources.getColor(R.color.colorRed))
                isUnitAvailable = false
                btn_collect_store.text = getString(R.string.replace_product)
                rlAddToCart.makeGone()
            }
            img_fav_btn.setOnClickListener {
                if (productData.favorite == 0) {
                    presenter.addToFavourite(productInfo.productID)
                } else {
                    presenter.removeFromFavourite(productInfo.productID)
                }
            }
        }

        /**Product Slider Image*/
        if (detailsResponse.sliderImageList.isNotEmpty()) {
            productSliderAdapter.add(detailsResponse.sliderImageList)
            indicator.setViewPager(view_pager_product_image)
        }

        /**Product Name, favourite and price*/
        tv_product_name.setLanguageString(
            productInfo.productName,
            productInfo.productNameAr
        )

        tv_product_price.setLanguageString(
            productInfo.discountUnitPrice,
            productInfo.discountUnitPriceAr
        )

        img_fav_btn.isSelected = detailsResponse.product.favorite == 1

        /**Description ,brand*/
        if (!detailsResponse.product.description.isNullOrBlank()) {

            tv_product_desc_value.makeVisible()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tv_product_desc_value.text =
                    Html.fromHtml(
                        LangUtils.getLanguageString(
                            productInfo.description, productInfo.descriptionAr
                        ), Html.FROM_HTML_MODE_COMPACT
                    )
            } else {
                tv_product_desc_value.text = Html.fromHtml(
                    LangUtils.getLanguageString(
                        productInfo.description, productInfo.descriptionAr
                    )
                )
            }
        }

        if (!detailsResponse.product.brandName.isNullOrBlank()) {
            tv_product_brand_title.makeVisible()
            tv_product_brand_value.makeVisible()
            tv_product_brand_value.setLanguageString(
                englishString = productInfo.brandName,
                arabicString = productInfo.brandNameAr
            )
        }

        /**Collect from store*/
        if (!detailsResponse.productStores.isNullOrEmpty()) {
            btn_collect_store.makeVisible()
            if (productInfo.replacementProduct == null && !isUnitAvailable) {
                btn_collect_store.makeGone()
                rlAddToCart.makeGone()
            }

            btn_collect_store.setOnClickListener {
                if (isUnitAvailable) {
                    var bundle = Bundle()
                    bundle.putSerializable(
                        AppConstants.KEY_STORE_LIST,
                        detailsResponse.productStores
                    )
                    bundle.putInt(AppConstants.KEY_SELECTED_STORE_POSITION, selectedStorePosition)
                    ActivitiesManager.startActivityForResult(
                        this,
                        StoresActivity::class.java,
                        bundle,
                        STORE_REQUEST
                    )
                } else {


                    var dialog = Dialog(this, 0)

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.window!!.attributes.dimAmount = 0.4f

                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialogue_replace_product)

                    dialog.img_replace_product.loadImg(productInfo.replacementProduct.thumbImage)

                    dialog.tv_replace_message.text =
                        getString(
                            R.string.replacement_string,
                            LangUtils.getLanguageString(
                                productInfo.replacementProduct.productName,
                                productInfo.replacementProduct.productNameAr
                            )
                        )


                    dialog.btn_cancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.img_close_dialogue.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.btn_replace.setOnClickListener {
                        dialog.dismiss()
                        var bundle = Bundle()
                        bundle.putSerializable(
                            AppConstants.KEY_PRODUCT_DATA,
                            productInfo.replacementProduct
                        )
                        ActivitiesManager.goTOAnotherActivityWithBundle(
                            this, ProductDetailsActivity::class.java, bundle
                        )
                    }

                    dialog.show()
                }
            }
        }
        /**Age Category*/
        if (productInfo.featured.isEmpty()) {
            img_featured.makeGone()
            tv_featured_category.makeGone()
        } else {
            img_featured.makeVisible()
            tv_featured_category.makeVisible()
            tv_featured_category.setLanguageString(productInfo.featured, productInfo.featuredAr)
        }
        if (productInfo.ageGroups.isNullOrEmpty()) {


        } else {
            rlCategoryAge.makeVisible()
            tv_featured_age.setLanguageString(
                productInfo.ageGroups, productInfo.ageGroupsAr
            )
        }

        /**TAgs*/
        if (!productInfo.tags.isNullOrBlank()) {
            tv_product_tags_title.makeVisible()
            rv_tags.makeVisible()
            rv_tags.layoutManager = LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false
            )
            var result =
                LangUtils.getLanguageString(productInfo.tags, productInfo.tagsAr).split(",")
                    .map { it.trim() }
            rv_tags.adapter = ProductTagsAdapter(result, this)
        }

        /**Reviews Section*/
        if (detailsResponse.reviewsCount > 0) {
            frameRatingCount.makeVisible()
            tv_review_count.text = getString(
                R.string.review_count_string,
                detailsResponse.reviewsCount.toString()
            )
            rating_bar.rating = productInfo.rating
                .toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
        } else {
            tv_review_count.text = getString(
                R.string.review_count_string,
                detailsResponse.reviewsCount.toString()
            )
            rating_bar.makeGone()
        }

        if (!detailsResponse.reviewList.isNullOrEmpty()) {
            tv_review_count_two.makeVisible()
            rating_bar_two.makeVisible()
            tv_rating.makeVisible()
            tv_rating.text =
                productInfo.rating.toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
                    .toString() + "/5"
            tv_review_count_two.text = getString(
                R.string.review_count_string,
                detailsResponse.reviewsCount.toString()
            )
            rating_bar_two.rating = productInfo.rating
                .toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
        }

        //Review Images
        if (!detailsResponse.allReviewImages.isNullOrEmpty()) {
            rv_review_images.makeVisible()
            rv_review_images.layoutManager = LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false
            )
            var imageAdapter = ReviewImageAdapter(
                detailsResponse.allReviewImages, this
            )
            rv_review_images.adapter = imageAdapter
            imageAdapter.onItemClick =
                { reviewImage: ReviewListData.ReviewImage, position: Int ->
                    var bundle = Bundle()
                    bundle.putString(AppConstants.BUNDLE_ZOOM_URL, reviewImage.image)
                    ActivitiesManager.goTOAnotherActivityWithBundle(
                        this, SingleImageActivity::class.java, bundle
                    )
                }
        }

        //Review List
        if (!detailsResponse.reviewList.isNullOrEmpty()) {
            rv_review_list.makeVisible()
            if (detailsResponse.reviewList.size > 3) {

                tv_view_more_review.makeVisible()

                tv_view_more_review.setOnClickListener {
                    val bundle = Bundle()

                    bundle.putInt(
                        AppConstants.BUNDLE_REVIEW_COUNT,
                        detailsResponse.reviewsCount
                    )
                    bundle.putString(
                        AppConstants.BUNDLE_REVIEW_RATING,
                        productInfo.rating
                    )
                    bundle.putSerializable(
                        AppConstants.BUNDLE_REVIEW_LIST,
                        detailsResponse.reviewList
                    )
                    bundle.putSerializable(
                        AppConstants.BUNDLE_REVIEW_IMAGE_LIST,
                        detailsResponse.allReviewImages
                    )
                    ActivitiesManager.goTOAnotherActivityWithBundle(
                        this, ReviewsActivity::class.java, bundle
                    )
                }
            }
            rv_review_list.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            rv_review_list.adapter =
                ProductReviewAdapter(detailsResponse.reviewList.take(3), this)
        }

        /**SIze and Color Option*/
        if (!detailsResponse.colorCombinationResponseList.isNullOrEmpty()) {

            ll_size_content.makeVisible()
            if (detailsResponse.colorCombinationResponseList[0].sizes[0].sizeID == 0&&detailsResponse.colorCombinationResponseList[0].sizes.size==1) {
                tv_product_size.text=getString(R.string.select_varient_string)
            } else {
                tv_product_size.text = getString(
                    R.string.size_string,
                    LangUtils.getLanguageString(
                        detailsResponse.colorCombinationResponseList[0].sizes[0].sizeName,
                        detailsResponse.colorCombinationResponseList[0].sizes[0].sizeName
                    )
                )
            }
            selectedCombinationId =
                detailsResponse.colorCombinationResponseList[0].sizes[0].combinationID
            selectedProductPrice =
                detailsResponse.colorCombinationResponseList[0].sizes[0].discountUnitPrice.getAmount()
            tv_product_price.setLanguageString(
                detailsResponse.colorCombinationResponseList[0].sizes[0].discountUnitPrice,
                detailsResponse.colorCombinationResponseList[0].sizes[0].discountUnitPriceAr
            )
            /*rv_size.layoutManager = LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false
            )
            var productSizeAdapter =
                ProductSizeAdapter(detailsResponse.colorCombinationResponseList, this)
            rv_size.adapter = productSizeAdapter

            productSizeAdapter.onItemClick = { list: ProductDetailsResponse.ColorCombination ->
                showProductColor(listOf(list.colorCode), list.combinationID)
                if (combinationSelectedFirstTime) {
                    selectedCombinationId = list.combinationID
                    combinationSelectedFirstTime = false
                }
                tv_product_size.text = getString(
                    R.string.size_string,
                    LangUtils.getLanguageString(
                        list.sizeName,
                        list.sizeNameAr
                    )
                )
            }*/

            if (detailsResponse.colorCombinationResponseList.any { it -> it.colorID == 0 } && detailsResponse.colorCombinationResponseList.size == 1) {
                tv_color.makeGone()
                view_color.makeGone()
                rv_color.makeGone()
                tv_color_title.makeGone()
                tv_product_size.text=getString(R.string.select_varient_string)

                showProductSize(detailsResponse.colorCombinationResponseList[0].sizes)
            } else {
                val shape = GradientDrawable()
                shape.cornerRadius = 48f
                var colorCode = detailsResponse.colorCombinationResponseList[0].colorCode
                shape.setColor(Color.parseColor(colorCode))
                view_color.background = shape

                rv_color.layoutManager = LinearLayoutManager(
                    this, RecyclerView.HORIZONTAL, false
                )

                var productColorAdapter =
                    ProductColorAdapter(detailsResponse.colorCombinationResponseList, this)

                rv_color.adapter = productColorAdapter

                productColorAdapter.onItemClick = {
                    if (combinationSelectedFirstTime) {
                        selectedCombinationId = it.sizes[0].combinationID
                        combinationSelectedFirstTime = false
                    }
                    val shape = GradientDrawable()
                    shape.cornerRadius = 48f
                    var colorCode = it
                    shape.setColor(Color.parseColor(it.colorCode))
                    view_color.background = shape
                    showProductSize(it.sizes)
                    tv_product_size.text = getString(
                        R.string.size_string,
                        LangUtils.getLanguageString(
                            it.sizes[0].sizeName,
                            it.sizes[0].sizeNameAr
                        )
                    )
                }
            }
            checkInCart()
        } else {
            ll_size_content.makeGone()
            checkInCart()
        }

        /**Related Products*/
        if (detailsResponse.relatedProductList.isNullOrEmpty()) {
            tv_related_products.makeGone()
            rv_related_products.makeGone()
        } else {
            tv_related_products.makeVisible()
            rv_related_products.makeVisible()

            rv_related_products.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            relatedProductsAdapter =
                RelatedProductListAdapter(detailsResponse.relatedProductList, this)
            rv_related_products.adapter = relatedProductsAdapter

            relatedProductsAdapter.onItemClick = {
                var bundle = Bundle()
                bundle.putSerializable(AppConstants.KEY_PRODUCT_DATA, it)
                ActivitiesManager.goTOAnotherActivityWithBundle(
                    this, ProductDetailsActivity::class.java, bundle
                )
            }

            relatedProductsAdapter.onFavItemClick =
                { productItemData: ProductItemData, position: Int ->
                    if (LoggedUser.getInstance().isUserLoggedIn()) {

                        if (productItemData.favorite == 0) {
                            presenter.addToFavourite(productItemData, position)
                        } else {
                            presenter.removeFromFavourite(productItemData, position)
                        }
                    } else {
                        favProductItemData = productItemData!!
                        favPosition = position
                        favListFromRelatedList = true
                        openLoginDialogue()
                    }
                }
            relatedProductsAdapter.onCartClick = {
                presenter.addToCart(it!!.productID, 0, 1, 0, true, it)

            }
        }
    }

    private fun showProductSize(sizes: List<ProductSizeCombinationResponse.Size>) {
        if (sizes.size == 1 && sizes[0].sizeID == 0) {
            combinationIDTemp = sizes[0].combinationID
            selectedTempPrice = sizes[0].discountUnitPrice.getAmount()
            tv_product_price.setLanguageString(
                sizes[0].discountUnitPrice,
                sizes[0].discountUnitPriceAr
            )
            tv_size_title.makeGone()
            rv_size.makeGone()
            tv_product_size.makeGone()
        } else {
            rv_size.layoutManager = LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false
            )
            var productSizeAdapter = ProductSizeAdapter(sizes, this)
            rv_size.adapter = productSizeAdapter
            productSizeAdapter.onItemClick = {
                combinationIDTemp = it.combinationID
                tv_product_size.text = getString(
                    R.string.size_string,
                    LangUtils.getLanguageString(
                        it.sizeName,
                        it.sizeNameAr
                    )
                )
                selectedTempPrice = it.discountUnitPrice.getAmount()
                tv_product_price.setLanguageString(
                    it.discountUnitPrice,
                    it.discountUnitPriceAr
                )
            }
        }
    }

    private fun openLoginDialogue() {
        loginDialog = Dialog(this, 0)
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loginDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loginDialog.window!!.attributes.dimAmount = 0.4f

        loginDialog.setCancelable(true)

        loginDialog.setContentView(R.layout.dialogue_checkout_message)
        loginDialog.tv_cart_message.text = getString(R.string.login_description_string)
        loginDialog.tv_cart_pop_product_name.text = getString(R.string.please_login_string)
        loginDialog.img_close_dialogue.setOnClickListener {
            loginDialog.dismiss()
        }
        loginDialog.btn_login.setOnClickListener {
            ActivitiesManager.startActivityForResult(
                this, SignInActivity::class.java,
                null, AppConstants.LOGIN_REQUEST_CODE
            )
        }
        loginDialog.btn_register.setOnClickListener {
            ActivitiesManager.startActivityForResult(
                this, RegisterActivity::class.java,
                null, AppConstants.REGISTER_REQUEST_CODE
            )
        }
        loginDialog.show()
    }

    private fun showProductColor(it: List<String>, combinationID: Int) {
//        combinationIDTemp = combinationID
//        rv_color.layoutManager = LinearLayoutManager(
//            this, RecyclerView.HORIZONTAL, false
//        )
//        var prodColorAdapter = ProductColorAdapter(it, this)
//        rv_color.adapter = prodColorAdapter
//        prodColorAdapter.onItemClick = {
//            val shape = GradientDrawable()
//            shape.cornerRadius = 48f
//            var colorCode = it
//            shape.setColor(Color.parseColor(colorCode))
//            view_color.background = shape
//        }
    }

    override fun onCartItemAddSuccess(
        cartCount: Int,
        isFromList: Boolean,
        productItemData: ProductItemData
    ) {
        Waleed.appContext.getAppPref().saveInteger(PREF_KEY_CART_COUNT, cartCount)
        this.sendMyBroadCast(AppConstants.ACTION_BROADCAST_CART_COUNT)
        onCartAdded(cartCount, isFromList, productItemData)
    }

    override fun onFavAddedSuccess() {
        productData.favorite = 1
        img_fav_btn.isSelected = true
    }

    override fun onFavAddedSuccess(data: ProductItemData, position: Int) {
        data.favorite = 1
        relatedProductsAdapter.updateItem(data, position)
    }

    override fun onFavRemoveSuccess() {
        productData.favorite = 0
        img_fav_btn.isSelected = false
    }

    override fun onFavRemoveSuccess(data: ProductItemData, position: Int) {
        data.favorite = 0
        relatedProductsAdapter.updateItem(data, position)
    }

    override fun onAddedToCartFromList(cartCountResponse: AddToCartResponse) {
        showPop(cartCountResponse.message)
    }

    /*private fun openViewer(
        startPosition: Int,
        target: ImageView,
        imageList: ArrayList<ReviewListData.ReviewImage>
    ) {
        viewer = StfalconImageViewer.Builder<ReviewListData.ReviewImage>(
            this,
            imageList,
            ::loadPosterImage
        )
            .withStartPosition(startPosition)
            .withTransitionFrom(target)
            .withImageChangeListener {
                viewer.updateTransitionImage(postersGridView.imageViews[it])
            }
            .show()
    }*/


    private fun loadPosterImage(imageView: ImageView, poster: ReviewListData.ReviewImage?) {
        imageView.apply {
            background = getDrawableCompat(R.drawable.ic_app_launcher)
            loadImg(poster!!.image)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            ll_checkout_btn -> cartButtonClicked()
            tv_cart_count -> cartButtonClicked()

            tv_color -> onColorSizeClick()
            view_color -> onColorSizeClick()
            tv_product_size -> onColorSizeClick()
            ll_size -> onColorSizeClick()
            img_close_sheet -> onColorSizeClick()
            btn_sheet_done -> sheetButtonDoneClick()

        }
    }

    private fun cartButtonClicked() {
        if (fromSearch) {
            presenter.addToCart(
                productId = searchItem.productID,
                combinationId = selectedCombinationId,
                quantity = itemInCartCount + 1,
                storeId = selectedStoreId,
                isFromList = false,
                productItemData = ProductItemData()
            )
        } else {
            presenter.addToCart(
                productId = productData.productID,
                combinationId = selectedCombinationId,
                quantity = itemInCartCount + 1,
                storeId = selectedStoreId,
                isFromList = false,
                productItemData = ProductItemData()
            )
        }
    }

    private fun sheetButtonDoneClick() {
        onColorSizeClick()
        selectedCombinationId = combinationIDTemp
        selectedProductPrice = selectedTempPrice
    }

    private fun onColorSizeClick() {

        if (isColorLayoutVisible) {
            isColorLayoutVisible = false
            layout_color.startAnimation(
                AnimationUtils.loadAnimation(
                    this, R.anim.bottom_sheet_anim_down
                )
            )
            layout_color.makeGone()
        } else {
            isColorLayoutVisible = true
            layout_color.startAnimation(
                AnimationUtils.loadAnimation(
                    this, R.anim.bottom_sheet_anim_up
                )
            )
            layout_color.makeVisible()
        }
    }

    private fun onCartAdded(cartCount: Int, isFromList: Boolean, productItemData: ProductItemData) {

        checkInCart()
        showAddedToCartMessage(isFromList, productItemData)
    }

    private fun showAddedToCartMessage(isFromList: Boolean, productItemData: ProductItemData) {

        var dialog = Dialog(this, 0)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.dimAmount = 0.4f

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogue_cart_message)

        if (isFromList) {
            if (productItemData.points > 0) {
                dialog.tv_cart_message.text =
                    getString(
                        R.string.earning_point_message,
                        productItemData.points.toString()
                    )
                dialog.tv_cart_message.makeVisible()
            }

            dialog.tv_cart_pop_product_name.text =
                getString(
                    R.string.item_added_message,
                    LangUtils.getLanguageString(
                        productItemData.productName,
                        productItemData.productNameAr
                    )
                )
        } else {
//            if (productDetails.product.points > 0) {
            dialog.tv_cart_message.text =
                getString(
                    R.string.earning_point_message,
                    productDetails.product.points.toString()
                )
            dialog.tv_cart_message.makeVisible()
//            }


            dialog.tv_cart_pop_product_name.text =
                getString(
                    R.string.item_added_message,
                    LangUtils.getLanguageString(
                        productDetails.product.productName,
                        productDetails.product.productNameAr
                    )
                )
        }
        dialog.btn_login.setOnClickListener {
            dialog.dismiss()

        }
        dialog.img_close_dialogue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.btn_register.setOnClickListener {
            dialog.dismiss()
            ActivitiesManager.startActivityForResult(
                this, CartActivity::class.java, Bundle(), REQUEST_CODE_CART_FROM_DETAILS
            )
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                STORE_REQUEST -> {
                    selectedStorePosition =
                        data!!.getIntExtra(AppConstants.KEY_SELECTED_STORE_POSITION, -1)
                    var selectedStore = data!!.getSerializableExtra(
                        AppConstants.KEY_SELECTED_STORE
                    ) as ProductStore
                    selectedStoreId = selectedStore.storeID
                    btn_collect_store.text = getString(
                        R.string.collect_from_store_string_name,
                        LangUtils.getLanguageString(selectedStore.area, selectedStore.areaAr)
                    )
                    Log.e("Selected StoreID", selectedStoreId.toString())
                }
                AppConstants.LOGIN_REQUEST_CODE -> {
                    if (favListFromRelatedList) {
                        if (favProductItemData!!.favorite == 0) {
                            presenter.addToFavourite(favProductItemData.productID)
                        } else {
                            presenter.removeFromFavourite(favProductItemData.productID)
                        }
                    } else {
                        if (productData!!.favorite == 0) {
                            presenter.addToFavourite(productData.productID)
                        } else {
                            presenter.removeFromFavourite(productData.productID)
                        }
                    }
                }
                AppConstants.REGISTER_REQUEST_CODE -> {
                    if (favListFromRelatedList) {
                    } else {
                        if (productData!!.favorite == 0) {
                            presenter.addToFavourite(productData.productID)
                        } else {
                            presenter.removeFromFavourite(productData.productID)
                        }
                    }
                }
                REQUEST_CODE_CART_FROM_DETAILS -> {
                    checkInCart()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (isColorLayoutVisible) {
            onColorSizeClick()
        } else {
            super.onBackPressed()
        }
    }

    private fun checkInCart() {
        var localCart: CartListDataResponse? = null
        if (Waleed.appContext.getAppPref()
                .loadObject(PREF_LOCAL_CART, CartListDataResponse::class.java) != null
        ) {
            localCart = Waleed.appContext.getAppPref()
                .loadObject(
                    PREF_LOCAL_CART,
                    CartListDataResponse::class.java
                ) as CartListDataResponse
        }
        var hasProductInCart = false
        if (localCart != null) {
            for (cartItem in localCart.cartItems) {
                if (cartItem.productID == productData.productID && cartItem.combinationID == selectedCombinationId) {
                    itemInCartCount = cartItem.selectedQuantity
                    tv_cart_count.text = cartItem.selectedQuantity.toString()
                    tv_cart_btn.text = getString(R.string.item_in_cart)
                    tv_cart_count.makeVisible()
                    img_cart_btn.makeGone()
                    tv_cart_price.text = getString(
                        R.string.unit_price_string,
                        calculateProductAmount(selectedProductPrice, itemInCartCount)
                    )
                    hasProductInCart = true
                }
            }
        }
        if (hasProductInCart) {
            tv_cart_count.makeVisible()
            img_cart_btn.makeGone()
            tv_cart_btn.text = getString(R.string.item_in_cart)
        } else {
            tv_cart_count.makeGone()
            img_cart_btn.makeVisible()
            tv_cart_btn.text = getString(R.string.add_to_cart_string)
        }
    }
}