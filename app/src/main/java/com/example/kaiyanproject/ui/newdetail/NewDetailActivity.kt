package com.example.kaiyanproject.ui.newdetail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaiyanproject.R
import com.example.kaiyanproject.databinding.ActivityNewDetailBinding
import com.example.kaiyanproject.extension.gone
import com.example.kaiyanproject.extension.goneAlphaAnimation
import com.example.kaiyanproject.extension.invisibleAlphaAnimation
import com.example.kaiyanproject.extension.load
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.setOnClickListener
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.extension.visible
import com.example.kaiyanproject.extension.visibleAlphaAnimation
import com.example.kaiyanproject.logic.model.Author
import com.example.kaiyanproject.logic.model.Consumption
import com.example.kaiyanproject.logic.model.Cover
import com.example.kaiyanproject.logic.model.WebUrl
import com.example.kaiyanproject.ui.common.ui.BaseActivity
import com.example.kaiyanproject.ui.common.view.NoStatusFooter
import com.example.kaiyanproject.ui.login.LoginActivity
import com.example.kaiyanproject.util.InjectorUtil
import com.example.kaiyanproject.util.ResponseHandler
import com.example.kaiyanproject.util.SHARE_QQ
import com.example.kaiyanproject.util.SHARE_QQZONE
import com.example.kaiyanproject.util.SHARE_WECHAT
import com.example.kaiyanproject.util.SHARE_WECHAT_MEMORIES
import com.example.kaiyanproject.util.SHARE_WEIBO
import com.shuyu.gsyvideoplayer.GSYVideoADManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class NewDetailActivity : BaseActivity() {
    private var _binging: ActivityNewDetailBinding? = null
    private val binding: ActivityNewDetailBinding
        get() = _binging!!
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getNewDetailViewModelFactory()
        )[NewDetailViewModel::class.java]
    }

    private lateinit var relatedAdapter: NewDetailRelatedAdapter
    private lateinit var replyAdapter: NewDetailReplyAdapter
    private lateinit var mergeAdapter: ConcatAdapter

    private var orientationUtils: OrientationUtils? = null

    private val globalJob by lazy { Job() }
    private var hideTitleBarJob: Job? = null
    private var hideBottomContainerJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binging = ActivityNewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setContentView(view: View?) {
        if (checkArguments()) {
            super.setContentView(view)
            setStatusBarBackground(R.color.black)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (checkArguments()) {
            initParams()
            startVideoPlayer()
            viewModel.onRefresh()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        binding.videoPlayer.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoADManager.releaseAllVideos()
        orientationUtils?.releaseListener()
        binding.videoPlayer.release()
        binding.videoPlayer.setVideoAllCallBack(null)
        globalJob.cancel()
        _binging = null
    }

    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()
        if (GSYVideoManager.backFromWindowFull(this)) return
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.anl_push_bottom_out)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
    }

    override fun setupViews() {
        super.setupViews()
        initParams()
        orientationUtils = OrientationUtils(this, binding.videoPlayer)
        relatedAdapter =
            NewDetailRelatedAdapter(this, viewModel.relatedDataList, viewModel.videoInfoData)
        replyAdapter = NewDetailReplyAdapter(this, viewModel.repliesDataList)
        mergeAdapter = ConcatAdapter(relatedAdapter, replyAdapter)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = mergeAdapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.itemAnimator = null
        binding.refreshLayout.run {
            setDragRate(0.7f)
            setHeaderTriggerRate(0.6f)
            setFooterTriggerRate(0.6f)
            setEnableLoadMoreWhenContentNotFull(true)
            setEnableFooterFollowWhenNoMoreData(true)
            setEnableFooterTranslationContent(true)
            setEnableScrollContentWhenLoaded(true)
            binding.refreshLayout.setEnableNestedScroll(true)
            setFooterHeight(153f)
            setRefreshFooter(NoStatusFooter(this@NewDetailActivity).apply {
                setAccentColorId(R.color.white)
                setTextTitleSize(16f)
            })
            setOnRefreshListener { finish() }
            setOnLoadMoreListener { viewModel.onLoadMore() }
        }

        setOnClickListener(
            binding.ivPullDown,
            binding.ivMore,
            binding.ivShare,
            binding.ivCollection,
            binding.ivToWechatFriends,
            binding.ivToWechatMomeries,
            binding.ivShareToWeibo,
            binding.ivShareToQQ,
            binding.ivShareToQQzone,
            binding.ivAvatar,
            binding.etComment,
            binding.ivReply,
            binding.tvReplyCount,
            listener = ClickListener()
        )

        startVideoPlayer()
        viewModel.onRefresh()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observe() {
        if (!viewModel.videoDetailLiveData.hasObservers()) {
            viewModel.videoDetailLiveData.observe(this, Observer { result ->
                val response = result.getOrNull()
                if (response == null) {
                    ResponseHandler.getFailureTips(result.exceptionOrNull()).showToast()
                    return@Observer
                }
                viewModel.nextPageUrl = response.videoReplies.nextPageUrl
                if (response.videoRelated == null || response.videoRelated.itemList.isEmpty() && response.videoReplies.itemList.isEmpty()) {
                    return@Observer
                }
                response.videoBeanForClient?.run {
                    viewModel.videoInfoData = VideoInfo(
                        this.id,
                        this.playUrl,
                        this.title,
                        this.description,
                        this.category,
                        this.library,
                        this.consumption,
                        this.cover,
                        this.author,
                        this.webUrl
                    )
                    startVideoPlayer()
                    relatedAdapter.bindVideoInfo(viewModel.videoInfoData)
                }
                viewModel.relatedDataList.clear()
                viewModel.repliesDataList.clear()
                viewModel.relatedDataList.addAll(response.videoRelated.itemList)
                viewModel.repliesDataList.addAll(response.videoReplies.itemList)
                relatedAdapter.notifyDataSetChanged()
                replyAdapter.notifyDataSetChanged()
                when {
                    viewModel.repliesDataList.isEmpty() -> binding.refreshLayout.finishLoadMoreWithNoMoreData()
                    response.videoReplies.nextPageUrl.isNullOrEmpty() -> binding.refreshLayout.finishLoadMoreWithNoMoreData()
                    else -> binding.refreshLayout.closeHeaderOrFooter()
                }
            })
            //刷新，相关推荐+评论
            if (!viewModel.repliesAndRepliesLiveData.hasObservers()) {
                viewModel.repliesAndRepliesLiveData.observe(this, Observer { result ->
                    val response = result.getOrNull()
                    if (response == null) {
                        ResponseHandler.getFailureTips(result.exceptionOrNull()).showToast()
                        return@Observer
                    }

                    viewModel.nextPageUrl = response.videoReplies.nextPageUrl
                    if (response.videoRelated == null || response.videoRelated.itemList.isEmpty() && response.videoReplies.itemList.isEmpty()) {
                        return@Observer
                    }
                    viewModel.relatedDataList.clear()
                    viewModel.repliesDataList.clear()
                    viewModel.relatedDataList.addAll(response.videoRelated.itemList)
                    viewModel.repliesDataList.addAll(response.videoReplies.itemList)
                    relatedAdapter.bindVideoInfo(viewModel.videoInfoData)
                    relatedAdapter.notifyDataSetChanged()
                    replyAdapter.notifyDataSetChanged()
                    when {
                        viewModel.repliesDataList.isEmpty() -> binding.refreshLayout.finishLoadMoreWithNoMoreData()
                        response.videoReplies.nextPageUrl.isNullOrEmpty() -> binding.refreshLayout.finishLoadMoreWithNoMoreData()
                        else -> binding.refreshLayout.closeHeaderOrFooter()
                    }
                })
            }

            //上拉加载，评论
            if (!viewModel.repliesLiveData.hasObservers()) {
                viewModel.repliesLiveData.observe(this, Observer { result ->
                    val response = result.getOrNull()
                    if (response == null) {
                        ResponseHandler.getFailureTips(result.exceptionOrNull()).showToast()
                        return@Observer
                    }
                    viewModel.nextPageUrl = response.nextPageUrl
                    if (response.itemList.isEmpty()) {
                        return@Observer
                    }
                    val itemCount = replyAdapter.itemCount

                    viewModel.repliesDataList.addAll(response.itemList)
                    replyAdapter.notifyItemRangeInserted(itemCount, response.itemList.size)
                    if (response.nextPageUrl.isNullOrEmpty()) {
                        binding.refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        binding.refreshLayout.closeHeaderOrFooter()
                    }
                })
            }
        }
    }

    private fun initParams() {
        var videoInfo: VideoInfo?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            videoInfo = intent.getParcelableExtra(
                EXTRA_VIDEOINFO,
                VideoInfo::class.java
            )
        } else {
            videoInfo = intent.getParcelableExtra<VideoInfo>(
                EXTRA_VIDEOINFO
            )

        }
        if (videoInfo != null) viewModel.videoInfoData = videoInfo
        if (intent.getLongExtra(
                EXTRA_VIDEO_ID, 0L
            ) != 0L
        ) viewModel.videoId = intent.getLongExtra(
            EXTRA_VIDEO_ID, 0L
        )
    }

    private fun startVideoPlayer() {
        viewModel.videoInfoData?.run {
            binding.ivBlurredBg.load(this.cover.blurred)
            binding.tvReplyCount.text = this.consumption.replyCount.toString()
            binding.videoPlayer.startPlay()
        }
    }

    private fun checkArguments(): Boolean {
        var videoInfo: VideoInfo?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            videoInfo = intent.getParcelableExtra(
                EXTRA_VIDEOINFO,
                VideoInfo::class.java
            )
        } else {
            videoInfo = intent.getParcelableExtra<VideoInfo>(
                EXTRA_VIDEOINFO
            )

        }

        if (videoInfo == null && intent.getLongExtra(
                EXTRA_VIDEO_ID, 0L
            ) == 0L
        ) {
            R.string.jump_page_unknown_error.resString.showToast()
            finish()
            return false
        } else {
            return true
        }
    }


    private fun GSYVideoPlayer.startPlay() {
        viewModel.videoInfoData?.let {
            //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
            fullscreenButton.setOnClickListener { showFull() }
            //防止错位设置
            playTag = TAG
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = false
            //增加封面
            val imageView = ImageView(this@NewDetailActivity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.load(it.cover.detail)
            thumbImageView = imageView
            thumbImageView.setOnClickListener { switchTitleBarVisible() }
            //是否开启自动旋转
            isRotateViewAuto = false
            //是否需要全屏锁定屏幕功能
            isNeedLockFull = true
            //是否可以滑动调整
            setIsTouchWiget(true)
            //设置触摸显示控制ui的消失时间
            dismissControlTime = 5000
            //设置播放过程中的回调
            setVideoAllCallBack(
                VideoCallPlayBack(
                    binding,
                    ::switchTitleBarVisible,
                    ::delayHideBottomContainer
                )
            )
            //设置播放URL
            setUp(it.playUrl, false, it.title)
            //开始播放
            startPlayLogic()
        }
    }

    private fun showFull() {
        orientationUtils?.resolveByClick()
    }

    private fun switchTitleBarVisible() {
        if (binding.videoPlayer.currentState == GSYVideoView.CURRENT_STATE_AUTO_COMPLETE) return
        if (binding.rlHeader.visibility == View.VISIBLE) {
            hideTitleBar()
        } else {
            binding.rlHeader.invisibleAlphaAnimation(1000)
            binding.ivPullDown.invisibleAlphaAnimation(1000)
            binding.ivCollection.invisibleAlphaAnimation(1000)
            binding.ivMore.invisibleAlphaAnimation(1000)
            binding.ivShare.invisibleAlphaAnimation(1000)
            delayHideTitleBar()
        }
    }

    private fun hideTitleBar() {
        binding.rlHeader.invisibleAlphaAnimation(1000)
        binding.ivPullDown.goneAlphaAnimation(1000)
        binding.ivCollection.goneAlphaAnimation(1000)
        binding.ivMore.goneAlphaAnimation(1000)
        binding.ivShare.goneAlphaAnimation(1000)
    }

    private fun delayHideTitleBar() {
        hideTitleBarJob?.cancel()
        hideTitleBarJob = CoroutineScope(globalJob).launch(Dispatchers.Main) {
            delay(binding.videoPlayer.dismissControlTime.toLong())
            hideTitleBar()
        }
    }

    private fun delayHideBottomContainer() {
        hideBottomContainerJob?.cancel()
        hideBottomContainerJob = CoroutineScope(globalJob).launch(Dispatchers.Main) {
            delay(binding.videoPlayer.dismissControlTime.toLong())
            binding.videoPlayer.getBottomContainer().gone()
            binding.videoPlayer.startButton.gone()
        }
    }


    fun scrollTop() {
        if (relatedAdapter.itemCount != 0) {
            binding.recyclerView.scrollToPosition(0)
            binding.refreshLayout.invisibleAlphaAnimation(2500)
            binding.refreshLayout.visibleAlphaAnimation(1500)
        }
    }

    private fun scrollRepliesTop() {
        val targetPosition = (relatedAdapter.itemCount - 1) + 2 //+相关推荐最后一项，+1评论标题，+1条评论
        if (targetPosition < mergeAdapter.itemCount - 1) {
            binding.recyclerView.smoothScrollToPosition(targetPosition)
        }
    }

    class VideoCallPlayBack(
        val binding: ActivityNewDetailBinding,
        val switchTitleBarVisible: () -> Unit,
        val delayHideBottomContainer: () -> Unit
    ) : GSYSampleCallBack() {
        override fun onStartPrepared(url: String?, vararg objects: Any?) {
            super.onStartPrepared(url, *objects)
            binding.rlHeader.gone()
            binding.llShares.gone()
        }

        override fun onClickBlank(url: String?, vararg objects: Any?) {
            super.onClickBlank(url, *objects)
            switchTitleBarVisible()
        }

        override fun onClickStop(url: String?, vararg objects: Any?) {
            super.onClickStop(url, *objects)
            delayHideBottomContainer()
        }

        override fun onAutoComplete(url: String?, vararg objects: Any?) {
            super.onAutoComplete(url, *objects)
            binding.rlHeader.visible()
            binding.ivPullDown.visible()
            binding.ivCollection.gone()
            binding.ivShare.gone()
            binding.ivMore.gone()
            binding.llShares.visible()
        }
    }

    inner class ClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            viewModel.videoInfoData?.let {
                when (v) {
                    binding.ivPullDown -> finish()
                    binding.ivMore -> {

                    }

                    binding.ivShare -> showDialogShare(it.webUrl.raw)
                    binding.ivCollection -> LoginActivity.start(this@NewDetailActivity)
                    binding.ivToWechatFriends -> share(it.webUrl.raw, SHARE_WECHAT)
                    binding.ivToWechatMomeries -> share(it.webUrl.raw, SHARE_WECHAT_MEMORIES)
                    binding.ivShareToWeibo -> share(it.webUrl.forWeibo, SHARE_WEIBO)
                    binding.ivShareToQQ -> share(it.webUrl.raw, SHARE_QQ)
                    binding.ivShareToQQzone -> share(it.webUrl.raw, SHARE_QQZONE)
                    binding.ivAvatar, binding.etComment -> LoginActivity.start(this@NewDetailActivity)
                    binding.ivReply, binding.tvReplyCount -> scrollRepliesTop()
                }
            }
        }
    }

    @Parcelize
    data class VideoInfo(
        val videoId: Long,
        val playUrl: String,
        val title: String,
        val description: String,
        val category: String,
        val library: String,
        val consumption: Consumption,
        val cover: Cover, val author: Author?, val webUrl: WebUrl
    ) : Parcelable

    companion object {
        const val EXTRA_VIDEOINFO = "videoInfo"
        const val EXTRA_VIDEO_ID = "videoId"
        const val TAG = "NewDetailActivity"
        fun start(context: Activity, videoInfo: VideoInfo) {
            val startIntent = Intent(context, NewDetailActivity::class.java)
            startIntent.putExtra(EXTRA_VIDEOINFO, videoInfo)
            context.startActivity(startIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) context.overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_OPEN,
                R.anim.anl_push_bottom_in,
                R.anim.anl_push_up_out
            ) else context.overridePendingTransition(
                R.anim.anl_push_bottom_in,
                R.anim.anl_push_up_out
            )
        }

        fun start(context: Activity, videoId: Long) {
            val startIntent = Intent(context, NewDetailActivity::class.java)
            startIntent.putExtra(EXTRA_VIDEO_ID, videoId)
            context.startActivity(startIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) context.overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_OPEN,
                R.anim.anl_push_bottom_in,
                R.anim.anl_push_up_out
            ) else context.overridePendingTransition(
                R.anim.anl_push_bottom_in,
                R.anim.anl_push_up_out
            )
        }
    }
}
