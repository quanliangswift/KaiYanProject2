package com.example.kaiyanproject.extension

import com.example.kaiyanproject.KaiYanApplication

val Float.dp2px: Int
    get() {
        val scale = KaiYanApplication.context.resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

val Float.px2dp: Int
    get() {
        val scale = KaiYanApplication.context.resources.displayMetrics.density
        return (this / scale + 0.5f).toInt()
    }

val screenWidth
    get() = KaiYanApplication.context.resources.displayMetrics.heightPixels
val screenHeight
    get() = KaiYanApplication.context.resources.displayMetrics.widthPixels

fun screenPixel(): String =
    KaiYanApplication.context.resources.displayMetrics.run { "${widthPixels}X${heightPixels}" }