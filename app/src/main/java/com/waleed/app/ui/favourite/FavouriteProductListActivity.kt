package com.waleed.app.ui.favourite

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.FavoriteListResponse
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.cart.CartActivity
import com.waleed.app.ui.productdetails.ProductDetailsActivity
import com.waleed.app.util.*
import kotlinx.android.synthetic.main.activity_favourite_list.*
import kotlinx.android.synthetic.main.dialogue_checkout_message.*
import kotlinx.android.synthetic.main.toolbar_common.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class FavouriteProductListActivity : BaseActivity(), FavouriteProductListView {
    @Inject
    lateinit var presenter: FavouriteProductListPresenter
    private lateinit var productList: ArrayList<FavoriteListResponse.Favourite>

    private lateinit var productListAdapter: FavouriteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_list)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        initViews()

    }

    private fun initViews() {
        btn_close.setOnClickListener { onBackPressed() }
        toolbar_page_title_tv.text = getString(R.string.favourite_item_string)
        btn_cart.makeGone()
        presenter.getFavouriteProducts()
        productList = arrayListOf()
        productListAdapter = FavouriteListAdapter(productList)

        rv_product_list.layoutManager = GridLayoutManager(this, 2)
        rv_product_list.adapter = productListAdapter

        productListAdapter.onCartClick={
            var productItemData = ProductItemData(
                productID = it.productID,
                productName = it.productName,
                productNameAr = it.productNameAr,
                discountUnitPrice = it.unitPrice,
                discountUnitPriceAr = it.unitPriceAr,
                favorite = 1,
                thumbImage = it.thumbImage,
                unitPrice = it.unitPrice,
                unitPriceAr = it.unitPriceAr
            )
            presenter.addToCart(it!!.productID, 0, 1, 0, productItemData)
        }

        productListAdapter.onItemClick = {
            var bundle = Bundle()
            var productItemData = ProductItemData(
                productID = it.productID,
                productName = it.productName,
                productNameAr = it.productNameAr,
                discountUnitPrice = it.unitPrice,
                discountUnitPriceAr = it.unitPriceAr,
                favorite = 1,
                thumbImage = it.thumbImage,
                unitPrice = it.unitPrice,
                unitPriceAr = it.unitPriceAr
            )
            bundle.putSerializable(AppConstants.KEY_PRODUCT_DATA, productItemData)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                this, ProductDetailsActivity::class.java, bundle
            )
        }


    }

    override fun showProductList(productListResponse: ArrayList<FavoriteListResponse.Favourite>) {
        tvEmptyList.makeGone()
        rv_product_list.makeVisible()
        productList.addAll(productListResponse)
        productListAdapter.notifyDataSetChanged()
    }

    override fun showEmptyList() {
        tvEmptyList.makeVisible()
        rv_product_list.makeGone()
    }

    override fun onFavRemoveSuccess(data: ProductItemData, position: Int) {

    }

    override fun onCartAddedSuccessfully(productItemData: ProductItemData) {
        showAddedToCartMessage(productItemData)
    }

    override fun onCartItemAddSuccess(size: Int) {
        Waleed.appContext.getAppPref().saveInteger(AppConstants.PREF_KEY_CART_COUNT, size)
        this.sendMyBroadCast(AppConstants.ACTION_BROADCAST_CART_COUNT)
    }

    private fun showAddedToCartMessage(productItemData: ProductItemData) {

        var dialog = Dialog(this, 0)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.dimAmount = 0.4f

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogue_cart_message)
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
        dialog.btn_login.setOnClickListener {
            dialog.dismiss()

        }
        dialog.img_close_dialogue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.btn_register.setOnClickListener {
            dialog.dismiss()
            ActivitiesManager.goTOAnotherActivityWithBundle(
                this, CartActivity::class.java, Bundle()
            )
        }

        dialog.show()
    }
}