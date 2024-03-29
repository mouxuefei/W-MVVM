package com.wanandroid.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mvvm.core.base.BaseActivity
import com.wanandroid.R
import com.wanandroid.ui.home.HomeFragment
import com.wanandroid.ui.profile.ProfileFragment
import com.wanandroid.ui.project.BlogFragment
import com.wanandroid.ui.project.ProjectFragment
import com.wanandroid.ui.search.SearchFragment
import com.wanandroid.view.ScaleInTransformer
import kotlinx.android.synthetic.main.activity_bottom_navigation.*


/**
 * @FileName: MainActivity.java
 * @author: villa_mou
 * @date: 06-18:13
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class MainActivity : BaseActivity() {

    private val fragmentList = arrayListOf<Fragment>()
    private val mainFragment by lazy { HomeFragment() }
    private val blogFragment by lazy { BlogFragment() }
    private val searchFragment by lazy { SearchFragment() }
    private val projectFragment by lazy { ProjectFragment() }
    private val profileFragment by lazy { ProfileFragment() }

    private val bottomIds =
        arrayListOf<Int>(R.id.home, R.id.blog, R.id.search, R.id.project, R.id.profile)

    init {
        fragmentList.run {
            add(mainFragment)
            add(blogFragment)
            add(searchFragment)
            add(projectFragment)
            add(profileFragment)
        }
    }


    override fun getLayoutResId() = R.layout.activity_bottom_navigation

    override fun initView() {
        initViewPager()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelected)
    }

    override fun initData() {

    }


    private val onNavigationItemSelected = BottomNavigationView.OnNavigationItemSelectedListener {
        bottomIds.forEachIndexed { index, item ->
            if (it.itemId == item) {
                switchFragment(index, false)
            }
        }

        true
    }

    private fun switchFragment(position: Int, smoothScroll: Boolean): Boolean {
        mainViewpager.setCurrentItem(position, smoothScroll)
        return true
    }

    private fun initViewPager() {
        mainViewpager.isUserInputEnabled = true
        mainViewpager.offscreenPageLimit = 4
        val adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun createFragment(position: Int) = fragmentList[position]

            override fun getItemCount() = fragmentList.size
        }
        mainViewpager.adapter = adapter

        mainViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomIds.forEachIndexed { index, item ->
                    if (position == index) {
                        navView.selectedItemId = item
                    }
                }

            }
        })
    }

}