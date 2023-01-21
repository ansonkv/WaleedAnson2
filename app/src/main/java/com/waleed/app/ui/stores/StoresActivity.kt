package com.waleed.app.ui.stores

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.StoreData
import com.waleed.app.business.data.newdata.ProductStore
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.AppConstants
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.activity_reviews.*
import kotlinx.android.synthetic.main.toolbar_store.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class StoresActivity : BaseActivity(), StoresView {

    @Inject
    lateinit var presenter: StorePresenter
    lateinit var storesAdapter: StoreAdapter
    private var selectedStoreData: ProductStore? = null
    private lateinit var storeList: ArrayList<ProductStore>
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_store)
        storeList =
            intent.getSerializableExtra(AppConstants.KEY_STORE_LIST) as ArrayList<ProductStore>
        selectedPosition = intent.getIntExtra(AppConstants.KEY_SELECTED_STORE_POSITION, -1)
        initViews()
        if (selectedPosition!=-1){
            tv_done.makeVisible()
        }

        showStoreList(storeList)
    }

    private fun initViews() {
        tv_done.setOnClickListener {
            if (selectedStoreData != null) {
                var intent = Intent()
                intent.putExtra(AppConstants.KEY_SELECTED_STORE, selectedStoreData)
                intent.putExtra(AppConstants.KEY_SELECTED_STORE_POSITION,selectedPosition)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            onBackPressed()
        }

    }

    fun showStoreList(listData: ArrayList<ProductStore>) {
        rv_review_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        storesAdapter = StoreAdapter(listData, this, selectedPosition)
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
                        Locale.ENGLISH, "geo:%s,%s",
                        it.latitude, it.longitude
                    )
                Log.e("Uri", uri)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }
//            val uri = Uri.parse("smsto:" + it.phone)
//            val intent = Intent(Intent.ACTION_SENDTO, uri)
//            startActivity(intent)
        }
    }

    override fun showStoreList(listData: List<StoreData>) {

    }
}