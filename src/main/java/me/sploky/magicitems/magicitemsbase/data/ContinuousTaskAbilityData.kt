package me.sploky.magicitems.magicitemsbase.data

interface ContinuousTaskAbilityData {
    val abilityActive: Boolean
        get() = taskId != -1

    var taskId: Int
}