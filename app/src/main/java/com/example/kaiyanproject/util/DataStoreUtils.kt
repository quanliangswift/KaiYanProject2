package com.example.kaiyanproject.util

import kotlinx.coroutines.flow.Flow

/**
 *
 * 异步获取数据
 * [getData] [readBooleanFlow] [readFloatFlow] [readIntFlow] [readLongFlow] [readStringFlow]
 * 同步获取数据
 * [getSyncData] [readBooleanData] [readFloatData] [readIntData] [readLongData] [readStringData]
 *
 * 异步写入数据
 * [putData] [saveBooleanData] [saveFloatData] [saveIntData] [saveLongData] [saveStringData]
 * 同步写入数据
 * [putSyncData] [saveSyncBooleanData] [saveSyncFloatData] [saveSyncIntData] [saveSyncLongData] [saveSyncStringData]
 *
 * 异步清除数据
 * [clear]
 * 同步清除数据
 * [clearSync]
 *
 * 描述：DataStore 工具类
 *
 */
object DataStoreUtils {
    fun <U> getSyncData(key: String, default: U): U {
        val res = when (default) {
//            is Long ->
            else -> throw IllegalArgumentException("This type can not be saved into DataStore")
        }
        return res as U
    }

    fun <U> getData(key: String, default: U): Flow<U> {
        val data = when (default) {
            else -> throw IllegalArgumentException("This type can not be saved into DataStore")
        }
        return data as Flow<U>
    }
}