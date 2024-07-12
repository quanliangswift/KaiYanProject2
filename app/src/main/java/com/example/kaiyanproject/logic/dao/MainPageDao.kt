package com.example.kaiyanproject.logic.dao

import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.logic.model.Daily
import com.example.kaiyanproject.logic.model.Discovery
import com.example.kaiyanproject.logic.model.Follow
import com.example.kaiyanproject.logic.model.HomePageRecommend
import com.example.kaiyanproject.logic.model.PushMessage


/**
 * 主页界面（主要包含：首页，社区，通知，我的），对应的Dao操作类。
 *
 * @author vipyinzhiwei
 * @since  2020/5/15
 */

class MainPageDao {
    /*----------------------------首页相关----------------------------*/

    fun cacheDiscovery(bean: Discovery?) {
        //TODO("存储数据到本地")
    }

    fun getCachedDiscovery(): Discovery? {
        TODO("获取本地存储的数据")
    }

    fun cacheHomePageRecommend(bean: HomePageRecommend?) {
        //TODO("存储数据到本地")
    }

    fun getCachedHomePageRecommend(): HomePageRecommend? {
        TODO("获取本地存储的数据")
    }

    fun cacheDaily(bean: Daily?) {
        //TODO("存储数据到本地")
    }

    fun getCachedDaily(): Daily? {
        TODO("获取本地存储的数据")
    }

    /*----------------------------社区相关----------------------------*/

    fun cacheCommunityRecommend(bean: CommunityRecommend?) {
        //TODO("存储数据到本地")
    }

    fun getCachedCommunityRecommend(): CommunityRecommend? {
        TODO("获取本地存储的数据")
    }

    fun cacheFollow(bean: Follow?) {
        //TODO("存储数据到本地")
    }

    fun getCachedFollow(): Follow? {
        TODO("获取本地存储的数据")
    }

    /*----------------------------通知相关----------------------------*/

    fun cachePushMessageInfo(bean: PushMessage?) {
        //TODO("存储数据到本地")
    }

    fun getCachedPushMessageInfo(): PushMessage? {
        TODO("获取本地存储的数据")
    }

    /*----------------------------搜索相关----------------------------*/

    fun cacheHotSearch(bean: List<String>?) {
        //TODO("存储数据到本地")
    }

    fun getHotSearch(): List<String>? {
        TODO("获取本地存储的数据")
    }
}