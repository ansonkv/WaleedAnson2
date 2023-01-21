package com.waleed.app.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.waleed.app.R
import com.waleed.app.util.Dialogs
import kotlinx.android.synthetic.main.content_order_error.*
import kotlinx.android.synthetic.main.fragment_base.*
import kotlin.error

/**
 * Created by hanihussein on 9/30/17.
 */
abstract class BaseFragment : Fragment(), BaseView {

    lateinit var rootView: View

    lateinit var loaderView: View

    lateinit var mainView: View

    var errorView: View? = null

    var emptyView: View? = null

    private var loadingDialog: Dialog? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_base, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }


    open fun initView() {

        main.layoutResource = getMainLayout()

        loaderView = loader.inflate()
        mainView = main.inflate()
        errorView = error.inflate()

        retry_btn.setOnClickListener({ onRetry() })
    }


    override fun onRetry() {

    }

    open fun getEmptyLayout(): Int {

        return 0
    }


    open fun getErrorLayout(): Int {

        return 0
    }

    override fun showErrorView() {

        if (emptyView != null)
            emptyView!!.visibility = View.GONE

        //errorView!!.visibility = View.VISIBLE

        mainView.visibility = View.GONE

        loaderView.visibility = View.GONE


    }

    abstract fun getMainLayout(): Int


    override fun showContent() {
        mainView.visibility = View.VISIBLE

        loaderView.visibility = View.GONE

        errorView!!.visibility = View.GONE
    }

    override fun showEmpty() {

        if (emptyView == null) {
            empty.layoutResource = getEmptyLayout()
            emptyView = empty.inflate()
        }
        emptyView!!.visibility = View.VISIBLE
        mainView.visibility = View.GONE
        loaderView.visibility = View.GONE
        errorView!!.visibility = View.GONE
    }

    override fun showLoading() {
        mainView.visibility = View.GONE
        if (emptyView != null)
            emptyView!!.visibility = View.GONE
        if (errorView != null)
            errorView!!.visibility = View.GONE
        loaderView.visibility = View.VISIBLE
    }

    override fun showLoading(context: Context) {
        if (loadingDialog == null)
            loadingDialog = Dialogs.createCustomDialog(context, R.layout.dialog_loading,
                    0, true, false, false)
        loadingDialog!!.show()
    }

    override fun hideLoading() {
        mainView.visibility = View.VISIBLE
        loaderView.visibility = View.GONE
        if (loadingDialog != null && loadingDialog!!.isShowing)
            loadingDialog!!.dismiss()
        if (errorView != null)
            errorView!!.visibility = View.GONE
    }

    override fun showLoaderDialog() {
        if (loadingDialog == null)
            loadingDialog = Dialogs.createCustomDialog(activity, R.layout.dialog_loading,
                    0, true, true, false)
        loadingDialog!!.show()
    }

    override fun hideLoaderDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing)
            loadingDialog!!.dismiss()
    }

    override fun showPop(message: String?) {
        Dialogs.alertDialog(activity, message, getString(R.string.oke_text), null)
    }

    override  fun showNoNetwork() {
        showPop(getString(R.string.no_network_message))
    }
}