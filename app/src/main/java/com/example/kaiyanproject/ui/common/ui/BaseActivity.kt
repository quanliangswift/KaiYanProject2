package com.example.kaiyanproject.ui.common.ui

import android.app.Activity
import android.app.StatusBarManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import com.example.kaiyanproject.R
import com.example.kaiyanproject.databinding.ActivityMainBinding
import com.example.kaiyanproject.databinding.ActivitySplashBinding
import com.example.kaiyanproject.databinding.LayoutTitleBarBinding
import com.example.kaiyanproject.event.MessageEvent
import com.example.kaiyanproject.extension.logD
import com.example.kaiyanproject.util.ActivityCollector
import com.example.kaiyanproject.util.ShareUtil
import com.gyf.immersionbar.ImmersionBar
import com.umeng.analytics.MobclickAgent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

open class BaseActivity : AppCompatActivity() {
    protected var isActive: Boolean = false

    protected var activity: Activity? = null

    private var activityWR: WeakReference<Activity>? = null

    protected val TAG: String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD(TAG, "BaseActivity --> OnCreate()")
        activity = this
        activityWR = WeakReference(activity!!)
        ActivityCollector.pushTask(activityWR)

        EventBus.getDefault().register(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logD(TAG, "BaseActivity-->onSaveInstanceState()")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logD(TAG, "BaseActivity-->onRestoreInstanceState()")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        logD(TAG, "BaseActivity-->onNewIntent()")
    }

    override fun onRestart() {
        super.onRestart()
        logD(TAG, "BaseActivity-->onRestart()")
    }

    override fun onStart() {
        super.onStart()
        logD(TAG, "BaseActivity-->onStart()")
    }

    override fun onResume() {
        super.onResume()
        logD(TAG, "BaseActivity-->onResume()")
        isActive = true
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        logD(TAG, "BaseActivity-->onPause()")
        isActive = false
        MobclickAgent.onPause(this)
    }

    override fun onStop() {
        super.onStop()
        logD(TAG, "BaseActivity-->onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        logD(TAG, "BaseActivity-->onDestroy()")
        activity = null
        ActivityCollector.removeTask(activityWR)
        EventBus.getDefault().unregister(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setStatusBarBackground(R.color.colorPrimaryDark)
//        set
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        setupViews()
    }

    open fun setupViews() {
        val navigateBefore = findViewById<ImageView>(R.id.ivNavigateBefore)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        navigateBefore?.setOnClickListener { finish() }
        tvTitle?.isSelected = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {

    }

    fun setStatusBarBackground(@ColorRes statusBarColor: Int) {
        ImmersionBar.with(this).autoStatusBarDarkModeEnable(true, 0.2f)
            .statusBarColor(statusBarColor).fitsSystemWindows(true).init()
    }

    protected fun share(shardContent: String, shareType: Int) {
        ShareUtil.share(this, shardContent, shareType)
    }

    protected fun showDialogShare(shareContent: String) {
        com.example.kaiyanproject.extension.showDialogShare(this, shareContent)
    }
}