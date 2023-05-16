package me.sploky.magicitems.magicitemsbase.items.ability.weapons

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.magicitemsbase.MagicItemType
import me.sploky.magicitems.magicitemsbase.items.MagicItemStack
import me.sploky.magicitems.magicitemsbase.items.ability.BowAbilityItem
import me.sploky.magicitems.utils.math.MathUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

open class BouncyBow: MagicItemStack(), BowAbilityItem {
    override val name: TextComponent
        get() = Component.text("Bouncy ", NamedTextColor.GREEN).append(Component.text("Bow", NamedTextColor.WHITE))
    override val description: List<TextComponent>
        get() = listOf(Component.text("Commit War Crimes ", NamedTextColor.GOLD).
        append(Component.text("STYLISHLY", NamedTextColor.GOLD, TextDecoration.BOLD)))
    override val material: Material
        get() = Material.BOW
    override val itemType: MagicItemType
        get() = MagicItemType.BOUNCY_BOW
    protected open val arrowHitSound: Sound
        get() = Sound.BLOCK_SLIME_BLOCK_PLACE

    override fun onFire(player: Player, itemStack: ItemStack, projectile: Projectile) {
    }

    override fun onArrowHit(player: Player, itemStack: ItemStack, projectile: Projectile, hitEntity: Entity?, hitBlock: Block?, hitBlockFace: BlockFace?): Boolean {
       if (hitBlock == null || hitBlockFace == null)
           return false

        val vel = projectile.velocity
        projectile.world.playSound(projectile.location, arrowHitSound, 1.0f, 1.2f)
        projectile.world.spawnParticle(Particle.SLIME, projectile.location, 5)


        object: BukkitRunnable() {
            override fun run() {
                projectile.teleport(projectile.location.clone().add(hitBlockFace.direction.multiply(.1)))
            }
        }.runTaskLater(SplokysMagicItems.pluginInstance, 1)

        object: BukkitRunnable() {
            override fun run() {
                projectile.velocity = MathUtils.reflect(vel, hitBlockFace.direction).multiply(1.1f)

            }
        }.runTaskLater(SplokysMagicItems.pluginInstance, 2)


        return false
    }
}