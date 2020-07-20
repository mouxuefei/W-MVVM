package com.wanandroid.ui.example

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.placeholderOf
import com.mvvm.core.base.BaseActivity
import com.wanandroid.R
import com.wanandroid.ui.example.util.Constants.IMAGE_URL_KEY
import kotlinx.android.synthetic.main.activity_example.*

/**
 * @FileName: ExampleActivity.java
 * @author: villa_mou
 * @date: 07-11:08
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ExampleActivity : BaseActivity() {
    val url = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2091711702,2468700162&fm=11&gp=0.jpg"
    override fun getLayoutResId() = R.layout.activity_example

    override fun initView() {
        icon.setOnClickListener {
            goToDetails(url, icon)
        }

    }

    fun goToDetails(url: String, imageView: View) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, url).toBundle()
        Intent(this, ImageDetailActivity::class.java)
                .putExtra(IMAGE_URL_KEY, url)
                .let {
                    startActivity(it, options)
                }
    }


    override fun initData() {
        loadImage()
    }

    private fun loadImage() {
        val dontTransform = placeholderOf(R.drawable.ic_launcher_background).dontTransform()
        Glide.with(this)
                .load(url)
                .apply(dontTransform)
                .into(icon)
    }
}