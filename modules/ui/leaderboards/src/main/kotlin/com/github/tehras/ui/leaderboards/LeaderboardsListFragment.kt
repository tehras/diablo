/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.ui.leaderboards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.tehras.base.arch.viewModel
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.ui.leaderboards.adapter.LeaderboardsListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.leaderboards_list_fragment_view.*
import javax.inject.Inject

class LeaderboardsListFragment : Fragment() {
    companion object {
        fun newInstance() = LeaderboardsListFragment()
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModel<LeaderboardsListViewModel> { factory }
    private val leaderboardsListAdapter by lazy { LeaderboardsListAdapter() }

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findComponent<LeaderboardsListComponentCeator>()
            .plusLeaderboardsListComponent()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.leaderboards_list_fragment_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
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
    }

    override fun onStop() {
        super.onStop()

        disposables.clear()
    }

    private fun initRv() {
        leaderboards_rv.run {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
            adapter = leaderboardsListAdapter
        }
    }

}