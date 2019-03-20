package com.github.tehras.ui.commonviews.animations

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.github.tehras.base.ext.animation.onAnimEnd
import com.jakewharton.rxbinding3.view.globalLayouts

@SuppressLint("CheckResult")
fun View.slideInBottom() {
    globalLayouts()
        .take(1)
        .subscribe {
            translationY = measuredHeight.toFloat()

            animate()
                .translationY(0f)
                .onAnimEnd { translationY = 0f }
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
}

fun View.fadeIn() {
    alpha = 0f

    animate()
        .alpha(1f)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .start()
}