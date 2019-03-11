package com.github.tehras.ui.players.searchhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.ui.commonviews.views.SearchEvent
import com.github.tehras.ui.players.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.players_search_history_fragment.*
import javax.inject.Inject

class SearchHistoryFragment : Fragment() {
    companion object {
        fun newInstance() = SearchHistoryFragment()
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by com.github.tehras.base.arch.viewModel<SearchHistoryViewModel> { factory }

    private val createDisposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findComponent<SearchHistoryFragmentComponentCreator>()
            .plusSearchHistoryFragmentComponent()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.players_search_history_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createDisposables += players_search_layout
            .textEvents()
            .ofType<SearchEvent.Complete>()
            .map { SearchHistoryUiEvent.Search(it.text.toString()) }
            .subscribe(viewModel)
    }
}