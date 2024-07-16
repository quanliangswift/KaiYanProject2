package com.example.kaiyanproject.util

import com.example.kaiyanproject.logic.MainPageRepository
import com.example.kaiyanproject.logic.dao.KaiYanDatabase
import com.example.kaiyanproject.logic.network.KaiYanNetwork
import com.example.kaiyanproject.ui.home.commend.CommendViewModelFactory

object InjectorUtil {
    private fun getMainPageRepository() =
        MainPageRepository.getInstance(KaiYanDatabase.getMainPageDao(), KaiYanNetwork.getInstance())
//    private fun getVideoRepository() = Videre

    fun getHomePageCommendViewModelFactory() = CommendViewModelFactory(getMainPageRepository())
}