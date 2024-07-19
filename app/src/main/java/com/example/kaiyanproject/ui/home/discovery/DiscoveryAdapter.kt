package com.example.kaiyanproject.ui.home.discovery

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaiyanproject.BuildConfig
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.R
import com.example.kaiyanproject.extension.conversionVideoDuration
import com.example.kaiyanproject.extension.dp2px
import com.example.kaiyanproject.extension.gone
import com.example.kaiyanproject.extension.invisible
import com.example.kaiyanproject.extension.load
import com.example.kaiyanproject.extension.resDimensionPixelOffset
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.setOnClickListener
import com.example.kaiyanproject.extension.showDialogShare
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.extension.visible
import com.example.kaiyanproject.logic.model.Discovery
import com.example.kaiyanproject.ui.common.holder.AutoPlayVideoAdViewHolder
import com.example.kaiyanproject.ui.common.holder.Banner3ViewHolder
import com.example.kaiyanproject.ui.common.holder.BannerViewHolder
import com.example.kaiyanproject.ui.common.holder.ColumnCardListViewHolder
import com.example.kaiyanproject.ui.common.holder.FollowCardViewHolder
import com.example.kaiyanproject.ui.common.holder.HorizontalScrollCardViewHolder
import com.example.kaiyanproject.ui.common.holder.RecyclerViewHelp
import com.example.kaiyanproject.ui.common.holder.SpecialSquareCardCollectionViewHolder
import com.example.kaiyanproject.ui.common.holder.TagBriefCardViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewFooter2ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewFooter3ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader5ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader7ViewHolder
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader8ViewHolder
import com.example.kaiyanproject.ui.common.holder.TopicBriefCardViewHolder
import com.example.kaiyanproject.ui.common.holder.VideoSmallCardViewHolder
import com.example.kaiyanproject.ui.common.view.GridListItemDecoration
import com.example.kaiyanproject.ui.home.commend.CommendAdapter
import com.example.kaiyanproject.ui.home.daily.DailyAdapter
import com.example.kaiyanproject.ui.login.LoginActivity
import com.example.kaiyanproject.ui.newdetail.NewDetailActivity
import com.example.kaiyanproject.util.ActionUrlUtil
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

class DiscoveryAdapter(val fragment: DiscoveryFragment) :
    PagingDataAdapter<Discovery.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

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

            is FollowCardViewHolder -> {
                holder.ivVideo.load(item.data.content.data.cover.feed, 4f)
                holder.ivAvatar.load(item.data.header.icon)
                holder.tvVideoDuration.text =
                    item.data.content.data.duration.conversionVideoDuration()
                holder.tvDescription.text = item.data.header.description
                holder.tvTitle.text = item.data.header.title
                if (item.data.content.data.ad) holder.tvLabel.visible() else holder.tvLabel.gone()
                if (item.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) holder.ivChoiceness.visible() else holder.ivChoiceness.gone()
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

            is HorizontalScrollCardViewHolder -> {
                holder.bannerViewPager.run {
                    setCanLoop(false)
                    setRoundCorner(4f.dp2px)
                    setRevealWidth(R.dimen.listSpaceSize.resDimensionPixelOffset)
                    if (item.data.itemList.size == 1) setPageMargin(0) else setPageMargin(4f.dp2px)
                    setIndicatorVisibility(View.GONE)
                    setAdapter(HorizontalScrollCardAdapter())
                    removeDefaultPageTransformer()
                    setOnPageClickListener { position ->
                        ActionUrlUtil.process(
                            fragment,
                            item.data.itemList[position].data.actionUrl,
                            item.data.itemList[position].data.title
                        )
                    }
                    create(item.data.itemList)
                }
            }

            is SpecialSquareCardCollectionViewHolder -> {
                holder.tvTitle.text = item.data.header.title
                holder.tvRightText.text = item.data.header.rightText
                setOnClickListener(
                    holder.tvRightText,
                    holder.ivInto
                ) { "${item.data.header.rightText},${R.string.currently_not_supported.resString}".showToast() }
                holder.recyclerView.setHasFixedSize(true)
                holder.recyclerView.isNestedScrollingEnabled = true
                holder.recyclerView.layoutManager = GridLayoutManager(fragment.activity, 2).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(SpecialSquareCardCollectionItemDecoration())
                }
                val list =
                    item.data.itemList.filter { it.type == "squareCardOfCategory" && it.data.dataType == "SquareCard" }
                holder.recyclerView.adapter = SpecialSquareCardCollectionAdapter(list)
            }

            is ColumnCardListViewHolder -> {
                holder.tvTitle.text = item.data.header.title
                holder.tvRightText.text = item.data.header.rightText
                setOnClickListener(
                    holder.tvRightText,
                    holder.ivInto
                ) { "${item.data.header.rightText},${R.string.currently_not_supported.resString}".showToast() }
                holder.recyclerView.setHasFixedSize(true)
                holder.recyclerView.layoutManager = GridLayoutManager(fragment.activity, 2)
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(GridListItemDecoration(2))
                }
                val list =
                    item.data.itemList.filter { it.type == "squareCardOfColumn" && it.data.dataType == "SquareCard" }
                holder.recyclerView.adapter = ColumnCardListAdapter(list)
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

            is VideoSmallCardViewHolder -> {
                holder.ivPicture.load(item.data.cover.feed, 4f)
                holder.tvDescription.text =
                    if (item.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${item.data.category} / 开眼精选" else "#${item.data.category}"
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
                    CommendAdapter.startAutoPlay(
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

    class HorizontalScrollCardAdapter :
        BaseBannerAdapter<Discovery.ItemX, HorizontalScrollCardAdapter.ViewHolder>() {
        class ViewHolder(val view: View) : BaseViewHolder<Discovery.ItemX>(view) {
            override fun bindData(data: Discovery.ItemX?, position: Int, pageSize: Int) {
                val ivPicture = findView<ImageView>(R.id.ivPicture)
                val tvLabel = findView<TextView>(R.id.tvLabel)
                if (data?.data?.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
                tvLabel.text = data?.data?.label?.text ?: ""
                data?.data?.image?.let {
                    ivPicture.load(it, 4f)
                }

            }
        }

        override fun getLayoutId(viewType: Int) = R.layout.item_banner_item_type
        override fun onBind(
            holder: ViewHolder?,
            data: Discovery.ItemX?,
            position: Int,
            pageSize: Int
        ) {
            holder?.bindData(data, position, pageSize)
        }

        override fun createViewHolder(itemView: View?, viewType: Int) = ViewHolder(itemView!!)
    }

    class SpecialSquareCardCollectionAdapter(val dataList: List<Discovery.ItemX>) :
        RecyclerView.Adapter<SpecialSquareCardCollectionAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivPicture = view.findViewById<ImageView>(R.id.ivPicture)
            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        }

        override fun getItemCount() = dataList.count()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_special_square_card_collection_type, parent, false)
        )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.ivPicture.load(item.data.image, 4f)
            holder.tvTitle.text = item.data.title
            holder.itemView.setOnClickListener { "${item.data.title}, ${R.string.currently_not_supported.resString}".showToast() }
        }

    }

    class ColumnCardListAdapter(val dataList: List<Discovery.ItemX>) :
        RecyclerView.Adapter<ColumnCardListAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivPicture = view.findViewById<ImageView>(R.id.ivPicture)
            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_column_card_list_type_item, parent, false)
            )
        }

        override fun getItemCount() = dataList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.ivPicture.load(item.data.image, 4f)
            holder.tvTitle.text = item.data.title
            holder.itemView.setOnClickListener { "${item.data.title},${R.string.currently_not_supported.resString}".showToast() }
        }
    }

    class SpecialSquareCardCollectionItemDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val count = parent.adapter?.itemCount //item count
            val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
            val spanCount = 2
            val lastRowFirstItemPostion = count?.minus(spanCount)   //最后一行,第一个item索引
            val space = 2f.dp2px
            val rightCountSpace = 14f.dp2px

            when (spanIndex) {
                0 -> {
                    outRect.bottom = space
                }

                spanCount - 1 -> {
                    outRect.top = space
                }

                else -> {
                    outRect.top = space
                    outRect.bottom = space
                }
            }
            when {
                position < spanCount -> {
                    outRect.right = space
                }

                position < lastRowFirstItemPostion!! -> {
                    outRect.left = space
                    outRect.right = space
                }

                else -> {
                    outRect.left = space
                    outRect.right = rightCountSpace
                }
            }
        }
    }

    companion object {
        const val TAG = "DiscoveryAdapter"
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Discovery.Item>() {
            override fun areItemsTheSame(
                oldItem: Discovery.Item,
                newItem: Discovery.Item
            ) = newItem.id == oldItem.id

            override fun areContentsTheSame(
                oldItem: Discovery.Item,
                newItem: Discovery.Item
            ) = oldItem == newItem
        }
    }

}