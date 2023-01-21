package com.waleed.app.ui.home.landing.ideal

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.AgeGroupListResponse
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.ui.account.register.RegisterActivity
import com.waleed.app.ui.account.signin.SignInActivity
import com.waleed.app.ui.base.BaseFragment
import com.waleed.app.ui.cart.CartActivity
import com.waleed.app.ui.home.CartUpdateListener
import com.waleed.app.ui.home.landing.IdealCategoryClickListener
import com.waleed.app.ui.productdetails.ProductDetailsActivity
import com.waleed.app.util.*
import kotlinx.android.synthetic.main.dialogue_cart_message.*
import kotlinx.android.synthetic.main.dialogue_checkout_message.*
import kotlinx.android.synthetic.main.dialogue_checkout_message.btn_login
import kotlinx.android.synthetic.main.dialogue_checkout_message.btn_register
import kotlinx.android.synthetic.main.dialogue_checkout_message.img_close_dialogue
import kotlinx.android.synthetic.main.dialogue_checkout_message.tv_cart_message
import kotlinx.android.synthetic.main.dialogue_checkout_message.tv_cart_pop_product_name
import kotlinx.android.synthetic.main.fragment_home_boys.*
import kotlinx.android.synthetic.main.fragment_home_boys.rvAgeGroupProductList
import javax.inject.Inject
import kotlin.properties.Delegates

class HomeIdealFragment : BaseFragment(), HomeIdealView {

    @Inject
    lateinit var presenter: HomeIdealPresenter

    var idealID by Delegates.notNull<Int>()

    private lateinit var ageGroupList: ArrayList<AgeGroupListResponse.AgeGroupData>
    private lateinit var ageGroupAdapter: IdealAgeGroupAdapter
    private lateinit var productItem: ArrayList<ProductItemData>
    private lateinit var productListAdapter: IdealListProductAdapter
    private lateinit var carUpdateListener: CartUpdateListener
    private lateinit var idealCategoryClickListener: IdealCategoryClickListener
    private lateinit var loginDialog: Dialog
    private lateinit var favProductItemData: ProductItemData
    private var favProductPosition = -1

    companion object {
        fun newInstance(idealID: Int): HomeIdealFragment? {
            val f = HomeIdealFragment()
            val args = Bundle()
            args.putInt(AppConstants.BUNDLE_KEY_IDEAL_ID, idealID)
            f.arguments = args
            return f
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        carUpdateListener = context as CartUpdateListener
        idealCategoryClickListener = context as IdealCategoryClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idealID = arguments!!.getInt(AppConstants.BUNDLE_KEY_IDEAL_ID)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        showContent()
        initViews()

        presenter.getAgeGroupList(idealID)

    }

    private fun initViews() {

        var ageGroupData = AgeGroupListResponse.AgeGroupData(
            ageGroupID = R.drawable.ic_boys_all,
            ageGroupName = context!!.getString(R.string.all_string)
        )

        ageGroupList = arrayListOf(ageGroupData)
        ageGroupAdapter = IdealAgeGroupAdapter(ageGroupList)

        rvAgeGroupList.layoutManager = LinearLayoutManager(
            activity, RecyclerView.HORIZONTAL, false
        )

        //Age Group List
        rvAgeGroupList.adapter = ageGroupAdapter
        ageGroupAdapter.onItemClick =
            { groupData: AgeGroupListResponse.AgeGroupData, position: Int ->
                if (position == 0) {
                    presenter.getAllProductList(idealID)
                } else {
                    presenter.getAgeGroupData(groupData.ageGroupID)
                }
            }

        //ProductList
        productItem = arrayListOf<ProductItemData>()
        productListAdapter = IdealListProductAdapter(
            listData = productItem, context = context!!
        )
        rvAgeGroupProductList.layoutManager = GridLayoutManager(context, 2)
        rvAgeGroupProductList.adapter = productListAdapter

        productListAdapter.onItemClick = {
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.KEY_PRODUCT_DATA, it)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context,
                ProductDetailsActivity::class.java, bundle
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
                favProductPosition = position
                openLoginDialogue()
            }
        }
        productListAdapter.onCartClicked = {
            presenter.addToCart(it!!.productID, 0, 1, 0, it)

        }
        presenter.getAllProductList(idealID)
    }

    override fun getMainLayout(): Int {
        return R.layout.fragment_home_boys
    }

    override fun viewAgeGroupList(ageGroupResponseList: List<AgeGroupListResponse.AgeGroupData>) {
        ageGroupList.addAll(ageGroupResponseList)
        ageGroupAdapter.notifyDataSetChanged()
    }


    override fun showIdealProductList(productItemResponse: java.util.ArrayList<ProductItemData>) {
//        progressBar.makeGone()
        rvAgeGroupProductList.makeVisible()
        tvEmptyList.makeGone()
        productItem.clear()
        productItem.addAll(productItemResponse)
        productListAdapter.notifyDataSetChanged()
    }

    override fun showEmptyList() {
        progressBar.makeGone()
        rvAgeGroupProductList.makeGone()
        tvEmptyList.makeVisible()
    }

    override fun onFavAddedSuccess(data: ProductItemData, position: Int) {
        data.favorite = 1
        productListAdapter.updateItem(data, position)
        Toast.makeText(
            context, getString(
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
            context, getString(
                R.string.fave_remove_msg, LangUtils.getLanguageString(
                    data.productName, data.productNameAr
                )
            ), Toast.LENGTH_SHORT
        )
    }

    override fun onCartAddedSuccessfully(productItem: ProductItemData) {
        showAddedToCartMessage(productItem)
        carUpdateListener.onUpdateCartCount()
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

    override fun onProductLoadingFinished() {
        idealCategoryClickListener.onLoadingFinished()
    }

    override fun onProductStartLoading() {
        idealCategoryClickListener.onLoadingStarted()
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

    override fun showLoaderInList() {
        progressBar.makeVisible()
        rvAgeGroupProductList.makeGone()
        tvEmptyList.makeGone()
    }

    override fun hideLoaderInList() {
        progressBar.makeGone()
        rvAgeGroupProductList.makeGone()
        tvEmptyList.makeGone()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (favProductItemData!!.favorite == 0) {
                presenter.addToFavourite(favProductItemData, favProductPosition)
            } else {
                presenter.removeFromFavourite(favProductItemData, favProductPosition)
            }
        }
    }
}