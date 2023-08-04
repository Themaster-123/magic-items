package me.sploky.magicitems.magicitemsbase.items.ability.misc

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.magicitemsbase.MagicItemType
import me.sploky.magicitems.magicitemsbase.data.TotemOfDyingData
import me.sploky.magicitems.magicitemsbase.items.MagicItemStack
import me.sploky.magicitems.magicitemsbase.items.ability.InteractAbilityItem
import me.sploky.magicitems.magicitemsbase.items.ability.PunchInteractAbilityItem
import me.sploky.magicitems.player.getMagicData
import me.sploky.magicitems.player.setMagicData
import me.sploky.magicitems.utils.math.MathUtils
import me.sploky.magicitems.utils.math.toBukkitVector
import me.sploky.magicitems.utils.math.vector3d
import me.sploky.magicitems.utils.misc.TransformationUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import org.joml.Vector3d
import org.joml.Math
import kotlin.math.*

open class TotemOfDying: MagicItemStack(), InteractAbilityItem, PunchInteractAbilityItem {
    override val name: TextComponent
        get() = Component.text("Totem Of ", NamedTextColor.GOLD).append(Component.text("Dying", NamedTextColor.DARK_RED))
    override val description: List<TextComponent>
        get() = listOf(Component.text("Last Resort.", NamedTextColor.RED))
    override val material: Material
        get() = Material.TOTEM_OF_UNDYING
    override val itemType: MagicItemType
        get() = MagicItemType.TOTEM_OF_DYING
    open val deathRadius: Float
        get() = 10f
    open val grabbedRadius: Float
        get() = 5f
    open val grabSpeed: Float
        get() = .1f
    open val launchForce: Float
        get() = 20f
    open val ungrabbableDuration: Float
        get() = 3f
    open val armParts: Int
        get() = 8
    open val armBlock: Material
        get() = Material.BLACK_WOOL


    override fun useAbility(itemStack: ItemStack, player: Player) {
        val totemOfDyingData = getTotemOfDyingData(player)

        if (!totemOfDyingData.abilityActive) {
            startAbility(itemStack, player)
            return
        }

        stopAbility(itemStack, player)
    }

    open fun stopAbility(itemStack: ItemStack, player: Player) {
        val totemOfDyingData = getTotemOfDyingData(player)
        Bukkit.getScheduler().cancelTask(totemOfDyingData.taskId)
        totemOfDyingData.taskId = -1
        deactivatedAlert(player)
        clearEntities(player)
    }

    override fun punchAbility(itemStack: ItemStack, player: Player) {
        launchEntities(itemStack, player)
    }

    protected open fun getTotemOfDyingData(player: Player): TotemOfDyingData {
        return player.getMagicData(itemType).let { it as TotemOfDyingData? } ?: TotemOfDyingData()
    }

    protected open fun startAbility(itemStack: ItemStack, player: Player) {
        val totemOfDyingData = getTotemOfDyingData(player)

        activatedAlert(player)

        totemOfDyingData.taskId = object: BukkitRunnable() {
            override fun run() {
                grabEntitiesInRadius(deathRadius, player, totemOfDyingData.grabbedEntities, totemOfDyingData.releasedEntities)
                dragEntities(totemOfDyingData.grabbedEntities, player)
                filterEntities(player)
            }

        }.runTaskTimer(SplokysMagicItems.pluginInstance, 0, 1).taskId

        player.setMagicData(MagicItemType.TOTEM_OF_DYING, totemOfDyingData)
    }

    protected open fun grabEntitiesInRadius(radius: Float, player: Player, grabbedEntities: MutableList<GrabbedEntity>, releasedEntities: MutableMap<Int, Long>) {
        val world = player.world
        val deathRadiusSquared = deathRadius * deathRadius

        val targetedEntities = world.getNearbyLivingEntities(player.location, deathRadiusSquared.toDouble())
        { entity ->
            entity.location.distanceSquared(player.location) <= deathRadiusSquared &&
                    entity.entityId != player.entityId &&
                    grabbedEntities.find { entity.entityId == it.entity.entityId } == null &&
                    !releasedEntities.containsKey(entity.entityId) &&
                    !entity.isDead
        }

        targetedEntities.forEach { grabbedEntities.add(generateGrabbedEntity(it, player)) }

    }

    protected open fun generateGrabbedEntity(livingEntity: LivingEntity, player: Player): GrabbedEntity {
        val armDisplays = List(armParts) {
            val blockDisplay = player.world.spawnEntity(player.location, EntityType.BLOCK_DISPLAY) as BlockDisplay
            blockDisplay.block = armBlock.createBlockData()
            blockDisplay
        }
        val grabbedEntity = GrabbedEntity(livingEntity, armDisplays)
        moveArm(grabbedEntity, player)

        grabbedEntity.arm.forEach {
            spawnArmParticle(it)
        }

        playPickupSound(livingEntity.location)

        return grabbedEntity
    }

    protected open fun dragEntities(grabbedEntities: List<GrabbedEntity>, player: Player) {
        for (i in grabbedEntities.indices) {
            val angle = Math.toRadians(i.toDouble() / grabbedEntities.size * 360.0 + player.eyeLocation.yaw)
            val grabbedEntity = grabbedEntities[i]
            val entity = grabbedEntity.entity

            val direction = Vector3d(cos(angle), 0.0, sin(angle))
            val teleportPos = player.location.toVector().vector3d().add(direction.mul(grabbedRadius.toDouble())).add(0.0, 1.5, 0.0)
            val teleportPosLerped = entity.location.toVector().vector3d().lerp(teleportPos, grabSpeed.toDouble())

            entity.teleport(entity.location.set(teleportPosLerped.x, teleportPosLerped.y, teleportPosLerped.z))
            moveArm(grabbedEntity, player)
        }
    }

    protected open fun moveArm(grabbedEntity: GrabbedEntity, player: Player) {
        val arm = grabbedEntity.arm
        for (i in arm.indices) {
            val blockDisplay = grabbedEntity.arm[i]
            val armProgress = i.toDouble() / (arm.size.toDouble() - 1)
            val entityPos = grabbedEntity.entity.location.toVector()
            val playerPos = player.location.toVector()

            val armPartPosition = Vector(
                Math.lerp(playerPos.x, entityPos.x, armProgress),
                Math.lerp(playerPos.y, entityPos.y,  armProgress.pow(3)),
                Math.lerp(playerPos.z, entityPos.z, armProgress))

            // rotates the display towards the entity
            val armDirection = playerPos.clone().subtract(entityPos).normalize()
            val angle = atan2(armDirection.x, armDirection.z).toFloat()

            SplokysMagicItems.pluginInstance.logger.info(angle.toString())
            blockDisplay.interpolationDelay = 0
            blockDisplay.interpolationDuration = 1
            blockDisplay.transformation = TransformationUtils.createTransformation(blockDisplay.location.toVector(), armPartPosition,
                MathUtils.eulerToQuaternion(0.0f, angle, 0.0f))
        }
    }

    protected open fun launchEntities(itemStack: ItemStack, player: Player) {
        val totemOfDyingData = getTotemOfDyingData(player)

        totemOfDyingData.grabbedEntities.forEach {
            val entity = it.entity
            var multiplier = 1f

            if (entity !is Player)
                multiplier = .3f

            entity.velocity = entity.velocity.add(player.eyeLocation.direction.multiply(launchForce * multiplier))
        }

        stopAbility(itemStack, player)
    }

    protected open fun clearEntities(player: Player) {
        val totemOfDyingData = getTotemOfDyingData(player)

        totemOfDyingData.grabbedEntities.forEach {
            totemOfDyingData.releasedEntities[it.entity.entityId] = it.entity.world.gameTime + (ungrabbableDuration * 20).toInt()
            destroyArm(it)
            playReleaseSound(it.entity.location)
        }
        totemOfDyingData.grabbedEntities.clear()
    }

    protected open fun filterEntities(player: Player) {
        val totemOfDyingData = getTotemOfDyingData(player)

        val world = player.world
        totemOfDyingData.grabbedEntities = totemOfDyingData.grabbedEntities.filter {
            if (it.entity.isDead) {
                destroyArm(it)
                playReleaseSound(it.entity.location)
                return@filter false
            }
            true
        }.toMutableList()

        totemOfDyingData.releasedEntities = totemOfDyingData.releasedEntities.filter { it.value > world.gameTime }.toMutableMap()
    }

    protected open fun activatedAlert(player: Player) {
        player.sendMessage(name.append{Component.text(" Activated", NamedTextColor.GREEN)})
    }

    protected open fun deactivatedAlert(player: Player) {
        player.sendMessage(name.append{Component.text(" Deactivated", NamedTextColor.RED)})

    }

    protected open fun destroyArm(grabbedEntity: GrabbedEntity) {
        grabbedEntity.arm.forEach {
            it.remove()

            spawnArmParticle(it)
        }
    }

    protected open fun spawnArmParticle(blockDisplay: BlockDisplay) {
        blockDisplay.world.spawnParticle(Particle.REDSTONE,
            blockDisplay.location.add(blockDisplay.transformation.translation.toBukkitVector()),
            40, .8, .8, .8, DustOptions(Color.BLACK, 1f))
    }

    protected open fun playPickupSound(location: Location) {
        location.world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 2.5f, 2.0f)

    }

    protected open fun playReleaseSound(location: Location) {
        location.world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 3.5f, 1f)

    }

    data class GrabbedEntity(val entity: LivingEntity, val arm: List<BlockDisplay>) {

        override fun equals(other: Any?): Boolean {
            if (other is GrabbedEntity)
                return other.entity.entityId == this.entity.entityId
            if (other is Entity)
                return entity.entityId == other.entityId

            return false
        }

        override fun hashCode(): Int {
            return entity.hashCode()
        }
    }


}
