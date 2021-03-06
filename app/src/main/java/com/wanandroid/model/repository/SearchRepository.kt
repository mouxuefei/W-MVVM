package com.wanandroid.model.repository

import com.mvvm.core.Result
import com.wanandroid.model.api.BaseRepository
import com.wanandroid.model.api.WanRetrofitClient
import com.wanandroid.model.bean.ArticleList
import com.wanandroid.model.bean.Hot

/**
 * Created by mou
 * on 2019/4/10 14:26
 */
class SearchRepository : BaseRepository() {
    suspend fun searchHot(page: Int, key: String): Result<ArticleList> {
        return safeApiCall(call = {requestSearch(page, key)})
    }

    suspend fun getWebSites(): Result<List<Hot>> {
        return safeApiCall(call = {requestWebSites()})
    }

    suspend fun getHotSearch(): Result<List<Hot>> {
        return safeApiCall(call = {requestHotSearch()})
    }

    private suspend fun requestWebSites(): Result<List<Hot>> =
            executeResponse(WanRetrofitClient.service.getWebsites())

    private suspend fun requestHotSearch(): Result<List<Hot>> =
            executeResponse(WanRetrofitClient.service.getHot())

    private suspend fun requestSearch(page: Int, key: String): Result<ArticleList> =
            executeResponse(WanRetrofitClient.service.searchHot(page, key))
}