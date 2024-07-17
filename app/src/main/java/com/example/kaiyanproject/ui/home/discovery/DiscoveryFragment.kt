package com.example.kaiyanproject.ui.home.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaiyanproject.R
import com.example.kaiyanproject.databinding.FragmentRefreshLayoutBinding
import com.example.kaiyanproject.event.MessageEvent
import com.example.kaiyanproject.event.RefreshEvent
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.logic.MainPageRepository
import com.example.kaiyanproject.ui.common.ui.BaseFragment
import com.example.kaiyanproject.ui.common.ui.FooterAdapter
import com.example.kaiyanproject.ui.home.commend.CommendFragment
import com.example.kaiyanproject.util.InjectorUtil
import com.example.kaiyanproject.util.ResponseHandler
import com.scwang.smart.refresh.layout.constant.RefreshState
import kotlinx.coroutines.launch

class DiscoveryFragment : BaseFragment() {
    private var _binding: FragmentRefreshLayoutBinding? = null
    val binding: FragmentRefreshLayoutBinding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getDiscoveryViewModelFactory()
        ).get(DiscoveryViewModel::class.java)
    }

    private lateinit var adapter: DiscoveryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRefreshLayoutBinding.inflate(layoutInflater, container, false)
        return super.onCreateView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DiscoveryAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter.withLoadStateFooter(FooterAdapter(adapter::retry))
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.itemAnimator = null
        binding.refreshLayout.setOnRefreshListener { adapter.refresh() }
        addLoadStateListener()

        lifecycleScope.launch {
            viewModel.getPagingData().collect {
                adapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun loadFinished() {
        super.loadFinished()
        binding.refreshLayout.finishRefresh()
    }

    override fun loadFailed(msg: String?) {
        super.loadFailed(msg)
        binding.refreshLayout.finishRefresh()
        showLoadErrorView(msg ?: R.string.unknown_error.resString) {
            startLoading()
            adapter.refresh()
        }
    }

    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && messageEvent.javaClass == this.javaClass) {
            binding.refreshLayout.autoRefresh()
            if ((binding.recyclerView.adapter?.itemCount ?: 0) > 0)
                binding.recyclerView.scrollToPosition(0)
        }
    }

    private fun addLoadStateListener() {
        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    loadFinished()
                    if (it.source.append.endOfPaginationReached) {
                        binding.refreshLayout.setEnableLoadMore(true)
                        binding.refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        binding.refreshLayout.setEnableLoadMore(false)
                    }
                }

                is LoadState.Loading -> {
                    if (binding.refreshLayout.state != RefreshState.Refreshing) {
                        startLoading()
                    }
                }

                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    loadFailed(ResponseHandler.getFailureTips(state.error))
                }
            }
        }
        adapter.addLoadStateListener {
            when (it.append) {
                is LoadState.NotLoading -> {
                    if (it.source.append.endOfPaginationReached) {
                        binding.refreshLayout.setEnableLoadMore(true)
                        binding.refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        binding.refreshLayout.setEnableLoadMore(false)
                    }
                }

                is LoadState.Loading -> {

                }

                is LoadState.Error -> {
                    val state = it.append as LoadState.Error
                    ResponseHandler.getFailureTips(state.error).showToast()
                }
            }
        }
    }

    companion object {

        fun newInstance() = DiscoveryFragment()
    }
}