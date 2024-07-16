package com.example.kaiyanproject.ui.home.daily

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kaiyanproject.logic.model.Daily
import com.example.kaiyanproject.logic.model.HomePageRecommend
import com.example.kaiyanproject.logic.network.api.MainPageService

class DailyPagingSource(private val mainPageService: MainPageService) :
    PagingSource<String, Daily.Item>() {
    override fun getRefreshKey(state: PagingState<String, Daily.Item>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Daily.Item> {
        val page = params.key ?: MainPageService.DAILY_URL
        var repoResponse = mainPageService.getDaily(page)
        val repoItems = repoResponse.itemList
        val prevKey = null
        val nextKey =
            if (repoItems.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
        return LoadResult.Page(repoItems, prevKey, nextKey)
    }
}