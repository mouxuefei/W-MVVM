package com.wanandroid.ui.share

import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.util.ktx.ext.toast
import kotlinx.android.synthetic.main.activity_share.*
import com.mvvm.core.base.BaseVMActivity
import com.wanandroid.R
import com.wanandroid.databinding.ActivityShareBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Created by mou
 * on 2019/10/15 15:21
 */
class ShareActivity : BaseVMActivity<ShareViewModel>() {


    override fun initVM(): ShareViewModel = getViewModel()

    override fun getLayoutResId() = R.layout.activity_share

    override fun initView() {
        mBinding.lifecycleOwner = this
        (mBinding as ActivityShareBinding).viewModel = mViewModel
    }

    override fun initData() {
    }


    override fun startObserve() {
        mViewModel.uiState.observe(this, Observer {

            it.showSuccess?.let {
                Navigation.findNavController(shareBt).navigateUp()
            }

            it.showError?.let { err -> toast(err) }
        })
    }
}