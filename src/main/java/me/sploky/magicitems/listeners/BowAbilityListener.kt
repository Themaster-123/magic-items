package me.sploky.magicitems.listeners

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.magicitemsbase.items.ability.BowAbilityItem
import me.sploky.magicitems.utils.datacontainer.MagicDataContainerUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack
import java.util.UUID

class BowAbilityListener: Listener {
    private var shotBowsMap = mutableMapOf<UUID, ItemStack>()

    init {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(
            SplokysMagicItems.pluginInstance,
            {
                shotBowsMap = shotBowsMap.filter { !(Bukkit.getEntity(it.key)?.isDead ?: true) }.toMutableMap()
            }, 0, 20)
    }

    @EventHandler
    fun onEntityShootBow(event: EntityShootBowEvent) {
        val entity = event.entity

        if (entity !is Player)
            return

        val itemStack = event.bow ?: return
        val magicItemType = MagicDataContainerUtils.getMagicItemType(itemStack) ?: return
        val magicItem = magicItemType.getMagicItem()

        if (magicItem is BowAbilityItem) {
            magicItem.onFire(entity, itemStack, event.projectile as Projectile)
            shotBowsMap[event.projectile.uniqueId] = itemStack
        }


        MagicDataContainerUtils.setMagicItemType(event.projectile.persistentDataContainer, magicItemType)
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        val projectile = event.entity
        val entity = projectile.shooter

        if (entity !is Player)
            return

        val persistentDataContainer = projectile.persistentDataContainer

        val magicItem = MagicDataContainerUtils.getMagicItem(persistentDataContainer)


        if (magicItem is BowAbilityItem) {

            val itemStack = shotBowsMap[projectile.uniqueId] ?: return
            event.isCancelled = magicItem.onArrowHit(entity, itemStack, projectile, event.hitEntity, event.hitBlock, event.hitBlockFace)
        }
    }

}