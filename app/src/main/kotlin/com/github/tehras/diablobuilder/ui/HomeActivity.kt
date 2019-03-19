package com.github.tehras.diablobuilder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.tehras.base.arch.rx.GlobalBusInjector
import com.github.tehras.base.arch.rx.NavEvent
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.diablobuilder.R
import com.github.tehras.ui.herodetails.HeroDetailsFragment
import com.github.tehras.ui.leaderboards.LeaderboardsListFragment
import com.github.tehras.ui.playerdetails.PlayerDetailsFragment
import com.github.tehras.ui.players.searchhistory.SearchHistoryFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val rxBus by lazy { findComponent<GlobalBusInjector>().globalBus() }
    private val startDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        initializeBottomNav()

        if (savedInstanceState == null) {
            // Initial state
            home_bottom_nav_view.selectedItemId = R.id.menu_action_leaderboards
        }
    }

    override fun onStart() {
        super.onStart()

        startDisposable += rxBus.observe()
            .ofType<NavEvent>()
            .subscribeBy {
                when (it) {
                    is NavEvent.StartPlayerDetailsScreen -> startPlayerDetailsScreen(it.battleTag)
                    is NavEvent.StartHeroDetailsScreen -> startHeroDetailsScreen(it.battleTag, it.heroId)
                }
            }
    }

    override fun onStop() {
        startDisposable.clear()

        super.onStop()
    }

    // Sets up Bottom Sheet
    private fun initializeBottomNav() {
        home_bottom_nav_view
            .setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_action_leaderboards -> startLeaderboardsFragment()
                    R.id.menu_action_players -> startPlayersFragment()
                }

                true
            }
    }


    private fun startPlayerDetailsScreen(battleTag: String) =
        startFullScreenFragment(PlayerDetailsFragment.newInstance(battleTag))

    private fun startHeroDetailsScreen(battleTag: String, heroId: Long) =
        startFullScreenFragment(HeroDetailsFragment.newInstance(battleTag, heroId))

    private fun startPlayersFragment() = startFragment(SearchHistoryFragment.newInstance())
    private fun startLeaderboardsFragment() = startFragment(LeaderboardsListFragment.newInstance())

    private fun startFullScreenFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.common_views_fragment_slide_in_bottom,
                R.anim.common_views_fragment_slide_out_top,
                R.anim.common_views_fragment_slide_in_top,
                R.anim.common_views_fragment_slide_out_bottom
            )
            .replace(R.id.home_fragment_full_container, fragment)
            .addToBackStack(fragment::class.simpleName)
            .commit()
    }

    private fun startFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_fragment_container, fragment)
            .commit()
    }
}
