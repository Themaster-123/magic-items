package me.sploky.magicitems.magicitemsbase.items.ability

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface MeleeAbilityItem  {
    fun onAttackEntity(itemStack: ItemStack, player: Player, entity: LivingEntity)

    fun onKillEntity(itemStack: ItemStack, player: Player, entity: LivingEntity)
}