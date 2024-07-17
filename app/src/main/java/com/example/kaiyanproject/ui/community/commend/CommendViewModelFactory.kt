package com.example.kaiyanproject.ui.community.commend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kaiyanproject.logic.MainPageRepository

class CommendViewModelFactory(private val repository: MainPageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommendViewModel(repository) as T
    }
}