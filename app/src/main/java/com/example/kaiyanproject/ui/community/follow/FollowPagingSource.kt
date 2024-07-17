package com.example.kaiyanproject.ui.community.follow

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.logic.model.Follow
import com.example.kaiyanproject.logic.network.api.MainPageService

class FollowPagingSource(private val mainPageService: MainPageService) :
    PagingSource<String, Follow.Item>() {
    override fun getRefreshKey(state: PagingState<String, Follow.Item>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Follow.Item> {
        val page = params.key ?: MainPageService.FOLLOW_URL
        var repoResponse = mainPageService.getFollow(page)
        val repoItems = repoResponse.itemList
        val prevKey = null
        val nextKey =
            if (repoItems.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
        return LoadResult.Page(repoItems, prevKey, nextKey)
    }
}