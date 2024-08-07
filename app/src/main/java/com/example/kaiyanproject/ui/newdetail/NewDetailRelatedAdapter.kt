package com.example.kaiyanproject.ui.newdetail

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kaiyanproject.BuildConfig
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.R
import com.example.kaiyanproject.extension.conversionVideoDuration
import com.example.kaiyanproject.extension.gone
import com.example.kaiyanproject.extension.inflate
import com.example.kaiyanproject.extension.load
import com.example.kaiyanproject.extension.setOnClickListener
import com.example.kaiyanproject.extension.showDialogShare
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.extension.visible
import com.example.kaiyanproject.logic.model.VideoRelated
import com.example.kaiyanproject.ui.common.holder.RecyclerViewHelp
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader4ViewHolder
import com.example.kaiyanproject.ui.common.holder.VideoSmallCardViewHolder
import com.example.kaiyanproject.ui.home.daily.DailyAdapter
import com.example.kaiyanproject.ui.login.LoginActivity

class NewDetailRelatedAdapter(
    private val activity: NewDetailActivity,
    val dataList: List<VideoRelated.Item>,
    private var videoInfoData: NewDetailActivity.VideoInfo?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        Const.ItemViewType.CUSTOM_HEADER -> CustomHeaderViewHolder(
            R.layout.item_new_detail_custom_header_type.inflate(
                parent
            )
        )

        SIMPLE_HOT_REPLY_CARD_TYPE -> SimpleHotReplyCardViewHolder(View(parent.context))
        else -> RecyclerViewHelp.getViewHolder(parent, viewType)
    }

    override fun getItemCount() = dataList.count() + 1
    override fun getItemViewType(position: Int) = when {
        position == 0 -> Const.ItemViewType.CUSTOM_HEADER
        dataList[position - 1].type == "simpleHotReplyScrollCard" && dataList[position - 1].data.dataType == "ItemCollection" -> SIMPLE_HOT_REPLY_CARD_TYPE
        else -> RecyclerViewHelp.getItemViewType(dataList[position - 1])
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomHeaderViewHolder -> {
                videoInfoData?.let {
                    holder.run {
                        groupAuthor.gone()
                        tvTitle.text = videoInfoData?.title
                        tvCategory.text =
                            if (videoInfoData?.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${videoInfoData?.category} / 开眼精选" else "#${videoInfoData?.category}"
                        tvDescription.text = videoInfoData?.description
                        tvCollectionCount.text =
                            videoInfoData?.consumption?.collectionCount.toString()
                        tvShareCount.text = videoInfoData?.consumption?.shareCount.toString()
                        videoInfoData?.author?.run {
                            groupAuthor.visible()
                            ivAvatar.load(videoInfoData?.author?.icon ?: "")
                            tvAuthorDescription.text = videoInfoData?.author?.description
                            tvAuthorName.text = videoInfoData?.author?.name
                        }
                        setOnClickListener(
                            ivCollectionCount,
                            tvCollectionCount,
                            ivShare,
                            tvShareCount,
                            ivCache,
                            tvCache,
                            ivFavorites,
                            tvFavorites,
                            tvFollow
                        ) {
                            when (this) {
                                ivCollectionCount, tvCollectionCount, ivFavorites, tvFavorites -> LoginActivity.start(
                                    activity
                                )

                                ivShare, tvShareCount -> showDialogShare(
                                    activity,
                                    "${videoInfoData?.title}：${videoInfoData?.webUrl?.raw}"
                                )

                                ivCache, tvCache -> R.string.currently_not_supported.showToast()
                                tvFollow -> LoginActivity.start(activity)
                            }
                        }
                    }
                }
            }

            is SimpleHotReplyCardViewHolder -> {

            }

            is TextCardViewHeader4ViewHolder -> {
                val item = dataList[position - 1]
                holder.tvTitle4.text = item.data.text
            }

            is VideoSmallCardViewHolder -> {
                val item = dataList[position - 1]
                holder.ivPicture.load(item.data.cover.feed, 4f)
                holder.tvDescription.text =
                    if (item.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${item.data.category} / 开眼精选" else "#${item.data.category}"
                holder.tvDescription.setTextColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.whiteAlpha35
                    )
                )
                holder.tvTitle.text = item.data.title
                holder.tvTitle.setTextColor(ContextCompat.getColor(activity, R.color.white))
                holder.tvVideoDuration.text = item.data.duration.conversionVideoDuration()
                holder.ivShare.setOnClickListener {
                    showDialogShare(
                        activity,
                        "${item.data.title}：${item.data.webUrl.raw}"
                    )
                }
                holder.itemView.setOnClickListener {
                    item.data.run {
                        NewDetailActivity.start(
                            activity,
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
                        activity.scrollTop()
                    }
                }
            }

            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }

        }
    }

    fun bindVideoInfo(videoInfoData: NewDetailActivity.VideoInfo?) {
        this.videoInfoData = videoInfoData
    }

    class CustomHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvCategory = view.findViewById<TextView>(R.id.tvCategory)
        val ivFoldText = view.findViewById<ImageView>(R.id.ivFoldText)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val ivCollectionCount = view.findViewById<ImageView>(R.id.ivCollectionCount)
        val tvCollectionCount = view.findViewById<TextView>(R.id.tvCollectionCount)
        val ivShare = view.findViewById<ImageView>(R.id.ivShare)
        val tvShareCount = view.findViewById<TextView>(R.id.tvShareCount)
        val ivCache = view.findViewById<ImageView>(R.id.ivCache)
        val tvCache = view.findViewById<TextView>(R.id.tvCache)
        val ivFavorites = view.findViewById<ImageView>(R.id.ivFavorites)
        val tvFavorites = view.findViewById<TextView>(R.id.tvFavorites)
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvAuthorDescription = view.findViewById<TextView>(R.id.tvAuthorDescription)
        val tvAuthorName = view.findViewById<TextView>(R.id.tvAuthorName)
        val tvFollow = view.findViewById<TextView>(R.id.tvFollow)
        val groupAuthor = view.findViewById<Group>(R.id.groupAuthor)
    }

    class SimpleHotReplyCardViewHolder(view: View) : RecyclerView.ViewHolder(view)
    companion object {
        const val TAG = "NewDetailRelatedAdapter"
        const val SIMPLE_HOT_REPLY_CARD_TYPE = Const.ItemViewType.MAX
    }
}