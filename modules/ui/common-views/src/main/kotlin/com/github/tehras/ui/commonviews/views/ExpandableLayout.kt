package com.github.tehras.ui.commonviews.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.view.isVisible
import com.github.tehras.base.ext.animation.onAnimEnd
import com.github.tehras.ui.commonviews.R
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.common_views_expandable_layout.view.*
import java.util.concurrent.TimeUnit

class ExpandableLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var disposable: Disposable? = null

    init {
        inflate(context, R.layout.common_views_expandable_layout, this)

        var isExpanded = false
        var header = ""
        var layout: Int = -1

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ExpandableLayout,
            0, 0
        ).apply {
            try {
                isExpanded = getBoolean(R.styleable.ExpandableLayout_expanded, false)
                header = getString(R.styleable.ExpandableLayout_header) ?: ""
                layout = getResourceIdOrThrow(R.styleable.ExpandableLayout_layout)
            } finally {
                recycle()
            }
        }

        common_views_expandable_title.text = header
        common_views_expandable_container.addView(View.inflate(context, layout, null))

        toggle(isExpanded)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        disposable?.dispose()
        disposable = common_views_expandable_header
            .clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .map { !isExpanded() }
            .subscribe(::toggle)
    }

    override fun onDetachedFromWindow() {
        disposable?.dispose()

        super.onDetachedFromWindow()
    }

    fun toggle(expand: Boolean) {
        if (expand) {
            expand()
        } else {
            collapse()
        }
    }

    private fun isExpanded() = common_views_expandable_container.isVisible

    private fun expand() {
        if (isExpanded()) return

        common_views_expandable_button
            .animate()
            .rotation(180f)
            .onAnimEnd {
                common_views_expandable_container.isVisible = true
            }
            .start()
    }

    private fun collapse() {
        if (!isExpanded()) return

        common_views_expandable_button
            .animate()
            .rotation(0f)
            .onAnimEnd {
                common_views_expandable_container.isVisible = false
            }
            .start()

    }
}