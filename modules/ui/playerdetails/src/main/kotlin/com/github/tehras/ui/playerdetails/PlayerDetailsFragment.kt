package com.github.tehras.ui.playerdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.tehras.base.arch.rx.shareBehavior
import com.github.tehras.base.arch.viewModel
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.base.ext.fragment.withBundle
import com.github.tehras.base.ext.kotlin.format
import com.github.tehras.db.models.Kills
import com.github.tehras.db.models.Player
import com.github.tehras.ui.playerdetails.PlayerDetailsFragment.Companion.BUNDLE_PLAYER_GAME_TAG
import com.github.tehras.ui.playerdetails.hero.HeroAdapter
import com.github.tehras.ui.playerdetails.seasonal.SeasonalAdapter
import com.jakewharton.rxbinding3.appcompat.navigationClicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.playerdetails_view_fragment.*
import kotlinx.android.synthetic.main.playerdetails_view_fragment_content.*
import kotlinx.android.synthetic.main.playerdetails_view_fragment_kills.*
import kotlinx.android.synthetic.main.playerdetails_view_fragment_levels.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PlayerDetailsFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val playerDetailsViewModel by viewModel<PlayerDetailsViewModel> { factory }
    private val createDisposable = CompositeDisposable()

    private val heroAdapter by lazy { HeroAdapter() }
    private val seasonalAdapter by lazy { SeasonalAdapter() }

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
        initHeroesList()
        initSeasonalList()

        val stateObservable = playerDetailsViewModel
            .observeState()
            .shareBehavior()

        createDisposable += stateObservable
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.data }
            .subscribeBy { renderData(it) }

        createDisposable += stateObservable
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.data.player.heroes }
            .distinctUntilChanged()
            .subscribe(heroAdapter)

        createDisposable += stateObservable
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.data.player.seasonalProfiles }
            .distinctUntilChanged()
            .subscribe(seasonalAdapter)
    }


    override fun onDestroyView() {
        createDisposable.clear()

        super.onDestroyView()
    }

    private fun renderData(data: PlayerData) {
        playerdetails_toolbar.title = data.player.battleTag.substringBeforeLast("#")
        playerdetails_toolbar.subtitle = data.player.guildName

        populateLevels(data.player)
        populateKills(data.player.kills)
    }

    private fun populateLevels(player: Player) {
        playerdetails_paragon_level.value("${player.paragonLevel}")
        playerdetails_hardcore_level.value("${player.highestHardcoreLevel}")
        playerdetails_season_hardcore_level.value("${player.paragonLevelSeasonHardcore}")
    }

    private fun populateKills(kills: Kills) {
        playerdetails_kills_monsters.value(kills.monsters.format())
        playerdetails_kills_hc_monsters.value(kills.hardcoreMonsters.format())
        playerdetails_kills_elites.value(kills.elites.format())
    }

    private fun initHeroesList() {
        playerdetails_heroes_rv.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = heroAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun initSeasonalList() {
        playerdetails_seasonal_rv.run {
            layoutManager = LinearLayoutManager(context)
            adapter = seasonalAdapter
            isNestedScrollingEnabled = false
        }
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