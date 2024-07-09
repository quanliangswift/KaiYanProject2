package com.example.kaiyanproject.ui.home

import androidx.fragment.app.Fragment
import com.example.kaiyanproject.ui.common.ui.BaseViewPagerFragment
import com.flyco.tablayout.listener.CustomTabEntity

class HomePageFragment : BaseViewPagerFragment() {
    override val createFragments: Array<Fragment> = arrayOf()
    override val createTitles: ArrayList<CustomTabEntity> = arrayListOf()

    companion object {

        fun newInstance() = HomePageFragment()
    }
}