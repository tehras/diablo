package com.github.tehras.base.ext.views

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager


fun View.hideKeyboard() {
    (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager?)
        ?.run {
            hideSoftInputFromWindow(
                rootView.windowToken,
                0
            )
        }
}