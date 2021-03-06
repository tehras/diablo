package com.github.tehras.ui.herodetails

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
import com.github.tehras.db.models.HeroDetails
import com.github.tehras.ui.herodetails.HeroDetailsFragment.Companion.BUNDLE_GAME_TAG
import com.github.tehras.ui.herodetails.HeroDetailsFragment.Companion.BUNDLE_HERO_ID
import com.github.tehras.ui.herodetails.delegates.HeroItemsDelegate
import com.github.tehras.ui.herodetails.skills.ActiveSkillsAdapter
import com.github.tehras.ui.herodetails.skills.PassiveSkillsAdapter
import com.github.tehras.ui.herodetails.views.HeroItemDetailsViewModelProvider
import com.jakewharton.rxbinding3.appcompat.navigationClicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.herodetails_fragment_layout.*
import kotlinx.android.synthetic.main.herodetails_fragment_layout_content.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HeroDetailsFragment : Fragment(), HeroItemDetailsViewModelProvider {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val heroDetailsViewModel by viewModel<HeroDetailsViewModel> { factory }
    private val heroItemDetailsViewModel by viewModel<HeroItemDetailsViewModel> { factory }

    private val createDisposable = CompositeDisposable()
    private val heroItemsDelegate = HeroItemsDelegate.attach(this)

    private val activeSkillsAdapter by lazy { ActiveSkillsAdapter() }
    private val passiveSkillsAdapter by lazy { PassiveSkillsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findComponent<HomeDetailsFragmentComponentCreator>()
            .plusHomeDetailssFragmentComponentBuilder()
            .battleTag(battleTag)
            .heroId(heroId)
            .build()
            .inject(this)
    }

    override fun itemDetailsViewModel(): HeroItemDetailsViewModel = heroItemDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.herodetails_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupActiveSkills()

        val stateObservable = heroDetailsViewModel
            .observeState()
            .shareBehavior()

        createDisposable += stateObservable
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.heroDetails.items }
            .distinctUntilChanged()
            .subscribe { heroItemsDelegate.populateHeroDetails(it, herodetails_hero_layout, this) }

        createDisposable += stateObservable
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.heroDetails }
            .subscribe(::populateLayouts)

        createDisposable += stateObservable
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.heroDetails.skills.active }
            .distinctUntilChanged()
            .subscribe(activeSkillsAdapter)

        createDisposable += stateObservable
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.heroDetails.skills.passive }
            .distinctUntilChanged()
            .subscribe(passiveSkillsAdapter)
    }

    private fun setupActiveSkills() {
        herodetails_active_skills.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = SlideInRightAnimator()
            adapter = activeSkillsAdapter
        }

        herodetails_passive_skills.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = SlideInRightAnimator()
            adapter = passiveSkillsAdapter
        }
    }

    private fun setupToolbar() {
        createDisposable += herodetails_toolbar
            .navigationClicks()
            .throttleFirst(200, TimeUnit.MILLISECONDS)
            .subscribe { activity?.onBackPressed() }
    }

    private fun populateLayouts(heroDetails: HeroDetails) {
        herodetails_toolbar.title = "${heroDetails.name} • ${heroDetails.heroeClass.capitalize()}"
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