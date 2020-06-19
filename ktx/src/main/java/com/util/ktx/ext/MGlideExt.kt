package com.util.ktx.ext

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


/**
 * 加载普通图片给imageview
 */
fun ImageView.loadUrl(imgUrl: String, @DrawableRes placeholder: Int, @DrawableRes error: Int) {
    if (TextUtils.isEmpty(imgUrl)) {
        return
    }
    val options = RequestOptions().also {
        it.placeholder(placeholder)
        it.error(error)
    }
    Glide.with(context).load(imgUrl).apply(options).into(this)
}

/**
 * 加载圆角图片给imageview
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrlRoundCorner(context: Context, imgUrl: String, cornerDp: Float, @DrawableRes placeholder: Int, @DrawableRes error: Int) {
    if (TextUtils.isEmpty(imgUrl)) {
        return
    }
    val options = RequestOptions()
    options.placeholder(placeholder)
    options.error(error)
    options.optionalTransform(MGlideRoundedCornersTransform(context,cornerDp, MGlideRoundedCornersTransform.CornerType.ALL))
    Glide.with(context).load(imgUrl).apply(options).into(this)
}

class GlideWrapper {
    var context: Context? = null
    var url: String? = null

    var imageView: ImageView? = null

    @DrawableRes
    var placeholder: Int = -1

    @DrawableRes
    var error: Int = -1

    var corner: Float = -1f
}

fun loadImage(init: GlideWrapper.() -> Unit) {
    val glideWrapper = GlideWrapper()
    glideWrapper.init()
    executeWrapper(glideWrapper)
}

fun executeWrapper(glideWrapper: GlideWrapper) {
    glideWrapper.imageView?.let {
        val options = RequestOptions()
        if (glideWrapper.placeholder > 0) {
            options.placeholder(glideWrapper.placeholder)
        }
        if (glideWrapper.error > 0) {
            options.error(glideWrapper.error)
        }
        if (glideWrapper.corner > 0) {
            glideWrapper.context?.let { it1 -> MGlideRoundedCornersTransform(it1,glideWrapper.corner, MGlideRoundedCornersTransform.CornerType.ALL) }?.let { it2 -> options.optionalTransform(it2) }
        }
        glideWrapper.context?.let { it1 -> Glide.with(it1).load(glideWrapper.url).apply(options).into(it) }
    }
}
