package me.sploky.magicitems.magicitemsbase.items.ability

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface PunchInteractAbilityItem {

    fun punchAbility(itemStack: ItemStack, player: Player)
}