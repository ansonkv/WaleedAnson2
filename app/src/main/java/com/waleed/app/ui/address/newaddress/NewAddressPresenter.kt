package com.waleed.app.ui.address.newaddress

import com.waleed.app.Waleed
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewAddressPresenter @Inject
constructor(private var apiDataSource: APIDataSource) : BasePresenter<NewAddressView>() {
    fun getGovernorateList() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getGovernorateList()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.setGovernorateList(it.govStateList)
                    }
                }, {

                })
        } else {
            view?.showNoNetwork()
        }
    }

    fun getAreaList(govStateID: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getAreListId(govStateID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.setAreaCityList(it.areaCityList)
                    } else {

                    }
                }, {})
        } else {
            view?.hideLoading()
        }

    }

    fun addNewAddress(
        addressType: Int, title: String, govtId: Int, areaId: Int, street: String,
        block: String, buildingNumber: String, floorNumber: String, flatNumber: String,
        description: String, latitude: String, longitude: String
    ) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.addNewAddress(
                customerID = LoggedUser.getInstance().getAccount()!!.customerID,
                longitude = longitude, latitude = latitude, addressType = addressType,
                title = title, areaId = areaId, block = block,
                building = buildingNumber, direction = description,
                flatNumber = flatNumber, floor = floorNumber,
                govId = govtId, houseNumber = buildingNumber, street = street

            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.onAddressAddedSuccess()
                    } else {
                        view?.hideLoading()
                    }
                }, {})

        } else {
            view?.showNoNetwork()
        }
    }

    fun updateAddress(
        addressType: Int, title: String, govtId: Int, areaId: Int, street: String,
        block: String, buildingNumber: String, floorNumber: String, flatNumber: String,
        description: String, latitude: String, longitude: String, addressId: Int
    ) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.updateAddress(
                customerID = LoggedUser.getInstance().getAccount()!!.customerID,
                longitude = longitude, latitude = latitude, addressType = addressType,
                title = title, areaId = areaId, block = block,
                building = buildingNumber, direction = description,
                flatNumber = flatNumber, floor = floorNumber,
                govId = govtId, houseNumber = buildingNumber, street = street,
                addressId = addressId

            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.onAddressUpdateSuccess()
                    } else {
                        view?.hideLoading()
                    }
                }, {
                    view?.hideLoading()
                    view?.showErrorView()
                })

        } else {
            view?.showNoNetwork()
        }
    }

    fun updateGuestAddress(
        addressType: Int,
        title: String,
        govtId: Int,
        areaId: Int,
        street: String,
        flatNumber: String,
        block: String,
        buildingNumber: String,
        description: String,
        floorNumber: String,
        custName: String,
        custEmail: String,
        mobile: String
    ) {
        if (isConnected) {
            apiDataSource.guestUserLogin(
                areaId = areaId,
                addressType = addressType,
                appType = 2,
                block = block,
                building = buildingNumber,
                callingCode = "965",
                customerName = custName,
                deviceId = Waleed.getInstance().getAppPref().getString(
                    AppConstants.PREF_DEVICE_TOKEN
                )!!,
                direction = description,
                emailId = custEmail,
                flatNumber = flatNumber,
                floor = floorNumber,
                govId = govtId,
                houseNumber = buildingNumber,
                mobileNUmber = mobile,
                street = street,
                title = title
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.onGuestUserLoginSuccess(it)
                    } else {
                        view?.onGuestUserLoginFailed()
                        view?.hideLoading()
                    }
                }, {
                    view?.hideLoading()
                    view?.showErrorView()
                })
        } else {
            view?.showNoNetwork()
        }
    }
}