package com.example.kaiyanproject.ui.home.commend

import android.app.Activity
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kaiyanproject.BuildConfig
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.R
import com.example.kaiyanproject.event.RefreshEvent
import com.example.kaiyanproject.event.SwitchPagesEvent
import com.example.kaiyanproject.extension.conversionVideoDuration
import com.example.kaiyanproject.extension.dp2px
import com.example.kaiyanproject.extension.gone
import com.example.kaiyanproject.extension.invisible
import com.example.kaiyanproject.extension.load
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.setOnClickListener
import com.example.kaiyanproject.extension.showDialogShare
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.extension.visible
import com.example.kaiyanproject.logic.model.HomePageRecommend
import com.example.kaiyanproject.ui.common.holder.AutoPlayVideoAdViewHolder
import com.example.kaiyanproject.ui.common.holder.Banner3ViewHolder
import com.example.kaiyanproject.ui.common.holder.BannerViewHolder
import com.example.kaiyanproject.ui.common.holder.FollowCardViewHolder
import com.example.kaiyanproject.ui.common.holder.InformationCardFollowCardViewHolder
import com.example.kaiyanproject.ui.common.holder.RecyclerViewHelp
import com.example.kaiyanproject.ui.common.holder.TagBriefCardViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewFooter2ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewFooter3ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader5ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader7ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader8ViewHolder
import com.example.kaiyanproject.ui.common.holder.TopicBriefCardViewHolder
import com.example.kaiyanproject.ui.common.holder.UgcSelectedCardCollectionViewHolder
import com.example.kaiyanproject.ui.common.holder.VideoSmallCardViewHolder
import com.example.kaiyanproject.ui.community.CommunityFragment
import com.example.kaiyanproject.ui.login.LoginActivity
import com.example.kaiyanproject.ui.newdetail.NewDetailActivity
import com.example.kaiyanproject.util.ActionUrlUtil
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.greenrobot.eventbus.EventBus
import retrofit2.http.Url

class CommendAdapter(val fragment: CommendFragment) :
    PagingDataAdapter<HomePageRecommend.Item, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun getItemViewType(position: Int) =
        RecyclerViewHelp.getItemViewType(getItem(position)!!)

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        RecyclerViewHelp.getViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)!!
        when (holder) {
            is TextCardViewHeader5ViewHolder -> {
                holder.tvTitle5.text = item.data.text
                if (item.data.actionUrl != null) holder.ivInto5.visible() else holder.ivInto5.gone()
                if (item.data.follow != null) holder.tvFollow.visible() else holder.tvFollow.gone()
                holder.tvFollow.setOnClickListener {
                    LoginActivity.start(fragment.activity)
                }
                setOnClickListener(holder.tvTitle5, holder.ivInto5) { }
            }

            is TextCardViewHeader7ViewHolder -> {
                holder.tvTitle7.text = item.data.text
                holder.tvRightText7.text = item.data.rightText
                setOnClickListener(holder.tvRightText7, holder.ivInto7) {
                    ActionUrlUtil.process(
                        fragment,
                        item.data.actionUrl,
                        "${item.data.text},${item.data.rightText}"
                    )
                }
            }

            is TextCardViewHeader8ViewHolder -> {
                holder.tvTitle8.text = item.data.text
                holder.tvRightText8.text = item.data.rightText
                setOnClickListener(holder.tvRightText8, holder.ivInto8) {
                    ActionUrlUtil.process(
                        fragment,
                        item.data.actionUrl,
                        item.data.text
                    )
                }
            }

            is TextCardViewFooter2ViewHolder -> {
                holder.tvFooterRightText2.text = item.data.text
                setOnClickListener(
                    holder.tvFooterRightText2,
                    holder.ivTooterInto2
                ) {
                    ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text)
                }
            }

            is TextCardViewFooter3ViewHolder -> {
                holder.tvFooterRightText3.text = item.data.text
                setOnClickListener(
                    holder.tvRefresh,
                    holder.tvFooterRightText3,
                    holder.ivTooterInto3
                ) {
                    if (this == holder.tvRefresh) {
                        "${holder.tvRefresh.text},${R.string.currently_not_supported.resString}".showToast()
                    } else if (this == holder.tvFooterRightText3 || this == holder.ivTooterInto3) {
                        ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text)
                    }
                }
            }

            is FollowCardViewHolder -> {
                holder.ivVideo.load(item.data.content.data.cover.feed, 4f)
                holder.ivAvatar.load(item.data.header.icon)
                holder.tvVideoDuration.text =
                    item.data.content.data.duration.conversionVideoDuration()
                holder.tvDescription.text = item.data.header.description
                holder.tvTitle.text = item.data.header.title
                if (item.data.content.data.ad) holder.tvLabel.visible() else holder.tvLabel.gone()
//                if (item.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) holder.ivChoiceness.visible() else holder.ivChoiceness.gone()
                holder.ivShare.setOnClickListener {
                    showDialogShare(
                        fragment.activity,
                        "${item.data.content.data.title}：${item.data.content.data.webUrl.raw}"
                    )
                }
                holder.itemView.setOnClickListener {
                    item.data.content.data.run {
                        if (ad || author == null) {
                            NewDetailActivity.start(fragment.activity, id)
                        } else {
                            NewDetailActivity.start(
                                fragment.activity,
                                NewDetailActivity.VideoInfo(
                                    id,
                                    playUrl,
                                    title,
                                    description,
                                    category,
                                    library,
                                    consumption,
                                    cover,
                                    author,
                                    webUrl
                                )
                            )
                        }
                    }
                }
            }

            is BannerViewHolder -> {
                holder.ivPicture.load(item.data.image, 4f)
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(
                        fragment,
                        item.data.actionUrl,
                        item.data.title
                    )
                }
            }

            is Banner3ViewHolder -> {
                holder.ivPicture.load(item.data.image, 4f)
                holder.ivAvatar.load(item.data.header.icon)
                holder.tvTitle.text = item.data.header.title
                holder.tvDescription.text = item.data.header.description
                if (item.data.label?.text.isNullOrEmpty()) holder.tvLabel.invisible() else holder.tvLabel.visible()
                holder.tvLabel.text = item.data.label?.text ?: ""
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(
                        fragment,
                        item.data.actionUrl,
                        item.data.header.title
                    )
                }
            }

            is InformationCardFollowCardViewHolder -> {
                holder.ivCover.load(
                    item.data.backgroundImage,
                    4f,
                    RoundedCornersTransformation.CornerType.TOP
                )
                holder.recyclerView.setHasFixedSize(true)
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(InformationCardFollowCardItemDecoration())
                }
                holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
                holder.recyclerView.adapter = InformationCardFollowCardAdapter(
                    fragment.activity,
                    item.data.actionUrl,
                    item.data.titleList
                )
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(
                        fragment,
                        item.data.actionUrl
                    )
                }
            }

            is VideoSmallCardViewHolder -> {
                holder.ivPicture.load(item.data.cover.feed, 4f)
//                holder.tvDescription.text =
//                if (item.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${item.data.category} / 开眼精选" else "#${item.data.category}"
                holder.tvTitle.text = item.data.title
                holder.tvVideoDuration.text = item.data.duration.conversionVideoDuration()
                holder.ivShare.setOnClickListener {
                    showDialogShare(
                        fragment.activity,
                        "${item.data.title}：${item.data.webUrl.raw}"
                    )
                }
                holder.itemView.setOnClickListener {
                    item.data.run {
                        NewDetailActivity.start(
                            fragment.activity,
                            NewDetailActivity.VideoInfo(
                                id,
                                playUrl,
                                title,
                                description,
                                category,
                                library,
                                consumption,
                                cover,
                                author,
                                webUrl
                            )
                        )
                    }
                }
            }

            is UgcSelectedCardCollectionViewHolder -> {
                holder.tvTitle.text = item.data.header.title
                holder.tvRightText.text = item.data.header.rightText
                holder.tvRightText.setOnClickListener {
                    EventBus.getDefault()
                        .post(SwitchPagesEvent(CommendFragment::class.java))
                    EventBus.getDefault().post(RefreshEvent(CommunityFragment::class.java))
                }
                item.data.itemList.forEachIndexed { index, it ->
                    when (index) {
                        0 -> {
                            holder.ivCoverLeft.load(
                                it.data.url,
                                4f,
                                RoundedCornersTransformation.CornerType.LEFT
                            )
                            if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) holder.ivLayersLeft.visible()
                            holder.ivAvatarLeft.load(it.data.userCover)
                            holder.tvNicknameLeft.text = it.data.nickname
                        }

                        1 -> {
                            holder.ivCoverRightTop.load(
                                it.data.url,
                                4f,
                                RoundedCornersTransformation.CornerType.TOP_RIGHT
                            )
                            if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) holder.ivLayersRightTop.visible()
                            holder.ivAvatarRightTop.load(it.data.userCover)
                            holder.tvNicknameRightTop.text = it.data.nickname
                        }

                        2 -> {
                            holder.ivCoverRightBottom.load(
                                it.data.url,
                                4f,
                                RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
                            )
                            if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) holder.ivLayersRightBottom.visible()
                            holder.ivAvatarRightBottom.load(it.data.userCover)
                            holder.tvNicknameRightBottom.text = it.data.nickname
                        }
                    }
                }
                holder.itemView.setOnClickListener { R.string.currently_not_supported.showToast() }
            }

            is TagBriefCardViewHolder -> {
                holder.ivPicture.load(item.data.icon, 4f)
                holder.tvDescription.text = item.data.description
                holder.tvTitle.text = item.data.title
                if (item.data.follow != null) holder.tvFollow.visible() else holder.tvFollow.gone()
                holder.tvFollow.setOnClickListener { LoginActivity.start(fragment.activity) }
                holder.itemView.setOnClickListener { "${item.data.title},${R.string.currently_not_supported.resString}".showToast() }
            }

            is TopicBriefCardViewHolder -> {
                holder.ivPicture.load(item.data.icon, 4f)
                holder.tvDescription.text = item.data.description
                holder.tvTitle.text = item.data.title
                holder.itemView.setOnClickListener { "${item.data.title},${R.string.currently_not_supported.resString}".showToast() }
            }

            is AutoPlayVideoAdViewHolder -> {
                item.data.detail?.run {
                    holder.ivAvatar.load(item.data.detail.icon)
                    holder.tvTitle.text = item.data.detail.title
                    holder.tvDescription.text = item.data.detail.description
                    startAutoPlay(
                        fragment.activity,
                        holder.videoPlayer,
                        position,
                        url,
                        imageUrl,
                        TAG,
                        object : GSYSampleCallBack() {
                            override fun onPrepared(url: String?, vararg objects: Any?) {
                                super.onPrepared(url, *objects)
                                GSYVideoManager.instance().isNeedMute = true
                            }

                            override fun onClickBlank(url: String?, vararg objects: Any?) {
                                super.onClickBlank(url, *objects)
                                ActionUrlUtil.process(fragment, item.data.detail.actionUrl)
                            }
                        })
                    setOnClickListener(holder.videoPlayer.thumbImageView, holder.itemView) {
                        ActionUrlUtil.process(fragment, item.data.detail.actionUrl)
                    }
                }
            }

            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }
        }

    }

    class InformationCardFollowCardAdapter(
        val activity: Activity,
        val actionUrl: String?,
        val dataList: List<String>
    ) :
        RecyclerView.Adapter<InformationCardFollowCardAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNews = view.findViewById<TextView>(R.id.tvNews)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_information_card_type_item, parent, false)
        )

        override fun getItemCount() = dataList.count()

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.tvNews.text = item
            holder.itemView.setOnClickListener { ActionUrlUtil.process(activity, actionUrl) }
        }
    }

    class InformationCardFollowCardItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val count = parent.adapter?.itemCount
            count?.run {
                when (position) {
                    0 -> {
                        outRect.top = 18f.dp2px
                    }

                    count.minus(1) -> {
                        outRect.top = 13f.dp2px
                        outRect.bottom = 18f.dp2px
                    }

                    else -> {
                        outRect.top = 13f.dp2px
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "CommendAdapter"
        private val DIFF_CALLBACK = object : ItemCallback<HomePageRecommend.Item>() {
            override fun areItemsTheSame(
                oldItem: HomePageRecommend.Item,
                newItem: HomePageRecommend.Item
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: HomePageRecommend.Item,
                newItem: HomePageRecommend.Item
            ): Boolean = oldItem == newItem

        }

        fun startAutoPlay(
            activity: Activity,
            player: GSYVideoPlayer,
            position: Int,
            playUrl: String,
            coverUrl: String,
            playTag: String,
            callBack: GSYSampleCallBack? = null
        ) {
            player.run {
                this.isReleaseWhenLossAudio = false
                this.playPosition = position
                this.playTag = playTag
                this.isLooping = true
                val cover = ImageView(activity)
                cover.scaleType = ImageView.ScaleType.CENTER_CROP
                cover.load(coverUrl, 4f)
                cover.parent?.run { removeView(cover) }
                this.thumbImageView = cover
                this.setVideoAllCallBack(callBack)
                setUp(playUrl, false, null)
            }
        }
    }

}