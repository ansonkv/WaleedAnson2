package com.waleed.app.ui.home.landing

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.CategoryListResponse
import com.waleed.app.business.data.newdata.HomeSliderDataResponse
import com.waleed.app.business.data.newdata.IdealTypeListResponse
import com.waleed.app.business.data.newdata.SearchResponse
import com.waleed.app.ui.base.BaseFragment
import com.waleed.app.ui.home.FragmentClickListener
import com.waleed.app.ui.home.landing.ideal.HomeIdealFragment
import com.waleed.app.ui.home.landing.featured.HomeFeaturedFragment
import com.waleed.app.ui.productdetails.ProductDetailsActivity
import com.waleed.app.ui.productlist.ProductListActivity
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.AppConstants
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import javax.inject.Inject


class HomeFragment : BaseFragment(), HomeView {

    @Inject
    lateinit var presenter: HomePresenter
    private lateinit var fragmentClickListener: FragmentClickListener
    private var currentPage = 0
    private var pageNum = 0
    private val swipeTimer = Timer()

    private lateinit var idealCategoryList: ArrayList<IdealTypeListResponse.IdealTypeData>
    private lateinit var idealCategoryListAdapter: IdealCategoryListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Waleed.getAppComponent().inject(this)

        presenter.attachView(this)
        showContent()
        fragmentClickListener.selectBottomBar(0)

        presenter.getHomeSliderData()

        initViews()
    }

    private fun initViews() {

        val idealCategoryData = IdealTypeListResponse.IdealTypeData(
            idealID = R.drawable.ic_home_featured,
            idealName = context!!.getString(R.string.featured_string)
        )
        //Create list
        idealCategoryList = arrayListOf(idealCategoryData)
        //Add response data to list
        idealCategoryListAdapter = IdealCategoryListAdapter(idealCategoryList)
        rvIdealCategories.layoutManager = LinearLayoutManager(
            context, RecyclerView.HORIZONTAL, false
        )
        rvIdealCategories.adapter = idealCategoryListAdapter
        idealCategoryListAdapter.onItemClick =
            { idealTypeData: IdealTypeListResponse.IdealTypeData, position: Int ->
                if (position == 0) {
                    openFeaturedFragment()
                } else {
                    openBoysFragment(idealTypeData.idealID)
                }
            }
    }

    override fun getMainLayout(): Int {
        return R.layout.fragment_home
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentClickListener = context as FragmentClickListener
    }

    override fun loadOfferSliderData(offerListData: List<HomeSliderDataResponse.HomeSliderData>) {
        var homeSliderList = offerListData.filter { it.showHome == 1 }
        var sliderAdapter = HomeSliderAdapter(homeSliderList, context!!)
        view_pager_offer.adapter = sliderAdapter
        indicator.setViewPager(view_pager_offer)
        pageNum = homeSliderList.size
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
            if (currentPage == pageNum) {
                currentPage = 0
            }
            view_pager_offer.setCurrentItem(currentPage++, true)
        }

        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 3000, 3000)
    }

    override fun showIdealCategoryList(idealTypeList: ArrayList<IdealTypeListResponse.IdealTypeData>) {
        //Add response data to list
        idealCategoryList.addAll(idealTypeList)
        idealCategoryListAdapter.notifyDataSetChanged()
        openFeaturedFragment()
    }


    override fun onPause() {
        super.onPause()
        swipeTimer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        swipeTimer.cancel()
    }


    private fun openBoysFragment(idealID: Int) {
        val fragment2 = HomeIdealFragment.newInstance(idealID)
        val fragmentManager: FragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.cat_frag_container, fragment2!!, "tag")
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.no_change)

        fragmentTransaction.commit()
    }

    private fun openFeaturedFragment() {
        val fragment2 = HomeFeaturedFragment()
        val fragmentManager: FragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.cat_frag_container, fragment2, "tag")
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.no_change)

        fragmentTransaction.commit()
    }

 /*   override fun onLoadingStarted() {
        rvIdealCategories.isClickable = false
        progressBar.makeVisible()
    }

    override fun onLoadingFinished() {
        rvIdealCategories.isClickable = true
        progressBar.makeGone()
    }*/

}