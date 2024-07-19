package com.example.kaiyanproject.ui.common.ui

import android.content.Context
import android.content.Intent
import com.example.kaiyanproject.util.GlobalUtil

class WebViewActivity : BaseActivity() {
    companion object {

        private const val TITLE = "title"

        private const val LINK_URL = "link_url"

        private const val IS_SHARE = "is_share"

        private const val IS_TITLE_FIXED = "isTitleFixed"

        const val MODE_DEFAULT = 0

        const val MODE_SONIC = 1

        const val MODE_SONIC_WITH_OFFLINE_CACHE = 2

        const val PARAM_MODE = "param_mode"

        const val DEFAULT_URL = "https://github.com/VIPyinzhiwei/Eyepetizer"

        val DEFAULT_TITLE = GlobalUtil.appName

        /**
         * 跳转WebView网页界面
         *
         * @param context       上下文环境
         * @param title         标题
         * @param url           加载地址
         * @param isShare       是否显示分享按钮
         * @param isTitleFixed  是否固定显示标题，不会通过动态加载后的网页标题而改变。true：固定，false 反之。
         * @param mode          加载模式：MODE_DEFAULT 默认使用WebView加载；MODE_SONIC 使用VasSonic框架加载； MODE_SONIC_WITH_OFFLINE_CACHE 使用VasSonic框架离线加载
         */
        fun start(
            context: Context,
            title: String,
            url: String,
            isShare: Boolean = true,
            isTitleFixed: Boolean = true,
            mode: Int = MODE_SONIC
        ) {
//            url.preCreateSession()  //预加载url
//            val intent = Intent(context, WebViewActivity::class.java).apply {
//                putExtra(TITLE, title)
//                putExtra(LINK_URL, url)
//                putExtra(IS_SHARE, isShare)
//                putExtra(IS_TITLE_FIXED, isTitleFixed)
//                putExtra(PARAM_MODE, mode)
//                putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis())
//            }
//            context.startActivity(intent)
        }
    }
}