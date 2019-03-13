/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.ui.leaderboards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.tehras.api.leaderboards.LeaderboardsType.values
import com.github.tehras.base.arch.viewModel
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.ui.commonviews.bottomsheet.SelectorBottomSheet
import com.github.tehras.ui.leaderboards.adapter.LeaderboardsListAdapter
import com.jakewharton.rxbinding3.recyclerview.scrollStateChanges
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.leaderboards_list_fragment_view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LeaderboardsListFragment : Fragment() {
    companion object {
        private const val SELECT_ITEM_REQUEST_CODE = 169

        fun newInstance() = LeaderboardsListFragment()
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModel<LeaderboardsListViewModel> { factory }
    private val leaderboardsListAdapter by lazy { LeaderboardsListAdapter() }

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val createDisposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findComponent<LeaderboardsListComponentCreator>()
            .plusLeaderboardsListComponent()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.leaderboards_list_fragment_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        initFab()
    }

    override fun onStart() {
        super.onStart()

        disposables += viewModel
            .observeState()
            .filter { it.loadingState == LeaderboardsListState.DataState.SUCCESS }
            .map {
                it.listData
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(leaderboardsListAdapter)

        disposables += viewModel
            .observeState()
            .filter { it.dialogToShow != LeaderboardsDialog.NoDialog }
            .map { it.dialogToShow }
            .subscribe {
                when (it) {
                    is LeaderboardsDialog.LeaderboardsSelectDialog -> showSelectDialog(it.items)
                }
            }

        disposables += viewModel
            .observeState()
            .map { it.loadingState }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it == LeaderboardsListState.DataState.LOADING) {
                    leaderboards_loading.show()
                } else {
                    leaderboards_loading.hide()
                }
            }
    }

    private fun showSelectDialog(items: List<SelectorBottomSheet.Item>) {
        SelectorBottomSheet.show(
            requestCode = SELECT_ITEM_REQUEST_CODE,
            fragment = this,
            items = *items.toTypedArray(),
            title = requireContext().getString(R.string.leaderboards_select_item_title)
        )
    }

    override fun onStop() {
        super.onStop()

        disposables.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        createDisposables.clear()
    }

    private fun initRv() {
        leaderboards_rv.run {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = SlideInUpAnimator()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
            adapter = leaderboardsListAdapter
        }
    }

    private fun initFab() {
        createDisposables += leaderboards_filter_button
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .map { LeaderboardsListUiEvent.LeaderboardsTypeFilterTapped }
            .subscribe(viewModel)

        val hideFilter = {
            leaderboards_filter_button?.animate()?.alpha(0f)?.scaleX(0.5f)?.scaleY(0.5f)?.start()
        }

        val showFilter = {
            leaderboards_filter_button?.animate()?.alpha(1f)?.scaleX(1f)?.scaleY(1f)?.start()
        }

        createDisposables += leaderboards_rv
            .scrollStateChanges()
            .distinctUntilChanged()
            .subscribe {
                when (it) {
                    1, 2 -> hideFilter()
                    0 -> showFilter()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SELECT_ITEM_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val item = SelectorBottomSheet.itemFromResult(data)
                    val type = values()[item.dataCode]

                    viewModel.accept(LeaderboardsListUiEvent.LeaderboardsTypeSelected(type))
                }
            }
        }
    }
}