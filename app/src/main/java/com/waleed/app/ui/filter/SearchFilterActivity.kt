package com.waleed.app.ui.filter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.SearchFilterCategory
import com.waleed.app.business.data.newdata.*
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.filter.adapter.*
import com.waleed.app.util.AppConstants
import com.waleed.app.util.AppConstants.BUNDLE_ADDRESS_LIST
import com.waleed.app.util.AppConstants.FILTER_ADDRESS_EMPTY
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.activity_search_filter.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.toolbar_product_details.*
import java.util.*
import java.util.stream.Collector
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.collections.ArrayList
import kotlin.contracts.ReturnsNotNull

class SearchFilterActivity : BaseActivity(), SearchFilterView {

    @Inject
    lateinit var presenter: SearchFilterPresenter
    private lateinit var filterCategoryAdapter: FilterCategoryAdapter

    private var selectedCategoryId = 0

    private var selectedSortValue = ""
    private var selectedSortPosition = -1

    private var selectedMinPrice = 0
    private var selectedMaxPrice = 1000

    private var selectedRatingValue = 0
    private var selectedRatingsValuePosition = -1

    private var selectedAvailabilityValue = 1
    private var selectedAvailabilityValuePosition = -1

    private var selectedBrandPositions = mutableListOf<Int>()
    private var selectedBrandValues = mutableListOf<Int>()

    private var selectedAgePosition = mutableListOf<Int>()
    private var selectedAgeValues = mutableListOf<Int>()

    private var selectedShopForPositions = mutableListOf<Int>()
    private var selectedShopForValues = mutableListOf<Int>()

    private var searchCategoryList = arrayListOf<SearchFilterCategoriesData>()
    private var sortValueList = arrayListOf<FilterSortValues>()
    private var ratingValueList = arrayListOf<FilterRatingValues>()
    private var availabilityValueList = arrayListOf<FilterRatingValues>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_search_filter)

        initView()
//        presenter.getFilterCategories()
        showLoading()
        Handler().postDelayed({
            presenter.getFilterInfo()
        }, 900)

    }

    private fun initView() {
        toolbar_page_title_tv.text = getText(R.string.filter_by_string)
        selectedCategoryId = intent.getIntExtra(
            AppConstants.KEY_CATEGORY_ID, 0
        )
        btn_cart.makeGone()
        btn_close.setOnClickListener { onBackPressed() }
        btn_apply.setOnClickListener {
            var brandValues = if (selectedBrandValues.isEmpty()) {
                " "
            } else {
                selectedBrandValues.toString()
                    .substring(1, selectedBrandValues.toString().length - 1)
            }
            var ageValues = if (selectedAgeValues.isEmpty()) {
                " "
            } else {
                selectedAgeValues.toString()
                    .substring(1, selectedAgeValues.toString().length - 1)
            }
            var shopForValues = if (selectedShopForValues.isEmpty()) {
                " "
            } else {
                selectedShopForValues.toString()
                    .substring(1, selectedShopForValues.toString().length - 1)
            }
            Log.e("selectedCategoryId", selectedCategoryId.toString())
            Log.e("selectedRatingValue", selectedRatingValue.toString())
            Log.e("selectedMinPrice", selectedMinPrice.toString())
            Log.e("selectedMaxPrice", selectedMaxPrice.toString())
            Log.e("selectedSortValue", selectedSortValue)
            Log.e("selectedAvailability", selectedAvailabilityValue.toString())
            Log.e("selectedBrandValues", brandValues)
            Log.e("selectedAgeValues", ageValues)
            Log.e("selectedShopForValues", shopForValues)
            presenter.filterItem(
                categoryID = selectedCategoryId, rating = selectedRatingValue,
                maxPrice = selectedMaxPrice, minPrice = selectedMinPrice,
                idealIDList = shopForValues, brandIDList = brandValues,
                ageGroupIDList = ageValues, availability = selectedAvailabilityValue,
                sortBy = selectedSortValue
            )
        }

        //SearchFilter Categories Values

        var data1 = SearchFilterCategoriesData(getString(R.string.price_string), 0, false)
        var data2 = SearchFilterCategoriesData(getString(R.string.sort_by_string), 0, false)
        var data3 = SearchFilterCategoriesData(getString(R.string.brand_string), 0, true)
        var data4 = SearchFilterCategoriesData(getString(R.string.age_group_string), 0, true)
        var data5 = SearchFilterCategoriesData(getString(R.string.shop_for_string), 0, true)
        var data6 = SearchFilterCategoriesData(getString(R.string.ratings_string), 0, false)
        var data7 = SearchFilterCategoriesData(getString(R.string.availability_string), 0, false)

        searchCategoryList.addAll(arrayListOf(data5, data1, data2, data3, data4, data6, data7))


        //SortValues
        var sortData1 = FilterSortValues(getString(R.string.relevence_string), "r")
        var sortData2 = FilterSortValues(getString(R.string.high_to_low_string), "d")
        var sortData3 = FilterSortValues(getString(R.string.low_to_high_string), "a")
        sortValueList.addAll(arrayListOf(sortData1, sortData2, sortData3))
        //RatingData
        var ratingData1 = FilterRatingValues(getString(R.string.five_stars), 5)
        var ratingData2 = FilterRatingValues(getString(R.string.four_stars), 4)
        var ratingData3 = FilterRatingValues(getString(R.string.three_stars), 3)
        var ratingData4 = FilterRatingValues(getString(R.string.two_stars), 2)
        var ratingData5 = FilterRatingValues(getString(R.string.one_stars), 1)
        ratingValueList.addAll(
            arrayListOf(
                ratingData1, ratingData2, ratingData3,
                ratingData4, ratingData5
            )
        )
        //Availability
        var availability1 = FilterRatingValues(getString(R.string.show_in_stock_string), 1)
        var availability2 = FilterRatingValues(getString(R.string.show_all_string), 2)
        availabilityValueList.addAll(arrayListOf(availability1, availability2))
    }

    override fun showSearchCategory(listData: List<SearchFilterCategory>) {

    }

    override fun showError(message: String) {

    }

    override fun setUpFilterValues(response: FilterResponse) {
        var filteredAgeGroupList: ArrayList<FilterResponse.AgeGroupValue> = arrayListOf()
        filteredAgeGroupList.addAll(response.ageGroupValueList)
        rv_category.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        filterCategoryAdapter = FilterCategoryAdapter(searchCategoryList, this)
        rv_category.adapter = filterCategoryAdapter
        filterCategoryAdapter.onItemClick =
            { position: Int, searchFilterCategoriesData: SearchFilterCategoriesData ->
                var selectedAgeGroupList = arrayListOf<FilterResponse.AgeGroupValue>()

                if (position == 1) {
                    rl_price.makeVisible()
                    rv_category_values.makeGone()
                } else {
                    rl_price.makeGone()
                    rv_category_values.makeVisible()

                    when (position) {
                        2 -> {
                            var sortByAdapter = FilterSortByAdapter(sortValueList, this)
                            sortByAdapter.onSingleValueItemClick =
                                { isSelected: Boolean, selectedPosition: Int, selectedValue: String ->
                                    selectedSortValue = selectedValue
                                    selectedSortPosition = selectedPosition
                                    if (isSelected) {
                                        searchCategoryList[1].count = 1
                                        filterCategoryAdapter.notifyItemChanged(1)
                                    } else {
                                        searchCategoryList[1].count = 0
                                        filterCategoryAdapter.notifyItemChanged(1)
                                    }
                                }
                            sortByAdapter.setSelectedPosition(selectedSortPosition)
                            rv_category_values.layoutManager = LinearLayoutManager(
                                this, RecyclerView.VERTICAL, false
                            )
                            rv_category_values.adapter = sortByAdapter
                        }

                        3 -> {
                            var brandAdapter = FilterBrandAdapter(response.brandValueList, this)
                            brandAdapter.onMultiChooserClick =
                                { selectedPositions: MutableList<Int>, values: MutableList<Int> ->
                                    selectedBrandPositions = selectedPositions
                                    selectedBrandValues = values
                                    searchCategoryList[2].count = selectedPositions.size
                                    filterCategoryAdapter.notifyItemChanged(2)
                                }
                            brandAdapter.setSelectedPositionList(
                                selectedBrandPositions, selectedBrandValues
                            )
                            rv_category_values.layoutManager = LinearLayoutManager(
                                this, RecyclerView.VERTICAL, false
                            )
                            rv_category_values.adapter = brandAdapter
                        }

                        4 -> {
                            var ageGroupAdapter =
                                FilterAgeGroupAdapter(filteredAgeGroupList, this)
                            ageGroupAdapter.onMultiChooserClick =
                                { selectedPositions: MutableList<Int>, values: MutableList<Int> ->
                                    selectedAgePosition = selectedPositions
                                    selectedAgeValues = values
                                    searchCategoryList[3].count = selectedPositions.size
                                    filterCategoryAdapter.notifyItemChanged(3)
                                }
                            ageGroupAdapter.setSelectedPositionList(
                                selectedAgePosition, selectedAgeValues
                            )
                            rv_category_values.layoutManager = LinearLayoutManager(
                                this, RecyclerView.VERTICAL, false
                            )
                            rv_category_values.adapter = ageGroupAdapter
                        }
                        0 -> {
                            var idealGroupAdapter =
                                FilterIdealGroupAdapter(response.idealList, this)
                            idealGroupAdapter.onMultiChooserClick =
                                { selectedPositions: MutableList<Int>, values: MutableList<Int> ->
                                    selectedShopForPositions = selectedPositions
                                    selectedShopForValues = values
                                    searchCategoryList[0].count = selectedPositions.size
                                    filterCategoryAdapter.notifyItemChanged(0)
                                }
                            idealGroupAdapter.setSelectedPositionList(
                                selectedShopForPositions, selectedShopForValues
                            )
//
                            if (selectedShopForValues.isNullOrEmpty()) {
                                filteredAgeGroupList.addAll(response.ageGroupValueList)
                            } else {
                                filteredAgeGroupList.clear()
                                for (int in selectedShopForValues) {
                                    for (ageGroupItem in response.ageGroupValueList) {
                                        if (ageGroupItem.idealId == int) {
                                            filteredAgeGroupList.add(ageGroupItem)
                                        }
                                    }
                                }
                            }
                            rv_category_values.layoutManager = LinearLayoutManager(
                                this, RecyclerView.VERTICAL, false
                            )
                            rv_category_values.adapter = idealGroupAdapter
                        }
                        5 -> {
                            var ratingAdapter = FilterRatingAdapter(ratingValueList, this)
                            ratingAdapter.onSingleValueItemClick =
                                { isSelected: Boolean, selectedPosition: Int, selectedValue: Int ->
                                    selectedRatingValue = selectedValue
                                    selectedRatingsValuePosition = selectedPosition
                                    if (isSelected) {
                                        searchCategoryList[5].count = 1
                                        filterCategoryAdapter.notifyItemChanged(5)
                                    } else {
                                        searchCategoryList[5].count = 0
                                        filterCategoryAdapter.notifyItemChanged(5)
                                    }
                                }
                            ratingAdapter.setSelectedPosition(selectedRatingsValuePosition)
                            rv_category_values.layoutManager = LinearLayoutManager(
                                this, RecyclerView.VERTICAL, false
                            )
                            rv_category_values.adapter = ratingAdapter
                        }

                        6 -> {
                            var availabilityAdapter =
                                FilterRatingAdapter(availabilityValueList, this)
                            availabilityAdapter.onSingleValueItemClick =
                                { isSelected: Boolean, selectedPosition: Int, selectedValue: Int ->
                                    selectedAvailabilityValue = selectedValue
                                    selectedAvailabilityValuePosition = selectedPosition
                                    if (isSelected) {
                                        searchCategoryList[6].count = 1
                                        filterCategoryAdapter.notifyItemChanged(6)
                                    } else {
                                        searchCategoryList[6].count = 0
                                        filterCategoryAdapter.notifyItemChanged(6)
                                    }
                                }
                            availabilityAdapter.setSelectedPosition(
                                selectedAvailabilityValuePosition
                            )
                            rv_category_values.layoutManager = LinearLayoutManager(
                                this, RecyclerView.VERTICAL, false
                            )
                            rv_category_values.adapter = availabilityAdapter
                        }
                    }
                }
            }
        btn_reset.setOnClickListener {
            selectedSortValue = ""
            selectedSortPosition = -1

            selectedRatingValue = 0
            selectedRatingsValuePosition = -1

            selectedAvailabilityValue = 0
            selectedAvailabilityValuePosition = -1

            selectedBrandPositions = mutableListOf()
            selectedBrandValues = mutableListOf()

            selectedAgePosition = mutableListOf()
            selectedAgeValues = mutableListOf()

            selectedShopForPositions = mutableListOf()
            selectedShopForValues = mutableListOf()

            filterCategoryAdapter.notifyDataSetChanged()
            for (data in searchCategoryList) {
                data.count = 0
            }
            filterCategoryAdapter.notifyDataSetChanged()
            range_seek.setMaxStartValue((response.price.maxValue).toFloat())
            range_seek.setMinStartValue((response.price.minValue).toFloat())

            selectedMinPrice = 0
            selectedMaxPrice = 1000
            range_seek.apply()
        }

        range_seek.setMaxStartValue((response.price.maxValue).toFloat())
        range_seek.setMinStartValue((response.price.minValue).toFloat())
        range_seek.apply()
        range_seek.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            tv_seek_min_value.text = getString(R.string.unit_price_string, minValue)
            tv_seek_max_value.text = getString(R.string.unit_price_string, maxValue)
            selectedMinPrice = minValue.toInt()
            selectedMaxPrice = maxValue.toInt()
        }


    }

    override fun onProductListSuccess(list: ArrayList<ProductItemData>) {
        var intent = Intent()
        var bundle = Bundle()
        bundle.putBoolean(FILTER_ADDRESS_EMPTY, true)
        bundle.putSerializable(BUNDLE_ADDRESS_LIST, list)
        intent.putExtras(bundle)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onProductListEmpty() {
        var intent = Intent()
        var bundle = Bundle()
        bundle.putBoolean(FILTER_ADDRESS_EMPTY, false)
        intent.putExtras(bundle)
        setResult(RESULT_OK, intent)
        finish()
    }
}

