package com.example.kaiyanproject.util

import com.example.kaiyanproject.R
import com.example.kaiyanproject.extension.resString
import com.example.kaiyanproject.ui.common.exception.ResponseCodeException
import com.google.gson.JsonSyntaxException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ResponseHandler {
    fun getFailureTips(e: Throwable) = when (e) {
        is ConnectException -> R.string.network_connect_error.resString
        is SocketTimeoutException -> R.string.network_connect_timeout.resString
        is ResponseCodeException -> R.string.network_response_code_error.resString + e.responseCode
        is NoRouteToHostException -> R.string.no_route_to_host.resString
        is UnknownHostException -> R.string.network_error.resString
        is JsonSyntaxException -> R.string.json_data_error.resString
        else -> R.string.unknown_error.resString
    }
}