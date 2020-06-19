package com.wanandroid.model.repository

import com.mvvm.core.Result
import com.wanandroid.model.api.BaseRepository
import com.wanandroid.model.api.WanRetrofitClient
import com.wanandroid.model.bean.ArticleList

/**
 * Created by mou
 * on 2019/4/10 14:01
 */
class CollectRepository : BaseRepository() {

    suspend fun getCollectArticles(page: Int): Result<ArticleList> {
        return safeApiCall(call = { requestCollectArticles(page) }, errorMessage = "网络错误")
    }

    suspend fun collectArticle(articleId: Int): Result<ArticleList> {
        return safeApiCall(call = { requestCollectArticle(articleId) }, errorMessage = "网络错误")
    }

    suspend fun unCollectArticle(articleId: Int): Result<ArticleList> {
        return safeApiCall(call = { requestCancelCollectArticle(articleId) }, errorMessage = "网络错误")
    }

    private suspend fun requestCollectArticles(page: Int): Result<ArticleList> =
            executeResponse(WanRetrofitClient.service.getCollectArticles(page))

    private suspend fun requestCollectArticle(articleId: Int): Result<ArticleList> =
            executeResponse(WanRetrofitClient.service.collectArticle(articleId))

    private suspend fun requestCancelCollectArticle(articleId: Int): Result<ArticleList> =
            executeResponse(WanRetrofitClient.service.cancelCollectArticle(articleId))
}