package com.waleed.app.ui.subcategory

import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.CategoryListResponse
import com.waleed.app.business.data.newdata.SubCategoryListResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.filter.SearchFilterActivity
import com.waleed.app.ui.productlist.ProductListActivity
import com.waleed.app.util.*
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.toolbar_common.*
import java.util.ArrayList
import javax.inject.Inject

class SubCategoryActivity : BaseActivity(), SubCategoryView {

    @Inject
    lateinit var presenter: SubCategoryPresenter
    private lateinit var categoryData: CategoryListResponse.CategoryListData

    private lateinit var subCategoryAdapter: SubCategoryListAdapter
    private lateinit var subCategoryList: ArrayList<SubCategoryListResponse.SubCategoryData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        categoryData =
            intent.getSerializableExtra(AppConstants.KEY_CATEGORY_DATA) as CategoryListResponse.CategoryListData
        initViews()
    }

    private fun initViews() {
        btn_close.setOnClickListener { onBackPressed() }

        toolbar_page_title_tv.setLanguageString(
            categoryData.categoryName,
            categoryData.categoryNameAr
        )
        btn_cart.makeGone()
        showLoading()
        Handler().postDelayed({
            presenter.getSubCategoryList(categoryId = categoryData.categoryID)
        }, 800)

        btn_cart.setOnClickListener {
            var bundle = Bundle()
            ActivitiesManager.startActivityForResult(
                this, SearchFilterActivity::class.java,
                bundle, AppConstants.SEARCH_CODE
            )
        }
        subCategoryList = arrayListOf()
        subCategoryAdapter = SubCategoryListAdapter(subCategoryList)
        rv_sub_category_list.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_sub_category_list.adapter = subCategoryAdapter
        subCategoryAdapter.onItemClick = {
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.KEY_SUB_CATEGORY_DATA, it)
            bundle.putBoolean(AppConstants.KEY_IS_FROM_SUBCATEGORY, true)
            bundle.putInt(AppConstants.KEY_CATEGORY_ID, categoryData.categoryID)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                this, ProductListActivity::class.java, bundle
            )
        }
    }

    override fun showSubCategoryList(subCategoryListResponse: ArrayList<SubCategoryListResponse.SubCategoryData>) {
        rv_sub_category_list.makeVisible()
        tvEmptyList.makeGone()

        subCategoryList.addAll(subCategoryListResponse)
        subCategoryAdapter.notifyDataSetChanged()


    }

    override fun showEmptyList() {
        tvEmptyList.makeVisible()
        rv_sub_category_list.makeGone()
    }
}