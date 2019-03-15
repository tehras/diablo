package com.github.tehras.ui.players.searchhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.tehras.base.arch.rx.shareBehavior
import com.github.tehras.base.arch.viewModel
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.db.models.Player
import com.github.tehras.ui.commonviews.animations.slideInBottom
import com.github.tehras.ui.commonviews.views.SearchEvent
import com.github.tehras.ui.players.R
import com.github.tehras.ui.players.searchhistory.adapter.SearchHistoryPlayersAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.players_search_history_fragment.*
import javax.inject.Inject
import androidx.recyclerview.widget.ItemTouchHelper
import com.github.tehras.base.ext.animation.onAnimStart
import com.github.tehras.ui.players.searchhistory.adapter.SearchHistorySwipeHelper
import kotlinx.android.synthetic.main.players_search_history_empty.*


class SearchHistoryFragment : Fragment() {
    companion object {
        fun newInstance() = SearchHistoryFragment()
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val searchVm by viewModel<SearchHistorySearchViewModel> { factory }
    private val playersVm by viewModel<SearchHistoryPlayersListViewModel> { factory }

    private val createDisposables: CompositeDisposable = CompositeDisposable()

    private val playersAdapter by lazy { SearchHistoryPlayersAdapter() }

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

        handleSearch()
        handlePlayersList()
    }

    private fun handleSearch() {
        animateSearchIn()

        createDisposables += players_search_layout
            .textEvents()
            .ofType<SearchEvent.Complete>()
            .map { SearchHistoryUiEvent.Search(it.text.toString()) }
            .subscribe(searchVm)

        val searchState = searchVm
            .observeState()
            .observeOn(AndroidSchedulers.mainThread())
            .shareBehavior()

        createDisposables += searchState
            .map { it.result }
            .distinctUntilChanged()
            .filter { it != SearchResult.NoResult }
            .subscribe(::searchResult)
    }

    private fun handlePlayersList() {
        initPlayersList()

        createDisposables += playersVm
            .observeState()
            .observeOn(AndroidSchedulers.mainThread())
            .filter { !it.isLoading }
            .map { it.players }
            .doOnNext { handleEmptyLayout(it) }
            .subscribe(playersAdapter)
    }

    private fun handleEmptyLayout(players: List<Player>) {
        if (players.isEmpty() && !players_search_empty.isVisible) {
            players_search_empty.isVisible = true
            players_search_no_players_text.alpha = 0f
            players_search_no_players_text.animate().alpha(1f).start()

            players_search_no_players_icon.alpha = 0f
            players_search_no_players_icon.animate().alpha(1f).start()
        } else {
            players_search_empty.isVisible = false
        }
    }

    private fun initPlayersList() {
        players_list.run {
            adapter = playersAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = SlideInUpAnimator()
            ItemTouchHelper(SearchHistorySwipeHelper(playersVm, context))
                .apply {
                    attachToRecyclerView(this@run)
                }
        }
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

    private fun animateSearchIn() {
        players_search_layout.slideInBottom()
    }
}