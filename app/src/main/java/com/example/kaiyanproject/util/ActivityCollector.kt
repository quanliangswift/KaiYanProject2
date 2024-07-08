package com.example.kaiyanproject.util

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.Stack

object ActivityCollector {
    private val activitys = Stack<WeakReference<Activity>>()

    fun pushTask(task: WeakReference<Activity>?) {
        activitys.push(task)
    }

    fun removeTask(task: WeakReference<Activity>?) {
        activitys.remove(task)
    }

    fun removeTask(taskIndex: Int) {
        activitys.removeAt(taskIndex)
    }

    fun removeToTop() {
        val lastIndex = activitys.lastIndex
        val start = 1
        for (i in lastIndex downTo start) {
            val mActivity = activitys[i].get()
            mActivity.takeIf { it != null && !it.isFinishing }?.finish()
        }
    }

    fun removeAll() {
        for (task in activitys) {
            val mActivity = task.get()
            mActivity.takeIf { it != null && !it.isFinishing }?.finish()
        }
    }
}