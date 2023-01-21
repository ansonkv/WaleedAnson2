package com.waleed.app.ui.account.birthday.newchild

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.BaseResponse
import com.waleed.app.business.data.newdata.ChildListResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.AppConstants.KEY_CHILD_DATA
import com.waleed.app.util.AppConstants.KEY_IS_EDIT
import com.waleed.app.util.getStringValue
import com.waleed.app.util.setTextValue
import kotlinx.android.synthetic.main.activity_new_child.*
import kotlinx.android.synthetic.main.activity_new_child.btn_submit
import kotlinx.android.synthetic.main.toolbar_address.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NewChildActivity : BaseActivity(), NewChildView {

    @Inject
    lateinit var presenter: NewChildPresenter
    private var selectedDate = ""
    private var isEditing = false
    private lateinit var childData: ChildListResponse.ChildItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_child)

        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        intViews()

    }

    private fun intViews() {
        btn_close.setOnClickListener { onBackPressed() }
        isEditing = intent.getBooleanExtra(KEY_IS_EDIT, false)
        if (isEditing) {
            toolbar_page_title_tv.text = getString(R.string.edit_profile_string)
            childData = intent.getSerializableExtra(KEY_CHILD_DATA) as ChildListResponse.ChildItem
            etName.setTextValue(childData.childName)
            etDOB.text = childData.birthDate

        } else {
            toolbar_page_title_tv.text = getString(R.string.add_child_string)
        }
        etDOB.setOnClickListener {
            openCalender()
        }
        btn_submit.setOnClickListener {
            if (verifyData()) {
                if (isEditing) {
                    presenter.updateChildData(
                        childName = etName.getStringValue(),
                        bob = etDOB.text.toString(),
                        childId = childData.childID
                    )
                } else {
                    presenter.addChildData(
                        childName = etName.getStringValue(),
                        bob = etDOB.text.toString()
                    )
                }
            }
        }
    }

    private fun verifyData(): Boolean {
        return when {
            etName.text.isEmpty() -> {
                showPop(getString(R.string.enter_valid_name_string))
                false
            }
            etDOB.text.isEmpty() -> {
                showPop(getString(R.string.select_dob))
                false
            }
            else -> {
                true
            }
        }
    }

    private fun openCalender() {
        var myCalendar = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val myFormat = "dd/MM/yyyy" //In which you need put here

                val sdf = SimpleDateFormat(myFormat, Locale.US)
                selectedDate = sdf.format(myCalendar.getTime())
                val displayFormat = "MM/dd/yyyy"

                val sdf1 = SimpleDateFormat(displayFormat, Locale.US)
                etDOB.text = sdf1.format(myCalendar.getTime())

                Log.e("Selected date", selectedDate)
            }
        DatePickerDialog(
            this, date, myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    override fun addedSuccessfully(baseResponse: BaseResponse) {
        setResult(RESULT_OK, Intent())
        finish()
    }
}