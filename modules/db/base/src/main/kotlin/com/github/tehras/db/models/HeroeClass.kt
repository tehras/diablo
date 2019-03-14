package com.github.tehras.db.models

import androidx.annotation.ColorRes
import com.github.tehras.db.base.R

enum class HeroeClass(@ColorRes val color: Int, val iconName: String) {
    DEMON_HUNTER(R.color.demon_hunter, "demon hunter"),
    BARBARIAN(R.color.barbarian, "barbarian"),
    WITCH_DOCTOR(R.color.witch_doctor, "witchdoctor"),
    NECROMANCER(R.color.necromancer, "necromancer"),
    WIZARD(R.color.wizard, "wizard"),
    MONK(R.color.monk, "monk"),
    CRUSADER(R.color.crusader, "crusader")
}

fun TimePlayed.fromHero(heroeClass: HeroeClass): Double {
    return when (heroeClass) {
        HeroeClass.DEMON_HUNTER -> this.demonHunter
        HeroeClass.BARBARIAN -> this.barbarian
        HeroeClass.WITCH_DOCTOR -> this.witchDoctor
        HeroeClass.NECROMANCER -> this.necromancer
        HeroeClass.WIZARD -> this.wizard
        HeroeClass.MONK -> this.monk
        HeroeClass.CRUSADER -> this.crusader
    }
}