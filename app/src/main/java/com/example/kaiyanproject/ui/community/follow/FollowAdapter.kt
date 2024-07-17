package com.example.kaiyanproject.ui.community.follow

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kaiyanproject.BuildConfig
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.R
import com.example.kaiyanproject.extension.conversionVideoDuration
import com.example.kaiyanproject.extension.dp2px
import com.example.kaiyanproject.extension.gone
import com.example.kaiyanproject.extension.inflate
import com.example.kaiyanproject.extension.invisible
import com.example.kaiyanproject.extension.load
import com.example.kaiyanproject.extension.logW
import com.example.kaiyanproject.extension.resDimension
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.setOnClickListener
import com.example.kaiyanproject.extension.showDialogShare
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.extension.visible
import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.logic.model.DrawableDirection
import com.example.kaiyanproject.logic.model.Follow
import com.example.kaiyanproject.logic.model.setDrawable
import com.example.kaiyanproject.ui.common.holder.EmptyViewHolder
import com.example.kaiyanproject.ui.home.commend.CommendAdapter
import com.example.kaiyanproject.ui.home.daily.DailyAdapter
import com.example.kaiyanproject.ui.login.LoginActivity
import com.example.kaiyanproject.ui.newdetail.NewDetailActivity
import com.example.kaiyanproject.ui.ugcdetail.UgcDetailActivity
import com.example.kaiyanproject.util.ActionUrlUtil
import com.example.kaiyanproject.util.DateUtil
import com.example.kaiyanproject.util.GlobalUtil
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import de.hdodenhof.circleimageview.CircleImageView

class FollowAdapter(val fragment: FollowFragment) :
    PagingDataAdapter<Follow.Item, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun getItemCount() = super.getItemCount() + 1
    override fun getItemViewType(position: Int) = when {
        position == 0 -> Const.ItemViewType.CUSTOM_HEADER
        getItem(position - 1)!!.type == "autoPlayFollowCard" && getItem(position - 1)!!.data.dataType == "FollowCard" -> AUTO_PLAY_FOLLOW_CARD
        else -> Const.ItemViewType.UNKNOWN
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            Const.ItemViewType.CUSTOM_HEADER -> HeaderViewHolder(
                R.layout.item_community_follow_header_type.inflate(
                    parent
                )
            )

            AUTO_PLAY_FOLLOW_CARD -> AutoPlayFollowCardViewHolder(
                R.layout.item_community_auto_play_follow_card_follow_card_type.inflate(
                    parent
                )
            )

            else -> EmptyViewHolder(View(parent.context))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)!!
        when (holder) {
            is HeaderViewHolder -> holder.itemView.setOnClickListener { LoginActivity.start(fragment.activity) }
            is AutoPlayFollowCardViewHolder -> {
                val item = getItem(position - 1)!!
                item.data.content.data.run {
                    holder.ivAvatar.load(item.data.header.icon ?: author?.icon ?: "")

                    holder.tvReleaseTime.text = DateUtil.getDate(
                        releaseTime ?: author?.latestReleaseTime ?: System.currentTimeMillis(),
                        "HH:mm"
                    )
                    holder.tvTitle.text = title
                    holder.tvNickname.text = author?.name ?: ""
                    holder.tvContent.text = description
                    holder.tvCollectionCount.text = consumption.collectionCount.toString()
                    holder.tvReplyCount.text = consumption.replyCount.toString()
                    holder.tvVideoDuration.visible()    //视频播放后，复用tvVideoDuration直接隐藏了
                    holder.tvVideoDuration.text = duration.conversionVideoDuration()
                    CommendAdapter.startAutoPlay(
                        fragment.activity,
                        holder.videoPlayer,
                        position,
                        playUrl,
                        cover.feed,
                        TAG,
                        object : GSYSampleCallBack() {
                            override fun onPrepared(url: String?, vararg objects: Any?) {
                                super.onPrepared(url, *objects)
                                holder.tvVideoDuration.gone()
                                GSYVideoManager.instance().isNeedMute = true
                            }

                            override fun onClickResume(url: String?, vararg objects: Any?) {
                                super.onClickResume(url, *objects)
                                holder.tvVideoDuration.gone()
                            }

                            override fun onClickBlank(url: String?, vararg objects: Any?) {
                                super.onClickBlank(url, *objects)
                                holder.tvVideoDuration.visible()
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
                                        author!!,
                                        webUrl
                                    )
                                )
                            }
                        })
                    holder.let {
                        setOnClickListener(
                            it.videoPlayer.thumbImageView,
                            it.itemView,
                            it.ivCollectionCount,
                            it.tvCollectionCount,
                            it.ivFavorites,
                            it.tvFavorites,
                            it.ivShare
                        ) {
                            when (this) {
                                it.videoPlayer.thumbImageView, it.itemView -> {
                                    NewDetailActivity.start(
                                        fragment.activity, NewDetailActivity.VideoInfo(
                                            item.data.content.data.id,
                                            playUrl,
                                            title,
                                            description,
                                            category,
                                            library,
                                            consumption,
                                            cover,
                                            author!!,
                                            webUrl
                                        )
                                    )
                                }

                                it.ivCollectionCount, it.tvCollectionCount, it.ivFavorites, it.tvFavorites -> {
                                    LoginActivity.start(fragment.activity)
                                }

                                it.ivShare -> {
                                    showDialogShare(fragment.activity, getShareContent(item))
                                }
                            }
                        }
                    }
                }

            }

            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }
        }

    }

    private fun getShareContent(item: Follow.Item): String {
        item.data.content.data.run {
            val linkUrl =
                "${item.data.content.data.webUrl.raw}&utm_campaign=routine&utm_medium=share&utm_source=others&uid=0&resourceType=${resourceType}"
            return "${title}|${GlobalUtil.appName}：\n${linkUrl}"
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)
    class AutoPlayFollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvReleaseTime = view.findViewById<TextView>(R.id.tvReleaseTime)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvNickname = view.findViewById<TextView>(R.id.tvNickname)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val ivCollectionCount = view.findViewById<ImageView>(R.id.ivCollectionCount)
        val tvCollectionCount = view.findViewById<TextView>(R.id.tvCollectionCount)
        val ivReply = view.findViewById<ImageView>(R.id.ivReply)
        val tvReplyCount = view.findViewById<TextView>(R.id.tvReplyCount)
        val ivFavorites = view.findViewById<ImageView>(R.id.ivFavorites)
        val tvFavorites = view.findViewById<TextView>(R.id.tvFavorites)
        val tvVideoDuration = view.findViewById<TextView>(R.id.tvVideoDuration)
        val ivShare = view.findViewById<ImageView>(R.id.ivShare)
        val videoPlayer: GSYVideoPlayer = view.findViewById(R.id.videoPlayer)
    }

    companion object {
        const val TAG = "FollowAdapter"
        const val AUTO_PLAY_FOLLOW_CARD =
            Const.ItemViewType.MAX    //type:autoPlayFollowCard -> dataType:FollowCard

        private val DIFF_CALLBACK = object : ItemCallback<Follow.Item>() {
            override fun areItemsTheSame(
                oldItem: Follow.Item,
                newItem: Follow.Item
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Follow.Item,
                newItem: Follow.Item
            ): Boolean = oldItem == newItem

        }

    }

}