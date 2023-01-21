package com.waleed.app.ui.filter

import com.waleed.app.business.data.SearchFilterCategory
import com.waleed.app.business.data.SearchFilterCategoryValues
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchFilterPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<SearchFilterView>() {
    fun getFilterCategories() {

        var sortValue1 = SearchFilterCategoryValues(11, "Relevence")
        var sortValue2 = SearchFilterCategoryValues(222, "High to Low")
        var sortValue3 = SearchFilterCategoryValues(233, "Low to High")
        var sortValueList = listOf<SearchFilterCategoryValues>(sortValue1, sortValue2, sortValue3)

        var brandValue1 = SearchFilterCategoryValues(444, "Lego")
        var brandValue2 = SearchFilterCategoryValues(445, "Bandai Namco")
        var brandValue3 = SearchFilterCategoryValues(446, "Fisher Price")
        var brandValue4 = SearchFilterCategoryValues(447, "Barbie")
        var brandValue5 = SearchFilterCategoryValues(448, "Nerf")
        var brandValue6 = SearchFilterCategoryValues(449, "Hasbro")
        var brandValue7 = SearchFilterCategoryValues(454, "Hot Wheels")
        var brandValue8 = SearchFilterCategoryValues(464, "My Little Pony")
        var brandValue9 = SearchFilterCategoryValues(424, "Mattel")
        var brandValue10 = SearchFilterCategoryValues(464, "Mobile Suit Gathering")
        var brandValue11 = SearchFilterCategoryValues(484, "Monster High")
        var brandValue12 = SearchFilterCategoryValues(484, "MEGA Blocks")
        var brandValue13 = SearchFilterCategoryValues(484, "Yo- Kai Watch")
        var brandValueList = listOf<SearchFilterCategoryValues>(
            brandValue1,
            brandValue2,
            brandValue3,
            brandValue4,
            brandValue5,
            brandValue6,
            brandValue7,
            brandValue8,
            brandValue9,
            brandValue10,
            brandValue11,
            brandValue12,
            brandValue13
        )

        var ageValue1 = SearchFilterCategoryValues(5555, "0-3 Months")
        var ageValue2 = SearchFilterCategoryValues(5557, "3-6 Months")
        var ageValue3 = SearchFilterCategoryValues(5558, "6-9 Months")
        var ageValue4 = SearchFilterCategoryValues(5559, "9-12 Months")
        var ageValue5 = SearchFilterCategoryValues(5553, "1-2 Year")
        var ageValue6 = SearchFilterCategoryValues(55543, "2-3 Year")
        var ageValue7 = SearchFilterCategoryValues(4445, "3-4 Year")
        var ageValue8 = SearchFilterCategoryValues(8988, "4-5 Year")
        var ageValue9 = SearchFilterCategoryValues(543234, "5-6 Year")
        var ageValueList = listOf<SearchFilterCategoryValues>(
            ageValue1, ageValue2, ageValue3, ageValue4, ageValue5,
            ageValue6, ageValue7, ageValue8, ageValue9
        )

        var shopforValue1 = SearchFilterCategoryValues(22234, "Boys")
        var shopforValue2 = SearchFilterCategoryValues(22134, "Girls")
        var shopforValue3 = SearchFilterCategoryValues(223234, "UniSex")
        var shopforValue4 = SearchFilterCategoryValues(22534, "Young School goings")
        var shopForList = listOf<SearchFilterCategoryValues>(
            shopforValue1,
            shopforValue2,
            shopforValue3,
            shopforValue4
        )

        var ratingValue1 = SearchFilterCategoryValues(122, "5 Stars")
        var ratingValue2 = SearchFilterCategoryValues(122, "4 Stars and more")
        var ratingValue3 = SearchFilterCategoryValues(122, "3 Stars and More")
        var ratingValue4 = SearchFilterCategoryValues(122, "2 Stars and More ")
        var ratingValue5 = SearchFilterCategoryValues(122, "1 Star and More")
        var ratingValueList = listOf<SearchFilterCategoryValues>(
            ratingValue1,
            ratingValue2,
            ratingValue3,
            ratingValue4,
            ratingValue5
        )

        var availabilityValues1 = SearchFilterCategoryValues(1133, "Show In Stock")
        var availabilityValues2 = SearchFilterCategoryValues(1133, "Show All")
        var avalilabilityList =
            listOf<SearchFilterCategoryValues>(availabilityValues1, availabilityValues2)

        var discountValue1 = SearchFilterCategoryValues(3343444, "Upto 70% off")
        var discountValue2 = SearchFilterCategoryValues(3343454, "Upto 50% off")
        var discountValue3 = SearchFilterCategoryValues(3343774, "Any Discount")
        var discountValueList =
            listOf<SearchFilterCategoryValues>(discountValue1, discountValue2, discountValue3)
        var data1 = SearchFilterCategory(1, "Price", null, false, 0)
        var data2 = SearchFilterCategory(2, "Sort By", sortValueList, false, 0)
        var data3 = SearchFilterCategory(3, "Brand", brandValueList, true, 0)
        var data4 = SearchFilterCategory(4, "Age", ageValueList, true, 0)
        var data5 = SearchFilterCategory(4, "Shop for", shopForList, true, 0)
        var data6 = SearchFilterCategory(4, "Ratings", ratingValueList, false, 0)
        var data7 = SearchFilterCategory(4, "Availability", avalilabilityList, false, 0)
        var data8 = SearchFilterCategory(4, "Discount", discountValueList, false, 0)


        var listData =
            listOf<SearchFilterCategory>(data1, data2, data3, data4, data5, data6, data7, data8)
        view?.showSearchCategory(listData)
    }

    fun getFilterInfo() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getFilterInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.setUpFilterValues(it)
                    } else {
                        view?.showError(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    fun filterItem(
        categoryID: Int, sortBy: String, availability: Int, minPrice: Int,
        maxPrice: Int, brandIDList: String, idealIDList: String,
        ageGroupIDList: String, rating: Int
    ) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getFilteredProducts(
                categoryID = categoryID,sortBy = sortBy,availability = availability,
                customerID = if (LoggedUser.getInstance().isUserLoggedIn()) {
                    LoggedUser.getInstance().getAccount()!!.customerID
                } else {
                    0
                },ageGroupIDList = ageGroupIDList,brandIDList = brandIDList,
                idealIDList = idealIDList,maxPrice = maxPrice,minPrice =minPrice,
                rating = rating
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.list.isNullOrEmpty()){
                        view?.onProductListEmpty()
                    }else{
                        view?.onProductListSuccess(it.list)
                    }
                },{})
        } else view?.showNoNetwork()
    }

}