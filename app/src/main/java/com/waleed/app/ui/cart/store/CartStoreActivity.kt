package com.waleed.app.ui.cart.store

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.CartListDataResponse
import com.waleed.app.business.data.newdata.ProductStore
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.AppConstants
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.activity_reviews.*
import kotlinx.android.synthetic.main.toolbar_store.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CartStoreActivity : BaseActivity(), CartStoresView {

    @Inject
    lateinit var presenter: CartStorePresenter
    lateinit var storesAdapter: CartStoreAdapter
    private var selectedStoreData: ProductStore? = null
    private lateinit var cartItem: CartListDataResponse.CartItem
    private var selectedPosition = -1
    private var selectedStoreId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_store)
        cartItem =
            intent.getSerializableExtra(AppConstants.BUNDLE_KEY_SELECTED_STORE_DATA) as CartListDataResponse.CartItem
        initViews()
        selectedStoreId = cartItem.storeID
        presenter.getStoreData(cartItem.cartID)

    }

    private fun initViews() {
        tv_done.setOnClickListener {
            presenter.updateStoreCart(
                cartID = cartItem.cartID,
                storeID = selectedStoreData!!.storeID
            )
        }
    }


    override fun showCartStoreList(productStores: ArrayList<ProductStore>) {
        rv_review_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        storesAdapter = CartStoreAdapter(productStores, this, selectedStoreId)
        rv_review_list.adapter = storesAdapter
        storesAdapter.onItemClick = { productStore: ProductStore, position: Int ->
            tv_done.makeVisible()
            selectedStoreData = productStore
            selectedPosition = position
        }
        storesAdapter.onCallButtonClick = {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + it.phone)
            startActivity(intent)
        }
        storesAdapter.onLocationButtonClick = {
            Log.e("Uri", it.latitude + it.longitude)
            if (!(it.latitude.isNullOrEmpty() && it.longitude.isNullOrEmpty())) {
                val uri: String =
                    java.lang.String.format(
                        Locale.ENGLISH, "geo:%f,%f",
                        it.latitude, it.longitude
                    )
                Log.e("Uri", uri)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }
        }
    }

    override fun showEmptyList() {

    }

    override fun onStoreUpdatedSuccessfully() {
        if (selectedStoreData != null) {
            var intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}