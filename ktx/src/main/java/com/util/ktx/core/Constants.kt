package com.util.ktx.core

import android.os.Looper

/**
 * Created by mou
 * on 2019/8/30 15:06
 */
fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()