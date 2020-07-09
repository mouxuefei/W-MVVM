package com.wanandroid.model.api

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.wanandroid.App
import com.wanandroid.util.NetWorkUtils
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.io.File


/**
 * Created by mou
 * on 2018/3/13 15:45
 */
object WanRetrofitClient : BaseRetrofitClient() {

    val service by lazy { getService(WanService::class.java, WanService.BASE_URL) }
    private const val cacheSize = 10 * 1024 * 1024L // 10 MiB

    //max-age：告知缓存多长时间，在没有超过缓存时间的情况下，请求会返回缓存内的数据，在超出max-age的情况下向服务端发起新的请求，请求失败的情况下返回缓存数据（测试中已验证），否则向服务端重新发起请求。
    private const val maxAge = 60 * 60

    //max-stale： 指示客户机可以接收超出max-age时间的响应消息，max-stale在请求设置中有效，在响应设置中无效
    private const val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale

    private val cookieJar by lazy { PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.CONTEXT)) }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        val httpCacheDirectory = File(App.CONTEXT.cacheDir, "responses")
        val cache = Cache(httpCacheDirectory, cacheSize)
        builder.cache(cache)
//                .cookieJar(cookieJar)
                .addInterceptor { chain ->
                    var request = chain.request()
                    // 如果没有网络，强制使用缓存
                    if (!NetWorkUtils.isNetworkAvailable(App.CONTEXT)) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build()
                    }
                    val response = chain.proceed(request)
                    if (!NetWorkUtils.isNetworkAvailable(App.CONTEXT)) {
                        response.newBuilder()
                                .removeHeader("Pragma")
                                .header("Cache-Control", "public, max-age=$maxAge")
                                .build()
                    } else {
                        response.newBuilder()
                                .removeHeader("Pragma")
                                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                                .build()
                    }
                    response
                }
    }
}