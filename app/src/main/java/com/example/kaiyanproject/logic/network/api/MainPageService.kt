package com.example.kaiyanproject.logic.network.api

import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.logic.model.Daily
import com.example.kaiyanproject.logic.model.Discovery
import com.example.kaiyanproject.logic.model.Follow
import com.example.kaiyanproject.logic.model.HomePageRecommend
import com.example.kaiyanproject.logic.model.PushMessage
import com.example.kaiyanproject.logic.network.ServiceCreator
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * 主页界面，主要包含：（首页，社区，通知，我的）对应的 API 接口。
 *
 * @author vipyinzhiwei
 * @since  2020/5/15
 */
interface MainPageService {

    @GET
    suspend fun getDiscovery(@Url url: String): Discovery

    @GET
    suspend fun getHomePageRecommend(@Url url: String): HomePageRecommend

    /**
     * 社区-推荐列表
     */
    @GET
    suspend fun getCommunityRecommend(@Url url: String): CommunityRecommend

    @GET
    suspend fun getDaily(@Url url: String): Daily

    @GET
    suspend fun getFollow(@Url url: String): Follow

    @GET
    suspend fun getPushMessage(@Url url: String): PushMessage

    @GET("api/v3/queries/hot")
    suspend fun getHotSearch(): List<String>

    companion object {

        const val DISCOVERY_URL = "${ServiceCreator.BASE_URL}api/v7/index/tab/discovery"

        const val HOMEPAGE_RECOMMEND_URL = "${ServiceCreator.BASE_URL}api/v5/index/tab/allRec"

        const val DAILY_URL = "${ServiceCreator.BASE_URL}api/v5/index/tab/feed"

        const val COMMUNITY_RECOMMEND_URL = "${ServiceCreator.BASE_URL}api/v7/community/tab/rec"

        const val FOLLOW_URL = "${ServiceCreator.BASE_URL}api/v6/community/tab/follow"

        const val PUSHMESSAGE_URL = "${ServiceCreator.BASE_URL}api/v3/message"
    }
}
