package com.wanandroid.ui.home

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.util.ktx.ext.dp2px
import com.util.ktx.ext.toast
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_home.*
import com.mvvm.core.base.BaseVMFragment
import com.util.ktx.ext.startKtxActivity
import com.wanandroid.R
import com.wanandroid.adapter.HomeArticleAdapter
import com.wanandroid.model.bean.Banner
import com.wanandroid.ui.BrowserActivity
import com.wanandroid.ui.login.LoginActivity
import com.wanandroid.ui.square.ArticleViewModel
import com.wanandroid.util.GlideImageLoader
import com.wanandroid.util.Preference
import com.wanandroid.view.CustomLoadMoreView
import org.koin.androidx.viewmodel.ext.android.getViewModel


/**
 * Created by mou
 * on 2018/3/13 14:15
 */
class HomeFragment : BaseVMFragment<ArticleViewModel>() {

    override fun initVM(): ArticleViewModel = getViewModel()

    private val isLogin by Preference(Preference.IS_LOGIN, false)
    private val homeArticleAdapter by lazy { HomeArticleAdapter() }
    private val bannerImages = mutableListOf<String>()
    private val bannerTitles = mutableListOf<String>()
    private val bannerUrls = mutableListOf<String>()
    private val banner by lazy { com.youth.banner.Banner(activity) }

    override fun getLayoutResId() = R.layout.fragment_home

    override fun initView() {
        mBinding.run {
            setVariable(BR.viewModel, mViewModel)
            setVariable(BR.adapter,homeArticleAdapter)
        }
        initRecycleView()
        initBanner()
    }

    override fun initData() {
        refresh()
    }

    private fun initRecycleView() {
        homeArticleAdapter.run {
            setOnItemClickListener { _, _, position ->
                val bundle = bundleOf(BrowserActivity.URL to homeArticleAdapter.data[position].link)
                startKtxActivity<BrowserActivity>(extra = bundle)
            }
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            if (headerLayoutCount > 0) removeAllHeaderView()
            addHeaderView(banner)
            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, homeRecycleView)
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        when (view.id) {
            R.id.articleStar -> {
                if (isLogin) {
                    homeArticleAdapter.run {
                        data[position].run {
                            collect = !collect
                            mViewModel.collectArticle(id, collect)
                        }
                        notifyDataSetChanged()
                    }
                } else {
                    startKtxActivity<LoginActivity>()
                }
            }
        }
    }

    private fun loadMore() {
        mViewModel.getHomeArticleList(false)
    }

    private fun initBanner() {

        banner.run {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, banner.dp2px(200))
            setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            setImageLoader(GlideImageLoader())
            setOnBannerListener { position ->
                run {
                    startKtxActivity<BrowserActivity>(extra = bundleOf(BrowserActivity.URL to bannerUrls[position]))
                }
            }
        }
    }

    fun refresh() {
        mViewModel.getHomeArticleList(true)
    }

    override fun startObserve() {
        mViewModel.apply {
            mBanners.observe(viewLifecycleOwner, Observer { it ->
                it?.let { setBanner(it) }
            })

            uiState.observe(viewLifecycleOwner, Observer {

                it.showSuccess?.let { list ->
                    homeArticleAdapter.run {
                        homeArticleAdapter.setEnableLoadMore(false)
                        if (it.isRefresh) replaceData(list.datas)
                        else addData(list.datas)
                        setEnableLoadMore(true)
                        loadMoreComplete()
                    }
                }

                if (it.showEnd) homeArticleAdapter.loadMoreEnd()

                it.showError?.let { message ->
                    activity?.toast(if (message.isBlank()) "网络异常" else message)
                }

            })
        }
    }

    private fun setBanner(bannerList: List<Banner>) {
        for (banner in bannerList) {
            bannerImages.add(banner.imagePath)
            bannerTitles.add(banner.title)
            bannerUrls.add(banner.url)
        }
        banner.setImages(bannerImages)
                .setBannerTitles(bannerTitles)
                .setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setDelayTime(3000)
        banner.start()
    }

    override fun onStart() {
        super.onStart()
        banner.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        banner.stopAutoPlay()
    }

}