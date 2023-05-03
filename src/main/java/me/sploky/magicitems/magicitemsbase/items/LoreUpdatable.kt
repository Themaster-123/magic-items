package me.sploky.magicitems.magicitemsbase.items

import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

interface LoreUpdatable {
    fun updateLore(itemStack: ItemStack, loreIndex: Int, component: Component)
}