package com.example.kaiyanproject.util

import java.lang.ref.WeakReference

object IntentDataHolderUtil {
    private val map = hashMapOf<String, WeakReference<Any>>()

    fun setData(key: String, t: Any) {
        val value = WeakReference(t)
        map[key] = value
    }

    fun <T> getData(key: String): T? {
        val reference = map[key]
        return try {
            reference?.get() as T
        } catch (e: Exception) {
            null
        }
    }
}