package com.waleed.app.ui.address.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.AddressListResponse
import com.waleed.app.ui.address.newaddress.NewAddressActivity
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.AppConstants
import com.waleed.app.util.AppConstants.BUNDLE_SELECTED_ADDRESS
import com.waleed.app.util.Dialogs
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.toolbar_address.*
import javax.inject.Inject

class AddressListActivity : BaseActivity(), AddressListView {

    @Inject
    lateinit var presenter: AddressListPresenter

    lateinit var addressAdapter: AddressListAdapter

    private var fromProfilePage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        setContentView(R.layout.activity_address_list)
        fromProfilePage = intent.getBooleanExtra(AppConstants.BUNDLE_FROM_HOME, false)
        presenter.getAddressList()
        initView()
    }

    private fun initView() {
        btn_close.setOnClickListener {
            finish()
        }
        ll_new_address.setOnClickListener {
            var bundle = Bundle()
            ActivitiesManager.startActivityForResult(
                this, NewAddressActivity::class.java,
                bundle, 303
            )
        }
    }


    override fun showEmptyList() {

    }

    override fun showAddressList(addressItemList: java.util.ArrayList<AddressListResponse.AddressItem>) {
        rv_address_list.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        addressAdapter = AddressListAdapter(
            addressItemList, this, fromProfilePage
        )
        rv_address_list.adapter = addressAdapter
        addressAdapter.onItemClick = {
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.BUNDLE_ADDRESS_DATA, it)
            if (fromProfilePage) {
                bundle.putBoolean(AppConstants.BUNDLE_KEY_ADDRESS_EDIT,true)
                ActivitiesManager.startActivityForResult(
                    this, NewAddressActivity::class.java, bundle,
                    606
                )
            }else{
                var intent=Intent()
                var bundle=Bundle()
                bundle.putSerializable(BUNDLE_SELECTED_ADDRESS,it)
                intent.putExtras(bundle)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        addressAdapter.onEditClick={
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.BUNDLE_ADDRESS_DATA, it)
            bundle.putBoolean(AppConstants.BUNDLE_KEY_ADDRESS_EDIT,true)
            ActivitiesManager.startActivityForResult(
                this, NewAddressActivity::class.java, bundle,
                606
            )
        }
        addressAdapter.onDeleteClick={
            presenter.deleteAddress(it.customerAddressID)
        }
    }

    override fun onSuccessDelete(message: String) {
        Dialogs.alertDialog(
            this, message, getString(R.string.oke_text)
        ) { _, _ ->
           presenter.getAddressList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                303 -> {
                    presenter.getAddressList()
                }
                606->{
                    presenter.getAddressList()
                }
            }
        }
    }

}