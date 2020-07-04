package com.wanandroid.ui.project

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_projecttype.*
import kotlinx.android.synthetic.main.fragment_systemtype.*
import com.mvvm.core.base.BaseVMFragment
import com.mvvm.core.view.SpaceItemDecoration
import com.util.ktx.ext.startKtxActivity
import com.wanandroid.BR
import com.wanandroid.R
import com.wanandroid.adapter.BaseBindAdapter
import com.wanandroid.model.bean.Article
import com.wanandroid.ui.BrowserActivity
import com.wanandroid.ui.square.ArticleViewModel
import com.wanandroid.util.Preference
import com.wanandroid.view.CustomLoadMoreView
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 最新项目/项目分类
 * Created by Lu
 * on 2018/4/1 17:06
 */
class ProjectTypeFragment : BaseVMFragment<ArticleViewModel>() {

    override fun initVM(): ArticleViewModel = getViewModel()

    private val isLogin by Preference(Preference.IS_LOGIN, false)
    private val cid by lazy { arguments?.getInt(CID) }
    private val isLasted by lazy { arguments?.getBoolean(LASTED) } // 区分是最新项目 还是项目分类
    private val projectAdapter by lazy { BaseBindAdapter<Article>(R.layout.item_project, BR.article) }

    override fun getLayoutResId() = R.layout.fragment_projecttype

    companion object {
        private const val CID = "projectCid"
        private const val LASTED = "lasted"
        fun newInstance(cid: Int, isLasted: Boolean): ProjectTypeFragment {
            val fragment = ProjectTypeFragment()
            val bundle = Bundle()
            bundle.putInt(CID, cid)
            bundle.putBoolean(LASTED, isLasted)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        mBinding.run {
            setVariable(BR.adapter,projectAdapter)
        }
        initRecycleView()
    }

    override fun initData() {
        refresh()
    }

    fun refresh() {
        projectAdapter.setEnableLoadMore(false)
        loadData(true)
    }

    private fun initRecycleView() {
        projectRefreshLayout.setOnRefreshListener { refresh() }
        projectAdapter.run {
            setOnItemClickListener { _, _, position ->
                startKtxActivity<BrowserActivity>( extra = bundleOf(BrowserActivity.URL to projectAdapter.data[position].link))
            }
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, typeRecycleView)
            onItemChildClickListener = this@ProjectTypeFragment.onItemChildClickListener
        }
    }

    private fun loadMore() {
        loadData(false)
    }


    private fun loadData(isRefresh: Boolean) {
        isLasted?.run {
            if (this) {
                mViewModel.getLatestProjectList(isRefresh)
            } else {
                cid?.let {
                    mViewModel.getProjectTypeDetailList(isRefresh, it)
                }
            }
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        when (view.id) {
            R.id.articleStar -> {
                if (isLogin) {
                    projectAdapter.run {
                        data[position].run {
                            collect = !collect
                            mViewModel.collectArticle(id, collect)
                        }
                        notifyDataSetChanged()
                    }
                } else {
//                    Navigation.findNavController(projectRecycleView).navigate(R.id.action_tab_to_login)
                }
            }
        }
    }

    override fun startObserve() {
        mViewModel.uiState.observe(viewLifecycleOwner, Observer {
            projectRefreshLayout.isRefreshing = it.showLoading

            it.showSuccess?.let { list ->
                projectAdapter.run {
                    if (it.isRefresh) replaceData(list.datas)
                    else addData(list.datas)
                    setEnableLoadMore(true)
                    loadMoreComplete()
                }
            }

            if (it.showEnd) projectAdapter.loadMoreEnd()
        })
    }

}