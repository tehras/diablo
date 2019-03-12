package com.github.tehras.base.ext.animation

import android.animation.Animator

class AnimListener(private val onStart: () -> Unit = {}, private val onEnd: () -> Unit = {}) :
    Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {}

    override fun onAnimationEnd(animation: Animator?) {
        onEnd()
    }

    override fun onAnimationCancel(animation: Animator?) {
        onEnd()
    }

    override fun onAnimationStart(animation: Animator?) {
        onStart()
    }
}