package com.wanandroid.ui.example

import android.graphics.drawable.Drawable
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionSet
import android.widget.ImageView
import androidx.core.transition.doOnEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mvvm.core.base.BaseActivity
import com.wanandroid.R
import com.wanandroid.ui.example.util.Constants.IMAGE_URL_KEY
import kotlinx.android.synthetic.main.activity_image_detail.*

/**
 * @FileName: ImageDetailActivity.java
 * @author: villa_mou
 * @date: 07-11:50
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class ImageDetailActivity : BaseActivity() {
    override fun getLayoutResId() = R.layout.activity_image_detail

    override fun initView() {
        val url = intent.getStringExtra(IMAGE_URL_KEY)
        window.sharedElementEnterTransition = TransitionSet()
                .addTransition(ChangeImageTransform())
                .addTransition(ChangeBounds())
                .apply {
                    doOnEnd { act_detail_icon.load(url) }
                }


    }

    override fun initData() {
        val url = intent.getStringExtra(IMAGE_URL_KEY)
        act_detail_icon.apply {
            load(url) {
                supportStartPostponedEnterTransition()
            }
            transitionName = url
            setOnClickListener {
                onBackPressed()
            }
        }
    }

}

fun ImageView.load(url: String, loadOnlyFromCache: Boolean = false, onLoadingFinished: () -> Unit = {}) {
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            onLoadingFinished()
            return false

        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            onLoadingFinished()
            return false
        }

    }

    val requestOptions = RequestOptions.placeholderOf(R.drawable.ic_launcher_background)
            .dontTransform()
            .onlyRetrieveFromCache(loadOnlyFromCache)
    Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .listener(listener)
            .into(this)
}
