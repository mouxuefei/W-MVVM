package com.wanandroid.ui.square

import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.util.ktx.ext.startKtxActivity
import com.util.ktx.ext.toast
import kotlinx.android.synthetic.main.fragment_square.*
import com.wanandroid.BR
import com.wanandroid.R
import com.wanandroid.adapter.BaseBindAdapter
import com.wanandroid.model.bean.Article
import com.wanandroid.ui.BrowserActivity
import com.wanandroid.view.CustomLoadMoreView
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Created by mou
 * on 2019/10/15 10:18
 */
class SquareFragment : com.mvvm.core.base.BaseVMFragment<ArticleViewModel>() {

    override fun initVM(): ArticleViewModel = getViewModel()

    private val squareAdapter by lazy { BaseBindAdapter<Article>(R.layout.item_square_constraint, BR.article) }

    override fun getLayoutResId() = R.layout.fragment_square


    override fun initView() {
        mBinding.run {
            setVariable(BR.viewModel, mViewModel)
            setVariable(BR.adapter, squareAdapter)
        }
        initRecycleView()
    }

    override fun initData() {
        refresh()
    }

    private fun initRecycleView() {
        squareAdapter.run {
            setOnItemClickListener { _, _, position ->
                startKtxActivity<BrowserActivity>(value = BrowserActivity.URL to squareAdapter.data[position].link)
            }
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, squareRecycleView)
        }
    }

    private fun loadMore() {
        mViewModel.getSquareArticleList(false)
    }

    fun refresh() {
        mViewModel.getSquareArticleList(true)
    }

    override fun startObserve() {
        mViewModel.uiState.observe(viewLifecycleOwner, Observer {

            it.showSuccess?.let { list ->
                squareAdapter.run {
                    setEnableLoadMore(false)
                    if (it.isRefresh) replaceData(list.datas)
                    else addData(list.datas)
                    setEnableLoadMore(true)
                    loadMoreComplete()
                }
            }

            if (it.showEnd) squareAdapter.loadMoreEnd()

            it.needLogin?.let { needLogin ->
//                if (needLogin) Navigation.findNavController(squareRecycleView).navigate(R.id.action_tab_to_login)
//                else Navigation.findNavController(squareRecycleView).navigate(R.id.action_tab_to_share)
            }

            it.showError?.let { message ->
                activity?.toast(if (message.isBlank()) "网络异常" else message)
            }

        })
    }

}