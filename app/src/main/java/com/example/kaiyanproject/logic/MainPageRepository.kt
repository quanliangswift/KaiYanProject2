package com.example.kaiyanproject.logic

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.logic.dao.MainPageDao
import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.logic.model.Daily
import com.example.kaiyanproject.logic.model.Discovery
import com.example.kaiyanproject.logic.model.Follow
import com.example.kaiyanproject.logic.model.HomePageRecommend
import com.example.kaiyanproject.logic.model.PushMessage
import com.example.kaiyanproject.logic.network.KaiYanNetwork
import com.example.kaiyanproject.ui.community.follow.FollowPagingSource
import com.example.kaiyanproject.ui.home.commend.CommendPagingSource
import com.example.kaiyanproject.ui.home.daily.DailyPagingSource
import com.example.kaiyanproject.ui.home.discovery.DiscoveryPagingSource
import com.example.kaiyanproject.ui.notification.push.PushPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * 主页界面，主要包含：（首页，社区，通知，我的），对应的仓库数据管理。
 *
 * @author vipyinzhiwei
 * @since  2020/5/2
 */

class MainPageRepository private constructor(
    private val mainPageDao: MainPageDao,
    private val kaiYanNetwork: KaiYanNetwork
) {
    suspend fun refreshHotSearch() = requestHotSearch()
    fun getMessagePagingData(): Flow<PagingData<PushMessage.Message>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = { PushPagingSource(kaiYanNetwork.mainPageService) }).flow
    }

    private suspend fun requestHotSearch() = withContext(Dispatchers.IO) {
        val response = kaiYanNetwork.fetchHotSearch()
        mainPageDao.cacheHotSearch(response)
        response
    }

    fun getHomePageRecommendPagingData(): Flow<PagingData<HomePageRecommend.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = {
                CommendPagingSource(kaiYanNetwork.mainPageService)
            }
        ).flow
    }

    fun getDailyPagingData(): Flow<PagingData<Daily.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = {
                DailyPagingSource(kaiYanNetwork.mainPageService)
            }
        ).flow
    }

    fun getDiscoveryPagingData(): Flow<PagingData<Discovery.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = {
                DiscoveryPagingSource(kaiYanNetwork.mainPageService)
            }
        ).flow
    }

    fun getCommunityCommendPagingData(): Flow<PagingData<CommunityRecommend.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = {
                com.example.kaiyanproject.ui.community.commend.CommendPagingSource(kaiYanNetwork.mainPageService)
            }
        ).flow
    }

    fun getFollowPagingData(): Flow<PagingData<Follow.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = {
                FollowPagingSource(kaiYanNetwork.mainPageService)
            }
        ).flow
    }

    companion object {
        @Volatile
        private var INSTANCE: MainPageRepository? = null

        fun getInstance(dao: MainPageDao, network: KaiYanNetwork): MainPageRepository {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = MainPageRepository(dao, network)
                    }
                }
            }
            return INSTANCE!!
        }
    }

}