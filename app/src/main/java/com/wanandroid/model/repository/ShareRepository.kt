package com.wanandroid.model.repository

import com.mvvm.core.Result
import com.wanandroid.model.api.BaseRepository
import com.wanandroid.model.api.WanRetrofitClient

/**
 * Created by mou
 * on 2019/10/15 16:31
 */
class ShareRepository : BaseRepository() {


    suspend fun shareArticle(title: String, url: String): Result<String> {
        return safeApiCall(call = { requestShareArticle(title, url) }, errorMessage = "分享失败")
    }


    private suspend fun requestShareArticle(title: String, url: String): Result<String> =
            executeResponse(WanRetrofitClient.service.shareArticle(title, url))
}