package com.example.kaiyanproject.util

import android.app.Activity
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.ui.common.ui.BaseActivity
import com.example.kaiyanproject.ui.common.ui.BaseFragment
import com.example.kaiyanproject.ui.mine.MineFragment
import retrofit2.http.Url
import java.net.URLDecoder

object ActionUrlUtil {
    /**
     * 处理ActionUrl事件。
     *
     * @param fragment 上下文环境
     * @param actionUrl 待处理的url
     * @param toastTitle toast提示标题 or 没有匹配的事件需要处理，给出的提示标题。
     */
    fun process(fragment: BaseFragment, actionUrl: String?, toastTitle: String = "") {
        process(fragment.activity, actionUrl, toastTitle)
    }

    fun process(activity: Activity, actionUrl: String?, toastTitle: String = "") {
        if (actionUrl == null) return
        val decodeUrl = URLDecoder.decode(actionUrl, "UTF-8")
        when {
            decodeUrl.startsWith(Const.ActionUrl.WEBVIEW) -> {

            }
        }
    }

}