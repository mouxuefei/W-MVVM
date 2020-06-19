package com.wanandroid.ui.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mvvm.core.base.BaseViewModel
import com.mvvm.core.Result
import com.wanandroid.checkResult
import com.wanandroid.checkSuccess
import com.wanandroid.model.bean.Navigation
import com.wanandroid.model.repository.NavigationRepository

/**
 * Created by mou
 * on 2019/4/8 16:21
 */
class NavigationViewModel(private val navigationRepository: NavigationRepository) : BaseViewModel() {

    private val _uiState = MutableLiveData<List<Navigation>>()
    val uiState : LiveData<List<Navigation>>
        get() = _uiState

    fun getNavigation() {
        launchOnUI {
            val result = withContext(Dispatchers.IO) { navigationRepository.getNavigation() }
            result.checkSuccess {
                _uiState.value = it
            }
        }
    }
}