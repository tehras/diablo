package com.github.tehras.ui.herodetails.skills

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.tehras.api.icons.skillIconMd
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.github.tehras.base.glide.GlideApp
import com.github.tehras.db.models.Passive
import com.github.tehras.ui.herodetails.R
import io.reactivex.functions.Consumer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.herodetails_passive_skills_layout.*

class PassiveSkillsAdapter : RecyclerView.Adapter<PassiveSkillsViewHolder>(), Consumer<List<Passive>> {
    private val skills: MutableList<Passive> = mutableListOf()

    override fun accept(newSkills: List<Passive>) {
        val animate = skills.isEmpty()

        skills.clear()
        skills.addAll(newSkills)

        if (animate) {
            notifyItemRangeInserted(0, skills.size)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassiveSkillsViewHolder =
        PassiveSkillsViewHolder(
            parent.viewHolderFromParent(R.layout.herodetails_passive_skills_layout)
        )

    override fun getItemCount(): Int = skills.size

    override fun onBindViewHolder(holder: PassiveSkillsViewHolder, position: Int) = holder.bind(skills[position])

}

class PassiveSkillsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun bind(active: Passive) {
        GlideApp
            .with(containerView.context)
            .load(skillIconMd(active.skill.icon))
            .fitCenter()
            .into(herodetails_passive_skill_image)
        herodetails_passive_skills_name.text = active.skill.name
    }
}