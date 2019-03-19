package com.github.tehras.ui.herodetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.tehras.base.arch.viewModel
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.base.ext.fragment.withBundle
import com.github.tehras.ui.herodetails.HeroDetailsFragment.Companion.BUNDLE_GAME_TAG
import com.github.tehras.ui.herodetails.HeroDetailsFragment.Companion.BUNDLE_HERO_ID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class HeroDetailsFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val heroDetailsViewModel by viewModel<HeroDetailsViewModel> { factory }
    private val createDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findComponent<HomeDetailsFragmentComponentCreator>()
            .plusHomeDetailssFragmentComponentBuilder()
            .battleTag(battleTag)
            .heroId(heroId)
            .build()
            .inject(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.herodetails_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createDisposable += heroDetailsViewModel
            .observeState()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { }
    }

    companion object {
        internal const val BUNDLE_GAME_TAG = "com.github.tehras.ui.herodetails.battleTag"
        internal const val BUNDLE_HERO_ID = "com.github.tehras.ui.herodetails.heroId"

        fun newInstance(battleTag: String, heroId: Long) = HeroDetailsFragment()
            .withBundle {
                putString(BUNDLE_GAME_TAG, battleTag)
                putLong(BUNDLE_HERO_ID, heroId)
            }
    }
}

val HeroDetailsFragment.battleTag: String
    get() = requireArguments().getString(BUNDLE_GAME_TAG)
        ?: throw IllegalStateException("Game tag passed in was null")

val HeroDetailsFragment.heroId: Long
    get() = requireArguments().getLong(BUNDLE_HERO_ID)