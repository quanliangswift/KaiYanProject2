package com.example.kaiyanproject.ui.home.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kaiyanproject.logic.MainPageRepository

class DiscoveryViewModelFactory(private val repository: MainPageRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiscoveryViewModel(repository) as T
    }
}