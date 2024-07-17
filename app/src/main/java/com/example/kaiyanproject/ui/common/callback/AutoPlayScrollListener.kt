package com.example.kaiyanproject.ui.common.callback

import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaiyanproject.extension.dp2px
import com.example.kaiyanproject.extension.screenHeight

class AutoPlayScrollListener(
    private val itemPlayId: Int,
    private val rangeTop: Int = PLAY_RANGE_TOP,
    private val rangeBottom: Int = PLAY_RANGE_BOTTOM
) : RecyclerView.OnScrollListener() {
    private var firstVisible = 0
    private var lastVisible = 0
    private var visibleCount = 0
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
        todo
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