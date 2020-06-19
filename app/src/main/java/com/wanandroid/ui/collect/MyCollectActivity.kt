package com.wanandroid.ui.collect

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.util.ktx.ext.dp
import com.util.ktx.ext.startKtxActivity
import com.util.ktx.ext.toast
import kotlinx.android.synthetic.main.activity_collect.*
import kotlinx.android.synthetic.main.title_layout.*
import com.mvvm.core.view.SpaceItemDecoration
import com.wanandroid.R
import com.wanandroid.adapter.HomeArticleAdapter
import com.wanandroid.databinding.ActivityCollectBinding
import com.wanandroid.ui.BrowserActivity
import com.wanandroid.ui.square.ArticleViewModel
import com.wanandroid.view.CustomLoadMoreView
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Created by Lu
 * on 2018/4/10 22:09
 */
class MyCollectActivity : com.mvvm.core.base.BaseVMActivity<ArticleViewModel>() {

    override fun initVM(): ArticleViewModel = getViewModel()

    private val articleAdapter by lazy { HomeArticleAdapter() }

    override fun getLayoutResId() = R.layout.activity_collect

    override fun initView() {

        (mBinding as ActivityCollectBinding).viewModel = mViewModel

        mToolbar.title = getString(R.string.my_collect)
        mToolbar.setNavigationIcon(R.drawable.arrow_back)

        collectRecycleView.run {
            layoutManager = LinearLayoutManager(this@MyCollectActivity)
            addItemDecoration(SpaceItemDecoration(bottom = 20.dp.toInt()))
        }

        initAdapter()

    }

    override fun initData() {
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        refresh()
    }

    private fun refresh() {
        mViewModel.getCollectArticleList(true)
    }

    private fun initAdapter() {
        articleAdapter.run {
            //            showStar(false)
            setOnItemClickListener { _, _, position ->
                startKtxActivity<BrowserActivity>(value = BrowserActivity.URL to articleAdapter.data[position].link)
//                Navigation.findNavController(collectRecycleView).navigate(R.id.action_collect_to_browser, bundleOf(BrowserActivity.URL to articleAdapter.data[position].link))
            }
            onItemChildClickListener = itemChildClickListener
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, collectRecycleView)
        }
        collectRecycleView.adapter = articleAdapter
    }

    private val itemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        when (view.id) {
            R.id.articleStar -> {
                articleAdapter.run {
                    data[position].run {
                        collect = !collect
                        mViewModel.collectArticle(originId, collect)
                    }
                    notifyItemRemoved(position)
                }
            }
        }
    }

    private fun loadMore() {
        mViewModel.getCollectArticleList(false)
    }

    override fun startObserve() {

        mViewModel.apply {

            uiState.observe(this@MyCollectActivity, Observer {

                it.showSuccess?.let { list ->
                    articleAdapter.setEnableLoadMore(false)
                    list.datas.forEach { it.collect = true }
                    articleAdapter.run {
                        if (it.isRefresh) replaceData(list.datas)
                        else addData(list.datas)
                        setEnableLoadMore(true)
                        loadMoreComplete()
                    }
                }

                if (it.showEnd) articleAdapter.loadMoreEnd()

                it.showError?.let { message ->
                    toast(if (message.isBlank()) "网络异常" else message)
                }
            })
        }
    }

}