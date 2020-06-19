package com.wanandroid.model.repository

import com.mvvm.core.Result
import com.wanandroid.model.api.BaseRepository
import com.wanandroid.model.api.WanRetrofitClient
import com.wanandroid.model.bean.ArticleList
import com.wanandroid.util.isSuccess
import java.io.IOException

/**
 * Created by mou
 * on 2019/10/15 10:33
 */
class SquareRepository :  BaseRepository(){


    suspend fun getSquareArticleList(page:Int): Result<ArticleList> {
        return safeApiCall(call = {requestSquareArticleList(page)},errorMessage = "网络异常")
    }

    private suspend fun requestSquareArticleList(page: Int): Result<ArticleList> {
        val response = WanRetrofitClient.service.getSquareArticleList(page)
        return if (response.isSuccess()) Result.Success(response.data)
        else Result.Error(IOException(response.errorMsg))

    }
}