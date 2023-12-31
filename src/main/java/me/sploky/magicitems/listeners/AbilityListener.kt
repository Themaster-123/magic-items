package me.sploky.magicitems.listeners

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.events.PlayerHeldItemChangeEvent
import me.sploky.magicitems.utils.datacontainer.MagicDataContainerUtils
import me.sploky.magicitems.magicitemsbase.items.ability.CancelableAbilityItem
import me.sploky.magicitems.magicitemsbase.items.ability.InteractAbilityItem
import me.sploky.magicitems.magicitemsbase.items.ability.PunchInteractAbilityItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class AbilityListener: Listener {
    private val playerAbilityUsedList = mutableListOf<Player>()

    init {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SplokysMagicItems.pluginInstance,
            { playerAbilityUsedList.clear() }, 0, 1)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.hand == EquipmentSlot.OFF_HAND) return

        val itemStack = event.item ?: return

        val magicItem = MagicDataContainerUtils.getMagicItem(itemStack) ?: return
        val player = event.player

        if (magicItem !is InteractAbilityItem && magicItem !is PunchInteractAbilityItem)
            return


        event.isCancelled = true

        if (hasUsedAbilityOnSameTick(player))
            return

        playerAbilityUsedList.add(player)

        if (magicItem is InteractAbilityItem && (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK))
            magicItem.useAbility(itemStack, player)

        if (magicItem is PunchInteractAbilityItem && (event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK))
            magicItem.punchAbility(itemStack, player)

    }

    @EventHandler
    fun onPlayerHeldItemChange(event: PlayerHeldItemChangeEvent) {
        val itemStack = event.prevHeldItem
        val magicItem = MagicDataContainerUtils.getMagicItem(itemStack) ?: return

        if (magicItem is CancelableAbilityItem) {
            magicItem.cancelAbility(itemStack, event.player)
        }
    }

    private fun hasUsedAbilityOnSameTick(player: Player): Boolean {
        return playerAbilityUsedList.contains(player)
    }
}