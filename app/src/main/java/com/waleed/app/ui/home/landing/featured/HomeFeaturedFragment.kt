package com.waleed.app.ui.home.landing.featured

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.FeaturedProductListResponse
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.ui.account.register.RegisterActivity
import com.waleed.app.ui.account.signin.SignInActivity
import com.waleed.app.ui.base.BaseFragment
import com.waleed.app.ui.cart.CartActivity
import com.waleed.app.ui.home.CartUpdateListener
import com.waleed.app.ui.home.landing.IdealCategoryClickListener
import com.waleed.app.ui.productdetails.ProductDetailsActivity
import com.waleed.app.util.*
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.dialogue_checkout_message.btn_login
import kotlinx.android.synthetic.main.dialogue_checkout_message.btn_register
import kotlinx.android.synthetic.main.dialogue_checkout_message.img_close_dialogue
import kotlinx.android.synthetic.main.dialogue_checkout_message.tv_cart_message
import kotlinx.android.synthetic.main.dialogue_checkout_message.tv_cart_pop_product_name
import kotlinx.android.synthetic.main.fragment_home_featured.*
import javax.inject.Inject

class HomeFeaturedFragment : BaseFragment(), HomeFeaturedView,
    FeaturedSectionAdapter.ClickListener {
    @Inject
    lateinit var presenter: HomeFeaturedPresenter

    private lateinit var featuredSectionAdapter: SectionedRecyclerViewAdapter
    private lateinit var loginDialog: Dialog
    private var favPosition: Int = 0
    private lateinit var favProductItemData: ProductItemData
    private lateinit var carUpdateListener: CartUpdateListener
    private lateinit var idealCategoryClickListener: IdealCategoryClickListener

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        showContent()
        presenter.getFeaturedProducts()
//        presenter.getProductListData()
    }

    override fun getMainLayout(): Int {
        return R.layout.fragment_home_featured
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        carUpdateListener = context as CartUpdateListener
        idealCategoryClickListener = context as IdealCategoryClickListener
    }

    override fun showFeaturedProductLists(featureListResponse: List<FeaturedProductListResponse.Feature>) {
        progressBar.makeGone()
        rvFeaturedProductList.makeVisible()
        tvEmptyList.makeGone()

        featuredSectionAdapter = SectionedRecyclerViewAdapter()
        for (featuredSection in featureListResponse) {
            if (featuredSection.productList.isNotEmpty()) {
                featuredSectionAdapter.addSection(
                    FeaturedSectionAdapter(
                        LangUtils.getLanguageString(
                            featuredSection.featureName,
                            featuredSection.featureNameAr
                        ), featuredSection.productList,
                        this, context!!
                    )
                )
            }
        }
        val glm = GridLayoutManager(context, 2)
        glm.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (featuredSectionAdapter.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER) {
                    2
                } else 1
            }
        }
        rvFeaturedProductList.layoutManager = glm
        rvFeaturedProductList.adapter = featuredSectionAdapter
    }

    override fun showLoaderInList() {
        progressBar.makeVisible()
        rvFeaturedProductList.makeGone()
        tvEmptyList.makeGone()
    }

    override fun hideLoaderInList() {
        progressBar.makeGone()
        rvFeaturedProductList.makeGone()
        tvEmptyList.makeGone()
    }

    override fun showEmptyList() {
        progressBar.makeGone()
        rvFeaturedProductList.makeGone()
        tvEmptyList.makeVisible()
    }

    override fun onFavAddedSuccess(data: ProductItemData) {
        presenter.getFeaturedProducts()
        Toast.makeText(
            context, getString(
                R.string.fav_added_msg, LangUtils.getLanguageString(
                    data.productName, data.productNameAr
                )
            ), Toast.LENGTH_SHORT
        )
    }

    override fun onFavRemoveSuccess(data: ProductItemData) {
        presenter.getFeaturedProducts()
        Toast.makeText(
            context, getString(
                R.string.fave_remove_msg, LangUtils.getLanguageString(
                    data.productName, data.productNameAr
                )
            ), Toast.LENGTH_SHORT
        )
    }

    override fun onCartAddedSuccessfully(productItemData: ProductItemData) {
        showAddedToCartMessage(productItemData)
        carUpdateListener.onUpdateCartCount()
    }

    override fun onFeaturedLoading() {
        idealCategoryClickListener.onLoadingStarted()
    }

    override fun onFeaturedLoadingFinished() {
        idealCategoryClickListener.onLoadingFinished()
    }

    private fun showAddedToCartMessage(productItemData: ProductItemData) {

        var dialog = Dialog(activity!!, 0)

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
                activity!!, CartActivity::class.java,
                Bundle()
            )
        }

        dialog.show()
    }


    override fun onFavButtonClicked(itemAdapterPosition: Int, data: ProductItemData?) {
        favPosition = itemAdapterPosition
        if (LoggedUser.getInstance().isUserLoggedIn()) {
            if (data!!.favorite == 0) {
                presenter.addToFavourite(data)
            } else {
                presenter.removeFromFavourite(data)
            }
        } else {
            favProductItemData = data!!
            openLoginDialogue()
        }
    }

    private fun openLoginDialogue() {
        loginDialog = Dialog(activity!!, 0)
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
                context, SignInActivity::class.java,
                null, AppConstants.LOGIN_REQUEST_CODE
            )
        }
        loginDialog.btn_register.setOnClickListener {
            ActivitiesManager.startActivityForResult(
                context, RegisterActivity::class.java,
                null, AppConstants.REGISTER_REQUEST_CODE
            )
        }
        loginDialog.show()
    }


    override fun onItemRootViewClicked(
        itemAdapterPosition: Int, data: ProductItemData?
    ) {
        var bundle = Bundle()
        bundle.putSerializable(AppConstants.KEY_PRODUCT_DATA, data)
        ActivitiesManager.goTOAnotherActivityWithBundle(
            context, ProductDetailsActivity::class.java, bundle
        )
    }

    override fun onCartButtonClicked(data: ProductItemData?) {
        presenter.addToCart(data!!.productID, 0, 1, 0, data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (favProductItemData!!.favorite == 0) {
                presenter.addToFavourite(favProductItemData)
            } else {
                presenter.removeFromFavourite(favProductItemData)
            }
        }
    }


}