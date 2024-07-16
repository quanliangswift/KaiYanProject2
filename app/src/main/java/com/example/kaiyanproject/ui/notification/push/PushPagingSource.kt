package com.example.kaiyanproject.ui.notification.push

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kaiyanproject.logic.model.PushMessage
import com.example.kaiyanproject.logic.network.api.MainPageService

class PushPagingSource(private val mainPageService: MainPageService) :
    PagingSource<String, PushMessage.Message>() {
    override fun getRefreshKey(state: PagingState<String, PushMessage.Message>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, PushMessage.Message> {
        val page = params.key ?: MainPageService.PUSHMESSAGE_URL
        val repoResponse = mainPageService.getPushMessage(page)
        val repoItems = repoResponse.itemList
        val prevKey = null
        val nextKey =
            if (repoItems.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
        return LoadResult.Page(repoItems, prevKey, nextKey)
    }
}