package com.waleed.app.ui.cart

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.CartListDataResponse
import com.waleed.app.ui.account.register.RegisterActivity
import com.waleed.app.ui.account.signin.SignInActivity
import com.waleed.app.ui.address.list.AddressListActivity
import com.waleed.app.ui.address.newaddress.NewAddressActivity
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.cart.store.CartStoreActivity
import com.waleed.app.ui.checkout.CheckoutActivity
import com.waleed.app.ui.wrap.WrapActivity
import com.waleed.app.util.*
import com.waleed.app.util.AppConstants.BUNDLE_CART
import com.waleed.app.util.AppConstants.BUNDLE_CART_RESPONSE
import com.waleed.app.util.AppConstants.BUNDLE_SUB_TOTAL
import com.waleed.app.util.AppConstants.BUNDLE_TOTAL
import com.waleed.app.util.AppConstants.KEY_BUNDLE_IS_GUEST
import com.waleed.app.util.AppConstants.LOGIN_REQUEST_CODE
import com.waleed.app.util.AppConstants.PREF_GUEST_USER
import com.waleed.app.util.AppConstants.PREF_KEY_CART_COUNT
import com.waleed.app.util.AppConstants.REGISTER_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.cart_delivery_types_sheet.*
import kotlinx.android.synthetic.main.dialogue_checkout_message.img_close_dialogue
import kotlinx.android.synthetic.main.dialogue_checkout_message.tv_cart_message
import kotlinx.android.synthetic.main.dialogue_guest_message.*
import kotlinx.android.synthetic.main.dialogue_wrapper_message.*
import kotlinx.android.synthetic.main.toolbar_cart.*
import javax.inject.Inject

class CartActivity : BaseActivity(), CartView {
    @Inject
    lateinit var presenter: CartPresenter

    private lateinit var cartAdapter: CartAdapter
    private var selectedItemsList = arrayListOf<Int>()
    private var selectedCartItemList = arrayListOf<CartListDataResponse.CartItem>()
    private var selectedDeliveryPosition = -1
    private lateinit var cartListData: ArrayList<CartListDataResponse.CartItem>
    private var dataFromStorePage: Intent? = null
    private var isDataChanged: Boolean = false
    private lateinit var loginDialog: Dialog
    private lateinit var cartListDataResponse: CartListDataResponse
    private var isCArtEmpty = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        setContentView(R.layout.activity_cart)

        presenter.getCartListData()
        initViews()
    }

    private fun initViews() {
        btn_close.setOnClickListener {
            onBackPressed()
        }
        toolbar_wrap_selection.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(BUNDLE_CART, selectedCartItemList)
            ActivitiesManager.startActivityForResult(this, WrapActivity::class.java, bundle, 102)
        }
        img_close_sheet.setOnClickListener {
            layout_delivery_sheet.startAnimation(
                AnimationUtils.loadAnimation(
                    this, R.anim.bottom_sheet_anim_down
                )
            )
            layout_delivery_sheet.makeGone()
        }

        ll_home_delivery.setOnClickListener {
            rb_home_delivery.isChecked = true
            rb_store.isChecked = false
        }
        ll_store.setOnClickListener {
            if (rb_home_delivery.isChecked) {
                openStorePage()
            }
            rb_home_delivery.isChecked = false
            rb_store.isChecked = true
        }

        rb_store.setOnClickListener {
            if (rb_home_delivery.isChecked) {
                openStorePage()
            }
            rb_home_delivery.isChecked = false
            rb_store.isChecked = true
        }

        rb_home_delivery.setOnClickListener {
            rb_home_delivery.isChecked = true
            rb_store.isChecked = false
        }

        btn_delivery_done.setOnClickListener {
            if (rb_home_delivery.isChecked) {
                presenter.updateStoreCart(
                    cartID = cartListData[selectedDeliveryPosition].cartID,
                    storeID = 0
                )
            }
        }

        ll_checkout_btn.setOnClickListener {
            if (isCArtEmpty)
                finish()
            else {
                if (selectedCartItemList.isEmpty()) {

                    if (LoggedUser.getInstance().isUserLoggedIn() || Waleed.appContext.getAppPref()
                            .getBoolean(PREF_GUEST_USER, false)
                    ) {
                        openCheckOutPage()
                    } else {
                        showLoginDialogue()
                    }
                } else {
                    showWrapperDialogue()
                }
            }
        }
    }


    override fun showCartListData(cartListDataResponse: CartListDataResponse) {
        toolbar_wrap_selection.makeGone()
        rv_cart.makeVisible()
        this.cartListDataResponse = cartListDataResponse
        cartListData = cartListDataResponse.cartItems
        isCArtEmpty = false
        Waleed.appContext.getAppPref()
            .saveInteger(PREF_KEY_CART_COUNT, cartListDataResponse.cartItems.size)
        this.sendMyBroadCast(AppConstants.ACTION_BROADCAST_CART_COUNT)
        rv_cart.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        cartAdapter = CartAdapter(cartListData, this)
        rv_cart.adapter = cartAdapter
        calculateAmount(cartListDataResponse)
        cartAdapter.onUnitsUnavailable = {
            Log.e("sdf", "asdf")
            Dialogs.alertDialog(
                this, getString(R.string.units_available_message),
                getString(R.string.oke_text)
            ) { dialogInterface: DialogInterface, i: Int ->
                presenter.updateCartItemCount(
                    cartId = it.cartID, quantity = it.unitsAvailable,
                    combinationID = it.combinationID
                )
            }
        }
        cartAdapter.onItemOutOfStock = {
            Dialogs.alertDialog(
                this, getString(R.string.remove_item_from_cart_no_stock),
                getString(R.string.update_cart_string)
            ) { dialogInterface: DialogInterface, i: Int ->
                presenter.removeCartItem(
                    it.cartID,
                    it.productID,
                    it.combinationID
                )
            }
        }
        cartAdapter.onItemDecreaseClicked = {
            presenter.updateCartItemCount(
                cartId = it.cartID, quantity = it.selectedQuantity - 1,
                combinationID = it.combinationID
            )
        }
        cartAdapter.onItemIncreaseClicked = {
            if (it.selectedQuantity + 1 > it.unitsAvailable) {
                showPop(getString(R.string.units_available_message))
            } else
                presenter.updateCartItemCount(
                    cartId = it.cartID, quantity = it.selectedQuantity + 1,
                    combinationID = it.combinationID
                )
        }
        cartAdapter.onDeleteClicked = {
            openCartDeleteDialogue(it)
        }
        cartAdapter.onCheckClicked =
            { selectedPositions: ArrayList<Int>, cartItem: CartListDataResponse.CartItem ->
                if (selectedPositions.isNotEmpty()) {
                    toolbar_wrap_selection.makeVisible()
                    selectedItemsList.clear()
                    if (!selectedCartItemList.contains(cartItem)) {
                        selectedCartItemList.add(cartItem)
                    }
                    selectedItemsList.addAll(selectedPositions)
                } else {
                    toolbar_wrap_selection.makeGone()
                    selectedItemsList.clear()
                    selectedCartItemList.clear()
                }
            }
        cartAdapter.onMultipleStoreForWrapping = {
            showPop(getString(R.string.no_wraping_option))
        }
        cartAdapter.onDeliveryChangeClicked = {
            selectedDeliveryPosition = it
            handleDeliveryOption()
        }
        cartAdapter.onWrapperCancelClick = {
            presenter.wrapperRemove(it.cartID)
        }
    }

    private fun openCartDeleteDialogue(cartItem: CartListDataResponse.CartItem) {
        Dialogs.alertDialogWithTwoButton(
            this, getString(R.string.cart_delete_message),
            getString(R.string.yes_string),
            { dialogInterface: DialogInterface, i: Int ->
                presenter.removeCartItem(
                    cartItem.cartID,
                    cartItem.productID,
                    cartItem.combinationID
                )
            },
            getString(R.string.no_string),
            null
        ).show()
    }

    override fun removeCartFailed() {

    }

    override fun onBackPressed() {
        var intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
        super.onBackPressed()

    }

    override fun itemRemovedSuccessfully(productID: Int, combinationID: Int) {
        this.sendMyBroadCast(AppConstants.ACTION_BROADCAST_CART_COUNT)
    }

    override fun showEmptyList() {
        Waleed.appContext.getAppPref()
            .saveInteger(PREF_KEY_CART_COUNT, 0)
        this.sendMyBroadCast(AppConstants.ACTION_BROADCAST_CART_COUNT)
        rv_cart.makeGone()
        ll_empty_cart.makeVisible()
        tv_cart_count.makeGone()
        tv_cart_price.makeGone()
        tv_cart_btn.text = getString(R.string.continue_shopping)
        isCArtEmpty = true
    }

    override fun onStoreUpdatedSuccessfully() {
        layout_delivery_sheet.startAnimation(
            AnimationUtils.loadAnimation(
                this, R.anim.bottom_sheet_anim_down
            )
        )
        layout_delivery_sheet.makeGone()
        presenter.getCartListData()
    }

    override fun onWrapperRemove() {
        presenter.getCartListData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                101 -> {
                    isDataChanged = true
                    layout_delivery_sheet.startAnimation(
                        AnimationUtils.loadAnimation(
                            this, R.anim.bottom_sheet_anim_down
                        )
                    )
                    layout_delivery_sheet.makeGone()
                    presenter.getCartListData()
                }
                102 -> {
                    selectedCartItemList.clear()
                    selectedItemsList.clear()
                    presenter.getCartListData()
                    if (loginDialog.isShowing)
                        loginDialog.dismiss()
                }
                LOGIN_REQUEST_CODE -> {
                    loginDialog.dismiss()
                    openCheckOutPage()
                }
                REGISTER_REQUEST_CODE -> {
                    loginDialog.dismiss()
                    openCheckOutPage()
                }
                707 -> {
                    openCheckOUtForGuest()
                }
            }
        }
    }


    private fun calculateAmount(response: CartListDataResponse) {
        var totalAmount = 0.0
        var productQuantity = 0
        for (storeData in response.cartItems) {
            totalAmount += (storeData.unitPrice.getAmount() * storeData.selectedQuantity)
            productQuantity += storeData.selectedQuantity
        }
        tv_cart_count.text = productQuantity.toString()
        tv_cart_price.setLanguageString(
            response.totalKD, response.totalKDAr
        )
    }

    private fun handleDeliveryOption() {
        rb_home_delivery.isChecked = false
        rb_store.isChecked = false
        layout_delivery_sheet.startAnimation(
            AnimationUtils.loadAnimation(
                this, R.anim.bottom_sheet_anim_up
            )
        )
        if (cartListData[selectedDeliveryPosition].storeID == 0) {
            rb_store.isChecked = false
            rb_home_delivery.isChecked = true
        } else {
            rb_store.isChecked = true
            rb_home_delivery.isChecked = false
        }
        tv_change_shop.setOnClickListener {
            openStorePage()
        }
        layout_delivery_sheet.makeVisible()

    }

    private fun handleToolbarVisibility(selectedItems: ArrayList<Int>) {
        selectedItemsList = selectedItems
        if (selectedItems.isEmpty()) {
            toolbar_wrap_selection.makeGone()
        } else {
            toolbar_wrap_selection.makeVisible()
        }
    }

    private fun openStorePage() {
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.BUNDLE_KEY_SELECTED_STORE_DATA,
            cartListData[selectedDeliveryPosition]
        )
        ActivitiesManager.startActivityForResult(
            this, CartStoreActivity::class.java,
            bundle, 101
        )
    }

    private fun showLoginDialogue() {
        loginDialog = Dialog(this, 0)
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loginDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loginDialog.window!!.attributes.dimAmount = 0.4f

        loginDialog.setCancelable(true)

        loginDialog.setContentView(R.layout.dialogue_guest_message)
        loginDialog.tv_cart_message.text = getString(R.string.want_to_continue_as_string)
        loginDialog.img_close_dialogue.setOnClickListener {
            loginDialog.dismiss()
        }
        loginDialog.btn_login.setOnClickListener {
            ActivitiesManager.startActivityForResult(
                this, SignInActivity::class.java,
                null, LOGIN_REQUEST_CODE
            )
            loginDialog.dismiss()
        }
        loginDialog.btn_guest.setOnClickListener {
            var bundle = Bundle()
            bundle.putBoolean(AppConstants.KEY_BUNDLE_GUEST, true)
            ActivitiesManager.startActivityForResult(
                this, NewAddressActivity::class.java,
                bundle, 707
            )
            loginDialog.dismiss()
        }
        loginDialog.show()
    }

    private fun showWrapperDialogue() {
        loginDialog = Dialog(this, 0)
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loginDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loginDialog.window!!.attributes.dimAmount = 0.4f

        loginDialog.setCancelable(true)

        loginDialog.setContentView(R.layout.dialogue_wrapper_message)
        loginDialog.tv_wrap_message.text = getString(R.string.please_select_gift_wrapper)

        loginDialog.img_close_dialogue.setOnClickListener {
            loginDialog.dismiss()
        }

        loginDialog.btn_ok.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(BUNDLE_CART, selectedCartItemList)
            ActivitiesManager.startActivityForResult(this, WrapActivity::class.java, bundle, 102)
        }
        loginDialog.show()
    }

    private fun openCheckOutPage() {
        var bundle = Bundle()
        bundle.putString(BUNDLE_TOTAL, cartListDataResponse.total)
        bundle.putString(BUNDLE_SUB_TOTAL, cartListDataResponse.subTotal)
        bundle.putSerializable(BUNDLE_CART_RESPONSE, cartListDataResponse)
        ActivitiesManager.goTOAnotherActivityWithBundle(
            this, CheckoutActivity::class.java, bundle
        )
    }

    private fun openCheckOUtForGuest() {
        var bundle = Bundle()
        bundle.putBoolean(KEY_BUNDLE_IS_GUEST, true)
        bundle.putString(BUNDLE_TOTAL, cartListDataResponse.total)
        bundle.putString(BUNDLE_SUB_TOTAL, cartListDataResponse.subTotal)
        bundle.putSerializable(BUNDLE_CART_RESPONSE, cartListDataResponse)
        ActivitiesManager.goTOAnotherActivityWithBundle(
            this, CheckoutActivity::class.java, bundle
        )
    }
}