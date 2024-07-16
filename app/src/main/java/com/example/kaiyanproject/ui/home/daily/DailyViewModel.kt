package com.example.kaiyanproject.ui.home.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kaiyanproject.logic.MainPageRepository
import com.example.kaiyanproject.logic.model.Daily
import kotlinx.coroutines.flow.Flow

class DailyViewModel(val repository: MainPageRepository) : ViewModel() {
    var dataList = ArrayList<Daily.Item>()

    fun getPagingData(): Flow<PagingData<Daily.Item>> {
        return repository.getDailyPagingData().cachedIn(viewModelScope)
    }
}