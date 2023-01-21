package com.waleed.app.ui.home.category

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.CategoryListResponse
import com.waleed.app.business.data.newdata.HomeSliderDataResponse
import com.waleed.app.business.data.newdata.SearchResponse
import com.waleed.app.ui.base.BaseFragment
import com.waleed.app.ui.home.FragmentClickListener
import com.waleed.app.ui.home.landing.HomeSliderAdapter
import com.waleed.app.ui.productdetails.ProductDetailsActivity
import com.waleed.app.ui.productlist.ProductListActivity
import com.waleed.app.ui.subcategory.SubCategoryActivity
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.AppConstants
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_home.indicator
import kotlinx.android.synthetic.main.fragment_home.view_pager_offer
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CategoryFragment : BaseFragment(), CategoryView {

    @Inject
    lateinit var presenter: CategoryPresenter
    private lateinit var fragmentClickListener: FragmentClickListener
    private var currentPage = 0
    private var NUM_PAGES = 0
    val swipeTimer = Timer()

    lateinit var categoryAdapter: CategoryListAdapter

    private lateinit var categoryList: ArrayList<CategoryListResponse.CategoryListData>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        showContent()
        fragmentClickListener.selectBottomBar(1)

        initViews()
    }

    private fun initViews() {
        presenter.getHomeSliderData()
        presenter.getCategoryData()

        categoryList = arrayListOf()
        categoryAdapter = CategoryListAdapter(categoryList, context!!)
        rv_product_category.layoutManager = GridLayoutManager(context, 2)
        rv_product_category.adapter = categoryAdapter
        categoryAdapter.onItemClick = {
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.KEY_CATEGORY_DATA, it)
            if (it.hasSubCategory == 1) {
                ActivitiesManager.goTOAnotherActivityWithBundle(
                    context,
                    SubCategoryActivity::class.java,
                    bundle
                )
            } else {
                bundle.putBoolean(AppConstants.KEY_IS_FROM_SUBCATEGORY, false)
                ActivitiesManager.goTOAnotherActivityWithBundle(
                    context, ProductListActivity::class.java, bundle
                )
            }
        }
    }

    override fun getMainLayout(): Int {
        return R.layout.fragment_category
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentClickListener = context as FragmentClickListener
    }

    override fun loadOfferSliderData(offerListData: List<HomeSliderDataResponse.HomeSliderData>) {
        var categorySliderList = offerListData.filter { it.showCategory == 1 }
       var sliderAdapter=HomeSliderAdapter(categorySliderList, context!!)
        view_pager_offer.adapter = sliderAdapter
        indicator.setViewPager(view_pager_offer)
        NUM_PAGES = categorySliderList.size
        sliderAdapter.onItemClick = {
            if (it.productID != 0) {
                var searchData = SearchResponse.SearchItem("", it.productID)
                var bundle = Bundle()
                bundle.putBoolean(AppConstants.BUNDLE_SEARCH_VALUE, true)
                bundle.putSerializable(AppConstants.KEY_PRODUCT_DATA, searchData)
                ActivitiesManager.goTOAnotherActivityWithBundle(
                    context, ProductDetailsActivity::class.java, bundle
                )
            } else {
                var categoryData = CategoryListResponse.CategoryListData(
                    categoryID = it.categoryID,
                    categoryNameAr = it.categoryNameAr,
                    categoryName = it.categoryName,
                    image = "",
                    hasSubCategory = 0,
                    show = 0
                )
                var bundle = Bundle()
                bundle.putSerializable(AppConstants.KEY_CATEGORY_DATA, categoryData)
                bundle.putBoolean(AppConstants.KEY_IS_FROM_SUBCATEGORY, false)
                ActivitiesManager.goTOAnotherActivityWithBundle(
                    context, ProductListActivity::class.java, bundle
                )
            }
        }
        view_pager_offer.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                currentPage = position
            }
        })
        val handler = Handler()
        val update = Runnable {
            if (currentPage === NUM_PAGES) {
                currentPage = 0
            }
            view_pager_offer.setCurrentItem(currentPage++, true)
        }

        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 5000, 5000)
    }

    override fun showCategoryList(listData: List<CategoryListResponse.CategoryListData>) {
        categoryList.addAll(listData)
        categoryAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        swipeTimer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        swipeTimer.cancel()
    }
}