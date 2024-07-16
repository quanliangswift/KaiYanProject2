package com.example.kaiyanproject.ui.home.discovery

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kaiyanproject.logic.model.Discovery
import com.example.kaiyanproject.ui.common.holder.RecyclerViewHelp

class DiscoveryAdapter : PagingDataAdapter<Discovery.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }
}