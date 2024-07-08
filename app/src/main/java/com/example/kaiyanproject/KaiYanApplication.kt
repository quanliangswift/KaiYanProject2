package com.example.kaiyanproject

import android.app.Application
import android.content.Context

class KaiYanApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: Context
    }
}