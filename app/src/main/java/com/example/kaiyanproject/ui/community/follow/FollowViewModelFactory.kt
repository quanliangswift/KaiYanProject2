package com.example.kaiyanproject.ui.community.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kaiyanproject.logic.MainPageRepository

class FollowViewModelFactory(private val repository: MainPageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FollowViewModel(repository) as T
    }
}