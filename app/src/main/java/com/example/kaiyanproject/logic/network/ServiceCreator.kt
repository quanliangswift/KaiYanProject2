package com.example.kaiyanproject.logic.network

import android.os.Build
import com.example.kaiyanproject.extension.logV
import com.example.kaiyanproject.extension.screenPixel
import com.example.kaiyanproject.logic.network.api.MainPageService
import com.example.kaiyanproject.util.GlobalUtil
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Date

object ServiceCreator {
    const val BASE_URL = "http://baobab.kaiyanapp.com/"
    var httpClient = OkHttpClient.Builder().build()

    private val builder = Retrofit.Builder().baseUrl(BASE_URL).client(httpClient)
        .addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(
            GsonConverterFactory.create()
        )

    private val retrofit = builder.build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val t1 = System.nanoTime()
            logV(TAG, "Sending request: ${originalRequest.url()} \n ${originalRequest.headers()}")

            val response = chain.proceed(originalRequest)

            val t2 = System.nanoTime()
            logV(
                TAG,
                "Received response for ${
                    response.request().url()
                } in ${(t2 - t1) / 1e6}\n ${response.headers()}"
            )
            return response
        }

        companion object {
            const val TAG = "LoggingInterceptor"
        }
    }

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder().apply {
                header("model", "Android")
                header("If-Modified-Since", "${Date()}")
                header("User-Agent", System.getProperty("http.agent") ?: "unknown")
            }.build()
            return chain.proceed(request)
        }
    }

    class BasicParamsInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url()
            val url = originalHttpUrl.newBuilder().apply {
                addQueryParameter("udid", GlobalUtil.getDeviceSerial())
                if (!originalHttpUrl.toString().contains(MainPageService.HOMEPAGE_RECOMMEND_URL)) {
                    addQueryParameter("vc", GlobalUtil.eyepetizerVersionCode.toString())
                    addQueryParameter("vn", GlobalUtil.eyepetizerVersionName)
                }
                addQueryParameter("size", screenPixel())
                addQueryParameter("deviceModel", GlobalUtil.deviceModel)
                addQueryParameter("first_channel", GlobalUtil.deviceBrand)
                addQueryParameter("last_channel", GlobalUtil.deviceBrand)
                addQueryParameter("system_version_code", "${Build.VERSION.SDK_INT}")
            }.build()
            val request = originalRequest.newBuilder().url(url).build()
            return chain.proceed(request)
        }
    }
}