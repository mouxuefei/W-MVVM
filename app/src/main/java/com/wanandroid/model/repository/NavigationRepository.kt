package com.wanandroid.model.repository

import com.mvvm.core.Result
import com.wanandroid.model.api.BaseRepository
import com.wanandroid.model.api.WanRetrofitClient
import com.wanandroid.model.bean.Navigation

/**
 * Created by mou
 * on 2019/4/10 14:15
 */
class NavigationRepository : BaseRepository() {


    suspend fun getNavigation(): Result<List<Navigation>> {
        return safeApiCall(call = { requestNavigation() }, errorMessage = "获取数据失败")
    }


    private suspend fun requestNavigation(): Result<List<Navigation>> =
            executeResponse(WanRetrofitClient.service.getNavigation())
}