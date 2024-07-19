package com.example.kaiyanproject.extension

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import com.example.kaiyanproject.KaiYanApplication
import com.example.kaiyanproject.util.GlobalUtil
import java.util.prefs.Preferences

val dataStore: DataStore<androidx.datastore.preferences.core.Preferences> =
    KaiYanApplication.context.dataStore
val sharedPreferences: SharedPreferences = KaiYanApplication.context.getSharedPreferences(
    GlobalUtil.appPackage + "_preferences",
    Context.MODE_PRIVATE
)

fun showDialogShare(activity: Activity, shareContent: String) {
    if (activity is AppCompatActivity) {
//        ShareDia
    }
}

/**
 * 获取资源文件中定义的字符串。
 *
 * @param resId
 * 字符串资源id
 * @return 字符串资源id对应的字符串内容。
 */
val Int.resString
    get() = KaiYanApplication.context.resources.getString(this)

val Int.resDimensionPixelOffset
    get() = KaiYanApplication.context.resources.getDimensionPixelOffset(this)
val Int.resDimensionPixelSize
    get() = KaiYanApplication.context.resources.getDimensionPixelSize(this)

fun setOnClickListener(vararg v: View?, block: View.() -> Unit) {
    val listener = View.OnClickListener { it.block() }
    v.forEach { it?.setOnClickListener(listener) }
}