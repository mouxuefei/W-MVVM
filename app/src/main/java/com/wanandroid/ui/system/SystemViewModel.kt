package com.wanandroid.ui.system

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mvvm.core.base.BaseViewModel
import com.mvvm.core.Result
import com.wanandroid.model.bean.SystemParent
import com.wanandroid.model.repository.CollectRepository
import com.wanandroid.model.repository.SystemRepository

/**
 * Created by mou
 * on 2019/4/8 16:40
 */
class SystemViewModel(
        private val systemRepository: SystemRepository,
        private val collectRepository: CollectRepository
) : BaseViewModel() {


    private val _mSystemParentList: MutableLiveData<BaseUiModel<List<SystemParent>>> = MutableLiveData()
    val uiState: LiveData<BaseUiModel<List<SystemParent>>>
        get() = _mSystemParentList


    fun getSystemTypes() {
        viewModelScope.launch(Dispatchers.Main) {
            emitArticleUiState(showLoading = true)
            val result = withContext(Dispatchers.IO) { systemRepository.getSystemTypes() }
            if (result is Result.Success)
                emitArticleUiState(showLoading = false, showSuccess = result.data)
            else if (result is Result.Error)
                emitArticleUiState(showLoading = false, showError = result.exception.message)
        }
    }

    fun collectArticle(articleId: Int, boolean: Boolean) {
        launchOnUI {
            withContext(Dispatchers.IO) {
                if (boolean) collectRepository.collectArticle(articleId)
                else collectRepository.unCollectArticle(articleId)
            }
        }
    }

    private fun emitArticleUiState(
            showLoading: Boolean = false,
            showError: String? = null,
            showSuccess: List<SystemParent>? = null
    ) {
        val uiModel = BaseUiModel(showLoading, showError, showSuccess)
        _mSystemParentList.value = uiModel
    }
}