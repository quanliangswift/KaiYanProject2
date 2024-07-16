package com.example.kaiyanproject.ui.login

import android.content.Context
import android.content.Intent
import com.example.kaiyanproject.ui.common.ui.BaseActivity

class LoginActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}