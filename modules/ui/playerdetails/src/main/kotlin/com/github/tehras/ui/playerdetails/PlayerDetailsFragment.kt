package com.github.tehras.ui.playerdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.tehras.base.arch.viewModel
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.base.ext.fragment.withBundle
import com.github.tehras.ui.playerdetails.PlayerDetailsFragment.Companion.BUNDLE_PLAYER_GAME_TAG
import com.jakewharton.rxbinding3.appcompat.navigationClicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.playerdetails_view_fragment.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PlayerDetailsFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val playerDetailsViewModel by viewModel<PlayerDetailsViewModel> { factory }

    private val createDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findComponent<PlayerDetailsFragmentComponentCreator>()
            .plusPlayerDetailsFragmentComponentBuilder()
            .battleTag(battleTag)
            .build()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.playerdetails_view_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        createDisposable += playerDetailsViewModel
            .observeState()
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.data }
            .subscribeBy { renderData(it) }
    }


    override fun onDestroyView() {
        createDisposable.clear()

        super.onDestroyView()
    }

    private fun renderData(data: PlayerData) {
        playerdetails_toolbar.title = data.player.battleTag
    }

    private fun initToolbar() {
        createDisposable += playerdetails_toolbar
            .navigationClicks()
            .throttleFirst(200, TimeUnit.MILLISECONDS)
            .subscribe { activity?.onBackPressed() }
    }

    companion object {
        const val BUNDLE_PLAYER_GAME_TAG = "com.github.tehras.ui.playerdetails.PLAYER_GAME_TAG"

        fun newInstance(battleTag: String): PlayerDetailsFragment {
            return PlayerDetailsFragment()
                .withBundle { putString(BUNDLE_PLAYER_GAME_TAG, battleTag) }
        }
    }
}

val PlayerDetailsFragment.battleTag: String
    get() = requireArguments().getString(BUNDLE_PLAYER_GAME_TAG)
        ?: throw IllegalStateException("Game tag passed in was null")