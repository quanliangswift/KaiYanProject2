package com.example.kaiyanproject.logic

import com.example.kaiyanproject.logic.dao.VideoDao
import com.example.kaiyanproject.logic.model.VideoDetail
import com.example.kaiyanproject.logic.model.WebUrl
import com.example.kaiyanproject.logic.network.KaiYanNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class VideoRepository(private val dao: VideoDao, private val network: KaiYanNetwork) {
    suspend fun refreshVideoReplies(repliesUrl: String) = requestVideoReplies(repliesUrl)
    suspend fun refreshVideoRelatedAndVideoReplies(videoId: Long, repliesUrl: String) =
        requestVideoRelatedAndVideoReplies(videoId, repliesUrl)

    suspend fun refreshVideoDetail(videoId: Long, repliesUrl: String) =
        requestVideoDetail(videoId, repliesUrl)

    private suspend fun requestVideoReplies(url: String) = withContext(Dispatchers.IO) {
        val deferredReplies = async { network.fetchVideoReplies(url) }
        val videoReplies = deferredReplies.await()
        videoReplies
    }

    private suspend fun requestVideoRelatedAndVideoReplies(videoId: Long, repliesUrl: String) =
        withContext(Dispatchers.IO) {
            val deferredVideoRelated = async { network.fetchVideoRelated(videoId) }
            val deferredVideoReplies = async { network.fetchVideoReplies(repliesUrl) }
            val videoRelated = deferredVideoRelated.await()
            val videoReplies = deferredVideoReplies.await()
            val videoDetail = VideoDetail(null, videoRelated, videoReplies)
            videoDetail
        }

    private suspend fun requestVideoDetail(videoId: Long, repliesUrl: String) =
        withContext(Dispatchers.IO) {
            val deferredVideoRelated = async { network.fetchVideoRelated(videoId) }
            val deferredVideoReplies = async { network.fetchVideoReplies(repliesUrl) }
            val deferredVideoBeanForClient = async { network.fetchVideoBeanForClient(videoId) }
            val videoRelated = deferredVideoRelated.await()
            val videoReplies = deferredVideoReplies.await()
            val videoBeanForClient = deferredVideoBeanForClient.await()
            val videoDetail = VideoDetail(videoBeanForClient, videoRelated, videoReplies)
            dao.cacheVideoDetail(videoDetail)
            videoDetail
        }

    companion object {
        @Volatile
        private var INSTANCE: VideoRepository? = null

        fun getInstance(dao: VideoDao, network: KaiYanNetwork): VideoRepository {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = VideoRepository(dao, network)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}