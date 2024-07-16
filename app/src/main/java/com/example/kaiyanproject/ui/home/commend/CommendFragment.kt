package com.example.kaiyanproject.ui.home.commend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.kaiyanproject.databinding.FragmentRefreshLayoutBinding
import com.example.kaiyanproject.event.MessageEvent
import com.example.kaiyanproject.ui.common.ui.BaseFragment
import com.example.kaiyanproject.ui.common.ui.BaseViewPagerFragment
import com.example.kaiyanproject.ui.home.HomePageFragment
import com.example.kaiyanproject.util.InjectorUtil

class CommendFragment : BaseFragment() {
    private var _binding: FragmentRefreshLayoutBinding? = null

    private val binding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getHomePageCommendViewModelFactory()
        )[CommendViewModel::class.java]
    }

    private lateinit var adapter: CommendAdapter

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
    }

    override fun loadFinished() {
        super.loadFinished()
    }

    override fun loadFailed(msg: String?) {
        super.loadFailed(msg)
    }


    companion object {

        fun newInstance() = CommendFragment()
    }

}