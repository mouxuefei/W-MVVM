package com.wanandroid.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mvvm.core.base.BaseViewModel
import com.mvvm.core.Result
import com.wanandroid.model.bean.SystemParent
import com.wanandroid.model.repository.ProjectRepository

/**
 * Created by mou
 * on 2019/4/8 16:28
 */
class ProjectViewModel(private val repository: ProjectRepository) : BaseViewModel() {

    private val _mSystemParentList: MutableLiveData<List<SystemParent>> = MutableLiveData()
    val systemData: LiveData<List<SystemParent>>
        get() = _mSystemParentList

    fun getProjectTypeList() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) { repository.getProjectTypeList() }
            if (result is Result.Success) _mSystemParentList.value = result.data
        }
    }

    fun getBlogType() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) { repository.getBlog() }
            if (result is Result.Success) _mSystemParentList.value = result.data
        }
    }
}