package com.waleed.app.ui.account.birthday.list

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.ChildListResponse
import com.waleed.app.ui.account.birthday.newchild.NewChildActivity
import com.waleed.app.ui.address.newaddress.NewAddressActivity
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.AppConstants
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.activity_birthday_list.*
import kotlinx.android.synthetic.main.toolbar_address.*
import java.util.ArrayList
import javax.inject.Inject

class BirthdayListActivity : BaseActivity(), BirthdayListView {

    @Inject
    lateinit var presenter: BirthdayListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday_list)

        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        initViews()

        presenter.getChildList()
    }

    private fun initViews() {
        btn_close.setOnClickListener { onBackPressed() }
        toolbar_page_title_tv.text = getString(R.string.child_list)
        ll_new_child.setOnClickListener {
            ActivitiesManager.startActivityForResult(
                this, NewChildActivity::class.java, Bundle(),944
            )
        }
    }

    override fun showEmptyList() {
        rv_child_list.makeGone()
        tvEmptyList.makeVisible()
    }

    override fun showList(list: ArrayList<ChildListResponse.ChildItem>) {

        rv_child_list.makeVisible()
        tvEmptyList.makeGone()
        rv_child_list.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        var childListAdapter = ChildListAdapter(list, this)
        rv_child_list.adapter = childListAdapter
        childListAdapter.onDeleteClick = {
            presenter.deleteChild(it.childID)
        }

        childListAdapter.onEditClick = {
            var bundle = Bundle()
            bundle.putBoolean(AppConstants.KEY_IS_EDIT, true)
            bundle.putSerializable(AppConstants.KEY_CHILD_DATA, it)
            ActivitiesManager.startActivityForResult(
                this, NewChildActivity::class.java, bundle, 944
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            944 -> {
                presenter.getChildList()
            }
            else -> {
                presenter.getChildList()
            }
        }
    }
}