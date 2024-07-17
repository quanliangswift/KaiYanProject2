package com.example.kaiyanproject.ui.community.commend

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.logic.model.HomePageRecommend
import com.example.kaiyanproject.logic.network.api.MainPageService

class CommendPagingSource(private val mainPageService: MainPageService) :
    PagingSource<String, CommunityRecommend.Item>() {
    override fun getRefreshKey(state: PagingState<String, CommunityRecommend.Item>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, CommunityRecommend.Item> {
        val page = params.key ?: MainPageService.COMMUNITY_RECOMMEND_URL
        var repoResponse = mainPageService.getCommunityRecommend(page)
        val repoItems = repoResponse.itemList
        val prevKey = null
        val nextKey =
            if (repoItems.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
        return LoadResult.Page(repoItems, prevKey, nextKey)
    }
}