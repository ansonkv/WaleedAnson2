package com.waleed.app.ui.home


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.cart.CartActivity
import com.waleed.app.ui.home.category.CategoryFragment
import com.waleed.app.ui.home.landing.HomeFragment
import com.waleed.app.ui.home.landing.IdealCategoryClickListener
import com.waleed.app.ui.home.profile.ProfileFragment
import com.waleed.app.ui.orders.list.OrdersListActivity
import com.waleed.app.ui.search.SearchActivity
import com.waleed.app.ui.splash.SplashActivity
import com.waleed.app.util.*
import com.waleed.app.util.AppConstants.ACTION_BROADCAST_CART_COUNT
import com.waleed.app.util.AppConstants.PREF_KEY_CART_COUNT
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.lay_out_choose_lang.*
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject


@SuppressLint("Registered")
class HomeActivity : BaseActivity(), FragmentClickListener,
    View.OnClickListener, HomeView, CartUpdateListener, IdealCategoryClickListener {

    @Inject
    lateinit var presenter: HomePresenter

    private var previousPage = 0
    private var currentFragment: Fragment? = null
    private var currentPage = 0
    private lateinit var searchToolbar: Toolbar
    lateinit var search_menu: Menu
    lateinit var item_search: MenuItem
    var isSearchOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        if (Waleed.getInstance().getAppPref()
                .getString(AppConstants.PREF_DEVICE_TOKEN) == null
        ) {
            showLoading()
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(
                OnCompleteListener {
                    try {
                        // Get new Instance ID token
                        if (!Waleed.getInstance().getAppPref()
                                .getBoolean(AppConstants.PREF_IS_VIDEO_SHOWN, true)
                        ) {

                            val token = it.result?.token
                            Log.e("AppToken>>", token!!)
                            Waleed.getInstance().getAppPref()
                                .saveString(AppConstants.PREF_DEVICE_TOKEN, token.toString())
                            hideLoading()
                            presenter.getCartCount()
                        }
                    } catch (e: Exception) {
                        if (!Waleed.getInstance().getAppPref()
                                .getBoolean(AppConstants.PREF_IS_VIDEO_SHOWN, true)
                        ) {
                            val android_id: String = java.util.UUID.randomUUID().toString()
                            Waleed.getInstance().getAppPref()
                                .saveString(AppConstants.PREF_DEVICE_TOKEN, android_id)
                            hideLoading()
                            presenter.getCartCount()
                        }
                    }
                }
            )
        } else
            presenter.getCartCount()
        if (intent.getBooleanExtra(AppConstants.KEY_ENABLE_TOOLBAR, false)) {

//            initToolBar(rl_toolbar, intent.getStringExtra(AppConstants.KEY_SCREEN_NAME), true,
//                intent.getBooleanExtra(AppConstants.KEY_IS_CLOSE, false))

        } else {

//            rl_toolbar.visibility= View.GONE

        }


        instantiateFragment()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                listenerCartCount, IntentFilter(ACTION_BROADCAST_CART_COUNT)
            )
        ll_bottom_profile.setOnClickListener(this)
        ll_bottom_home.setOnClickListener(this)
        ll_bottom_category.setOnClickListener(this)
        btn_home.setOnClickListener(this)
        btn_categories.setOnClickListener(this)
        btn_profile.setOnClickListener(this)
        btn_cart.setOnClickListener(this)
        btn_search.setOnClickListener(this)
        btn_language.setOnClickListener(this)

    }

    private val listenerCartCount = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                ACTION_BROADCAST_CART_COUNT -> {
                    val cartCount = Waleed.appContext.getAppPref()
                        .getInteger(PREF_KEY_CART_COUNT)
                    if (cartCount!! >= 1) {
                        tv_cart_count_toolbar.makeVisible()
                        tv_cart_count_toolbar.text = cartCount.toString()
                    } else
                        tv_cart_count_toolbar.makeGone()
                }
            }
        }
    }

    private fun instantiateFragment() {

        if (intent != null) {

            val tag = intent.getStringExtra(FRAGMENT_NAME_TAG)

            if (supportFragmentManager.findFragmentByTag(tag) == null) {

                intent.removeExtra(tag)

                currentFragment = Fragment.instantiate(this, tag!!, intent.extras)

                supportFragmentManager.beginTransaction().replace(
                    R.id.common_frag_container,
                    currentFragment!!, tag
                ).commit()

            }
            if (intent.getBooleanExtra(OPEN_MY_ORDER, false)) {
                ActivitiesManager.goTOAnotherActivityWithBundle(
                    this, OrdersListActivity::class.java, Bundle()
                )
            }
        }
    }

    private fun openFragmentPage(fragmentName: String) {

        if (supportFragmentManager.findFragmentByTag(fragmentName) == null) {

            intent.removeExtra(fragmentName)

            currentFragment = Fragment.instantiate(this, fragmentName, intent.extras)

            var transaction = supportFragmentManager.beginTransaction()

            transaction.setCustomAnimations(R.anim.enter, R.anim.no_change)

            transaction.replace(
                R.id.common_frag_container,
                currentFragment!!, fragmentName
            ).commit()

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (currentFragment != null)
            currentFragment!!.onActivityResult(requestCode, resultCode, data)

    }


    companion object {
        val FRAGMENT_NAME_TAG = "FRAGMENT_NAME_TAG"
        val OPEN_MY_ORDER = "OPEN_MY_ORDER"
    }

    override fun selectBottomBar(pageId: Int) {
        when (pageId) {
            0 -> {
                ll_bottom_home.isSelected = true
                ll_bottom_category.isSelected = false
                ll_bottom_profile.isSelected = false
            }
            1 -> {
                ll_bottom_home.isSelected = false
                ll_bottom_category.isSelected = true
                ll_bottom_profile.isSelected = false
            }
            2 -> {
                ll_bottom_home.isSelected = false
                ll_bottom_category.isSelected = false
                ll_bottom_profile.isSelected = true
            }
            3 -> {
                ll_bottom_home.isSelected = false
                ll_bottom_category.isSelected = true
                ll_bottom_profile.isSelected = false
            }
        }
    }

    override fun onLanguageSelector() {

        ActivitiesManager.startCommonActivity(this, HomeFragment::class.java.name, null)
        finish()

//        val intent = Intent(this, SplashActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
////        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v) {
            ll_bottom_home -> {
                previousPage = currentPage
                currentPage = 0
                openFragmentPage(HomeFragment::class.java.name)
            }
            ll_bottom_category -> {
                previousPage = currentPage
                currentPage = 1
                openFragmentPage(CategoryFragment::class.java.name)
            }
            ll_bottom_profile -> {
                previousPage = currentPage
                currentPage = 2
                openFragmentPage(ProfileFragment::class.java.name)
            }
            btn_home -> {
                previousPage = currentPage
                currentPage = 0
                openFragmentPage(HomeFragment::class.java.name)
            }
            btn_categories -> {
                previousPage = currentPage
                currentPage = 1
                openFragmentPage(CategoryFragment::class.java.name)

            }
            btn_profile -> {
                previousPage = currentPage
                currentPage = 2
                openFragmentPage(ProfileFragment::class.java.name)
            }
            btn_cart -> {
                var bundle = Bundle()
                ActivitiesManager.goTOAnotherActivityWithBundle(
                    this, CartActivity::class.java, bundle
                )
            }
            btn_search -> {
                var bundle = Bundle()
                ActivitiesManager.goTOAnotherActivityWithBundle(
                    this, SearchActivity::class.java, bundle
                )
            }
            btn_language->{
                showLanguageOption()
            }
        }
    }

    override fun onCartItemAddSuccess(cartCount: Int) {
        Waleed.appContext.getAppPref().saveInteger(PREF_KEY_CART_COUNT, cartCount)
        if (cartCount > 0) {
            tv_cart_count_toolbar.makeVisible()
            tv_cart_count_toolbar.text = cartCount.toString()
        } else
            tv_cart_count_toolbar.makeGone()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun circleReveal(
        viewID: Int, posFromRight: Int, containsOverflow: Boolean, isShow: Boolean
    ) {
        val myView: View = findViewById(viewID)
        var width = myView.width
        if (posFromRight > 0) width -= posFromRight * resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - resources.getDimensionPixelSize(
            R.dimen.abc_action_button_min_width_material
        ) / 2
        if (containsOverflow) width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)
        val cx = width
        val cy = myView.height / 2
        val anim: Animator
        anim = if (isShow) ViewAnimationUtils.createCircularReveal(
            myView, cx, cy, 0f, width.toFloat()
        ) else ViewAnimationUtils.createCircularReveal(myView, cx, cy, width.toFloat(), 0f)
        anim.duration = 220.toLong()

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    super.onAnimationEnd(animation)
                    myView.visibility = View.INVISIBLE
                }
            }
        })

        // make the view visible and start the animation
        if (isShow) myView.visibility = View.VISIBLE

        // start the animation
        anim.start()
    }

    override fun onUpdateCartCount() {
        presenter.getCartCount()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(listenerCartCount)
        super.onDestroy()
    }

    override fun onLoadingStarted() {
        showLoading()
    }

    override fun onLoadingFinished() {
        hideLoading()
    }


    private fun showLanguageOption() {
        val dialogue = Dialogs.createCustomDialog(
            this,
            R.layout.lay_out_choose_lang,
            R.anim.slide_up,
            true,
            true,
            false
        )
        if (LangUtils.isCurrentLangArabic()) {
            dialogue.rb_arabic.isChecked = true
            dialogue.rb_eng.isChecked = false
        } else {
            dialogue.rb_arabic.isChecked = false
            dialogue.rb_eng.isChecked = true
        }
        dialogue.ll_arabic.setOnClickListener {
            dialogue.rb_arabic.isChecked = true
            dialogue.rb_eng.isChecked = false
        }
        dialogue.ll_english.setOnClickListener {
            dialogue.rb_arabic.isChecked = false
            dialogue.rb_eng.isChecked = true
        }
        dialogue.rb_eng.setOnClickListener {
            dialogue.rb_arabic.isChecked = false
            dialogue.rb_eng.isChecked = true
        }
        dialogue.rb_arabic.setOnClickListener {
            dialogue.rb_arabic.isChecked = true
            dialogue.rb_eng.isChecked = false
        }
        dialogue.btn_submit.setOnClickListener {
            Log.e("hnjn", "jhdkjh")
            if (dialogue.rb_arabic.isChecked) {
                Waleed.getInstance().getAppPref()
                    .saveString(AppConstants.PREF_KEY_SELECTED_LANG, LangUtils.LANG_AR)
            } else {
                Waleed.getInstance().getAppPref()
                    .saveString(AppConstants.PREF_KEY_SELECTED_LANG, LangUtils.LANG_EN)
            }

            dialogue.dismiss()
           onLanguageSelector()

        }
        dialogue.show()
    }
}
