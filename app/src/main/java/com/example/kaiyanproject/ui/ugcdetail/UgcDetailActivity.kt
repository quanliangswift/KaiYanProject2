package com.example.kaiyanproject.ui.ugcdetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.kaiyanproject.R
import com.example.kaiyanproject.databinding.ActivityMainBinding
import com.example.kaiyanproject.databinding.ActivityNewDetailBinding
import com.example.kaiyanproject.databinding.ActivityUgcDetailBinding
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.extension.showToast
import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.ui.common.callback.AutoPlayPageChangeListener
import com.example.kaiyanproject.ui.common.ui.BaseActivity
import com.example.kaiyanproject.util.IntentDataHolderUtil
import com.shuyu.gsyvideoplayer.GSYVideoManager

class UgcDetailActivity : BaseActivity() {
    private var _binding: ActivityUgcDetailBinding? = null

    private val binding: ActivityUgcDetailBinding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[UgcDetailViewModel::class.java]
    }

    private lateinit var adapter: UgcDetailAdapter

    private var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUgcDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setContentView(view: View?) {
        if (checkArguments()) {
            super.setContentView(view)
            setStatusBarBackground(R.color.black)
        }
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        onPageChangeCallback?.run { binding.viewPager.unregisterOnPageChangeCallback(this) }
        onPageChangeCallback = null
        _binding = null
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.anl_push_bottom_out)
    }

    override fun setupViews() {
        super.setupViews()
        if (viewModel.dataList == null) {
            viewModel.itemPosition = getCurrentItemPosition()
            viewModel.dataList = IntentDataHolderUtil.getData<List<CommunityRecommend.Item>>(
                EXTRA_RECOMMEND_ITEM_LIST_JSON
            )
        }
        if (viewModel.dataList == null) {
            R.string.jump_page_unknown_error.resString.showToast()
            finish()
        } else {
            adapter = UgcDetailAdapter(this, viewModel.dataList!!)
            binding.viewPager.adapter = adapter
            binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
            binding.viewPager.offscreenPageLimit = 1
            onPageChangeCallback = AutoPlayPageChangeListener(
                binding.viewPager,
                viewModel.itemPosition,
                R.id.videoPlayer
            )
            binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback!!)
            binding.viewPager.setCurrentItem(viewModel.itemPosition, false)
        }
    }

    private fun checkArguments() = if (IntentDataHolderUtil.getData<List<CommunityRecommend.Item>>(
            EXTRA_RECOMMEND_ITEM_LIST_JSON
        ).isNullOrEmpty()
        || IntentDataHolderUtil.getData<CommunityRecommend.Item>(EXTRA_RECOMMEND_ITEM_JSON) == null
    ) {
        R.string.jump_page_unknown_error.resString.showToast()
        finish()
        false
    } else {
        true
    }

    private fun getCurrentItemPosition(): Int {
        val list = IntentDataHolderUtil.getData<List<CommunityRecommend.Item>>(
            EXTRA_RECOMMEND_ITEM_LIST_JSON
        )
        val currentItem =
            IntentDataHolderUtil.getData<CommunityRecommend.Item>(EXTRA_RECOMMEND_ITEM_JSON)

        list?.forEachIndexed { index, item ->
            if (currentItem == item) {
                viewModel.itemPosition = index
                return@forEachIndexed
            }
        }
        return viewModel.itemPosition
    }

    companion object {
        const val TAG = "UgcDetailActivity"
        const val EXTRA_RECOMMEND_ITEM_LIST_JSON = "recommend_item_list"
        const val EXTRA_RECOMMEND_ITEM_JSON = "recommend_item"

        fun start(
            context: Activity,
            dataList: List<CommunityRecommend.Item>,
            currentItem: CommunityRecommend.Item
        ) {
            IntentDataHolderUtil.setData(EXTRA_RECOMMEND_ITEM_LIST_JSON, dataList)
            IntentDataHolderUtil.setData(EXTRA_RECOMMEND_ITEM_JSON, currentItem)
            val startIntent = Intent(context, UgcDetailActivity::class.java)
            context.startActivity(startIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) context.overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_OPEN,
                R.anim.anl_push_bottom_in,
                R.anim.anl_push_up_out
            ) else context.overridePendingTransition(
                R.anim.anl_push_bottom_in,
                R.anim.anl_push_up_out
            )
        }
    }
}