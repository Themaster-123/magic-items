package me.sploky.magicitems.magicitemsbase.items.ability

import me.sploky.magicitems.magicitemsbase.items.MagicItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface InteractAbilityItem : MagicItem {

    fun useAbility(itemStack: ItemStack, player: Player);
}