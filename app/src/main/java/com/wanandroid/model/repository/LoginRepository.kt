package com.wanandroid.model.repository

import com.google.gson.Gson
import com.wanandroid.App
import com.mvvm.core.Result
import com.wanandroid.R
import com.wanandroid.model.api.BaseRepository
import com.wanandroid.model.api.WanService
import com.wanandroid.model.bean.User
import com.wanandroid.util.Preference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by mou
 * on 2019/4/10 9:42
 */
@Singleton
class LoginRepository @Inject constructor(val service:WanService) : BaseRepository() {

    private var isLogin by Preference(Preference.IS_LOGIN, false)
    private var userJson by Preference(Preference.USER_GSON, "")


    suspend fun login(userName: String, passWord: String): Result<User> {
        return safeApiCall(call = { requestLogin(userName, passWord) },
                errorMessage = App.CONTEXT.getString(R.string.about))
    }

    // TODO Move into DataSource Layer ?
    private suspend fun requestLogin(userName: String, passWord: String): Result<User> {
        val response = service.login(userName, passWord)

        return executeResponse(response, {
            val user = response.data
            isLogin = true
            userJson = Gson().toJson(user)
            App.CURRENT_USER = user
        })
    }

    suspend fun register(userName: String, passWord: String): Result<User> {
        return safeApiCall(call = { requestRegister(userName, passWord) }, errorMessage = "注册失败")
    }

    private suspend fun requestRegister(userName: String, passWord: String): Result<User> {
        val response = service.register(userName, passWord, passWord)
        return executeResponse(response, { requestLogin(userName, passWord) })
    }

}