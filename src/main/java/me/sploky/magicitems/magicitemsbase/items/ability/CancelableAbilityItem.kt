package me.sploky.magicitems.magicitemsbase.items.ability

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface CancelableAbilityItem {
    fun cancelAbility(itemStack: ItemStack, player: Player)
}