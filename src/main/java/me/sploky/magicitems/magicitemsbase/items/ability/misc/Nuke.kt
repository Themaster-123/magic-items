package me.sploky.magicitems.magicitemsbase.items.ability.misc

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.magicitemsbase.MagicItemType
import me.sploky.magicitems.magicitemsbase.items.ExplosionItem
import me.sploky.magicitems.magicitemsbase.items.MagicItemStack
import me.sploky.magicitems.magicitemsbase.items.ability.InteractAbilityItem
import me.sploky.magicitems.utils.misc.TransformationUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import org.joml.Math
import org.joml.Quaternionf

open class Nuke: MagicItemStack(), InteractAbilityItem, ExplosionItem {
    override val name: TextComponent
        get() = Component.text("Nuke", NamedTextColor.YELLOW, TextDecoration.BOLD)
    override val description: List<TextComponent>
        get() = listOf(Component.text("Why would you use this?", NamedTextColor.GOLD))
    override val material: Material
        get() = Material.TNT
    override val itemType: MagicItemType
        get() = MagicItemType.NUKE
    override val explosionPower: Float
        get() = 40f
    open val explosionDuration: Float
        get() = 8f
    open val animationDuration: Float
        get() = 10f
    open val explosionSoundPeriod: Long
        get() = 5

    override fun useAbility(itemStack: ItemStack, player: Player) {
        val location = player.location
        animateExplosion(location)
        object: BukkitRunnable() {
            override fun run() {
                createExplosion(location, player)
            }

        }.runTaskLater(SplokysMagicItems.pluginInstance, animationDuration.toLong() * 20)

        itemStack.amount -= 1
    }

    protected open fun createExplosion(location: Location, player: Player) {
        val center = location.toBlockLocation()

        val world = location.world

        var explosionInterpolation = 0f

        object: BukkitRunnable() {
            override fun run() {
                val interpolatedExplosionPower = Math.lerp(0f, explosionPower, explosionInterpolation)


                destroyBlocksInSquareRadius(interpolatedExplosionPower, world, center)
                killEntitiesInRadius(interpolatedExplosionPower, center, player)

                explosionInterpolation += 1f / (explosionDuration * 20)

                if (explosionInterpolation > 1)
                    cancel()
            }

        }.runTaskTimer(SplokysMagicItems.pluginInstance, 0, 1)
    }

    protected open fun destroyBlocksInSquareRadius(explosionPower: Float, world: World, center: Location) {
        val squareRadius = (explosionPower * 2).toInt()
        val explosionRadiusSquared = explosionPower * explosionPower
        for (x in -squareRadius..squareRadius) {
            for (y in -squareRadius..squareRadius) {
                for (z in -squareRadius..squareRadius) {
                    val blockLoc = Location(world, x.toDouble() + .5, y.toDouble() + .5, z.toDouble() + .5).add(center)
                    val block = blockLoc.block
                    if (blockLoc.distanceSquared(center) <= explosionRadiusSquared && blockLoc.block.type != Material.AIR) {
                        world.playSound(blockLoc, block.blockSoundGroup.breakSound, 2f, .25f)
                        block.type = Material.AIR

                    }
                }
            }
        }
    }

    protected open fun killEntitiesInRadius(explosionPower: Float, center: Location, player: Player) {
        val squareRadius = (explosionPower * 2).toDouble()
        val explosionRadiusSquared = explosionPower * explosionPower
        center.getNearbyEntitiesByType<LivingEntity>(LivingEntity::class.java, squareRadius, squareRadius, squareRadius)
        { it.location.distanceSquared(center) <= explosionRadiusSquared }.forEach { it.damage(9999.0, player)
            it.lastDamageCause = EntityDamageEvent(player, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, 9999.0) }
    }

    protected open fun animateExplosion(location: Location) {
        val startNukeLocation = location.toBlockLocation()
        val blockDisplay = location.world.spawnEntity(startNukeLocation, EntityType.BLOCK_DISPLAY) as BlockDisplay
        blockDisplay.block = Material.TNT.createBlockData()
        //blockDisplay.viewRange = 100000f

        location.world.playSound(location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 10f, .5f)

        blockDisplay.interpolationDelay = 0
        blockDisplay.interpolationDuration = animationDuration.toInt() * 10

        object: BukkitRunnable() {
            override fun run() {
                blockDisplay.transformation = TransformationUtils.createTransformation(Vector(0, 80, 0), Quaternionf(), centered = false)
            }
        }.runTaskLater(SplokysMagicItems.pluginInstance,2)

        object: BukkitRunnable() {
            override fun run() {
                blockDisplay.interpolationDelay = 0
                blockDisplay.interpolationDuration = animationDuration.toInt() * 10
                blockDisplay.transformation = TransformationUtils.createTransformation(Vector(0, 0, 0), Quaternionf(), centered = false)
            }
        }.runTaskLater(SplokysMagicItems.pluginInstance,animationDuration.toLong() * 10 + 2)

        val baseCloudLength = 2

        object: BukkitRunnable() {
            override fun run() {

                blockDisplay.interpolationDelay = 0
                blockDisplay.interpolationDuration = explosionDuration.toInt() * 10

                val halfExplosionPower = explosionPower / 2

                blockDisplay.transformation = TransformationUtils.createTransformation(Vector(0, 0, 0),
                    Quaternionf(), scale = Vector(halfExplosionPower, halfExplosionPower * baseCloudLength, halfExplosionPower), centered = true)

                blockDisplay.block = Material.FIRE.createBlockData()
            }
        }.runTaskLater(SplokysMagicItems.pluginInstance,animationDuration.toLong() * 20 + 2)

        object: BukkitRunnable() {
            override fun run() {

                blockDisplay.interpolationDelay = 0
                blockDisplay.interpolationDuration = explosionDuration.toInt() * 10

                val biggerExplosionPower = explosionPower * 1.35

                blockDisplay.transformation = TransformationUtils.createTransformation(Vector(0, 0, 0),
                    Quaternionf(), scale = Vector(biggerExplosionPower, biggerExplosionPower * baseCloudLength, biggerExplosionPower), centered = true)

            }
        }.runTaskLater(SplokysMagicItems.pluginInstance,animationDuration.toLong() * 20 + 2 + explosionDuration.toInt() * 10)


        object: BukkitRunnable() {
            override fun run() {

                blockDisplay.interpolationDelay = 0
                blockDisplay.interpolationDuration = explosionDuration.toInt() * 10

                val biggerExplosionPower = explosionPower * 1.35

                blockDisplay.transformation = TransformationUtils.createTransformation(Vector(0.0, -biggerExplosionPower, 0.0),
                    Quaternionf(), scale = Vector(0.0, 0.0, 0.0), centered = true)

            }
        }.runTaskLater(SplokysMagicItems.pluginInstance,animationDuration.toLong() * 20 + 2 + explosionDuration.toInt() * 20)

        object: BukkitRunnable() {
            override fun run() {
                blockDisplay.remove()
            }
        }.runTaskLater(SplokysMagicItems.pluginInstance,animationDuration.toLong() * 20 + 2 + explosionDuration.toInt() * 100)


        var explosionSoundTicks = 0

        object: BukkitRunnable() {
            override fun run() {
                explosionSoundTicks += 5


                playExplosionSound(location)

                if (explosionSoundTicks >= explosionDuration.toInt() * 20)
                    cancel()
            }
        }.runTaskTimer(SplokysMagicItems.pluginInstance, animationDuration.toLong() * 20 + 2, explosionSoundPeriod)
    }

    protected open fun playExplosionSound(location: Location) {
        playExplosionSound(location, 20f)
    }

    protected open fun playExplosionSound(location: Location, volume: Float) {
        location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, volume, 0f)

    }
}