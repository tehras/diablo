package com.github.tehras.ui.playerdetails.hero

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.tehras.api.icons.heroIconLg
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.github.tehras.base.glide.GlideApp
import com.github.tehras.db.models.Hero
import com.github.tehras.db.models.toGender
import com.github.tehras.ui.playerdetails.PlayerDetailsUiEvent
import com.github.tehras.ui.playerdetails.R
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.playerdetails_heroes_list_item.*
import java.util.concurrent.TimeUnit

class HeroAdapter(private val clickConsumer: Consumer<PlayerDetailsUiEvent>) :
    RecyclerView.Adapter<HeroViewHolder>(), Consumer<List<Hero>> {
    private val heroes: MutableList<Hero> = mutableListOf()

    override fun accept(newHeroes: List<Hero>) {
        val shouldAnimate = heroes.isEmpty()
        heroes.clear()
        heroes.addAll(newHeroes)

        if (shouldAnimate) {
            notifyItemRangeInserted(0, heroes.size)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = heroes.size
    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) = holder.bind(heroes[position])
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HeroViewHolder(parent.viewHolderFromParent(R.layout.playerdetails_heroes_list_item), clickConsumer)
}


class HeroViewHolder(
    override val containerView: View,
    private val clickConsumer: Consumer<PlayerDetailsUiEvent>
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    private val cornerRadius by lazy {
        containerView.resources.getDimensionPixelSize(R.dimen.playerdetails_hero_avatar_size).div(2)
    }
    private var clickDisposable: Disposable? = null

    fun bind(hero: Hero) {
        playerdetails_hero_level.text =
            if (hero.paragonLevel > 0) "Paragon ${hero.paragonLevel}" else "Level ${hero.level}"
        playerdetails_hero_name.text = hero.name
        clickDisposable?.dispose()
        clickDisposable = playerdetails_hero_container
            .clicks()
            .throttleFirst(200, TimeUnit.MILLISECONDS)
            .map { PlayerDetailsUiEvent.ShowHero(heroId = hero.id) }
            .subscribe(clickConsumer)

        val icon = heroIconLg(hero.heroeClass, hero.gender.toGender().name.toLowerCase())

        GlideApp
            .with(containerView.context)
            .load(icon)
            .transforms(RoundedCorners(cornerRadius), CenterCrop())
            .into(playerdetails_hero_avatar)

        var extra = ""
        if (hero.hardcore) extra += "Hardcore "
        else if (hero.seasonal) extra += "Seasonal"
        playerdetails_hero_extra.text = extra
    }
}