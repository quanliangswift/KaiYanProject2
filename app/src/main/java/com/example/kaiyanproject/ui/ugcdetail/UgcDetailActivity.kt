package com.example.kaiyanproject.ui.ugcdetail

import android.content.Context
import android.content.Intent
import com.example.kaiyanproject.logic.model.CommunityRecommend
import com.example.kaiyanproject.ui.common.ui.BaseActivity

class UgcDetailActivity : BaseActivity() {
    companion object {
        fun start(
            context: Context,
            dataList: List<CommunityRecommend.Item>,
            currentItem: CommunityRecommend.Item
        ) {

            val startIntent = Intent(context, UgcDetailActivity::class.java)

            context.startActivity(startIntent)
        }
    }
}