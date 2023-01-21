package com.waleed.app.ui.productlist

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.*
import com.waleed.app.ui.account.register.RegisterActivity
import com.waleed.app.ui.account.signin.SignInActivity
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.cart.CartActivity
import com.waleed.app.ui.filter.SearchFilterActivity
import com.waleed.app.ui.productdetails.ProductDetailsActivity
import com.waleed.app.util.*
import com.waleed.app.util.AppConstants.BUNDLE_ADDRESS_LIST
import com.waleed.app.util.AppConstants.FILTER_ADDRESS_EMPTY
import com.waleed.app.util.AppConstants.KEY_CATEGORY_ID
import com.waleed.app.util.AppConstants.LOGIN_REQUEST_CODE
import com.waleed.app.util.AppConstants.REGISTER_REQUEST_CODE
import com.waleed.app.util.AppConstants.SEARCH_CODE
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.dialogue_cart_message.*
import kotlinx.android.synthetic.main.dialogue_checkout_message.btn_login
import kotlinx.android.synthetic.main.dialogue_checkout_message.btn_register
import kotlinx.android.synthetic.main.dialogue_checkout_message.img_close_dialogue
import kotlinx.android.synthetic.main.dialogue_checkout_message.tv_cart_message
import kotlinx.android.synthetic.main.dialogue_checkout_message.tv_cart_pop_product_name
import kotlinx.android.synthetic.main.toolbar_common.*
import javax.inject.Inject
import kotlin.properties.Delegates

class ProductListActivity : BaseActivity(), ProductListView {

    @Inject
    lateinit var presenter: ProductListPresenter

    private var categoryId by Delegates.notNull<Int>()

    private lateinit var subCategoryData: SubCategoryListResponse.SubCategoryData

    private lateinit var categoryData: CategoryListResponse.CategoryListData

    private lateinit var productList: ArrayList<ProductItemData>

    private lateinit var productListAdapter: ProductListAdapter

    private lateinit var favProductItemData: ProductItemData

    private var favPosition = 0

    private lateinit var loginDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_product_list)

        initViews()

    }

    private fun initViews() {
        btn_close.setOnClickListener { onBackPressed() }
        if (intent.getBooleanExtra(AppConstants.KEY_IS_FROM_SUBCATEGORY, false)) {
            categoryId = intent.getIntExtra(AppConstants.KEY_CATEGORY_ID, 0)
            subCategoryData =
                intent.getSerializableExtra(AppConstants.KEY_SUB_CATEGORY_DATA) as
                        SubCategoryListResponse.SubCategoryData
            toolbar_page_title_tv.setLanguageString(
                subCategoryData.subCategoryName, subCategoryData.subCategoryNameAr
            )
            Handler().postDelayed({
                presenter.getSubCategoryProductList(
                    categoryId, subCategoryData.subCategoryID
                )
            }, 800)

        } else {
            categoryData =
                intent.getSerializableExtra(AppConstants.KEY_CATEGORY_DATA)
                        as CategoryListResponse.CategoryListData
            categoryId = categoryData.categoryID
            toolbar_page_title_tv.setLanguageString(
                categoryData.categoryName, categoryData.categoryNameAr
            )
            Handler().postDelayed({
                presenter.getCategoryProductList(categoryData.categoryID)
            }, 800)

        }
        btn_cart.setOnClickListener {
            var bundle = Bundle()
            bundle.putInt(KEY_CATEGORY_ID, categoryId)
            ActivitiesManager.startActivityForResult(
                this, SearchFilterActivity::class.java,
                bundle, SEARCH_CODE
            )
        }

        productList = arrayListOf()
        productListAdapter = ProductListAdapter(productList)

        rv_product_list.layoutManager = GridLayoutManager(this, 2)
        rv_product_list.adapter = productListAdapter

        productListAdapter.onItemClick = {
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.KEY_PRODUCT_DATA, it)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                this, ProductDetailsActivity::class.java, bundle
            )
        }
        productListAdapter.onFavItemClick = { productItemData: ProductItemData, position: Int ->
            if (LoggedUser.getInstance().isUserLoggedIn()) {
                if (productItemData.favorite == 0) {
                    presenter.addToFavourite(productItemData, position)
                } else {
                    presenter.removeFromFavourite(productItemData, position)
                }
            } else {
                favProductItemData = productItemData!!
                favPosition = position

                openLoginDialogue()
            }
        }
        productListAdapter.onCartClick = {
            presenter.addToCart(it!!.productID, 0, 1, 0, it)
        }
    }

    override fun showProductList(listDataResponse: ArrayList<ProductItemData>) {
        tvEmptyList.makeGone()
        rv_product_list.makeVisible()
        productList.addAll(listDataResponse)
        productListAdapter.notifyDataSetChanged()
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

    override fun showEmptyList() {
        tvEmptyList.makeVisible()
        rv_product_list.makeGone()
    }

    override fun onFavAddedSuccess(data: ProductItemData, position: Int) {
        data.favorite = 1
        productListAdapter.updateItem(data, position)
        Toast.makeText(
            this, getString(
                R.string.fav_added_msg, LangUtils.getLanguageString(
                    data.productName, data.productNameAr
                )
            ), Toast.LENGTH_SHORT
        )
    }

    override fun onFavRemoveSuccess(data: ProductItemData, position: Int) {
        data.favorite = 0
        productListAdapter.updateItem(data, position)
        Toast.makeText(
            this, getString(
                R.string.fave_remove_msg, LangUtils.getLanguageString(
                    data.productName, data.productNameAr
                )
            ), Toast.LENGTH_SHORT
        )
    }

    override fun onCartAddedSuccessfully(productItemData: ProductItemData) {
        showAddedToCartMessage(productItemData)
    }

    override fun onCartItemAddSuccess(cartCount: Int) {
        Waleed.appContext.getAppPref().saveInteger(AppConstants.PREF_KEY_CART_COUNT, cartCount)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SEARCH_CODE -> {
                    if (!data!!.getBooleanExtra(FILTER_ADDRESS_EMPTY, false)) {
                        showEmptyList()
                    } else {
                        productList.clear()
                        var list =
                            data.getSerializableExtra(BUNDLE_ADDRESS_LIST) as ArrayList<ProductItemData>
                        showProductList(list)
                    }
                }
                LOGIN_REQUEST_CODE -> {
                    if (favProductItemData!!.favorite == 0) {
                        presenter.addToFavourite(favProductItemData, favPosition)
                    } else {
                        presenter.removeFromFavourite(favProductItemData, favPosition)
                    }
                }
                REGISTER_REQUEST_CODE -> {
                    if (favProductItemData!!.favorite == 0) {
                        presenter.addToFavourite(favProductItemData, favPosition)
                    } else {
                        presenter.removeFromFavourite(favProductItemData, favPosition)
                    }
                }
            }
        }
    }
}