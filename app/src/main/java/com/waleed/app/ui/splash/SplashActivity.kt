package com.waleed.app.ui.splash

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.home.landing.HomeFragment
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.AppConstants
import com.waleed.app.util.AppConstants.PREF_IS_VIDEO_SHOWN
import com.waleed.app.util.LangUtils.LANG_AR
import com.waleed.app.util.LangUtils.LANG_EN
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.splash_layout.*
import javax.inject.Inject


class SplashActivity : BaseActivity(), SplashView, Player.EventListener {
    @Inject
    lateinit var presenter: SplashPresenter
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private lateinit var videoUrl: String
    private val android_id: String = java.util.UUID.randomUUID().toString()
    private lateinit var handler1:Handler
    private lateinit var handler2:Handler
    private lateinit var handler3:Handler
    private lateinit var mRunnable1: Runnable
    private lateinit var mRunnable2: Runnable
    private lateinit var mRunnable3: Runnable
    override fun onStart() {
        super.onStart()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        setContentView(R.layout.splash_layout)
        Handler().postDelayed({
            if (Waleed.getInstance().getAppPref().getBoolean(PREF_IS_VIDEO_SHOWN, false)) {
                setContentView(R.layout.splash_layout)
                videoView.makeVisible()
                imgViewCenter.makeGone()
                hideSystemUi()
                if (Util.SDK_INT >= 24) {
                    initializePlayer()
                }
            } else {
            }
        }, 1200)


        Log.e("SecureId>>>>", android_id)
//        startNextPage()
        if (Waleed.getInstance().getAppPref().getString(AppConstants.PREF_DEVICE_TOKEN) == null) {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(
                OnCompleteListener {
                    try {
                        // Get new Instance ID token
                        if (!Waleed.getInstance().getAppPref()
                                .getBoolean(PREF_IS_VIDEO_SHOWN, false)
                        ) {

                            val token = it.result?.token
                            Log.e("AppToken>>", token!!)
                            Waleed.getInstance().getAppPref()
                                .saveString(AppConstants.PREF_DEVICE_TOKEN, token.toString())
                            startNextPage()
                        }
                    } catch (e: Exception) {
                        if (!Waleed.getInstance().getAppPref()
                                .getBoolean(PREF_IS_VIDEO_SHOWN, false)
                        ) {

                            Waleed.getInstance().getAppPref()
                                .saveString(AppConstants.PREF_DEVICE_TOKEN, android_id)
                            startNextPage()
                        }
                    }
                }
            )
        } else {
            startNextPage()
        }
    }

    private fun startNextPage() {
        handler1= Handler()
        handler2=Handler()
        mRunnable1= Runnable {
            presenter.checkFirstOpen()
        }
        mRunnable2= Runnable {
            presenter.checkFirstOpen()
        }
        if (Waleed.getInstance().getAppPref().getBoolean(PREF_IS_VIDEO_SHOWN, false)) {
            handler1.postDelayed(mRunnable1,19600)
        } else {
            handler2.postDelayed(mRunnable2,1000)
        }
    }

    override fun showLanguageLayout() {
        imgViewCenter.makeVisible()
        videoView.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_out_splash)
        )
        videoView.makeGone()
        ll_lang.startAnimation(AnimationUtils.loadAnimation(this, R.anim.view_slide_up))
        ll_lang.makeVisible()
        btn_lang_eng.setOnClickListener {
            Waleed.getInstance().getAppPref().saveBoolean(AppConstants.PREF_KEY_IS_FIRST_OPEN, true)
            Waleed.getInstance().getAppPref().saveBoolean(AppConstants.PREF_IS_VIDEO_SHOWN, false)
            Waleed.getInstance().getAppPref()
                .saveString(AppConstants.PREF_KEY_SELECTED_LANG, LANG_EN)
            openHomePage()
        }
        btn_lang_ar.setOnClickListener {
            Waleed.getInstance().getAppPref().saveBoolean(AppConstants.PREF_KEY_IS_FIRST_OPEN, true)
            Waleed.getInstance().getAppPref().saveBoolean(AppConstants.PREF_IS_VIDEO_SHOWN, false)
            Waleed.getInstance().getAppPref()
                .saveString(AppConstants.PREF_KEY_SELECTED_LANG, LANG_AR)
            openHomePage()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        if (Waleed.getInstance().getAppPref().getBoolean(PREF_IS_VIDEO_SHOWN, true)) {
            if (Util.SDK_INT < 24) {
                releasePlayer()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        handler1.removeCallbacks(mRunnable1)
        handler2.removeCallbacks(mRunnable2)
        if (Waleed.getInstance().getAppPref().getBoolean(PREF_IS_VIDEO_SHOWN, true)) {

            if (Util.SDK_INT >= 24) {
                releasePlayer()
            }
        }
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this).build()
        videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        videoView.player = player
        player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        val videoPath: String =
            RawResourceDataSource.buildRawResourceUri(R.raw.splash_video).toString()
        val uri = RawResourceDataSource.buildRawResourceUri(R.raw.splash_video)

        val mediaSource = buildMediaSource(uri)
        player?.playWhenReady = true
        val loopingSource = LoopingMediaSource(mediaSource, 1)
        videoView?.setShutterBackgroundColor(Color.WHITE)
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare(loopingSource, false, false)
        player?.addListener(this)
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        videoView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    override fun openHomePage() {
        handler1.removeCallbacks(mRunnable1)
        handler2.removeCallbacks(mRunnable2)
        ActivitiesManager.startCommonActivity(this, HomeFragment::class.java.name, null)
        finish()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)

        if (playbackState == ExoPlayer.STATE_ENDED) {
//            startNextPage()
        }
    }

}