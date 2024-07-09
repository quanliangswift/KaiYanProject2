package com.example.kaiyanproject.ui.common.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.kaiyanproject.R
import com.example.kaiyanproject.extension.setOnClickListener
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener

abstract class BaseViewPagerFragment : BaseFragment() {
    protected var ivCalendar: ImageView? = null

    protected var ivSearch: ImageView? = null

    protected var viewPager: ViewPager2? = null

    protected var tabLayout: CommonTabLayout? = null

    protected var pageChangeCallback: PageChangeCallback? = null

    protected var offscreenPageLimit = 1

    abstract val createFragments: Array<Fragment>
    abstract val createTitles: ArrayList<CustomTabEntity>

    protected val adapter: VpAdapter by lazy {
        VpAdapter(requireActivity()).apply {
            addFragments(createFragments)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        pageChangeCallback?.run {
            viewPager?.unregisterOnPageChangeCallback(this)
        }
        pageChangeCallback = null
    }

    private fun setupViews() {
        ivCalendar = rootView?.findViewById(R.id.ivCalendar)
        ivSearch = rootView?.findViewById(R.id.ivSearch)
        setOnClickListener(ivCalendar, ivSearch) {
            when (this) {
                ivCalendar -> {

                }

                ivSearch -> {

                }
            }
        }
        initViewPager()
    }

    protected fun initViewPager() {
        viewPager = rootView?.findViewById(R.id.viewPager)
        tabLayout = rootView?.findViewById(R.id.tabLayout)
        viewPager?.offscreenPageLimit = offscreenPageLimit
        viewPager?.adapter = adapter
        tabLayout?.setTabData(createTitles)

        tabLayout?.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewPager?.currentItem = position
            }

            override fun onTabReselect(position: Int) {

            }
        })
        pageChangeCallback = PageChangeCallback(tabLayout)
        viewPager?.registerOnPageChangeCallback(pageChangeCallback!!)
        
    }

    class PageChangeCallback(val tabLayout: CommonTabLayout?) : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            tabLayout?.currentTab = position
        }
    }

    class VpAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        private val fragments = mutableListOf<Fragment>()

        fun addFragments(fragment: Array<Fragment>) {
            fragments.addAll(fragment)
        }

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int) = fragments[position]

    }

}