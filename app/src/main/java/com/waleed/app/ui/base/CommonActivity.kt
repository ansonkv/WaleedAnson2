package com.waleed.app.ui.base

import androidx.fragment.app.Fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.waleed.app.R
import com.waleed.app.util.AppConstants


@SuppressLint("Registered")
class CommonActivity : BaseActivity() {


    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_common)


        if(intent.getBooleanExtra(AppConstants.KEY_ENABLE_TOOLBAR , false)){

//            initToolBar(rl_toolbar, intent.getStringExtra(AppConstants.KEY_SCREEN_NAME), true,
//                intent.getBooleanExtra(AppConstants.KEY_IS_CLOSE, false))

        }else{

//            rl_toolbar.visibility= View.GONE

        }


        instantiateFragment()

    }


    private fun instantiateFragment() {

        if (intent != null) {

            val tag = intent.getStringExtra(FRAGMENT_NAME_TAG)!!

            if (supportFragmentManager.findFragmentByTag(tag) == null) {

                intent.removeExtra(tag)

                currentFragment = Fragment.instantiate(this, tag, intent.extras)

                supportFragmentManager.beginTransaction().replace(R.id.common_frag_container,
                    currentFragment!!, tag).commit()

            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (currentFragment != null)
            currentFragment!!.onActivityResult(requestCode, resultCode, data)

    }


    companion object {
        val FRAGMENT_NAME_TAG = "FRAGMENT_NAME_TAG"
    }
}
