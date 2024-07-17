package com.example.kaiyanproject.ui.community.commend

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.kaiyanproject.logic.MainPageRepository
import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.logic.model.HomePageRecommend
import com.example.kaiyanproject.logic.network.api.MainPageService
import kotlinx.coroutines.flow.Flow

class CommendViewModel(val repository: MainPageRepository) : ViewModel() {
    var dataList = ArrayList<CommunityRecommend.Item>()

    fun getPagingData(): Flow<PagingData<CommunityRecommend.Item>> {
        return repository.getCommunityCommendPagingData()
    }
}