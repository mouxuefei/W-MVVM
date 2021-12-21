package com.wanandroid.ui.profile

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.annotation.Nullable
import androidx.appcompat.widget.PopupMenu
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.gyf.immersionbar.components.SimpleImmersionProxy
import com.mvvm.core.base.BaseFragment
import com.util.ktx.ext.gone
import com.util.ktx.ext.openBrowser
import com.util.ktx.ext.startKtxActivity
import com.util.ktx.ext.visible
import com.wanandroid.R
import com.wanandroid.model.bean.User
import com.wanandroid.test.ConstraintLayoutTest
import com.wanandroid.ui.collect.MyCollectActivity
import com.wanandroid.ui.example.ExampleActivity
import com.wanandroid.ui.login.LoginActivity
import com.wanandroid.util.GITHUB_PAGE
import com.wanandroid.util.ISSUE_URL
import com.wanandroid.util.Preference
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.model.Notice
import kotlinx.android.synthetic.main.fragmnet_profile.*


/**
 * Created by mou
 * on 2019/12/12 14:12
 */
class ProfileFragment : BaseFragment(), SimpleImmersionOwner {

    private var isLogin by Preference(Preference.IS_LOGIN, false)
    private var userJson by Preference(Preference.USER_GSON, "")

    /**
     * ImmersionBar代理类
     */
    private val mSimpleImmersionProxy = SimpleImmersionProxy(this)
    override fun getLayoutResId() = R.layout.fragmnet_profile

    override fun initView() {
        titleTv.text = getString(R.string.me)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mSimpleImmersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mSimpleImmersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSimpleImmersionProxy.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mSimpleImmersionProxy.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mSimpleImmersionProxy.onConfigurationChanged(newConfig)
    }


    override fun onResume() {
        super.onResume()
        refreshData()
    }

    override fun initData() {
        versionName.setOnClickListener { startKtxActivity<ConstraintLayoutTest>() }
        license.setOnClickListener { showOwnLicense() }
        source.setOnClickListener { activity?.openBrowser(GITHUB_PAGE) }
        feedback.setOnClickListener { showFeedBackMenu() }
        thirdLib.setOnClickListener { showLicenseDialog() }
        developer.setOnClickListener { showMe() }
        loginLayout.setOnClickListener { startKtxActivity<LoginActivity>() }
        collect.setOnClickListener { startKtxActivity<MyCollectActivity>() }
        example.setOnClickListener {
            startKtxActivity<ExampleActivity>()
        }
    }

    private fun refreshData() {
        if (isLogin) {
            val user = Gson().fromJson<User>(userJson, User::class.java)
            Glide.with(icon).load(user.icon).error(R.drawable.ic_dynamic_user).into(icon)
            loginTv.text = user.username
            collect.visible()
        } else {
            loginTv.text = "登录/注册"
            collect.gone()
        }
    }

    private fun showOwnLicense() {
        val notice =
            Notice(getString(R.string.app_name), GITHUB_PAGE, "", ApacheSoftwareLicense20())
        LicensesDialog.Builder(activity)
            .setNotices(notice)
            .build()
            .show()
    }

    private fun showLicenseDialog() {
        LicensesDialog.Builder(activity)
            .setNotices(R.raw.licenses)
            .build()
            .show()
    }

    private fun showFeedBackMenu() {
        val feedbackMenu = context?.let { PopupMenu(it, feedback, Gravity.RIGHT) }
        feedbackMenu?.menuInflater?.inflate(R.menu.menu_feedback, feedbackMenu.menu)
        feedbackMenu?.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_issue -> {
                    activity?.openBrowser(ISSUE_URL)
                    true
                }
                R.id.menu_email -> {
                    val uri = Uri.parse(getString(R.string.sendto))
                    val intent = Intent(Intent.ACTION_SENDTO, uri)
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_topic))
//                    intent.putExtra(Intent.EXTRA_TEXT,
//                            getString(R.string.device_model) + Build.MODEL + "\n"
//                                    + getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n"
//                                    + getString(R.string.version))version
                    startActivity(intent)
                    true
                }
                else -> {
                    true
                }
            }
        }
        feedbackMenu?.show()
    }

    private fun showMe() {
        context?.let {
            MaterialDialog(it).show {
                customView(R.layout.dialog_me)
            }
        }
    }

    override fun initImmersionBar() {
//        ImmersionBar.with(this).statusBarColorTransformEnable(true)
//            .keyboardEnable(false)
//            .statusBarColor("#ff0000",0.2f)
//            .statusBarView(statusBar)
//            .init()
    }

    override fun immersionBarEnabled(): Boolean {
        return true
    }

}

