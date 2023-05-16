package me.sploky.magicitems.magicitemsbase.items.ability

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack

interface BowAbilityItem {
    fun onFire(player: Player, itemStack: ItemStack, projectile: Projectile)

    /**
     * Runs whenever the Arrows of this Bow hits an entity or block
     *
     * @param player The Player who shot the Bow.
     * @param itemStack The Bow itemstack.
     * @param projectile The Arrow of the Bow.
     * @param hitEntity The Entity that was hit, if was hit.
     * @param hitBlock The Block that was hit, if was hit.
     * @param hitBlockFace The face of the Block that was hit, if was hit.
     * @return If the Event is cancelled
     */
    fun onArrowHit(player: Player, itemStack: ItemStack, projectile: Projectile, hitEntity: Entity?, hitBlock: Block?, hitBlockFace: BlockFace?): Boolean
}