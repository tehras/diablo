package com.github.tehras.db.models

import androidx.annotation.ColorRes
import com.github.tehras.db.base.R

enum class HeroeClass(@ColorRes val color: Int, val iconName: String, val displayName: String) {
    DEMON_HUNTER(R.color.demon_hunter, "demon hunter", "DH"),
    BARBARIAN(R.color.barbarian, "barbarian", "Barb"),
    WITCH_DOCTOR(R.color.witch_doctor, "witchdoctor", "WD"),
    NECROMANCER(R.color.necromancer, "necromancer", "Necro"),
    WIZARD(R.color.wizard, "wizard", "Wizard"),
    MONK(R.color.monk, "monk", "Monk"),
    CRUSADER(R.color.crusader, "crusader", "Crusader")
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