package com.example.kaiyanproject.logic.network

import com.example.kaiyanproject.logic.network.api.MainPageService
import com.example.kaiyanproject.logic.network.api.VideoService
import retrofit2.http.Url

class KaiYanNetwork {
    var mainPageService = ServiceCreator.create(MainPageService::class.java)
        private set

    private val videoService = ServiceCreator.create(VideoService::class.java)

    suspend fun fetchHotSearch() = mainPageService.getHotSearch()

    suspend fun fetchVideoBeanForClient(videoId: Long) = videoService.getVideoBeanForClient(videoId)

    suspend fun fetchVideoRelated(videoId: Long) = videoService.getVideoRelated(videoId)

    suspend fun fetchVideoReplies(url: String) = videoService.getVideoReplies(url)

    companion object {
        @Volatile
        private var INSTANCE: KaiYanNetwork? = null

        fun getInstance(): KaiYanNetwork = INSTANCE ?: synchronized(this) {
            INSTANCE ?: KaiYanNetwork()
        }
    }

}