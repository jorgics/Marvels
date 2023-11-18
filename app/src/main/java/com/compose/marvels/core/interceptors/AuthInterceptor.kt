package com.compose.marvels.core.interceptors

import com.compose.marvels.core.utils.ApiKeyManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.security.MessageDigest
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val apiKeyManager: ApiKeyManager): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = getUrl(chain.request())
        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }

    private fun getUrl(request: Request): HttpUrl {
        val ts = System.currentTimeMillis().toString()
        val hash = "$ts${apiKeyManager.getPrivateKey()}${apiKeyManager.getApiKey()}".md5()
        return request.url.newBuilder()
            .addQueryParameter("apikey", apiKeyManager.getApiKey())
            .addQueryParameter("ts", ts)
            .addQueryParameter("hash", hash)
            .build()
    }

    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return md.digest(toByteArray()).joinToString("") { "%02x".format(it) }
    }
}