package com.wanandroid.ui.system

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.util.ktx.ext.startKtxActivity
import com.util.ktx.ext.toast
import kotlinx.android.synthetic.main.fragment_system.*
import com.mvvm.core.view.SpaceItemDecoration
import com.wanandroid.BR
import com.wanandroid.R
import com.wanandroid.adapter.BaseBindAdapter
import com.wanandroid.databinding.FragmentSystemBinding
import com.wanandroid.model.bean.SystemParent
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 体系
 * Created by Lu
 * on 2018/3/26 21:11
 */
class SystemFragment : com.mvvm.core.base.BaseVMFragment<SystemViewModel>() {

    override fun initVM(): SystemViewModel = getViewModel()

    private val systemAdapter by lazy { BaseBindAdapter<SystemParent>(R.layout.item_system, BR.systemParent) }

    override fun getLayoutResId() = R.layout.fragment_system

    override fun initView() {
        mBinding.run {
            setVariable(BR.viewModel,mViewModel)
            setVariable(BR.adapter,systemAdapter)
        }
        initRecycleView()
    }

    override fun initData() {
        refresh()
    }

    private fun initRecycleView() {

        systemAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            startKtxActivity<SystemTypeNormalActivity>(value = SystemTypeNormalActivity.ARTICLE_LIST to systemAdapter.data[position])
        }

        systemRefreshLayout.setOnRefreshListener { refresh() }
    }

    private fun refresh() {
        mViewModel.getSystemTypes()
    }


    override fun startObserve() {
        mViewModel.run {
            uiState.observe(viewLifecycleOwner, Observer {
                //                systemRefreshLayout.isRefreshing = it.showLoading

                it.showSuccess?.let { list -> systemAdapter.replaceData(list) }

                it.showError?.let { message -> activity?.toast(message) }
            })
        }
    }
}