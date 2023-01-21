package com.waleed.app.ui.payment

import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.CheckOutResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.AppConstants
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.toolbar_home.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class PaymentActivity : BaseActivity() {

    private var isErrorHappened: Boolean = false
    private var isKNETErrorHappened: Boolean = false
    private var isOrderSuccess: Boolean = false
    lateinit var paymentUrl: String
    lateinit var successUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        setContentView(R.layout.activity_payment)
        btn_close.makeGone()
        btn_cart.makeGone()
        btn_language.makeGone()
        paymentUrl =
            (intent.getSerializableExtra(
                AppConstants.BUNDLE_PAYMENT_DETAILS
            ) as CheckOutResponse).paymentURL
        wv_knet.settings.javaScriptEnabled = true
        progress.visibility = View.VISIBLE
        wv_knet.loadUrl(paymentUrl)
        wv_knet.webViewClient = yourWebClient
        wv_knet.addJavascriptInterface(MyJavaScriptInterface(), "HtmlViewer")
    }

    private var yourWebClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // wv_knet.visibility = View.INVISIBLE
            Log.e("PaymentUrl loading", url)
            if (url.contains("PaymentSuccessMobile")) {
                successUrl = url
                progress.makeVisible()
                wv_knet.makeGone()
                var content: SuccessContent = SuccessContent()
                content.execute()
            }

            return false
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.e("PaymentUrl loading", url!!)
            if (url!!.contains("PaymentSuccessMobile")) {
                isOrderSuccess = true
                successUrl = url
                var content: SuccessContent = SuccessContent()
                content.execute()
                wv_knet.makeGone()
                progress.makeVisible()
            }
            if (url.contains("PaymentErrorMobile")) {
                onFailedPayment()
            }
        }

        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            Log.e("OnError", description!!)
        }

        override fun onPageFinished(view: WebView, url: String) {
            Log.e("PaymentUrl finished", url)
            progress.makeGone()
            if (isOrderSuccess) {
                progress.makeVisible()
                wv_knet.makeGone()
            }
            Log.i("PageFinish", url)
//            if (url.contains("status=0")) {
//                isErrorHappened = true
//                if (url.contains("error_code=1")) {
//                    isKNETErrorHappened = true
//                }
//            } else if (url.contains("PaymentSuccessMobile")) {
//                isOrderSuccess = true
//                successUrl = url
//            } else if (url.contains("status=2")) {
//                isErrorHappened = true
//            }
            wv_knet.loadUrl("javascript:HtmlViewer.showHTML" + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
        }
    }


    inner class SuccessContent : AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                var doc: org.jsoup.nodes.Document? = Jsoup.connect(successUrl).get()
                var table: Element = doc!!.select("table").first()
                var values: Iterator<IndexedValue<Element>> =
                    table.select("td").iterator().withIndex()
                values.next()
                onSuccess(Jsoup.parse(values.next().value.text()).text())
            } catch (e: Exception) {

            }
            return null
        }

    }

    private fun onSuccess(text: String?) {
        var intent = Intent()
        intent.putExtra(AppConstants.BUNDLE_TRANSACTION_ID, text)
        intent.putExtra(AppConstants.BUNDLE_PAYMENT_STATUS, true)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun onFailedPayment() {
        var intent = Intent()
        intent.putExtra(AppConstants.BUNDLE_PAYMENT_STATUS, false)
        setResult(RESULT_OK, intent)
        finish()
    }

    internal inner class MyJavaScriptInterface {

        @JavascriptInterface
        fun showHTML(html: String) {

            if (isErrorHappened) {
                Log.e("PAyment", ">>>>Failed")

            } else if (isOrderSuccess) {

                Log.e("PAyemet", ">>>>Success")
//
            }

        }
    }
}