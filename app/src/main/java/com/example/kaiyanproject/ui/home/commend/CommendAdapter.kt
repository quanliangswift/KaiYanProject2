package com.example.kaiyanproject.ui.home.commend

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kaiyanproject.logic.model.HomePageRecommend
import com.example.kaiyanproject.ui.common.holder.RecyclerViewHelp

class CommendAdapter(val fragment: CommendFragment) :
    PagingDataAdapter<HomePageRecommend.Item, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {
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
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int) = RecyclerViewHelp.getItemViewType(position)


    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        RecyclerViewHelp.getViewHolder(parent, viewType)
}