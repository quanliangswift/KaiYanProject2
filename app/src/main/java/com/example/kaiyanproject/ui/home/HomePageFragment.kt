package com.example.kaiyanproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kaiyanproject.R
import com.example.kaiyanproject.databinding.FragmentMainContainerBinding
import com.example.kaiyanproject.event.MessageEvent
import com.example.kaiyanproject.event.RefreshEvent
import com.example.kaiyanproject.event.SwitchPagesEvent
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.logic.model.TabEntity
import com.example.kaiyanproject.ui.common.ui.BaseViewPagerFragment
import com.example.kaiyanproject.ui.home.commend.CommendFragment
import com.example.kaiyanproject.ui.home.daily.DailyFragment
import com.example.kaiyanproject.ui.home.discovery.DiscoveryFragment
import com.flyco.tablayout.listener.CustomTabEntity
import org.greenrobot.eventbus.EventBus

class HomePageFragment : BaseViewPagerFragment() {
    private var _binding: FragmentMainContainerBinding? = null
    private val binding
        get() = _binding!!

    override val createFragments: Array<Fragment> = arrayOf(
        DiscoveryFragment.newInstance(),
        CommendFragment.newInstance(),
        DailyFragment.newInstance()
    )
    override val createTitles: ArrayList<CustomTabEntity> =
        arrayListOf(
            TabEntity(R.string.discovery.resString),
            TabEntity(R.string.commend.resString),
            TabEntity(R.string.daily.resString)
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainContainerBinding.inflate(layoutInflater, container, false)
        return super.onCreateView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleBar.ivCalendar.visibility = View.VISIBLE
        viewPager?.currentItem = 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && this::class.java == messageEvent.activityClass) {
            when (viewPager?.currentItem) {
                0 -> EventBus.getDefault().post(RefreshEvent(DiscoveryFragment::class.java))
                1 -> EventBus.getDefault().post(RefreshEvent(CommendFragment::class.java))
                2 -> EventBus.getDefault().post(RefreshEvent(DailyFragment::class.java))
            }
        } else if (messageEvent is SwitchPagesEvent) {
            when (messageEvent.activityClass) {
                DiscoveryFragment::class.java -> viewPager?.currentItem = 0
                CommendFragment::class.java -> viewPager?.currentItem = 1
                DailyFragment::class.java -> viewPager?.currentItem = 1
            }
        }
    }

    companion object {

        fun newInstance() = HomePageFragment()
    }
}