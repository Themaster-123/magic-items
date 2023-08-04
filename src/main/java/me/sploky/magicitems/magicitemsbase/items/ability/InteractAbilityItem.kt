package me.sploky.magicitems.magicitemsbase.items.ability

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface InteractAbilityItem {

    fun useAbility(itemStack: ItemStack, player: Player)
}