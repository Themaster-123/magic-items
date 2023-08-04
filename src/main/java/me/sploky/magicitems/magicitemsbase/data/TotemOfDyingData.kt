package me.sploky.magicitems.magicitemsbase.data

import me.sploky.magicitems.magicitemsbase.items.ability.misc.TotemOfDying

data class TotemOfDyingData(override var taskId: Int = -1): MagicItemData, ContinuousTaskAbilityData {
    var grabbedEntities: MutableList<TotemOfDying.GrabbedEntity> = mutableListOf()
    var releasedEntities: MutableMap<Int, Long> = mutableMapOf()
}