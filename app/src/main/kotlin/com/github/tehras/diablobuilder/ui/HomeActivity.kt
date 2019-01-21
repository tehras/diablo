package com.github.tehras.diablobuilder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.tehras.diablobuilder.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        initializeBottomNav()

        if (savedInstanceState == null) {
            // Initial state
            home_bottom_nav_view.selectedItemId = R.id.menu_action_leaderboards
        }
    }

    // Sets up Bottom Sheet
    private fun initializeBottomNav() {
        home_bottom_nav_view
            .setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_action_leaderboards -> startLeaderboardsFragment()
                }

                true
            }
    }

    private fun startLeaderboardsFragment() {
        // TODO
    }

    private fun startFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_fragment_container, fragment)
            .commit()
    }
}
