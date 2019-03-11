package com.github.tehras.ui.commonviews.views

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.github.tehras.base.ext.views.hideKeyboard
import com.github.tehras.ui.commonviews.R
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.editorActions
import com.jakewharton.rxbinding3.widget.textChanges
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import kotlinx.android.synthetic.main.common_views_search_layout.view.*
import java.util.concurrent.TimeUnit

class SearchView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val textChangeRelay = PublishRelay.create<SearchEvent>()
    private val viewDisposable = CompositeDisposable()

    init {
        inflate(context, R.layout.common_views_search_layout, this)
        var hint = ""

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SearchView,
            0, 0
        ).apply {
            try {
                hint = getString(R.styleable.SearchView_hint) ?: ""
            } finally {
                recycle()
            }
        }

        common_views_search_layout_edit_text.hint = hint
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val textChangeObservable = common_views_search_layout_edit_text
            .textChanges()
            .doOnNext {
                if (it.isEmpty()) {
                    hideClear()
                } else {
                    showClear()
                }
            }

        viewDisposable += common_views_search_layout_edit_text
            .editorActions { it == EditorInfo.IME_ACTION_SEARCH }
            .throttleFirst(200, TimeUnit.MILLISECONDS)
            .withLatestFrom(textChangeObservable)
            .map { (_, text) ->
                SearchEvent.Complete(text)
            }
            .doOnNext { clearAndReleaseFocus() }
            .subscribe(textChangeRelay)

        viewDisposable += textChangeObservable
            .map { SearchEvent.Updating(it) }
            .subscribe(textChangeRelay)
    }

    override fun onDetachedFromWindow() {
        viewDisposable.clear()

        super.onDetachedFromWindow()
    }

    private fun hideClear() {
        common_views_search_clear.run {
            if (isVisible) {
                animate()
                    .alpha(0f)
                    .start()
                    .also {
                        isVisible = false
                    }
            }
        }
    }

    private fun showClear() {
        common_views_search_clear.run {
            if (!isVisible) {
                animate()
                    .alpha(1f)
                    .start()
                    .also {
                        isVisible = true

                        clicks()
                            .throttleFirst(500, TimeUnit.MILLISECONDS)
                            .subscribe {
                                clearAndReleaseFocus()
                            }
                    }
            }
        }
    }

    private fun clearAndReleaseFocus() {
        common_views_search_layout_edit_text.clearFocus()
        common_views_search_layout_edit_text.setText("")

        hideKeyboard()
    }

    fun textEvents(): Observable<SearchEvent> = textChangeRelay.distinctUntilChanged()
}

sealed class SearchEvent {
    data class Updating(val text: CharSequence) : SearchEvent()
    data class Complete(val text: CharSequence) : SearchEvent()
}
