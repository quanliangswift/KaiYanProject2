package com.example.kaiyanproject.ui.newdetail

import android.app.Activity
import android.content.Intent
import android.gesture.GestureLibrary
import android.os.Build
import android.os.Parcelable
import com.example.kaiyanproject.R
import com.example.kaiyanproject.logic.model.Author
import com.example.kaiyanproject.logic.model.Consumption
import com.example.kaiyanproject.logic.model.Cover
import com.example.kaiyanproject.logic.model.WebUrl
import com.example.kaiyanproject.ui.common.ui.BaseActivity
import kotlinx.parcelize.Parcelize

class NewDetailActivity : BaseActivity() {

    @Parcelize
    data class VideoInfo(
        val videoId: Long,
        val playUrl: String,
        val title: String,
        val description: String,
        val category: String,
        val library: String,
        val consumption: Consumption,
        val cover: Cover, val author: Author?, val webUrl: WebUrl
    ) : Parcelable

    companion object {
        const val EXTRA_VIDEOINFO = "videoInfo"
        const val EXTRA_VIDEO_ID = "videoId"
        const val TAG = "NewDetailActivity"
        fun start(context: Activity, videoInfo: VideoInfo) {
            val startIntent = Intent(context, NewDetailActivity::class.java)
            startIntent.putExtra(EXTRA_VIDEOINFO, videoInfo)
            context.startActivity(startIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) context.overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_OPEN,
                R.anim.anl_push_bottom_in,
                R.anim.anl_push_up_out
            ) else context.overridePendingTransition(
                R.anim.anl_push_bottom_in,
                R.anim.anl_push_up_out
            )
        }

        fun start(context: Activity, videoId: Long) {
            val startIntent = Intent(context, NewDetailActivity::class.java)
            startIntent.putExtra(EXTRA_VIDEO_ID, videoId)
            context.startActivity(startIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) context.overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_OPEN,
                R.anim.anl_push_bottom_in,
                R.anim.anl_push_up_out
            ) else context.overridePendingTransition(
                R.anim.anl_push_bottom_in,
                R.anim.anl_push_up_out
            )
        }
    }
}