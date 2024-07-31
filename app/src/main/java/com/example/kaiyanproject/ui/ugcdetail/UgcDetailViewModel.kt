package com.example.kaiyanproject.ui.ugcdetail

import androidx.lifecycle.ViewModel
import com.example.kaiyanproject.logic.model.CommunityRecommend

class UgcDetailViewModel : ViewModel() {
    var dataList: List<CommunityRecommend.Item>? = null
    var itemPosition: Int = -1
}