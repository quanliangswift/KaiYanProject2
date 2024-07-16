package com.example.kaiyanproject.logic.dao

object KaiYanDatabase {
    private lateinit var mainPageDao: MainPageDao
    private lateinit var videoDao: VideoDao

    fun getMainPageDao() = if (this::mainPageDao.isInitialized) mainPageDao else MainPageDao()

    fun getVideoDao() = if (this::videoDao.isInitialized) videoDao else VideoDao()
}