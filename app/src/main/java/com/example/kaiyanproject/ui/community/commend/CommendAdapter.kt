package com.example.kaiyanproject.ui.community.commend

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kaiyanproject.BuildConfig
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.R
import com.example.kaiyanproject.event.RefreshEvent
import com.example.kaiyanproject.event.SwitchPagesEvent
import com.example.kaiyanproject.extension.conversionVideoDuration
import com.example.kaiyanproject.extension.dp2px
import com.example.kaiyanproject.extension.gone
import com.example.kaiyanproject.extension.inflate
import com.example.kaiyanproject.extension.invisible
import com.example.kaiyanproject.extension.load
import com.example.kaiyanproject.extension.logW
import com.example.kaiyanproject.extension.resDimensionPixelOffset
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.setOnClickListener
import com.example.kaiyanproject.extension.showDialogShare
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.extension.visible
import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.logic.model.DrawableDirection
import com.example.kaiyanproject.logic.model.HomePageRecommend
import com.example.kaiyanproject.logic.model.setDrawable
import com.example.kaiyanproject.ui.common.holder.AutoPlayVideoAdViewHolder
import com.example.kaiyanproject.ui.common.holder.Banner3ViewHolder
import com.example.kaiyanproject.ui.common.holder.BannerViewHolder
import com.example.kaiyanproject.ui.common.holder.EmptyViewHolder
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
import com.example.kaiyanproject.ui.home.daily.DailyAdapter
import com.example.kaiyanproject.ui.login.LoginActivity
import com.example.kaiyanproject.ui.newdetail.NewDetailActivity
import com.example.kaiyanproject.ui.ugcdetail.UgcDetailActivity
import com.example.kaiyanproject.util.ActionUrlUtil
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.greenrobot.eventbus.EventBus

class CommendAdapter(val fragment: CommendFragment) :
    PagingDataAdapter<CommunityRecommend.Item, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item?.type) {
            STR_HORIZONTAL_SCROLLCARD_TYPE -> {
                when (item.data.dataType) {
                    STR_ITEM_COLLECTION_DATA_TYPE -> HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE
                    STR_ITEM_COLLECTION_DATA_TYPE -> HORIZONTAL_SCROLLCARD_TYPE
                    else -> Const.ItemViewType.UNKNOWN
                }
            }

            STR_COMMUNITY_COLUMNS_CARD -> {
                if (item.data.dataType == STR_FOLLOW_CARD_DATA_TYPE) FOLLOW_CARD_TYPE
                else Const.ItemViewType.UNKNOWN
            }

            else -> Const.ItemViewType.UNKNOWN
        }
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE -> HorizontalScrollcardItemCollectionViewHolder(
                R.layout.item_community_horizontal_scrollcard_item_collection_type.inflate(parent)
            )

            HORIZONTAL_SCROLLCARD_TYPE -> HorizontalScrollcardViewHolder(
                R.layout.item_community_horizontal_scrollcard_type.inflate(
                    parent
                )
            )

            FOLLOW_CARD_TYPE -> FollowCardViewHolder(
                R.layout.item_community_columns_card_follow_card_type.inflate(
                    parent
                )
            )

            else -> EmptyViewHolder(View(parent.context))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)!!
        when (holder) {
            is HorizontalScrollcardItemCollectionViewHolder -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true
                holder.recyclerView.layoutManager = LinearLayoutManager(fragment.activity).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                }
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(
                        SquareCardOfCommunityContentItemDecoration(
                            this.fragment
                        )
                    )
                }
                holder.recyclerView.adapter =
                    SquareCardOfCommunityContentAdapter(this.fragment, item.data.itemList)
            }

            is HorizontalScrollcardViewHolder -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true
                holder.bannerViewPager.run {
                    setCanLoop(true)
                    setRoundCorner(4f.dp2px)
                    setRevealWidth(0, R.dimen.listSpaceSize.resDimensionPixelOffset)
                    if (item.data.itemList.size == 1) setPageMargin(0) else setPageMargin(4f.dp2px)
                    setIndicatorVisibility(View.GONE)
                    removeDefaultPageTransformer()
                    setAdapter(BannerAdapter())
                    setOnPageClickListener {
                        ActionUrlUtil.process(
                            fragment,
                            item.data.itemList[it].data.actionUrl,
                            item.data.itemList[it].data.title
                        )
                    }
                    create(item.data.itemList)
                }
            }

            is FollowCardViewHolder -> {
                holder.tvChoiceness.gone()
                holder.ivPlay.gone()
                holder.ivLayers.gone()
                if (item.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) holder.tvChoiceness.visible()
                if ((item.data.header?.icon ?: "".trim()) == "round") {
                    holder.ivAvatar.invisible()
                    holder.ivRoundAvatar.visible()
                    holder.ivRoundAvatar.load(item.data.content.data.owner.avatar)
                } else {
                    holder.ivRoundAvatar.invisible()
                    holder.ivAvatar.visible()
                    holder.ivAvatar.load(item.data.content.data.owner.avatar)
                }
                holder.ivBgPicture.run {
                    val imageHeight = calculateImageHeight(
                        item.data.content.data.width,
                        item.data.content.data.height
                    )
                    layoutParams.width = fragment.maxImageWidth
                    layoutParams.height = imageHeight
                    load(item.data.content.data.cover.feed, 4f)
                }
                holder.tvCollectionCount.text =
                    item.data.content.data.consumption.collectionCount.toString()
                val drawable = ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_favorite_border_black_20dp
                )
                holder.tvCollectionCount.setDrawable(drawable, 17f, 17f, DrawableDirection.RIGHT)
                holder.tvDescription.text = item.data.content.data.description
                holder.tvNickName.text = item.data.content.data.owner.nickname
                when (item.data.content.type) {
                    STR_VIDEO_TYPE -> {
                        holder.ivPlay.visible()
                        holder.itemView.setOnClickListener {
                            val items =
                                snapshot().filter { it!!.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
                            UgcDetailActivity.start(fragment.activity, items.map { it!! }, item)
                        }
                    }

                    STR_UGC_PICTURE_TYPE -> {
                        if (!item.data.content.data.urls.isNullOrEmpty() && item.data.content.data.urls.size > 1) holder.ivLayers.visible()
                        holder.itemView.setOnClickListener {
                            val items =
                                snapshot().filter { it!!.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
                            UgcDetailActivity.start(fragment.activity, items.map { it!! }, item)
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

    /**
     * 根据屏幕比例计算图片高
     *
     * @param originalWidth   服务器图片原始尺寸：宽
     * @param originalHeight  服务器图片原始尺寸：高
     * @return 根据比例缩放后的图片高
     */
    private fun calculateImageHeight(originalWidth: Int, originalHeight: Int): Int {
        //服务器数据异常处理
        if (originalWidth == 0 || originalHeight == 0) {
            logW(TAG, R.string.image_size_error.resString)
            return fragment.maxImageWidth
        }
        return fragment.maxImageWidth * originalHeight / originalWidth
    }

    class HorizontalScrollcardItemCollectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
    }

    class SquareCardOfCommunityContentAdapter(
        val fragment: CommendFragment,
        var dataList: List<CommunityRecommend.ItemX>
    ) :
        RecyclerView.Adapter<SquareCardOfCommunityContentAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivBgPicture: ImageView = view.findViewById(R.id.ivBgPicture)
            val tvTitle: TextView = view.findViewById(R.id.tvTitle)
            val tvSubTitle: TextView = view.findViewById(R.id.tvSubTitle)
        }

        override fun getItemCount() = dataList.size

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SquareCardOfCommunityContentAdapter.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_community_horizontal_scroll_card_itemcollection_item_type,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(
            holder: SquareCardOfCommunityContentAdapter.ViewHolder,
            position: Int
        ) {
            val item = dataList[position]
            holder.ivBgPicture.layoutParams.width = fragment.maxImageWidth
            holder.ivBgPicture.load(item.data.bgPicture)
            holder.tvTitle.text = item.data.title
            holder.tvSubTitle.text = item.data.subTitle
            holder.itemView.setOnClickListener {
                ActionUrlUtil.process(
                    fragment,
                    item.data.actionUrl,
                    item.data.title
                )
            }
        }
    }

    class SquareCardOfCommunityContentItemDecoration(val fragment: CommendFragment) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val count = parent.adapter?.itemCount?.minus(1) //item count

            when (position) {
                0 -> {
                    /*outRect.left = fragment.bothSideSpace*/
                    outRect.right = fragment.middleSpace
                }

                count -> {
                    outRect.left = fragment.middleSpace
                    /*outRect.right = fragment.bothSideSpace*/
                }

                else -> {
                    outRect.left = fragment.middleSpace
                    outRect.right = fragment.middleSpace
                }
            }
        }
    }

    class HorizontalScrollcardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bannerViewPager =
            view.findViewById<BannerViewPager<CommunityRecommend.ItemX, BannerAdapter.ViewHolder>>(R.id.bannerViewPager)
    }

    class BannerAdapter : BaseBannerAdapter<CommunityRecommend.ItemX, BannerAdapter.ViewHolder>() {

        class ViewHolder(val view: View) : BaseViewHolder<CommunityRecommend.ItemX>(view) {

            override fun bindData(item: CommunityRecommend.ItemX, position: Int, pageSize: Int) {
                val ivPicture = findView<ImageView>(R.id.ivPicture)
                val tvLabel = findView<TextView>(R.id.tvLabel)
                if (item.data.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
                tvLabel.text = item.data.label?.text ?: ""
                ivPicture.load(item.data.image, 4f)
            }
        }

        override fun getLayoutId(viewType: Int): Int {
            return R.layout.item_banner_item_type
        }

        override fun createViewHolder(view: View, viewType: Int): ViewHolder {
            return ViewHolder(view)
        }

        override fun onBind(
            holder: ViewHolder,
            data: CommunityRecommend.ItemX,
            position: Int,
            pageSize: Int
        ) {
            holder.bindData(data, position, pageSize)
        }
    }

    class FollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBgPicture: ImageView = view.findViewById(R.id.ivBgPicture)
        val tvChoiceness: TextView = view.findViewById(R.id.tvChoiceness)
        val ivLayers: ImageView = view.findViewById(R.id.ivLayers)
        val ivPlay: ImageView = view.findViewById(R.id.ivPlay)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val ivRoundAvatar: CircleImageView = view.findViewById(R.id.ivRoundAvatar)
        val tvNickName: TextView = view.findViewById(R.id.tvNickName)
        val tvCollectionCount: TextView = view.findViewById(R.id.tvCollectionCount)
    }

    /**
     * 社区整个垂直列表的间隙
     */
    class ItemDecoration(val fragment: CommendFragment) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex

            outRect.top = fragment.bothSideSpace

            when (spanIndex) {
                0 -> {
                    outRect.left = fragment.bothSideSpace
                    outRect.right = fragment.middleSpace
                }

                else -> {
                    outRect.left = fragment.middleSpace
                    outRect.right = fragment.bothSideSpace
                }
            }
        }
    }

    companion object {
        const val TAG = "CommendAdapter"
        const val STR_HORIZONTAL_SCROLLCARD_TYPE = "horizontalScrollCard"
        const val STR_COMMUNITY_COLUMNS_CARD = "communityColumnsCard"

        const val STR_HORIZONTAL_SCROLLCARD_DATA_TYPE = "HorizontalScrollCard"
        const val STR_ITEM_COLLECTION_DATA_TYPE = "ItemCollection"
        const val STR_FOLLOW_CARD_DATA_TYPE = "FollowCard"

        const val STR_VIDEO_TYPE = "video"
        const val STR_UGC_PICTURE_TYPE = "ugcPicture"

        const val HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE =
            1   //type:horizontalScrollCard -> dataType:ItemCollection
        const val HORIZONTAL_SCROLLCARD_TYPE =
            2                   //type:horizontalScrollCard -> dataType:HorizontalScrollCard
        const val FOLLOW_CARD_TYPE = 3

        private val DIFF_CALLBACK = object : ItemCallback<CommunityRecommend.Item>() {
            override fun areItemsTheSame(
                oldItem: CommunityRecommend.Item,
                newItem: CommunityRecommend.Item
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CommunityRecommend.Item,
                newItem: CommunityRecommend.Item
            ): Boolean = oldItem == newItem

        }

    }

}