package com.example.kaiyanproject.logic.model

import com.flyco.tablayout.listener.CustomTabEntity

class TabEntity(
    private val title: String,
    private val selectedIcon: Int = 0,
    private val unselectedIcon: Int = 0
) : CustomTabEntity {
    override fun getTabTitle(): String = title

    override fun getTabSelectedIcon(): Int = selectedIcon

    override fun getTabUnselectedIcon(): Int = unselectedIcon
}