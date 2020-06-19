package com.wanandroid.model.repository

import com.mvvm.core.Result
import com.wanandroid.model.api.BaseRepository
import com.wanandroid.model.api.WanRetrofitClient
import com.wanandroid.model.bean.ArticleList
import com.wanandroid.model.bean.Banner

/**
 * Created by mou
 * on 2019/4/10 14:09
 */
class HomeRepository : BaseRepository() {

    suspend fun getBanners(): Result<List<Banner>> {
        return safeApiCall(call = {requestBanners()},errorMessage = "")
    }

    private suspend fun requestBanners(): Result<List<Banner>> =
        executeResponse(WanRetrofitClient.service.getBanner())


    suspend fun getArticleList(page: Int): Result<ArticleList> {
        return safeApiCall(call = { requestArticleList(page) }, errorMessage = "")
    }

    private suspend fun requestArticleList(page: Int): Result<ArticleList> =
            executeResponse(WanRetrofitClient.service.getHomeArticles(page))
}