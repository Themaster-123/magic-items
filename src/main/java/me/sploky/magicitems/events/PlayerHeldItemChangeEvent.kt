package me.sploky.magicitems.events

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class PlayerHeldItemChangeEvent(player: Player, val prevHeldItem: ItemStack, val currentHeldItem: ItemStack) : PlayerEvent(player, false) {
    companion object {
        @JvmStatic
        private val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLER_LIST
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLER_LIST
    }
}