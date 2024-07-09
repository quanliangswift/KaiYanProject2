package com.example.kaiyanproject.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.R
import com.example.kaiyanproject.extension.dp2px
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.screenWidth
import com.umeng.analytics.MobclickAgent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class DialogAppraiseTipsWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        return if (isNeedShowDialog) {
            Result.retry()
        } else {
            Result.success()
        }
    }

    companion object {
        const val TAG = "DialogAppraiseTipsWorker"

        val showDialogWorkRequest = OneTimeWorkRequest.Builder(DialogAppraiseTipsWorker::class.java)
            .addTag(TAG)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.SECONDS)
            .build()

        var isNeedShowDialog: Boolean
            get() = DataStoreUtils.readBooleanData("is_need_show_dialog", true)
            set(value) {
                CoroutineScope(Dispatchers.IO).launch {
                    DataStoreUtils.saveBooleanData(
                        "is_need_show_dialog",
                        value
                    )
                }
            }

        private var dialog: AlertDialog? = null

        fun showDialog(context: Context) {
            if (!isNeedShowDialog) return

            val dialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_appraise_tips, null).apply {
                    findViewById<TextView>(R.id.tvEncourageMessage).text =
                        String.format(R.string.encourage_message.resString, GlobalUtil.appName)
                    findViewById<TextView>(R.id.tvPositiveButton).setOnClickListener {
                        MobclickAgent.onEvent(context, Const.Mobclick.EVENT8)
                        dialog?.dismiss()
                    }
                    findViewById<TextView>(R.id.tvNegativeButton).setOnClickListener {
                        MobclickAgent.onEvent(context, Const.Mobclick.EVENT9)
                        dialog?.dismiss()
                        TODO("todo")
//                    WebViewActivity.start(context, WebViewActivity.DEFAULT_TITLE, WebViewActivity.DEFAULT_URL, true, false, WebViewActivity.MODE_SONIC_WITH_OFFLINE_CACHE)
                    }
                }
            dialog =
                AlertDialog.Builder(context, R.style.KaiYanAlertDialogStyle).setCancelable(false)
                    .setView(
                        dialogView
                    ).create()
            dialog?.show()
            dialog?.window?.attributes = dialog?.window?.attributes?.apply {
                width = screenWidth - 20f.dp2px * 2
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            isNeedShowDialog = false
            MobclickAgent.onEvent(context, Const.Mobclick.EVENT7)
        }
    }
}