package com.example.kaiyanproject.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.lifecycle.lifecycleScope
import com.example.kaiyanproject.MainActivity
import com.example.kaiyanproject.R
import com.example.kaiyanproject.databinding.ActivityLoginBinding
import com.example.kaiyanproject.databinding.ActivitySplashBinding
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.ui.common.ui.BaseActivity
import com.example.kaiyanproject.util.DataStoreUtils
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {
    private var _binding: ActivitySplashBinding? = null
    private val binding: ActivitySplashBinding
        get() = _binding!!

    private val splashDuration = 3 * 1000L
    private val alphaAnimation by lazy {
        AlphaAnimation(0.5f, 1.0f).apply {
            duration = splashDuration
            fillAfter = true
        }
    }
    private val scaleAnimation by lazy {
        ScaleAnimation(
            1f,
            1.05f,
            1f,
            1.05f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = splashDuration
            fillAfter = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWriteExternalStoragePermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun setupViews() {
        super.setupViews()
        binding.ivSlogan.startAnimation(alphaAnimation)
        binding.ivSplashPicture.startAnimation(scaleAnimation)
        lifecycleScope.launch {
            delay(splashDuration)
            MainActivity.start(this@SplashActivity)
            finish()
        }
        isFirstEntryApp = false
    }

    private fun requestWriteExternalStoragePermission() {
        val permissions: List<String> =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf(
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO
                )
            } else {
                listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
//                    ,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        PermissionX.init(this@SplashActivity).permissions(permissions)
            .onExplainRequestReason { scope, deniedList ->
                val message = R.string.request_permission_picture_processing.resString
                scope.showRequestReasonDialog(
                    deniedList,
                    message,
                    R.string.ok.resString,
                    R.string.cancel.resString
                )
            }
            .onForwardToSettings { scope, deniedList ->
                var message = R.string.request_permission_picture_processing.resString
                scope.showForwardToSettingsDialog(
                    deniedList,
                    message,
                    R.string.settings.resString,
                    R.string.cancel.resString
                )
            }
            .request { allGranted, grantedList, deniedList ->
                requestReadPhoneStatePermission()
            }
    }

    private fun requestReadPhoneStatePermission() {
        PermissionX.init(this@SplashActivity).permissions(Manifest.permission.READ_PHONE_STATE)
            .onExplainRequestReason { scope, deniedList ->
                val message = R.string.request_permission_access_phone_info.resString
                scope.showRequestReasonDialog(
                    deniedList,
                    message,
                    R.string.ok.resString,
                    R.string.cancel.resString
                )
            }
            .onForwardToSettings { scope, deniedList ->
                val message = R.string.request_permission_access_phone_info.resString
                scope.showForwardToSettingsDialog(
                    deniedList,
                    message,
                    R.string.settings.resString,
                    R.string.cancel.resString
                )
            }
            .request { allGranted, grantedList, deniedList ->
                _binding = ActivitySplashBinding.inflate(layoutInflater)
                setContentView(binding.root)
            }
    }

    companion object {
        var isFirstEntryApp: Boolean
            get() = DataStoreUtils.readBooleanData("is_first_entry_app", true)
            set(value) {
                CoroutineScope(Dispatchers.IO).launch {
                    DataStoreUtils.saveBooleanData(
                        "is_first_entry_app",
                        value
                    )
                }
            }
    }
}