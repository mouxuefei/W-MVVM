package com.wanandroid.ui.login

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mvvm.core.base.BaseViewModel
import com.wanandroid.CoroutinesDispatcherProvider
import com.wanandroid.checkResult
import com.wanandroid.model.bean.User
import com.wanandroid.model.repository.LoginRepository

/**
 * Created by mou
 * on 2019/4/2 16:36
 */
class LoginViewModel @ViewModelInject constructor(val repository: LoginRepository, val provider: CoroutinesDispatcherProvider) : BaseViewModel() {

    val userName = ObservableField<String>("")
    val passWord = ObservableField<String>("")

    private val _uiState = MutableLiveData<LoginUiState<User>>()
    val uiState: LiveData<LoginUiState<User>>
        get() = _uiState

    private fun isInputValid(userName: String, passWord: String) = userName.isNotBlank() && passWord.isNotBlank()

    private fun loginDataChanged() {
        _uiState.value = LoginUiState(enableLoginButton = isInputValid(userName.get()
                ?: "", passWord.get() ?: ""))
    }

    fun login() {

        launchOnUI {
            if (userName.get().isNullOrBlank() || passWord.get().isNullOrBlank()) {
                _uiState.value = LoginUiState(enableLoginButton = false)
                return@launchOnUI
            }

            _uiState.value = LoginUiState(isLoading = true)

            val result = repository.login(userName.get() ?: "", passWord.get() ?: "")

            result.checkResult(
                    onSuccess = {
                        _uiState.value = LoginUiState(isSuccess = it, enableLoginButton = true)
                    },
                    onError = {
                        _uiState.value = LoginUiState(isError = it, enableLoginButton = true)
                    })
        }
    }

    fun register() {
        viewModelScope.launch(provider.computation) {
            if (userName.get().isNullOrBlank() || passWord.get().isNullOrBlank()) return@launch

            withContext(provider.main) { _uiState.value = LoginUiState(isLoading = true) }

            val result = repository.register(userName.get() ?: "", passWord.get() ?: "")

            withContext(provider.main) {
                result.checkResult({
                    _uiState.value = LoginUiState(isSuccess = it, enableLoginButton = true)
                }, {
                    _uiState.value = LoginUiState(isError = it, enableLoginButton = true)
                })
            }

        }
    }

    val verifyInput: (String) -> Unit = { loginDataChanged() }
}