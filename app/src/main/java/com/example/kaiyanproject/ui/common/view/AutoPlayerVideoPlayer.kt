package com.example.kaiyanproject.ui.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import com.example.kaiyanproject.R
import com.example.kaiyanproject.extension.gone
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView

class AutoPlayerVideoPlayer : StandardGSYVideoPlayer {
    var start: ImageView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, fullFlag: Boolean?) : super(context, fullFlag)

    override fun getLayoutId() = R.layout.layout_auto_play_video_player

    override fun init(context: Context?) {
        super.init(context)
        start = findViewById(R.id.start)
    }

    override fun touchSurfaceMoveFullLogic(absDeltaX: Float, absDeltaY: Float) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
        mChangePosition = false
        mChangeVolume = false
        mBrightness = false
    }

    override fun updateStartImage() {
        super.updateStartImage()
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            when (mCurrentState) {
                GSYVideoView.CURRENT_STATE_PLAYING -> imageView.setImageResource(R.drawable.ic_pause_white_24dp)
                GSYVideoView.CURRENT_STATE_ERROR -> imageView.setImageResource(R.drawable.ic_play_white_24dp)
            }
        } else {
            super.updateStartImage()
        }
    }

    override fun touchDoubleUp(e: MotionEvent?) {
        super.touchDoubleUp(e)
    }

    override fun changeUiToNormal() {
        super.changeUiToNormal()
        mBottomContainer.gone()
    }

    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        mBottomContainer.gone()
    }

    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        mBottomContainer.gone()
        start?.gone()
    }

    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
    }

    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
        mBottomContainer.gone()
    }

    override fun changeUiToCompleteShow() {
        super.changeUiToCompleteShow()
    }

    override fun changeUiToError() {
        super.changeUiToError()
    }
}