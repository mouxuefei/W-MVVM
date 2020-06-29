package com.wanandroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bottom_navigation.*
import com.mvvm.core.base.BaseActivity
import com.wanandroid.R
import com.wanandroid.ui.home.HomeFragment
import com.wanandroid.ui.profile.ProfileFragment
import com.wanandroid.ui.project.BlogFragment
import com.wanandroid.ui.project.ProjectFragment
import com.wanandroid.ui.search.SearchFragment
import com.wanandroid.ui.square.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        when (it.itemId) {
            R.id.home -> {
                switchFragment(0)
            }
            R.id.blog -> {
                switchFragment(1)
            }
            R.id.search -> {
                switchFragment(2)
            }
            R.id.project -> {
                switchFragment(3)
            }
            R.id.profile -> {
                switchFragment(4)
            }
        }
        true
    }

    private fun switchFragment(position: Int): Boolean {
//        mainViewpager.currentItem = position
        mainViewpager.setCurrentItem(position, false)
        return true
    }

    private fun initViewPager() {
        mainViewpager.isUserInputEnabled = false
        mainViewpager.offscreenPageLimit = 2
        mainViewpager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int) = fragmentList[position]

            override fun getItemCount() = fragmentList.size
        }
    }

}