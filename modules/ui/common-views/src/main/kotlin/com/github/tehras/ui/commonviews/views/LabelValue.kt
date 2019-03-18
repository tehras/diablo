package com.github.tehras.ui.commonviews.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.github.tehras.ui.commonviews.R
import kotlinx.android.synthetic.main.common_views_label_value.view.*

class LabelValue @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.common_views_label_value, this)
        var label = ""
        var value = ""

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LabelValue,
            0, 0
        ).apply {
            try {
                label = getString(R.styleable.LabelValue_android_label) ?: ""
                value = getString(R.styleable.LabelValue_android_text) ?: ""
            } finally {
                recycle()
            }
        }

        common_views_label_value_label.text = label
        common_views_label_value_value.text = value
    }

    fun populate(label: String, value: String) {
        common_views_label_value_label.text = label
        common_views_label_value_value.text = value
    }

    fun value(value: String) {
        common_views_label_value_value.text = value
    }

    fun label(label: String) {
        common_views_label_value_label.text = label
    }
}