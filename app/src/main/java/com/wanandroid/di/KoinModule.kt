package com.wanandroid.di

import com.wanandroid.CoroutinesDispatcherProvider
import com.wanandroid.model.api.WanRetrofitClient
import com.wanandroid.model.api.WanService
import com.wanandroid.model.repository.*
import com.wanandroid.ui.login.LoginViewModel
import com.wanandroid.ui.navigation.NavigationViewModel
import com.wanandroid.ui.project.ProjectViewModel
import com.wanandroid.ui.search.SearchViewModel
import com.wanandroid.ui.share.ShareViewModel
import com.wanandroid.ui.square.ArticleViewModel
import com.wanandroid.ui.system.SystemViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by mou
 * on 2019/11/15 15:44
 */

val viewModelModule = module {
    viewModel { LoginViewModel(get(),get()) }
    viewModel { ArticleViewModel(get(), get(), get(), get(), get()) }
    viewModel { SystemViewModel(get(), get()) }
    viewModel { NavigationViewModel(get()) }
    viewModel { ProjectViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { ShareViewModel(get()) }
}

val repositoryModule = module {
    single { WanRetrofitClient.getService(WanService::class.java, WanService.BASE_URL) }
    single { CoroutinesDispatcherProvider() }
    single { LoginRepository(get()) }
    single { SquareRepository() }
    single { HomeRepository() }
    single { ProjectRepository() }
    single { CollectRepository() }
    single { SystemRepository() }
    single { NavigationRepository() }
    single { SearchRepository() }
    single { ShareRepository() }
}

val appModule = listOf(viewModelModule, repositoryModule)