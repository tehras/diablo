package com.github.tehras.ui.playerdetails.seasonal

import android.annotation.SuppressLint
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.tehras.api.icons.heroIconLg
import com.github.tehras.base.ext.kotlin.format
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.github.tehras.base.glide.GlideApp
import com.github.tehras.db.models.*
import com.github.tehras.ui.commonviews.views.MultiColorLayout
import com.github.tehras.ui.playerdetails.R
import io.reactivex.functions.Consumer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.playerdetails_seasonal_list_item.*

class SeasonalAdapter : RecyclerView.Adapter<SeasonalViewHolder>(), Consumer<Map<String, SeasonalProfile>> {
    private val seasonals: MutableList<Seasonal> = mutableListOf()
    override fun accept(profiles: Map<String, SeasonalProfile>) {
        val animate = seasonals.isEmpty()
        seasonals.clear()
        seasonals.addAll(profiles.asSequence().map { Seasonal(it.key, it.value) }.filter { it.profile.seasonId != 0 })

        if (animate) {
            notifyItemRangeInserted(0, seasonals.size)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SeasonalViewHolder(parent.viewHolderFromParent(R.layout.playerdetails_seasonal_list_item))

    override fun getItemCount() = seasonals.size
    override fun onBindViewHolder(holder: SeasonalViewHolder, position: Int) = holder.bind(seasonals[position])
}

internal data class Seasonal(val name: String, val profile: SeasonalProfile)

class SeasonalViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    private val cornerRadius by lazy {
        containerView.resources.getDimensionPixelSize(R.dimen.playerdetails_hero_avatar_size).div(2)
    }

    @SuppressLint("SetTextI18n")
    internal fun bind(seasonal: Seasonal) {
        playerdetails_seasonal_name.text =
            "Season ${seasonal.profile.seasonId}"
        playerdetails_seasonal_monster_kills.value(seasonal.profile.kills.monsters.format())
        playerdetails_seasonal_elite_kills.value(seasonal.profile.kills.elites.format())
        playerdetails_seasonal_hc_monster_kills.value(seasonal.profile.kills.hardcoreMonsters.format())

        var level = ""
        if (seasonal.profile.paragonLevel > 0) {
            level = "Paragon Lvl <b>${seasonal.profile.paragonLevel}</b>"
        } else if (seasonal.profile.paragonLevelHardcore > 0) {
            level = "HC Paragon Lvl <b>${seasonal.profile.highestHardcoreLevel}</b>"
        }

        @Suppress("DEPRECATION")
        playerdetails_seasonal_paragon_level.text = Html.fromHtml(level)

        playerdetails_seasonal_avatar.populateAvatar(seasonal.profile.timePlayed, cornerRadius)
        populateSections(seasonal.profile.timePlayed)
    }

    private fun ImageView.populateAvatar(timePlayed: TimePlayed, cornerRadius: Int) {
        var highestHero = 0.0
        var highestHeroeClass: HeroeClass? = null

        HeroeClass
            .values()
            .forEach {
                val value = timePlayed.fromHero(it)
                if (value > highestHero) {
                    highestHero = value
                    highestHeroeClass = it
                }
            }

        playerdetails_seasonal_class.text = highestHeroeClass?.displayName

        val icon = heroIconLg(highestHeroeClass?.iconName ?: "", Gender.random().toString().toLowerCase())
        GlideApp
            .with(context)
            .load(icon)
            .transforms(RoundedCorners(cornerRadius), CenterCrop())
            .into(this)
    }

    private fun populateSections(timePlayed: TimePlayed) {
        val sections =
            HeroeClass
                .values()
                .map { MultiColorLayout.Section(timePlayed.fromHero(it), it.color) }

        playerdetails_seasonal_avatar_outline.setSections(sections)
    }
}