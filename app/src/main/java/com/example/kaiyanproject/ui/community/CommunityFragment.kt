package com.example.kaiyanproject.ui.community

import androidx.fragment.app.Fragment
import com.example.kaiyanproject.ui.common.ui.BaseViewPagerFragment
import com.example.kaiyanproject.ui.home.HomePageFragment
import com.flyco.tablayout.listener.CustomTabEntity

class CommunityFragment : BaseViewPagerFragment() {
    override val createFragments: Array<Fragment> = arrayOf()
    override val createTitles: ArrayList<CustomTabEntity> = arrayListOf()

    companion object {

        fun newInstance() = CommunityFragment()
    }
}