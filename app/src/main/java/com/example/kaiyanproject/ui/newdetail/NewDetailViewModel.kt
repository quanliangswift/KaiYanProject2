package com.example.kaiyanproject.ui.newdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.kaiyanproject.logic.VideoRepository
import com.example.kaiyanproject.logic.model.VideoDetail
import com.example.kaiyanproject.logic.model.VideoRelated
import com.example.kaiyanproject.logic.model.VideoReplies
import com.example.kaiyanproject.logic.network.api.VideoService

class NewDetailViewModel(repository: VideoRepository) : ViewModel() {
    var relatedDataList = ArrayList<VideoRelated.Item>()
    var repliesDataList = ArrayList<VideoReplies.Item>()
    var videoInfoData: NewDetailActivity.VideoInfo? = null

    var videoId: Long = 0L

    private var repliesLiveData_ = MutableLiveData<String>()
    private var videoDetailLiveData_ = MutableLiveData<RequestParam>()
    private var repliesAndRepliesLiveData_ = MutableLiveData<RequestParam>()

    var nextPageUrl: String? = null

    val videoDetailLiveData = videoDetailLiveData_.switchMap {
        liveData {
            val resutlt = try {
                val videoDetail =
                    repository.refreshVideoDetail(it.videoId, it.repliesUrl)   //视频信息+相关推荐+评论
                Result.success(videoDetail)
            } catch (e: Exception) {
                Result.failure<VideoDetail>(e)
            }
            emit(resutlt)
        }
    }

    val repliesAndRepliesLiveData = repliesAndRepliesLiveData_.switchMap {
        liveData {
            val result = try {
                val videoDetail =
                    repository.refreshVideoRelatedAndVideoReplies(it.videoId, it.repliesUrl)
                Result.success(videoDetail)
            } catch (e: Exception) {
                Result.failure<VideoDetail>(e)
            }
            emit(result)
        }
    }

    val repliesLiveData = repliesLiveData_.switchMap {
        liveData {
            val result = try {
                val videoDetail = repository.refreshVideoReplies(it)
                Result.success(videoDetail)
            } catch (e: Exception) {
                Result.failure<VideoReplies>(e)
            }
            emit(result)
        }
    }

    fun onRefresh() {
        if (videoInfoData == null) {
            videoDetailLiveData_.value =
                RequestParam(videoId, "${VideoService.VIDEO_REPLIES_URL}$videoId")
        } else {
            repliesAndRepliesLiveData_.value = RequestParam(
                videoInfoData?.videoId ?: 0L,
                "${VideoService.VIDEO_REPLIES_URL}${videoInfoData?.videoId ?: 0L}"
            )
        }
    }

    fun onLoadMore() {
        repliesLiveData_.value = nextPageUrl ?: ""
    }

    inner class RequestParam(val videoId: Long, val repliesUrl: String)
}