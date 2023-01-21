package com.waleed.app.ui.address.newaddress

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView.OnEditorActionListener
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.*
import com.waleed.app.ui.address.mappickup.LocationMapActivity
import com.waleed.app.ui.address.newaddress.adapter.AreaCitySpinnerAdapter
import com.waleed.app.ui.address.newaddress.adapter.GovernorateSpinnerAdapter
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.*
import com.waleed.app.util.AppConstants.ADD_ADDRESS_MAP_REQUEST_CODE
import com.waleed.app.util.AppConstants.KEY_BUNDLE_GUEST
import com.waleed.app.util.AppConstants.PREF_GUEST_ADDRESS
import kotlinx.android.synthetic.main.activity_new_address.*
import kotlinx.android.synthetic.main.activity_new_address.btn_submit
import kotlinx.android.synthetic.main.activity_new_address.et_mobile
import kotlinx.android.synthetic.main.activity_new_address.spinner_gover
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar_address.*
import java.util.*
import javax.inject.Inject


class NewAddressActivity : BaseActivity(), NewAddressView {

    @Inject
    lateinit var presenter: NewAddressPresenter
    var areaName = ""
    var goverName = ""
    private lateinit var governorateList: ArrayList<GovStateListResponse.GovState>
    private lateinit var areaList: ArrayList<AreaCityListResponse.AreaCity>
    private var selectedGovID = 0
    private var selectedAreaId = 0
    private var selectedAddressType = 0
    private var isEditing = false
    private var isGuest = false
    private var govAreaEdited = false
    private var areaListEdited = false
    private lateinit var editAddressData: AddressListResponse.AddressItem
    private var selectedLatitude: String = ""
    private var selectedLongitude: String = ""
    private lateinit var guestAddress: GuestAddressData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_new_address)

        isEditing = intent.getBooleanExtra(AppConstants.BUNDLE_KEY_ADDRESS_EDIT, false)
        isGuest = intent.getBooleanExtra(KEY_BUNDLE_GUEST, false)
        if (isGuest) {
            llGuest.makeVisible()
            tv_pick_map.makeGone()
        }
        if (isEditing) {
            editAddressData = intent.getSerializableExtra(AppConstants.BUNDLE_ADDRESS_DATA)
                    as AddressListResponse.AddressItem
            et_address_name_title.setText(editAddressData.title)
            et_address_street.setText(editAddressData.street)
            et_address_flat_no.setText(editAddressData.flatNo)
            et_address_block.setText(editAddressData.block)
            et_address_build_no.setText(editAddressData.building)
            et_address_direction.setText(editAddressData.direction)
            et_address_floor.setText(editAddressData.floor)
        }
        presenter.getGovernorateList()
        tv_pick_map.setOnClickListener {
            var bundle = Bundle()

            ActivitiesManager.startActivityForResult(
                this,
                LocationMapActivity::class.java,
                bundle,
                ADD_ADDRESS_MAP_REQUEST_CODE
            )
        }

        rb_apartment.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rb_home.isChecked = false
                rb_apartment.isChecked = true
                selectedAddressType = 2
                llFloor.makeVisible()
                tvBuildingTitle.text = getString(R.string.building_no_string)
            }
        }
        rb_home.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rb_apartment.isChecked = false
                rb_home.isChecked = true
                selectedAddressType = 1
                llFloor.makeGone()
                tvBuildingTitle.text = getString(R.string.house_no_string)
            }
        }
        if (isEditing) {
            if (editAddressData.addressType == 1) {
                rb_home.isChecked = true
                rb_apartment.isChecked = false
                selectedAddressType = 1
                llFloor.makeGone()
                tvBuildingTitle.text = getString(R.string.house_no_string)
            } else {
                rb_apartment.isChecked = true
                rb_home.isChecked = false
                selectedAddressType = 2
                llFloor.makeVisible()
                tvBuildingTitle.text = getString(R.string.building_no_string)
            }
        } else {
            rb_home.isChecked = true
            rb_apartment.isChecked = false
            selectedAddressType = 1
            llFloor.makeGone()
            tvBuildingTitle.text = getString(R.string.house_no_string)
        }

        et_address_street.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                et_address_build_no.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })
        et_address_block.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                et_address_street.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })
        et_address_build_no.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (rb_apartment.isChecked)
                    et_address_floor.requestFocus()
                else
                    et_address_direction.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })
        et_address_floor.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                et_address_flat_no.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })
        et_address_flat_no.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                et_address_direction.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })
        btn_submit.setOnClickListener {

            if (LoggedUser.getInstance().isUserLoggedIn()) {
                if (rb_apartment.isChecked) {
                    if (validateDataAppartment()) {
                        if (isEditing) {
                            presenter.updateAddress(
                                addressType = selectedAddressType,
                                title = et_address_name_title.getStringValue(),
                                govtId = selectedGovID,
                                areaId = selectedAreaId,
                                street = et_address_street.getStringValue(),
                                flatNumber = et_address_flat_no.getStringValue(),
                                block = et_address_block.getStringValue(),
                                latitude = selectedLatitude,
                                longitude = selectedLongitude,
                                buildingNumber = et_address_build_no.getStringValue(),
                                description = et_address_direction.getStringValue(),
                                floorNumber = et_address_floor.getStringValue(),
                                addressId = editAddressData.customerAddressID
                            )
                        } else {

                            presenter.addNewAddress(
                                addressType = selectedAddressType,
                                title = et_address_name_title.getStringValue(),
                                govtId = selectedGovID,
                                areaId = selectedAreaId,
                                street = et_address_street.getStringValue(),
                                flatNumber = et_address_flat_no.getStringValue(),
                                block = et_address_block.getStringValue(),
                                latitude = selectedLatitude,
                                longitude = selectedLongitude,
                                buildingNumber = et_address_build_no.getStringValue(),
                                description = et_address_direction.getStringValue(),
                                floorNumber = et_address_floor.getStringValue()
                            )
                        }
                    }
                } else {
                    if (validateDataHome()) {
                        if (isEditing) {
                            presenter.updateAddress(
                                addressType = selectedAddressType,
                                title = et_address_name_title.getStringValue(),
                                govtId = selectedGovID,
                                areaId = selectedAreaId,
                                street = et_address_street.getStringValue(),
                                flatNumber = et_address_flat_no.getStringValue(),
                                block = et_address_block.getStringValue(),
                                latitude = selectedLatitude,
                                longitude = selectedLongitude,
                                buildingNumber = et_address_build_no.getStringValue(),
                                description = et_address_direction.getStringValue(),
                                floorNumber = et_address_floor.getStringValue(),
                                addressId = editAddressData.customerAddressID
                            )
                        } else {

                            presenter.addNewAddress(
                                addressType = selectedAddressType,
                                title = et_address_name_title.getStringValue(),
                                govtId = selectedGovID,
                                areaId = selectedAreaId,
                                street = et_address_street.getStringValue(),
                                flatNumber = et_address_flat_no.getStringValue(),
                                block = et_address_block.getStringValue(),
                                latitude = selectedLatitude,
                                longitude = selectedLongitude,
                                buildingNumber = et_address_build_no.getStringValue(),
                                description = et_address_direction.getStringValue(),
                                floorNumber = et_address_floor.getStringValue()
                            )
                        }
                    }
                }
            } else {
                if (rb_apartment.isChecked) {
                    if (validateDataGuestAppartment()) {
                        guestAddress = GuestAddressData(
                            addressType = selectedAddressType,
                            title = et_address_name_title.getStringValue(),
                            govtId = selectedGovID,
                            areaId = selectedAreaId,
                            street = et_address_street.getStringValue(),
                            flatNumber = et_address_flat_no.getStringValue(),
                            block = et_address_block.getStringValue(),
                            buildingNumber = et_address_build_no.getStringValue(),
                            description = et_address_direction.getStringValue(),
                            floorNumber = et_address_floor.getStringValue(),
                            custName = etName.getStringValue(),
                            custEmail = etEmail.getStringValue(),
                            mobile = et_mobile.getStringValue()
                        )
                        presenter.updateGuestAddress(
                            addressType = selectedAddressType,
                            title = et_address_name_title.getStringValue(),
                            govtId = selectedGovID,
                            areaId = selectedAreaId,
                            street = et_address_street.getStringValue(),
                            flatNumber = et_address_flat_no.getStringValue(),
                            block = et_address_block.getStringValue(),
                            buildingNumber = et_address_build_no.getStringValue(),
                            description = et_address_direction.getStringValue(),
                            floorNumber = et_address_floor.getStringValue(),
                            custName = etName.getStringValue(),
                            custEmail = etEmail.getStringValue(),
                            mobile = et_mobile.getStringValue()
                        )
                    }
                } else {
                    if (validateDataGuestHome()) {
                        guestAddress = GuestAddressData(
                            addressType = selectedAddressType,
                            title = et_address_name_title.getStringValue(),
                            govtId = selectedGovID,
                            areaId = selectedAreaId,
                            street = et_address_street.getStringValue(),
                            flatNumber = et_address_flat_no.getStringValue(),
                            block = et_address_block.getStringValue(),
                            buildingNumber = et_address_build_no.getStringValue(),
                            description = et_address_direction.getStringValue(),
                            floorNumber = et_address_floor.getStringValue(),
                            custName = etName.getStringValue(),
                            custEmail = etEmail.getStringValue(),
                            mobile = et_mobile.getStringValue()
                        )
                        presenter.updateGuestAddress(
                            addressType = selectedAddressType,
                            title = et_address_name_title.getStringValue(),
                            govtId = selectedGovID,
                            areaId = selectedAreaId,
                            street = et_address_street.getStringValue(),
                            flatNumber = et_address_flat_no.getStringValue(),
                            block = et_address_block.getStringValue(),
                            buildingNumber = et_address_build_no.getStringValue(),
                            description = et_address_direction.getStringValue(),
                            floorNumber = et_address_floor.getStringValue(),
                            custName = etName.getStringValue(),
                            custEmail = etEmail.getStringValue(),
                            mobile = et_mobile.getStringValue()
                        )
                    }
                }
            }
        }

//
//            var addressData = AddressData(
//                1,
//                et_address_name_title.text.toString().trim(),
//                "$areaName, $goverName",
//                block.toInt(),
//                street.toInt(),
//                et_address_build_no.text.toString().trim(),
//                et_address_flat_no.text.toString().trim(),
//                et_address_direction.text.toString().trim(),
//                false
//            )
//            intent.putExtra(AppConstants.BUNDLE_ADDRESS, addressData)
//            setResult(RESULT_OK, intent)
//            finish()

        btn_close.setOnClickListener {
            onBackPressed()
        }

    }

    private fun validateDataGuestHome(): Boolean {
        return when {
            et_address_name_title.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_title))
                false
            }
            etName.text.isEmpty() -> {
                showPop(getString(R.string.enter_valid_name_string))
                return false
            }
            et_mobile.text.toString().isEmpty() -> {
                showPop(getString(R.string.enter_valid_phone_number))
                return false
            }
            selectedGovID == 0 -> {
                showPop(getString(R.string.please_select_your_governorate))
                false
            }
            selectedAreaId == 0 -> {
                showPop(getString(R.string.please_select_area))
                false
            }
            et_address_block.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_block))
                false
            }
            et_address_street.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_your_street))
                false
            }
            et_address_build_no.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_your_building))
                false
            }

            else -> {
                true
            }
        }
    }

    private fun validateDataGuestAppartment(): Boolean {
        return when {
            et_address_name_title.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_title))
                false
            }
            etName.text.isEmpty() -> {
                showPop(getString(R.string.enter_valid_name_string))
                return false
            }
            et_mobile.text.toString().isEmpty() -> {
                showPop(getString(R.string.enter_valid_phone_number))
                return false
            }
            selectedGovID == 0 -> {
                showPop(getString(R.string.please_select_your_governorate))
                false
            }
            selectedAreaId == 0 -> {
                showPop(getString(R.string.please_select_area))
                false
            }
            et_address_block.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_block))
                false
            }
            et_address_street.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_your_street))
                false
            }
            et_address_build_no.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_your_building))
                false
            }
            et_address_floor.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_your_floor))
                false
            }
            et_address_flat_no.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_flat_number))
                false
            }
            else -> {
                true
            }
        }
    }

    private fun validateDataAppartment(): Boolean {
        return when {
            et_address_name_title.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_title))
                false
            }
            selectedGovID == 0 -> {
                showPop(getString(R.string.please_select_your_governorate))
                false
            }
            selectedAreaId == 0 -> {
                showPop(getString(R.string.please_select_area))
                false
            }
            et_address_block.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_block))
                false
            }
            et_address_street.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_your_street))
                false
            }
            et_address_build_no.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_your_building))
                false
            }
            et_address_floor.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_your_floor))
                false
            }
            et_address_flat_no.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_flat_number))
                false
            }
            else -> {
                true
            }
        }
    }


    private fun validateDataHome(): Boolean {
        return when {
            et_address_name_title.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_title))
                false
            }
            selectedGovID == 0 -> {
                showPop(getString(R.string.please_select_your_governorate))
                false
            }
            selectedAreaId == 0 -> {
                showPop(getString(R.string.please_select_area))
                false
            }
            et_address_block.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_block))
                false
            }
            et_address_street.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_your_street))
                false
            }
            et_address_build_no.text.isEmpty() -> {
                showPop(getString(R.string.please_enter_your_building))
                false
            }

            else -> {
                true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ADD_ADDRESS_MAP_REQUEST_CODE -> {
                if (data!!.getStringExtra(AppConstants.BUNDLE_MAP_AREA) != null) {
                    areaName = data!!.getStringExtra(AppConstants.BUNDLE_MAP_AREA)!!
                }
                if (data!!.getStringExtra(AppConstants.BUNDLE_MAP_GOV) != null) {
                    goverName = data!!.getStringExtra(AppConstants.BUNDLE_MAP_GOV)!!
                }
                if (data!!.getStringExtra(AppConstants.BUNDLE_MAP_STREET) != null) {
                    et_address_street.text =
                        data!!.getStringExtra(AppConstants.BUNDLE_MAP_STREET)!!.toEditable()
                }
                if (data!!.getStringExtra(AppConstants.BUNDLE_MAP_DIRECTION) != null) {
                    et_address_direction.text =
                        data!!.getStringExtra(AppConstants.BUNDLE_MAP_DIRECTION)!!.toEditable()
                }
                if (data.getStringExtra(AppConstants.BUNDLE_MAP_LATITUDE) != null) {
                    selectedLatitude = data.getStringExtra(AppConstants.BUNDLE_MAP_LATITUDE)!!
                }
                if (data.getStringExtra(AppConstants.BUNDLE_MAP_LONGITUDE) != null) {
                    selectedLongitude = data.getStringExtra(AppConstants.BUNDLE_MAP_LONGITUDE)!!
                }
            }
        }
    }

    override fun setGovernorateList(govStateList: ArrayList<GovStateListResponse.GovState>) {
        governorateList = ArrayList<GovStateListResponse.GovState>()
        governorateList.add(GovStateListResponse.GovState(govStateName = getString(R.string.select_governorate)))
        governorateList.addAll(govStateList)
        var spinnerGovAdapter = GovernorateSpinnerAdapter(
            this, governorateList
        )
        spinner_gover.adapter = spinnerGovAdapter
        spinner_gover.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGovID = (if (position == 0) {
                    0
                } else {
                    governorateList[position].govStateID
                })
                if (selectedGovID != 0) {
                    presenter.getAreaList(governorateList[position].govStateID)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        if (isEditing) {
            governorateList.forEachIndexed { index, govState ->
                if (editAddressData.govStateID == govState.govStateID) {
                    spinner_gover.setSelection(index)
                }
            }
        }

    }

    override fun setAreaCityList(areaCityList: ArrayList<AreaCityListResponse.AreaCity>) {
        selectedAreaId = 0
        areaList = ArrayList<AreaCityListResponse.AreaCity>()
        areaList.addAll(areaCityList)
        var areaAdapter = AreaCitySpinnerAdapter(
            this, areaList
        )
        spinner_area.adapter = areaAdapter
        spinner_area.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedAreaId = areaList[position].areaCityID
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        if (isEditing) {
            areaList.forEachIndexed { index, areaCity ->
                if (editAddressData.areaCityID == areaCity.areaCityID) {
                    spinner_area.setSelection(index)
                }
            }
        }
    }

    override fun onAddressAddedSuccess() {
        setResult(RESULT_OK, Intent())
        finish()
    }

    override fun onAddressUpdateSuccess() {
        setResult(RESULT_OK, Intent())
        finish()
    }

    override fun onGuestUserLoginFailed() {
        setResult(RESULT_CANCELED, Intent())
    }

    override fun onGuestUserLoginSuccess(guestLoginResponse: GuestLoginResponse) {
        LoggedUser.getInstance().setGuestAccount(guestLoginResponse,guestAddress)
        Waleed.appContext.getAppPref().saveObject(PREF_GUEST_ADDRESS, guestAddress)
        setResult(RESULT_OK, Intent())
        finish()
    }
}