package com.github.tehras.ui.herodetails.skills

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tehras.api.icons.skillIconMd
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.github.tehras.base.glide.GlideApp
import com.github.tehras.db.models.Active
import com.github.tehras.db.models.Rune
import com.github.tehras.ui.herodetails.R
import io.reactivex.functions.Consumer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.herodetails_active_skills_layout.*

class ActiveSkillsAdapter : RecyclerView.Adapter<SkillsViewHolder>(), Consumer<List<Active>> {
    private val skills: MutableList<Active> = mutableListOf()

    override fun accept(newSkills: List<Active>) {
        val animate = skills.isEmpty()

        skills.clear()
        skills.addAll(newSkills)

        if (animate) {
            notifyItemRangeInserted(0, skills.size)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillsViewHolder = SkillsViewHolder(
        parent.viewHolderFromParent(R.layout.herodetails_active_skills_layout)
    )

    override fun getItemCount(): Int = skills.size

    override fun onBindViewHolder(holder: SkillsViewHolder, position: Int) = holder.bind(skills[position])

}

class SkillsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(active: Active) {
        GlideApp
            .with(containerView.context)
            .load(skillIconMd(active.skill.icon))
            .fitCenter()
            .into(herodetails_skill_image)

        GlideApp
            .with(containerView.context)
            .load(containerView.context.getDrawable(active.rune.toDrawable()))
            .fitCenter()
            .into(herodetails_skill_rune_image)

        herodetails_skill_rune_name.text = active.rune.name
        herodetails_skills_name.text = active.skill.name
    }
}

fun Rune.toDrawable(): Int {
    return when (type) {
        "a" -> R.drawable.rune_a
        "b" -> R.drawable.rune_b
        "c" -> R.drawable.rune_c
        "d" -> R.drawable.rune_d
        "e" -> R.drawable.rune_e
        "f" -> R.drawable.rune_f
        else -> throw IllegalStateException("Unrecognized rune type ${type}")
    }
}