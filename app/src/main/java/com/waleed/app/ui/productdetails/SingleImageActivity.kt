package com.waleed.app.ui.productdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.waleed.app.R
import com.waleed.app.util.AppConstants.BUNDLE_ZOOM_URL
import kotlinx.android.synthetic.main.activity_single_image.*

class SingleImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_image)
        getIntentData()
    }

    private fun getIntentData() {
        if (intent != null) {
            val url: String = intent.getStringExtra(BUNDLE_ZOOM_URL)!!
            if (url.isNotEmpty()) {
                var requestOptions = RequestOptions()
                requestOptions =
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_launcher_background)
                        .placeholder(R.drawable.ic_launcher_background)
                Glide.with(this)
                    .load(url)
                    .apply(requestOptions)
                    .into(iv_zoom)
            }

        }
    }
}