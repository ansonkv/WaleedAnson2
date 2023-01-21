package com.waleed.app.ui.wrap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.CartDataForWrap
import com.waleed.app.business.data.newdata.CartListDataResponse
import com.waleed.app.business.data.newdata.WrapperResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.wrap.adapters.ProductListAdapter
import com.waleed.app.ui.wrap.adapters.WrapperPaperAdapter
import com.waleed.app.ui.wrap.adapters.WrapperTAgsAdapter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.AppConstants.BUNDLE_CART
import kotlinx.android.synthetic.main.activity_wrap.*
import kotlinx.android.synthetic.main.toolbar_wrap.*
import javax.inject.Inject

class WrapActivity : BaseActivity(), WrapView {

    @Inject
    lateinit var presenter: WrapPresenter

    private lateinit var productListAdapter: ProductListAdapter
    private var cartDataForWrapList = ArrayList<CartDataForWrap>()
    private lateinit var wrapperTagAdapter: WrapperTAgsAdapter
    private lateinit var wrapperPaperAdapter: WrapperPaperAdapter
    private var selectedWrapUrl: String = ""
    private var selectedCartItemList = arrayListOf<CartListDataResponse.CartItem>()
    private var selectedTagId = 0
    private var selectedPaperId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)


        setContentView(R.layout.activity_wrap)
        selectedCartItemList = intent.getSerializableExtra(BUNDLE_CART)
                as ArrayList<CartListDataResponse.CartItem>
        getData()
        presenter.getWrapperPaperData()
//        presenter.getWrapperData()
        btn_close.setOnClickListener { onBackPressed() }
        btn_wrap_submit.setOnClickListener {
            var selectedMessage = if (et_wrap_message.text.toString().isNullOrEmpty()) {
                ""
            } else {
                et_wrap_message.text.toString()
            }
            var selectedCartId = arrayListOf<Int>()
            for (cart in selectedCartItemList) {
                selectedCartId.add(cart.cartID)
            }
            var selectedCartIdString = selectedCartId.toString()
                .substring(1, selectedCartId.toString().length - 1)

            presenter.submitWrapper(
                selectedCartIdString, selectedTagId, selectedPaperId, selectedMessage
            )
//
        }
    }

    private fun getData() {
        cartDataForWrapList =
            intent.getSerializableExtra(BUNDLE_CART) as java.util.ArrayList<CartDataForWrap>
        showProductList()
    }

    private fun showProductList() {
        rv_wrap_products.layoutManager = LinearLayoutManager(
            this, RecyclerView.HORIZONTAL, false
        )
        productListAdapter = ProductListAdapter(selectedCartItemList, this)
        rv_wrap_products.adapter = productListAdapter
        productListAdapter.onDeleteClick = {

            if (selectedCartItemList.isEmpty()) {
                finish()
            }else{
                selectedCartItemList.removeAt(it)
            }
        }
    }


    override fun showWrapperData(response: WrapperResponse) {
        rv_wrap_tag.layoutManager = LinearLayoutManager(
            this, RecyclerView.HORIZONTAL, false
        )
        wrapperTagAdapter = WrapperTAgsAdapter(response.tags, this)
        rv_wrap_tag.adapter = wrapperTagAdapter
        selectedTagId = response.tags[0].tagID
        wrapperTagAdapter.onItemClick = {
            selectedTagId = it.tagID
        }

        rv_wrap_choice.layoutManager = LinearLayoutManager(
            this, RecyclerView.HORIZONTAL, false
        )
        wrapperPaperAdapter = WrapperPaperAdapter(response.papers, this)
        selectedPaperId = response.papers[0].paperID
        selectedWrapUrl = response.papers[0].paperImage
        rv_wrap_choice.adapter = wrapperPaperAdapter
        wrapperPaperAdapter.onItemClick = {
            selectedPaperId = it.paperID
            selectedWrapUrl = it.paperImage
        }
    }

    override fun onSubmitSuccess() {
        var intent = Intent()
        intent.putParcelableArrayListExtra(
            AppConstants.BUNDLE_CART_DATA,
            cartDataForWrapList as java.util.ArrayList<out Parcelable>
        )
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}