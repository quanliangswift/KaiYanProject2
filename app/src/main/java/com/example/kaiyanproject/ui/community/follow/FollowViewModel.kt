package com.example.kaiyanproject.ui.community.follow

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.kaiyanproject.logic.MainPageRepository
import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.logic.model.Follow
import kotlinx.coroutines.flow.Flow

class FollowViewModel(val repository: MainPageRepository) : ViewModel() {
    var dataList = ArrayList<Follow.Item>()

    fun getPagingData(): Flow<PagingData<Follow.Item>> {
        return repository.getFollowPagingData()
    }
}