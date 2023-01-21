package com.waleed.app.ui.account.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.ui.account.forgotpassword.ForgotPasswordActivity
import com.waleed.app.ui.account.register.RegisterActivity
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.AppConstants
import com.waleed.app.util.AppConstants.REGISTER_REQUEST_CODE
import com.waleed.app.util.makeGone
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.et_email
import kotlinx.android.synthetic.main.activity_sign_in.et_password
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject

class SignInActivity : BaseActivity(), SignInView {

    @Inject
    lateinit var presenter: SignInPresenter
    private var mExtras = Bundle()
    private lateinit var callbackManager: CallbackManager

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions
    private var RC_SIGN_IN = 333
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_sign_in)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        initView()
    }

    private fun initView() {
        btn_cart.makeGone()
        btn_language.makeGone()
        btn_login.setOnClickListener {
            if (validateData()) {
                presenter.loginUser(
                    et_email.text.toString().trim(),
                    et_password.text.toString().trim()
                )
            }
        }

        tv_forgot_password.setOnClickListener {
            var bundle = Bundle()
            ActivitiesManager.goTOAnotherActivityWithBundle(
                this,
                ForgotPasswordActivity::class.java,
                bundle
            )
        }
        tv_register_now.setOnClickListener {
            var bundle = Bundle()
            bundle.putInt(AppConstants.BUNDLE_FROM_LOGIN_PAGE, 1)
            ActivitiesManager.startActivityForResult(
                this,
                RegisterActivity::class.java,
                bundle, REGISTER_REQUEST_CODE
            )
        }
        /**Facebook Login*/
        callbackManager = CallbackManager.Factory.create()
        fb_login_button.setPermissions("email")
        ll_facebook_login.setOnClickListener {
            fb_login_button.performClick()
        }
        fb_login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            private var mProfileTracker: ProfileTracker? = null

            override fun onSuccess(result: LoginResult?) {
                Log.e("Facebook", "jsfsf")
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = object : ProfileTracker() {
                        override fun onCurrentProfileChanged(
                            oldProfile: Profile?,
                            currentProfile: Profile
                        ) {
                            Log.v("facebook - profile", currentProfile.firstName)
                            presenter.socialLogin(currentProfile!!.id, currentProfile.name, 2)
                            mProfileTracker!!.stopTracking()
                        }
                    }
                } else {
                    val fbProfile = Profile.getCurrentProfile()
                    Log.e("name", fbProfile.name)
                    Log.e("id", fbProfile.id)
                    presenter.socialLogin(fbProfile!!.id, fbProfile.name, 2)
                }
            }

            override fun onCancel() {
                Log.e("Facebook", "onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.e("Facebook", "onError" + error.toString())
            }
        })
        ll_google_login.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun validateData(): Boolean {
        et_email.error = null
        et_password.error = null
        et_email.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        et_password.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        return when {
            et_email.text.toString().isEmpty() -> {
                et_email.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_email.error = getString(R.string.enter_valid_email_string)
                et_email.requestFocus()
                false
            }
            et_password.text.toString().isEmpty() -> {
                et_password.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_password.error = getString(R.string.enter_valid_password_string)
                et_password.requestFocus()
                false
            }
            else -> {
                true
            }
        }
    }

    override fun onSuccessLogin() {
        if (mExtras.getInt(AppConstants.BUNDLE_FROM_PAGE, 0) == 1) {
//            val bundle = Bundle()
//            ActivitiesManager.startHomeActivity(this, HomeFragment::class.java.name, bundle, false)
//            finish()
        } else {
            var intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onInvalidCredentials() {
        showPop(getString(R.string.invalid_credentials_string))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REGISTER_REQUEST_CODE) {
                var intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            }
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            presenter.socialLogin(account!!.email, account!!.displayName, 3)
            Log.e("gamil.", account!!.email!!)
            Log.e("displayname", account!!.displayName!!)
            Log.e("given name", account.givenName!!)
        } catch (e: ApiException) {
            Log.w(
                "Login Failed",
                "signInResult:failed code=" + e.statusCode
            )
            showPop("Login Failed!!")
        }
    }
}