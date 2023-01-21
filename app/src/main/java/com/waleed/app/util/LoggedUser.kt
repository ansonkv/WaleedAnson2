package com.waleed.app.util


import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.CustomerDetailsResponse
import com.waleed.app.business.data.newdata.GuestAddressData
import com.waleed.app.business.data.newdata.GuestLoginResponse


class LoggedUser {

    companion object {
        private var loggedUser: LoggedUser? = null

        fun getInstance(): LoggedUser {
            if (loggedUser == null) {
                loggedUser = LoggedUser()
            }
            return loggedUser!!
        }
    }

    private var userProfileModel: CustomerDetailsResponse.Customer? = null

    fun getAccount(): CustomerDetailsResponse.Customer? {
        return if (userProfileModel != null)
            userProfileModel
        else
            Waleed.getInstance().getAppPref().loadObject(AppConstants.PREF_ACCOUNT,
                CustomerDetailsResponse.Customer::class.java) as CustomerDetailsResponse.Customer?
    }

    fun setAccount(accountDetails: CustomerDetailsResponse.Customer) {
        Waleed.getInstance().getAppPref().saveBoolean(AppConstants.KEY_IS_USER_LOGGED_IN, true)
        Waleed.getInstance().getAppPref().saveObject(AppConstants.PREF_ACCOUNT, accountDetails)
        this@LoggedUser.userProfileModel = accountDetails
    }

    fun setGuestAccount(guestLoginResponse: GuestLoginResponse, guestAddress: GuestAddressData){
        Waleed.appContext.getAppPref().saveObject(AppConstants.PREF_GUEST_USER_DATA, guestLoginResponse)
        Waleed.appContext.getAppPref().saveBoolean(AppConstants.PREF_GUEST_USER, true)
        var accountDetails=CustomerDetailsResponse.Customer(
            customerID = guestLoginResponse.customerID,
            customerName = guestAddress.custName,
            callingCode = "+965",
            mobile = guestAddress.mobile,
            emailID = guestAddress.custEmail
        )
        Waleed.getInstance().getAppPref().saveObject(AppConstants.PREF_ACCOUNT, accountDetails)
    }

    fun logout() {
        Waleed.getInstance().getAppPref().saveBoolean(AppConstants.KEY_IS_USER_LOGGED_IN, false)
        Waleed.appContext.getAppPref().saveBoolean(AppConstants.PREF_GUEST_USER, false)
        Waleed.getInstance().getAppPref().removeSharedPref(AppConstants.PREF_ACCOUNT)
    }


    fun isUserLoggedIn(): Boolean {
        return Waleed.getInstance().getAppPref().getBoolean(AppConstants.KEY_IS_USER_LOGGED_IN, false)
    }


}