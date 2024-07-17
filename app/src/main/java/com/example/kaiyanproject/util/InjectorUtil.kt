package com.example.kaiyanproject.util

import com.example.kaiyanproject.logic.MainPageRepository
import com.example.kaiyanproject.logic.dao.KaiYanDatabase
import com.example.kaiyanproject.logic.model.Daily
import com.example.kaiyanproject.logic.network.KaiYanNetwork
import com.example.kaiyanproject.ui.community.follow.FollowViewModelFactory
import com.example.kaiyanproject.ui.home.commend.CommendViewModelFactory
import com.example.kaiyanproject.ui.home.daily.DailyViewModelFactory
import com.example.kaiyanproject.ui.home.discovery.DiscoveryViewModelFactory

object InjectorUtil {
    private fun getMainPageRepository() =
        MainPageRepository.getInstance(KaiYanDatabase.getMainPageDao(), KaiYanNetwork.getInstance())
//    private fun getVideoRepository() = Videre

    fun getHomePageCommendViewModelFactory() = CommendViewModelFactory(getMainPageRepository())
    fun getDailyViewModelFactory() = DailyViewModelFactory(getMainPageRepository())
    fun getDiscoveryViewModelFactory() = DiscoveryViewModelFactory(getMainPageRepository())
    fun getCommunityCommendViewModelFactory() =
        com.example.kaiyanproject.ui.community.commend.CommendViewModelFactory(
            getMainPageRepository()
        )

    fun getFollowViewModelFactory() =
        FollowViewModelFactory(
            getMainPageRepository()
        )
}