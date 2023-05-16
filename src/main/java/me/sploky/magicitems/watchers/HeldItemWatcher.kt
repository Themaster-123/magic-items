package me.sploky.magicitems.watchers

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.events.PlayerHeldItemChangeEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class HeldItemWatcher: Watcher {
    private val lastItemHeldMap = mutableMapOf<Player, ItemStack>()

    override fun register() {
        object: BukkitRunnable() {
            override fun run() {
                checkPlayersHeldItems()
            }
        }.runTaskTimer(SplokysMagicItems.pluginInstance, 0, 1)
    }

    fun checkPlayersHeldItems() {
        Bukkit.getOnlinePlayers().forEach {
            val prevItem = lastItemHeldMap.getOrPut(it) { ItemStack(Material.AIR) }
            val currentHeldItem = it.inventory.getItem(EquipmentSlot.HAND)
            if (prevItem != currentHeldItem) {
                val itemChangeEvent = PlayerHeldItemChangeEvent(it, prevItem, currentHeldItem)
                itemChangeEvent.callEvent()
            }

            lastItemHeldMap[it] = currentHeldItem
        }
    }
}