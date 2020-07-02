package com.wanandroid.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mvvm.core.base.BaseViewModel
import com.mvvm.core.Result
import com.wanandroid.model.bean.ArticleList
import com.wanandroid.model.bean.Hot
import com.wanandroid.model.repository.CollectRepository
import com.wanandroid.model.repository.SearchRepository

/**
 * Created by mou
 * on 2019/4/8 15:29
 * LiveData与MutableLiveData的其实在概念上是一模一样的.唯一几个的区别如下:
 1.MutableLiveData的父类是LiveData
 2.LiveData在实体类里可以通知指定某个字段的数据更新.
 3.MutableLiveData则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段
 */
class SearchViewModel(private val searchRepository: SearchRepository,
                      private val collectRepository: CollectRepository) : BaseViewModel() {
    private var currentPage = 0
    val uiState = MutableLiveData<SearchUiModel>()


    fun searchHot(isRefresh: Boolean = false, key: String) {
        viewModelScope.launch(Dispatchers.Main) {
            emitArticleUiState(showLoading = true)
            val result = withContext(Dispatchers.Default) {
                if (isRefresh) currentPage = 0
                searchRepository.searchHot(currentPage, key)
            }
                if (result is Result.Success) {
                    val articleList = result.data
                    if (articleList.offset >= articleList.total) {
                        if (articleList.offset > 0)
                            emitArticleUiState(showLoading = false, showEnd = true)
                        else
                            emitArticleUiState(showLoading = false, isRefresh = true, showSuccess = ArticleList(0, 0, 0, 0, 0, false, emptyList()))
                        return@launch
                    }
                    currentPage++
                    emitArticleUiState(showLoading = false, showSuccess = articleList, isRefresh = isRefresh)

                } else if (result is Result.Error) {
                    emitArticleUiState(showLoading = false, showError = result.exception.message)
                }

        }
    }


    fun getWebSites() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) { searchRepository.getWebSites() }
            if (result is Result.Success) emitArticleUiState(showHot = true, showWebSites = result.data)
        }
    }

    fun getHotSearch() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) { searchRepository.getHotSearch() }
            if (result is Result.Success) emitArticleUiState(showHot = true, showHotSearch = result.data)
        }
    }

    fun collectArticle(articleId: Int, boolean: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                if (boolean) collectRepository.collectArticle(articleId)
                else collectRepository.unCollectArticle(articleId)
            }
        }
    }

    /**
     * 统一赋值
     */
    private fun emitArticleUiState(
            showHot: Boolean = false,
            showLoading: Boolean = false,
            showError: String? = null,
            showSuccess: ArticleList? = null,
            showEnd: Boolean = false,
            isRefresh: Boolean = false,
            showWebSites: List<Hot>? = null,
            showHotSearch: List<Hot>? = null
    ) {
        val uiModel = SearchUiModel(showHot, showLoading, showError, showSuccess, showEnd, isRefresh, showWebSites, showHotSearch)
        uiState.value = uiModel
    }


    data class SearchUiModel(
            val showHot: Boolean,
            val showLoading: Boolean,
            val showError: String?,
            val showSuccess: ArticleList?,
            val showEnd: Boolean, // 加载更多
            val isRefresh: Boolean, // 刷新
            val showWebSites: List<Hot>?,
            val showHotSearch: List<Hot>?
    )

}