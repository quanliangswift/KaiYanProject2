package com.example.kaiyanproject.extension

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import com.example.kaiyanproject.KaiYanApplication
import java.util.prefs.Preferences

val dataStore: DataStore<Preferences> = KaiYanApplication.context.dataStore
val sharedPreferences: SharedPreferences = KaiYanApplication.context.getSharedPreferences(
    GlobalUtil.appPackage + "_preferences",
    Context.MODE_PRIVATE
)

fun showDialogShare(activity: Activity, shareContent: String) {
    if (activity is AppCompatActivity) {
//        ShareDia
    }
}


val Int.resString
    get() = KaiYanApplication.context.resources.getString(this)