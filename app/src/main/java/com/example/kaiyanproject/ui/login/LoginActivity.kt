package com.example.kaiyanproject.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.R
import com.example.kaiyanproject.databinding.ActivityLoginBinding
import com.example.kaiyanproject.databinding.ActivityMainBinding
import com.example.kaiyanproject.databinding.ActivitySplashBinding
import com.example.kaiyanproject.event.MessageEvent
import com.example.kaiyanproject.extension.dp2px
import com.example.kaiyanproject.extension.gone
import com.example.kaiyanproject.extension.resDimensionPixelOffset
import com.example.kaiyanproject.extension.resDimensionPixelSize
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.setOnClickListener
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.extension.visible
import com.example.kaiyanproject.logic.model.DrawableDirection
import com.example.kaiyanproject.logic.model.setDrawable
import com.example.kaiyanproject.ui.common.ui.BaseActivity
import com.example.kaiyanproject.ui.common.ui.WebViewActivity
import com.zhpan.bannerview.transform.toDp

class LoginActivity : BaseActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding: ActivityLoginBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarBackground(R.color.black)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun setupViews() {
        super.setupViews()
        initTitleBar()
        initListener()
    }

    private fun initTitleBar() {
        binding.titleBar.titleBar.layoutParams.height =
            R.dimen.actionBarSizeSecondary.resDimensionPixelSize
        binding.titleBar.titleBar.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.transparent
            )
        )
        val padding = 9f.dp2px
        binding.titleBar.ivNavigateBefore.setPadding(padding, padding, padding, padding)
        binding.titleBar.ivNavigateBefore.setImageResource(R.drawable.ic_close_white_24dp)
        binding.titleBar.tvRightText.visible()
        binding.titleBar.tvRightText.text = R.string.forgot_password.resString
        binding.titleBar.tvRightText.setTextColor(
            ContextCompat.getColor(
                this@LoginActivity,
                R.color.white
            )
        )
        binding.titleBar.tvRightText.textSize = 12f
        binding.titleBar.divider.gone()
        binding.etPhoneNumberOrEmail.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_person_white_18dp
            ), 18f, 18f, DrawableDirection.LEFT
        )

        binding.etPassWord.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_password_white_lock_18dp
            ), 18f, 18f, DrawableDirection.LEFT
        )
    }

    private fun initListener() {
        setOnClickListener(
            binding.titleBar.tvRightText,
            binding.tvUserLogin,
            binding.tvUserRegister,
            binding.tvAuthorLogin,
            binding.tvUserAgreement,
            binding.tvUserLogin,
            binding.ivWechat,
            binding.ivSina,
            binding.ivQQ
        ) {
            when (this) {
                binding.titleBar.tvRightText -> {
                    WebViewActivity.start(
                        this@LoginActivity,
                        WebViewActivity.DEFAULT_TITLE,
                        Const.Url.FORGET_PASSWORD,
                        false,
                        false
                    )
                }

                binding.tvUserRegister -> {
                    WebViewActivity.start(
                        this@LoginActivity,
                        WebViewActivity.DEFAULT_TITLE,
                        Const.Url.AUTHOR_REGISTER,
                        false,
                        false
                    )
                }

                binding.tvAuthorLogin -> {
                    WebViewActivity.start(
                        this@LoginActivity,
                        WebViewActivity.DEFAULT_TITLE,
                        Const.Url.AUTHOR_LOGIN,
                        false,
                        false
                    )
                }

                binding.tvUserAgreement -> {
                    WebViewActivity.start(
                        this@LoginActivity,
                        WebViewActivity.DEFAULT_TITLE,
                        Const.Url.USER_AGREEMENT,
                        false,
                        false
                    )
                }

                binding.tvUserLogin, binding.ivWechat, binding.ivSina, binding.ivQQ -> {
                    R.string.currently_not_supported.showToast()
                }
            }
        }
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}