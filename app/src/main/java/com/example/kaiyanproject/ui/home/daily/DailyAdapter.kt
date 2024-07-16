package com.example.kaiyanproject.ui.home.daily

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaiyanproject.BuildConfig
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.R
import com.example.kaiyanproject.extension.conversionVideoDuration
import com.example.kaiyanproject.extension.gone
import com.example.kaiyanproject.extension.invisible
import com.example.kaiyanproject.extension.load
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.setOnClickListener
import com.example.kaiyanproject.extension.showDialogShare
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.extension.visible
import com.example.kaiyanproject.logic.model.Daily
import com.example.kaiyanproject.ui.common.holder.Banner3ViewHolder
import com.example.kaiyanproject.ui.common.holder.FollowCardViewHolder
import com.example.kaiyanproject.ui.common.holder.InformationCardFollowCardViewHolder
import com.example.kaiyanproject.ui.common.holder.RecyclerViewHelp
import com.example.kaiyanproject.ui.common.holder.TextCardViewFooter2ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewFooter3ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader5ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader7ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader8ViewHolder
import com.example.kaiyanproject.ui.home.commend.CommendAdapter
import com.example.kaiyanproject.ui.login.LoginActivity
import com.example.kaiyanproject.ui.newdetail.NewDetailActivity
import com.example.kaiyanproject.util.ActionUrlUtil
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class DailyAdapter(val fragment: DailyFragment) :
    PagingDataAdapter<Daily.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {


    override fun getItemViewType(position: Int) =
        RecyclerViewHelp.getItemViewType(getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHelp.getViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)!!
        when (holder) {
            is TextCardViewHeader5ViewHolder -> {
                holder.tvTitle5.text = item.data.text
                if (item.data.actionUrl != null) holder.ivInto5.visible() else holder.ivInto5.gone()
                if (item.data.follow != null) holder.tvFollow.visible() else holder.tvFollow.gone()
                holder.tvFollow.setOnClickListener { LoginActivity.start(fragment.activity) }
                setOnClickListener(
                    holder.tvTitle5,
                    holder.ivInto5
                ) { ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text) }
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
                ) { ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text) }
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

            is Banner3ViewHolder -> {
                holder.ivPicture.load(item.data.image, 4f)
                holder.ivAvatar.load(item.data.header.icon ?: "")
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

            is FollowCardViewHolder -> {
                holder.ivVideo.load(item.data.content.data.cover.feed, 4f)
                holder.ivAvatar.load(item.data.header.icon ?: "")
                holder.tvVideoDuration.text =
                    item.data.content.data.duration.conversionVideoDuration()
                holder.tvDescription.text = item.data.header.description
                holder.tvTitle.text = item.data.header.title
                if (item.data.content.data.ad) holder.tvLabel.visible() else holder.tvLabel.gone()
                if (item.data.content.data.library == DAILY_LIBRARY_TYPE) holder.ivChoiceness.visible() else holder.ivChoiceness.gone()
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

            is InformationCardFollowCardViewHolder -> {
                holder.ivCover.load(
                    item.data.backgroundImage,
                    4f,
                    RoundedCornersTransformation.CornerType.TOP
                )
                holder.recyclerView.setHasFixedSize(true)
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(CommendAdapter.InformationCardFollowCardItemDecoration())
                }
                holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
                holder.recyclerView.adapter = CommendAdapter.InformationCardFollowCardAdapter(
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

            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }
        }
    }

    companion object {
        const val TAG = "DailyAdapter"
        const val DEFAULT_LIBRARY_TYPE = "DEFAULT"
        const val NONE_LIBRARY_TYPE = "NONE"
        const val DAILY_LIBRARY_TYPE = "DAILY"
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Daily.Item>() {
            override fun areItemsTheSame(oldItem: Daily.Item, newItem: Daily.Item) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Daily.Item, newItem: Daily.Item) =
                oldItem == newItem

        }
    }
}