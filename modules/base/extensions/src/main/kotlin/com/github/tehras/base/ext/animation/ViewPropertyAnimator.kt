package com.github.tehras.base.ext.animation

import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.onAnimStart(start: () -> Unit): ViewPropertyAnimator {
    setListener(AnimListener(onStart = start))

    return this
}

fun ViewPropertyAnimator.onAnimEnd(end: () -> Unit): ViewPropertyAnimator {
    setListener(AnimListener(onEnd = end))

    return this
}

fun ViewPropertyAnimator.onAnim(start: () -> Unit, end: () -> Unit): ViewPropertyAnimator {
    setListener(AnimListener(onEnd = end, onStart = start))

    return this
}