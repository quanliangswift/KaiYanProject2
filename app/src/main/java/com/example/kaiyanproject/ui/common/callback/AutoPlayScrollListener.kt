package com.example.kaiyanproject.ui.common.callback

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaiyanproject.extension.dp2px
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.screenHeight
import com.example.kaiyanproject.extension.showToast
import com.shuyu.gsyvideoplayer.R
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.utils.NetworkUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

class AutoPlayScrollListener(
    private val itemPlayId: Int,
    private val rangeTop: Int = PLAY_RANGE_TOP,
    private val rangeBottom: Int = PLAY_RANGE_BOTTOM
) : RecyclerView.OnScrollListener() {
    private var firstVisible = 0
    private var lastVisible = 0
    private var visibleCount = 0
    private var runnable: PlayRunnable? = null
    private val playHandler = Handler()

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            playVideo(recyclerView)
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val firstVisibleItem =
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastVisibleItem =
            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        if (firstVisibleItem == firstVisible) return
        firstVisible = firstVisibleItem
        lastVisible = lastVisibleItem
        visibleCount = lastVisible - firstVisible
    }

    private fun playVideo(recyclerView: RecyclerView?) {
        recyclerView?.run {
            val layoutManager = this.layoutManager
            var gsyBaseVideoPlayer: GSYVideoPlayer? = null
            var needPlay = false
            looper@ for (i in 0 until visibleCount) {
                var canBreak = false
                (layoutManager?.getChildAt(i)
                    ?.findViewById<View?>(itemPlayId) as GSYVideoPlayer?)?.let {
                    val rect = Rect()
                    it.getLocalVisibleRect(rect)
                    val height = it.height
                    if (rect.top == 0 && rect.bottom == height) {
                        gsyBaseVideoPlayer = it
                        if (it.currentPlayer.currentState in intArrayOf(
                                GSYVideoPlayer.CURRENT_STATE_NORMAL,
                                GSYVideoPlayer.CURRENT_STATE_ERROR
                            )
                        ) {
                            needPlay = true
                        }
                        canBreak = true

                    }
                }
                if (canBreak) break@looper
            }
            if (gsyBaseVideoPlayer != null && needPlay) {
                runnable?.let {
                    val tmpPlayer = it.gsyBaseVideoPlayer
                    playHandler.removeCallbacks(it)
                    runnable = null
                    if (tmpPlayer === gsyBaseVideoPlayer) return
                }
                runnable = PlayRunnable(gsyBaseVideoPlayer, rangeTop, rangeBottom)
                playHandler.postDelayed(runnable!!, 400)
            }
        }
    }

    private class PlayRunnable(
        val gsyBaseVideoPlayer: GSYVideoPlayer?,
        val rangeTop: Int,
        val rangeBottom: Int
    ) : Runnable {
        private var isNeedShowWifiDialog = true
        override fun run() {
            var inPosition = false
            gsyBaseVideoPlayer?.let {
                val screenPosition = IntArray(2)
                it.getLocationOnScreen(screenPosition)
                val halfHeight = it.height / 2
                val rangePosition = screenPosition.last() + halfHeight
                if (rangePosition in rangeTop..rangeBottom) {
                    inPosition = true
                }
                if (inPosition) {
                    startPlayLogic(it, it.context)
                }
            }
        }

        private fun startPlayLogic(gsyBaseVideoPlayer: GSYVideoPlayer, context: Context) {
            if (!CommonUtil.isWifiConnected(context) && isNeedShowWifiDialog) {
                showWifiDialog(gsyBaseVideoPlayer, context)
                return
            }
            gsyBaseVideoPlayer.startPlayLogic()
        }

        private fun showWifiDialog(gsyBaseVideoPlayer: GSYVideoPlayer, context: Context) {
            if (!NetworkUtils.isAvailable(context)) {
                R.string.no_net.resString.showToast()
                return
            }
            AlertDialog.Builder(context).apply {
                this.setMessage(R.string.tips_not_wifi.resString)
                this.setPositiveButton(R.string.tips_not_wifi_confirm.resString) { dialog, which ->
                    dialog.dismiss()
                    gsyBaseVideoPlayer.startPlayLogic()
                    isNeedShowWifiDialog = false
                }
                setNegativeButton(R.string.tips_not_wifi_cancel.resString) { dialog, _ ->
                    dialog.dismiss()
                    isNeedShowWifiDialog = true
                }
                this.create()
            }.show()

        }

    }


    companion object {
        /**
         * 指定自动播放，在屏幕上的区域范围，上
         */
        val PLAY_RANGE_TOP = screenHeight / 2 - 180f.dp2px

        /**
         * 指定自动播放，在屏幕上的区域范围，下
         */
        val PLAY_RANGE_BOTTOM = screenHeight / 2 + 180f.dp2px
    }
}