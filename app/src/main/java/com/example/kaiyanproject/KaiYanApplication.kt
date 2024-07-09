package com.example.kaiyanproject

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.work.WorkManager
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.ui.SplashActivity
import com.example.kaiyanproject.ui.view.NoStatusFooter
import com.example.kaiyanproject.util.DialogAppraiseTipsWorker
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class KaiYanApplication : Application() {
    init {
        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            layout.setEnableLoadMore(true)
            layout.setEnableLoadMoreWhenContentNotFull(true)
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setEnableHeaderTranslationContent(true)
            MaterialHeader(context).setColorSchemeResources(
                R.color.blue,
                R.color.blue,
                R.color.blue
            )
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setEnableFooterFollowWhenNoMoreData(true)
            layout.setEnableFooterTranslationContent(true)
            layout.setFooterHeight(153f)
            layout.setFooterTriggerRate(0.6f)
            NoStatusFooter.REFRESH_FOOTER_NOTHING = R.string.footer_not_more.resString
            NoStatusFooter(context).apply {
                setAccentColorId(R.color.colorTextPrimary)
                setTextTitleSize(16f)
            }
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        if (!SplashActivity.isFirstEntryApp && DialogAppraiseTipsWorker.isNeedShowDialog) {
            WorkManager.getInstance(this).enqueue(DialogAppraiseTipsWorker.showDialogWorkRequest)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}