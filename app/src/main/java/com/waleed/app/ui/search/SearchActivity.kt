package com.waleed.app.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.SearchResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.productdetails.ProductDetailsActivity
import com.waleed.app.util.*
import kotlinx.android.synthetic.main.activity_serach_view.*
import kotlinx.android.synthetic.main.toolbar_search.*
import java.util.ArrayList
import javax.inject.Inject

class SearchActivity : BaseActivity(), SearchView {
    @Inject
    lateinit var presenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        setContentView(R.layout.activity_serach_view)
        btn_close.setOnClickListener {
            onBackPressed()
        }
        et_search.requestFocus()
        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (et_search.text.toString().isNotEmpty()) {
                    progress_search.makeVisible()
                    presenter.searchItem(s.toString())
                } else {
                    progress_search.makeInvisible()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    override fun noNetworkForSearch() {
        progress_search.makeInvisible()
    }

    override fun showEmptySearch() {
        progress_search.makeInvisible()
        rv_search_list.makeGone()
        tvEmptyList.makeVisible()
    }

    override fun showSearchResult(searchItemList: ArrayList<SearchResponse.SearchItem>) {
        progress_search.makeInvisible()
        tvEmptyList.makeGone()
        rv_search_list.makeVisible()
        rv_search_list.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        var searchAdapter = SearchAdapter(searchItemList)

        searchAdapter.onItemClick = {
            Log.e("dac", it.item)
            var bundle = Bundle()
            bundle.putBoolean(AppConstants.BUNDLE_SEARCH_VALUE, true)
            bundle.putSerializable(AppConstants.KEY_PRODUCT_DATA, it)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                this, ProductDetailsActivity::class.java, bundle
            )
        }
        rv_search_list.adapter = searchAdapter
    }
}