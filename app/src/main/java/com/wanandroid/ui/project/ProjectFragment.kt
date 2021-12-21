package com.wanandroid.ui.project

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_project.*
import com.wanandroid.R
import com.wanandroid.model.bean.SystemParent
import com.wanandroid.ui.system.SystemTypeFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

open class ProjectFragment : com.mvvm.core.base.BaseVMFragment<ProjectViewModel>(useDataBinding = false) {

    override fun initVM(): ProjectViewModel = getViewModel()

    private val mProjectTypeList = mutableListOf<SystemParent>()
    open var isBlog = false // 区分是公众号还是项目分类

    override fun getLayoutResId() = R.layout.activity_project

    override fun initView() {
        initViewPager()
    }

    override fun initData() {
        if (isBlog) mViewModel.getBlogType()
        else mViewModel.getProjectTypeList()
    }

    private fun initViewPager() {

      val adapter= object : FragmentStateAdapter(childFragmentManager,lifecycle) {
            override fun getItemCount() = mProjectTypeList.size

            override fun createFragment(position: Int) = chooseFragment(position)

        }

        projectViewPager.adapter=adapter

        TabLayoutMediator(tabLayout, projectViewPager) { tab, position ->
            tab.text = mProjectTypeList[position].name
        }.attach()
    }

    private fun chooseFragment(position: Int): Fragment {
        return if (isBlog) SystemTypeFragment.newInstance(mProjectTypeList[position].id, true)
        else ProjectTypeFragment.newInstance(mProjectTypeList[position].id, false)
    }

    private fun getProjectTypeList(projectTypeList: List<SystemParent>) {
        mProjectTypeList.clear()
        mProjectTypeList.addAll(projectTypeList)
        projectViewPager.adapter?.notifyDataSetChanged()
    }

    override fun startObserve() {
        mViewModel.systemData.observe(viewLifecycleOwner, Observer {
            it?.run { getProjectTypeList(it) }
        })
    }

}