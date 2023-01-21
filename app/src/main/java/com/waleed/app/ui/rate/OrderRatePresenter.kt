package com.waleed.app.ui.rate

import android.net.Uri
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class OrderRatePresenter @Inject constructor(private var apiDataSource: APIDataSource):BasePresenter<OrderRateView>() {
    fun submitReview(productID: Int, rating: String, review: String, imagelist: ArrayList<MultipartBody.Part>) {
         if (isConnected){
             view?.showLoading()
             val mFormProductID = productID.toString().toRequestBody(MultipartBody.FORM)
             val mFormRating = rating.toRequestBody(MultipartBody.FORM)
             val mFormReview = review.toRequestBody(MultipartBody.FORM)
             val mFormCustomerId = if (LoggedUser.getInstance().isUserLoggedIn()) {
                 LoggedUser.getInstance().getAccount()!!.customerID
             } else {
                 0
             }.toString().toRequestBody(MultipartBody.FORM)
             apiDataSource.submitReview(imagelist,mFormCustomerId,mFormProductID,mFormRating,
                 mFormReview
             ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                 .subscribe({
                    view?.hideLoading()
                     if (it.status==1){
                        view?.onSuccessReviewSubmission()
                    }else{
                         view?.showPop(it.message)
                     }
                 },{})
         }else
             view?.showNoNetwork()
    }

}