package com.compose.marvels.core.interceptors

import com.compose.marvels.core.utils.KeysManger
import com.compose.marvels.core.utils.SharedPreferenceManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.security.MessageDigest
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = getUrl(chain.request())
        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }

    private fun getUrl(request: Request): HttpUrl {
        val ts = System.currentTimeMillis().toString()
        val hash = "$ts${SharedPreferenceManager.getString(KeysManger.PRIVATE_KEY)}${SharedPreferenceManager.getString(KeysManger.API_KEY)}".md5()
        return request.url.newBuilder()
            .addQueryParameter("apikey", SharedPreferenceManager.getString(KeysManger.API_KEY))
            .addQueryParameter("ts", ts)
            .addQueryParameter("hash", hash)
            .build()
    }

    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return md.digest(toByteArray()).joinToString("") { "%02x".format(it) }
    }
}