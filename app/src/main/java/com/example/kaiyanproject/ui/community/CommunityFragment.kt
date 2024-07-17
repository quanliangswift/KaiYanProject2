package com.example.kaiyanproject.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kaiyanproject.R
import com.example.kaiyanproject.event.MessageEvent
import com.example.kaiyanproject.event.RefreshEvent
import com.example.kaiyanproject.event.SwitchPagesEvent
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.logic.model.TabEntity
import com.example.kaiyanproject.ui.common.ui.BaseViewPagerFragment
import com.example.kaiyanproject.ui.community.commend.CommendFragment
import com.example.kaiyanproject.ui.community.follow.FollowFragment
import com.example.kaiyanproject.ui.home.HomePageFragment
import com.flyco.tablayout.listener.CustomTabEntity
import org.greenrobot.eventbus.EventBus

class CommunityFragment : BaseViewPagerFragment() {
    override val createFragments: Array<Fragment> =
        arrayOf(CommendFragment.newInstance(), FollowFragment.newInstance())
    override val createTitles: ArrayList<CustomTabEntity> =
        arrayListOf(TabEntity(R.string.commend.resString), TabEntity(R.string.follow.resString))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(
            inflater.inflate(
                R.layout.fragment_main_container,
                container,
                false
            )
        )
    }

    override fun onMessageEvent(messageEvent: MessageEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && this.javaClass == messageEvent.activityClass) {
            when (viewPager?.currentItem) {
                0 -> EventBus.getDefault().post(RefreshEvent(CommendFragment::class.java))
                1 -> EventBus.getDefault().post(RefreshEvent(FollowFragment::class.java))
            }
        } else if (messageEvent is SwitchPagesEvent) {
            when (messageEvent.activityClass) {
                CommendFragment::class.java -> viewPager?.currentItem = 0
                FollowFragment::class.java -> viewPager?.currentItem = 1
            }
        }
    }

    companion object {
        fun newInstance() = CommunityFragment()
    }
}