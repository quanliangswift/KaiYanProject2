package com.example.kaiyanproject.ui.home.discovery

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kaiyanproject.logic.model.Daily
import com.example.kaiyanproject.logic.model.Discovery
import com.example.kaiyanproject.logic.network.api.MainPageService

class DiscoveryPagingSource(private val mainPageService: MainPageService) :
    PagingSource<String, Discovery.Item>() {
    override fun getRefreshKey(state: PagingState<String, Discovery.Item>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Discovery.Item> {
        val page = params.key ?: MainPageService.DISCOVERY_URL
        var repoResponse = mainPageService.getDiscovery(page)
        val repoItems = repoResponse.itemList
        val prevKey = null
        val nextKey =
            if (repoItems.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
        return LoadResult.Page(repoItems, prevKey, nextKey)
    }
}