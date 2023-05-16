package me.sploky.magicitems.listeners

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import me.sploky.magicitems.utils.datacontainer.MagicDataContainerUtils
import me.sploky.magicitems.magicitemsbase.items.ability.MeleeAbilityItem
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.EquipmentSlot

class MeleeAbilityListener: Listener {
    @EventHandler
    fun onPlayerAttackEntity(event: PrePlayerAttackEntityEvent) {
        val player = event.player
        val itemStack = player.inventory.getItem(EquipmentSlot.HAND)

        val magicItem = MagicDataContainerUtils.getMagicItem(itemStack) ?: return
        val entityAttacked = event.attacked

        if (magicItem is MeleeAbilityItem && entityAttacked is LivingEntity) {
            magicItem.onAttackEntity(itemStack, player, entityAttacked)
        }
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val killer = entity.killer
        if (killer is Player) {
            val itemStack = killer.inventory.getItem(EquipmentSlot.HAND)

            val magicItem = MagicDataContainerUtils.getMagicItem(itemStack) ?: return


            if (magicItem is MeleeAbilityItem) {
                magicItem.onKillEntity(itemStack, killer, entity)
            }


        }
    }

}