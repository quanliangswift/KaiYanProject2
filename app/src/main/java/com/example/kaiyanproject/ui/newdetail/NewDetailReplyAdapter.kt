package com.example.kaiyanproject.ui.newdetail

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.example.kaiyanproject.BuildConfig
import com.example.kaiyanproject.Const
import com.example.kaiyanproject.R
import com.example.kaiyanproject.extension.dp2px
import com.example.kaiyanproject.extension.gone
import com.example.kaiyanproject.extension.inflate
import com.example.kaiyanproject.extension.load
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.setOnClickListener
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.extension.visible
import com.example.kaiyanproject.logic.model.VideoReplies
import com.example.kaiyanproject.ui.common.holder.RecyclerViewHelp
import com.example.kaiyanproject.ui.common.holder.TextCardViewHeader4ViewHolder
import com.example.kaiyanproject.util.DateUtil

class NewDetailReplyAdapter(
    val activity: NewDetailActivity,
    val dataList: List<VideoReplies.Item>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        REPLY_BEAN_FOR_CLIENT_TYPE -> ReplyViewHolder(
            R.layout.item_new_detail_reply_type.inflate(
                parent
            )
        )

        else -> RecyclerViewHelp.getViewHolder(parent, viewType)
    }

    override fun getItemCount() = dataList.count()

    override fun getItemViewType(position: Int) = when {
        dataList[position].type == "reply" && dataList[position].data.dataType == "ReplyBeanForClient" -> REPLY_BEAN_FOR_CLIENT_TYPE
        else -> RecyclerViewHelp.getItemViewType(dataList[position])
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        when (holder) {
            is TextCardViewHeader4ViewHolder -> {
                holder.tvTitle4.text = item.data.text
                if (item.data.actionUrl != null && item.data.actionUrl.startsWith(Const.ActionUrl.REPLIES_HOT)) {
                    //热门评论
                    holder.ivInto4.visible()
                    holder.tvTitle4.layoutParams =
                        (holder.tvTitle4.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(
                                0,
                                30f.dp2px,
                                0,
                                6f.dp2px
                            )
                        }
                    setOnClickListener(
                        holder.tvTitle4,
                        holder.ivInto4
                    ) { R.string.currently_not_supported.showToast() }
                } else {
                    //相关推荐、最新评论
                    holder.tvTitle4.layoutParams =
                        (holder.tvTitle4.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(
                                0,
                                24f.dp2px,
                                0,
                                6f.dp2px
                            )
                        }
                    holder.ivInto4.gone()
                }
            }

            is ReplyViewHolder -> {
                holder.groupReply.gone()
                holder.ivAvatar.load(item.data.user?.avatar ?: "")
                holder.tvNickName.text = item.data.user?.nickname
                holder.tvLikeCount.text =
                    if (item.data.likeCount == 0) "" else item.data.likeCount.toString()
                holder.tvMessage.text = item.data.message
                holder.tvTime.text = getTimeReply(item.data.createTime)
                holder.ivLike.setOnClickListener { R.string.currently_not_supported.showToast() }

                item.data.parentReply?.run {
                    holder.groupReply.visible()
                    holder.tvReplyUser.text =
                        String.format(R.string.reply_target.resString, user?.nickname)
                    holder.ivReplyAvatar.load(user?.avatar ?: "")
                    holder.tvReplyNickName.text = user?.nickname
                    holder.tvReplyMessage.text = message
                    holder.tvShowConversation.setOnClickListener { R.string.currently_not_supported.showToast() }
                }
            }

            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }
        }
    }

    private fun getTimeReply(dateMillis: Long): String {
        val currentMills = System.currentTimeMillis()
        val timePast = currentMills - dateMillis
        if (timePast > -DateUtil.MINUTE) {
            when {
                timePast < DateUtil.MINUTE -> {
                    return DateUtil.getDate(dateMillis, "HH:mm")
                }

                else -> {
                    return DateUtil.getDate(dateMillis, "yyyy/MM/dd")
                }
            }
        } else {
            return DateUtil.getDate(dateMillis, "yyyy/MM/dd HH:mm")
        }
    }

    class ReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvNickName = view.findViewById<TextView>(R.id.tvNickName)
        val ivLike = view.findViewById<ImageView>(R.id.ivLike)
        val tvLikeCount = view.findViewById<TextView>(R.id.tvLikeCount)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)

        val groupReply = view.findViewById<Group>(R.id.groupReply)
        val tvReplyUser = view.findViewById<TextView>(R.id.tvReplyUser)
        val ivReplyAvatar = view.findViewById<ImageView>(R.id.ivReplyAvatar)
        val tvReplyNickName = view.findViewById<TextView>(R.id.tvReplyNickName)
        val tvReplyMessage = view.findViewById<TextView>(R.id.tvReplyMessage)
        val tvShowConversation = view.findViewById<TextView>(R.id.tvShowConversation)

    }

    companion object {
        const val TAG = "NewDetailReplyAdapter"
        const val REPLY_BEAN_FOR_CLIENT_TYPE = Const.ItemViewType.MAX
    }
}