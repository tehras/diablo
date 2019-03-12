package com.github.tehras.ui.players.searchhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.tehras.base.arch.rx.shareBehavior
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.ui.commonviews.animations.slideInBottom
import com.github.tehras.ui.commonviews.views.SearchEvent
import com.github.tehras.ui.players.R
import io.reactivex.android.schedulers.AndroidSchedulers
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

        animateIn()

        createDisposables += players_search_layout
            .textEvents()
            .ofType<SearchEvent.Complete>()
            .map { SearchHistoryUiEvent.Search(it.text.toString()) }
            .subscribe(viewModel)

        val state = viewModel
            .observeState()
            .observeOn(AndroidSchedulers.mainThread())
            .shareBehavior()
        createDisposables += state
            .map { it.result }
            .distinctUntilChanged()
            .filter { it != SearchResult.NoResult }
            .subscribe(::searchResult)
    }

    private fun searchResult(result: SearchResult) {
        when (result) {
            SearchResult.Error -> Toast.makeText(requireContext(), "Error searching", Toast.LENGTH_LONG).show()
            is SearchResult.Success -> Toast.makeText(
                requireContext(),
                "Player found :: ${result.player.battleTag}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun animateIn() {
        players_search_layout.slideInBottom()
    }
}