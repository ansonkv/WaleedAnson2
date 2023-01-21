package com.waleed.app.ui.checkout

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.*
import com.waleed.app.ui.address.list.AddressListActivity
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.payment.PaymentActivity
import com.waleed.app.ui.payment.PaymentSuccess
import com.waleed.app.util.*
import com.waleed.app.util.AppConstants.KEY_BUNDLE_IS_GUEST
import com.waleed.app.util.AppConstants.PAYMENT_REQUEST
import kotlinx.android.synthetic.main.activity_check_out.*
import kotlinx.android.synthetic.main.layout_apply_coupon.*
import kotlinx.android.synthetic.main.layout_apply_coupon.btn_apply
import kotlinx.android.synthetic.main.layout_apply_coupon.et_coupon
import kotlinx.android.synthetic.main.layout_apply_coupon.img_close_sheet
import kotlinx.android.synthetic.main.layout_enter_mobile.*
import kotlinx.android.synthetic.main.toolbar_check_out.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class CheckoutActivity : BaseActivity(), CheckoutView {
    @Inject
    lateinit var presenter: CheckOutPresenter
    private var selectedAddress: AddressListResponse.AddressItem? = null
    private var subTotal = ""
    private var total = ""
    private lateinit var deliveryTypeAdapter: DeliveryTypeListAdapter
    private lateinit var paymentTypeAdapter: PaymentListAdapter
    private var selectedDeliveryType = 0
    private var selectedDeliverTimeId = 0
    private var paymentMode = 0
    private var deliveryTimeList = arrayListOf<DeliveryTimeListResponse.DeliveryTime>()
    private var grandTotal = 0.0
    private var deliveryAmount = 0.0
    private var wrapAmount = 0.0
    private var discountAmount = 0.0
    var selectedDate = ""
    private lateinit var couponTemp: String
    private var couponValue: String = ""
    private lateinit var cartListResponse: CartListDataResponse
    private lateinit var checkOutData: CheckOutResponse
    private var isAddressNeed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        total = intent.getStringExtra(AppConstants.BUNDLE_TOTAL)!!
        subTotal = intent.getStringExtra(AppConstants.BUNDLE_SUB_TOTAL)!!
        cartListResponse = intent.getSerializableExtra(AppConstants.BUNDLE_CART_RESPONSE)
                as CartListDataResponse
        isAddressNeed = cartListResponse.cartItems.any { it.storeID == 0 }

        setContentView(R.layout.activity_check_out)
        if (isAddressNeed) {

            ll_address.makeVisible()
            rl_deliver_type.makeVisible()
            if (intent.getBooleanExtra(KEY_BUNDLE_IS_GUEST, false)) {
                presenter.getAddressListForGuest()
            }
        } else {
            ll_address.makeGone()
            rl_deliver_type.makeGone()
        }
        tv_total_amount.text = getString(R.string.unit_price_string, total)
        grandTotal = total.getAmount()
        if (!cartListResponse.wrappingTotal.isNullOrEmpty()) {
            rl_wrapping.makeVisible()
            tv_wrap_amount.setLanguageString(
                cartListResponse.wrappingTotalKD, cartListResponse.wrappingTotalKDAr
            )
            wrapAmount = cartListResponse.wrappingTotal.toDouble()
        } else {
            rl_wrapping.makeVisible()
            wrapAmount = 0.0
            tv_wrap_amount.text = getString(
                R.string.unit_price_string, String.format("%.3f", wrapAmount)
            )
        }
        tv_delivery_amount_value.text = getString(
            R.string.unit_price_string, String.format("%.3f", deliveryAmount)
        )
        tv_discount_amount.text = getString(
            R.string.unit_price_string, String.format("%.3f", discountAmount)
        )
        if (checkMultipleStore()) {
//            ll_cod.makeGone()
        }
        tv_grand_price.text = getString(
            R.string.unit_price_string, (grandTotal).toString()
        )
        initViews()
    }

    private fun checkMultipleStore(): Boolean {
        if (cartListResponse.cartItems.size > 1) {
            var isHaveMultipleStore = false

            for (cartItem in cartListResponse.cartItems) {
                if (cartItem.storeID != 0) {
                    isHaveMultipleStore =
                        cartListResponse.cartItems.any { it.storeID == cartItem.storeID }
                    if (isHaveMultipleStore) {
                        break
                    }
                }
            }
            return isHaveMultipleStore
        } else {
            return false
        }
    }

    private fun initViews() {
        if (selectedAddress == null) {
            rl_address.makeGone()
            btn_select_address.makeVisible()
        } else {

            rl_address.makeVisible()
            btn_select_address.makeGone()
        }
        btn_select_address.setOnClickListener {
            var bundle = Bundle()
            ActivitiesManager.startActivityForResult(
                this, AddressListActivity::class.java,
                bundle, 707
            )
        }

        btn_close.setOnClickListener {
            finish()
        }
        presenter.getDeliveryType()
        presenter.getPaymentMethods()

        /*    ll_knet.setOnClickListener {
                rb_knet.isChecked = true
                rb_vis.isChecked = false
                rb_cod.isChecked = false
                paymentMode = 2
            }

            rb_knet.setOnClickListener {
                rb_knet.isChecked = true
                rb_vis.isChecked = false
                rb_cod.isChecked = false
                paymentMode = 2
            }

            ll_visa.setOnClickListener {
                rb_knet.isChecked = false
                rb_vis.isChecked = true
                rb_cod.isChecked = false
                paymentMode = 3
            }

            rb_vis.setOnClickListener {
                rb_knet.isChecked = false
                rb_vis.isChecked = true
                rb_cod.isChecked = false
                paymentMode = 3
            }

            ll_cod.setOnClickListener {
                rb_knet.isChecked = false
                rb_vis.isChecked = false
                rb_cod.isChecked = true
                paymentMode = 1
            }

            rb_cod.setOnClickListener {
                rb_knet.isChecked = false
                rb_vis.isChecked = false
                rb_cod.isChecked = true
                paymentMode = 1
            }*/


        btn_pay_now.setOnClickListener {
            if (Waleed.getInstance().getAppPref().getBoolean(AppConstants.PREF_GUEST_USER, false)) {
                presenter.makeGuestCheckOut(
                    deliveryTypeId = selectedDeliveryType,
                    totalAmount = grandTotal.toString(),
                    subTotal = subTotal,
                    paymentMode = paymentMode,
                    deliveryTimeId = selectedDeliverTimeId,
                    addressId = selectedAddress!!.customerAddressID,
                    selectedDate = selectedDate,
                    couponCode = couponValue,
                    wrappingCharge = wrapAmount.toString(),
                    couponDiscount = discountAmount.toString()
                )
            } else {
                if (LoggedUser.getInstance().getAccount()!!.mobile.isNullOrEmpty()) {
                    showMobileDialogue()
                } else {
                    if (isAddressNeed) {
                        if (selectedAddress == null) {
                            showPop(getString(R.string.please_select_your_address))
                        } else {
                            presenter.makeCheckOut(
                                deliveryTypeId = selectedDeliveryType,
                                totalAmount = grandTotal.toString(),
                                subTotal = subTotal,
                                paymentMode = paymentMode,
                                deliveryTimeId = selectedDeliverTimeId,
                                addressId = selectedAddress!!.customerAddressID,
                                selectedDate = selectedDate,
                                couponCode = couponValue,
                                wrappingCharge = wrapAmount.toString(),
                                couponDiscount = discountAmount.toString()
                            )
                        }
                    } else {
                        presenter.makeCheckOut(
                            deliveryTypeId = selectedDeliveryType,
                            totalAmount = grandTotal.toString(),
                            subTotal = subTotal,
                            paymentMode = paymentMode,
                            deliveryTimeId = selectedDeliverTimeId,
                            addressId = 0,
                            selectedDate = selectedDate,
                            couponCode = couponValue,
                            wrappingCharge = wrapAmount.toString(),
                            couponDiscount = discountAmount.toString()
                        )
                    }
                }
            }
        }

        tv_address_change.setOnClickListener {
            var bundle = Bundle()
            ActivitiesManager.startActivityForResult(
                this, AddressListActivity::class.java,
                bundle, 707
            )
        }

        rl_coupon.setOnClickListener {
            showCouponDialogue()
        }

    }

    override fun showDeliverType(list: ArrayList<DeliveryTypeResponse.DeliveryType>) {
        deliveryTypeAdapter = DeliveryTypeListAdapter(
            list, this
        )
        rvDeliverType.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        rvDeliverType.adapter = deliveryTypeAdapter

        deliveryTypeAdapter.onItemClick =
            { deliveryType: DeliveryTypeResponse.DeliveryType, typeId: Int ->
                selectedDeliveryType = deliveryType.deliveryTypeID

                rl_delivery.makeVisible()
                tv_delivery_amount_value.setLanguageString(
                    deliveryType.deliveryPrice,
                    deliveryType.descriptionAr
                )
                deliveryAmount = deliveryType.deliveryPrice.getAmount()
                grandTotal = total.getAmount() + deliveryAmount + wrapAmount - discountAmount

                tv_grand_price.text = getString(
                    R.string.unit_price_string, String.format("%.3f", grandTotal)
                )

                if (discountAmount > 0) {
                    rl_discount.makeVisible()
                    tv_discount_amount.text = getString(
                        R.string.unit_price_string, String.format("%.3f", discountAmount)
                    )
                } else {
                    rl_discount.makeVisible()
                    tv_discount_amount.text = getString(
                        R.string.unit_price_string, String.format("%.3f", discountAmount)
                    )

                }
                if (deliveryTimeList.isNullOrEmpty()) {
                    presenter.getDeliveryTime()
                }
                if (selectedDeliveryType == 3) {
                    rl_custom_time.makeVisible()
                    rl_custom_date.makeVisible()
                    openCalender()
                    tvDeliveryDate.setOnClickListener {
                        openCalender()
                    }
//                    if (deliveryTimeList.isNullOrEmpty()) {
//                        presenter.getDeliveryTime()
//                    }
                } else {
//                    rl_custom_time.makeGone()
                    rl_custom_date.makeGone()
                }
            }
    }

    override fun showDeliverTimes(list: ArrayList<DeliveryTimeListResponse.DeliveryTime>) {
        deliveryTimeList = list
        var spinnerDeliveryAdapter = DeliveryTimeSpinnerAdapter(
            this, deliveryTimeList
        )
        spinnerDeliveryTime.adapter = spinnerDeliveryAdapter
        spinnerDeliveryTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedDeliverTimeId = deliveryTimeList[position].delTimeID
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onCheckOutSuccess(checkOutResponse: CheckOutResponse) {
        checkOutData = checkOutResponse
        //Check for Cash On Delivery
        if (checkOutResponse.paymentMode == 1) {
            Waleed.appContext.getAppPref().removeSharedPref(AppConstants.PREF_SAVED_CART_ITEM)
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.BUNDLE_PAYMENT_DETAILS, checkOutResponse)
            ActivitiesManager.goTOAnotherActivityWithBundleAndFinish(
                this, PaymentSuccess::class.java,
                bundle
            )
        } else {
            var bundle = Bundle()
            bundle.putSerializable(AppConstants.BUNDLE_PAYMENT_DETAILS, checkOutResponse)
            ActivitiesManager.startActivityForResult(
                this@CheckoutActivity, PaymentActivity::class.java,
                bundle, PAYMENT_REQUEST
            )
        }
    }

    @SuppressLint("StringFormatInvalid")
    override fun applyDisCountValue(it: DiscountCouponResponse?) {
        discountAmount = it!!.couponDiscount.getAmount()
        showPop(getString(R.string.discount_applied, discountAmount.toString()))
        grandTotal = total.getAmount() + wrapAmount - discountAmount + deliveryAmount
        tv_grand_price.text = getString(
            R.string.unit_price_string, String.format("%.3f", grandTotal)
        )
        tv_apply_coupon.text = getString(R.string.coupon_applied_string)
//        if (discountAmount > 0) {
        rl_discount.makeVisible()
        tv_discount_amount.text = getString(
            R.string.unit_price_string, String.format("%.3f", discountAmount)
        )
//        } else {
//            rl_discount.makeGone()
//        }
        couponValue = couponTemp
    }

    override fun onMobleUpdateSuccess() {
        Dialogs.alertDialog(
            this, getString(R.string.updated_profile_message), getString(R.string.oke_text), null
        )
    }

    override fun showPaymentMethods(methodList: List<PaymentMethodsResponse.PaymentMethod>) {

        rvPaymentMethods.layoutManager = LinearLayoutManager(this)
        paymentTypeAdapter =
            PaymentListAdapter(methodList as ArrayList<PaymentMethodsResponse.PaymentMethod>)
        rvPaymentMethods.adapter = paymentTypeAdapter
        paymentMode=methodList[0].paymentMethodID
        paymentTypeAdapter.onItemClick =
            { paymentMethod: PaymentMethodsResponse.PaymentMethod, i: Int ->
                paymentMode = paymentMethod.paymentMethodID
            }
    }

    override fun showEmptyList() {

    }

    override fun showAddressList(addressItemList: java.util.ArrayList<AddressListResponse.AddressItem>) {
        selectedAddress = addressItemList[0]
        rl_address.makeVisible()
        btn_select_address.makeGone()
        tv_address_title.text = selectedAddress!!.title
        tv_address_name.text =
            getString(R.string.building_no_string) + " " + selectedAddress!!.building + ", " +
                    getString(R.string.block_string) + " " + selectedAddress!!.block + ", " +
                    getString(R.string.Street_string) + " " + selectedAddress!!.street + ", " +
                    getString(R.string.floor_no_string) + " " + selectedAddress!!.floor + ", " +
                    getString(R.string.flat_no_string) + " " + selectedAddress!!.flatNo + ", " +
                    LangUtils.getLanguageString(
                        selectedAddress!!.areaCity,
                        selectedAddress!!.areaCityAr
                    ) + ", " +
                    LangUtils.getLanguageString(
                        selectedAddress!!.govState,
                        selectedAddress!!.govStateAr
                    )
        tv_address_direction.text =
            getString(R.string.direction_string) + " " + selectedAddress!!.direction
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                707 -> {
                    var bundle = data!!.extras
                    selectedAddress = bundle!!.getSerializable(AppConstants.BUNDLE_SELECTED_ADDRESS)
                            as AddressListResponse.AddressItem
                    rl_address.makeVisible()
                    btn_select_address.makeGone()
                    tv_address_title.text = selectedAddress!!.title
                    tv_address_name.text =
                        getString(R.string.building_no_string) + " " + selectedAddress!!.building + ", " +
                                getString(R.string.block_string) + " " + selectedAddress!!.block + ", " +
                                getString(R.string.Street_string) + " " + selectedAddress!!.street + ", " +
                                getString(R.string.floor_no_string) + " " + selectedAddress!!.floor + ", " +
                                getString(R.string.flat_no_string) + " " + selectedAddress!!.flatNo + ", " +
                                LangUtils.getLanguageString(
                                    selectedAddress!!.areaCity,
                                    selectedAddress!!.areaCityAr
                                ) + ", " +
                                LangUtils.getLanguageString(
                                    selectedAddress!!.govState,
                                    selectedAddress!!.govStateAr
                                )
                    tv_address_direction.text =
                        getString(R.string.direction_string) + " " + selectedAddress!!.direction
                }
                PAYMENT_REQUEST -> {
                    if (data!!.getBooleanExtra(AppConstants.BUNDLE_PAYMENT_STATUS, false)) {
                        var transactionId =
                            data!!.getStringExtra(AppConstants.BUNDLE_TRANSACTION_ID)
                        checkOutData.transactionID = transactionId!!
                        checkOutData.status = 1
                        Waleed.appContext.getAppPref()
                            .removeSharedPref(AppConstants.PREF_SAVED_CART_ITEM)
                        var bundle = Bundle()
                        bundle.putSerializable(AppConstants.BUNDLE_PAYMENT_DETAILS, checkOutData)
                        ActivitiesManager.goTOAnotherActivityWithBundleAndFinish(
                            this, PaymentSuccess::class.java, bundle
                        )
                    } else {
                        showPop(getString(R.string.payment_failed_string))
                    }
                }
            }
        }
    }

    private fun openCalender() {
        var myCalendar = Calendar.getInstance()
        myCalendar.add(Calendar.DATE, 1)
        val date =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val myFormat = "dd/MM/yyyy" //In which you need put here

                val sdf = SimpleDateFormat(myFormat, Locale.US)
                selectedDate = sdf.format(myCalendar.getTime())
                tvDeliveryDate.text = selectedDate
                Log.e("Selected date", selectedDate)
            }
        var datePicker =
            DatePickerDialog(
                this, date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
        datePicker.datePicker.minDate = myCalendar.timeInMillis
        datePicker.show()
    }

    private fun showMobileDialogue() {
        var dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.dimAmount = 0.4f
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_enter_mobile)

        dialog.btn_apply.setOnClickListener {
            var mobileNumber = dialog.et_mobile.getStringValue()
            if (mobileNumber.isNullOrEmpty()) {
                showPop(getString(R.string.enter_valid_phone_number))
            } else {
//                couponTemp = mobileNumber
                presenter.updateProfile(
                    LoggedUser.getInstance().getAccount()!!.customerName, mobileNumber
                )
                dialog.dismiss()
            }
        }
        dialog.img_close_sheet.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showCouponDialogue() {
        var dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.dimAmount = 0.4f
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_apply_coupon)

        dialog.btn_apply.setOnClickListener {
            var couponCode = dialog.et_coupon.getStringValue()
            if (couponCode.isNullOrEmpty()) {
                showPop(getString(R.string.enter_valid_coupon_code))
            } else {
                couponTemp = couponCode
                presenter.applyDiscountCoupon(
                    cartListResponse.subTotal, couponCode
                )
                dialog.dismiss()
            }
        }
        dialog.img_close_sheet.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}