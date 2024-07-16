package com.example.kaiyanproject.ui.home.commend

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.kaiyanproject.logic.MainPageRepository
import com.example.kaiyanproject.logic.model.HomePageRecommend
import com.example.kaiyanproject.logic.network.api.MainPageService
import kotlinx.coroutines.flow.Flow

class CommendViewModel(val repository: MainPageRepository) : ViewModel() {
    var dataList = ArrayList<HomePageRecommend.Item>()

    fun getPagingData(): Flow<PagingData<HomePageRecommend.Item>> {
        return repository.getHomePageRecommendPagingData()
    }
}