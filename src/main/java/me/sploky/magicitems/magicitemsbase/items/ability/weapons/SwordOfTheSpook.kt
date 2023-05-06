package me.sploky.magicitems.magicitemsbase.items.ability.weapons

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.magicitemsbase.MagicItemType
import me.sploky.magicitems.magicitemsbase.items.Cleanable
import me.sploky.magicitems.magicitemsbase.items.LoreUpdatableItemStack
import me.sploky.magicitems.magicitemsbase.items.ability.InteractAbilityItem
import me.sploky.magicitems.magicitemsbase.items.ability.MeleeAbilityItem
import me.sploky.magicitems.namespace.MagicItemKeys
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.Allay
import org.bukkit.entity.Enemy
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.random.Random

open class SwordOfTheSpook: LoreUpdatableItemStack(), MeleeAbilityItem, InteractAbilityItem, Cleanable {
    override val name: TextComponent
        get() = MiniMessage.miniMessage().deserialize("<yellow>Sword of the</yellow> <b><red>Spook</red></b>") as TextComponent
    override val description: List<TextComponent>
        get() = listOf(Component.text("Consumes the soul of mobs you kill, allows you to summon them later", NamedTextColor.GOLD))
    override val material: Material
        get() = Material.NETHERITE_SWORD
    override val itemType: MagicItemType
        get() = MagicItemType.SWORD_OF_THE_SPOOK
    open val releasedSoulDamage: Double
        get() = 8.0
    open val releasedSoulAttackRange: Double
        get() = 1.5
    protected open val soulSpawnRadius: Double
        get() = 1.0
    protected open val maxSpawnTries: Int
        get() = 10
    protected val releasedSouls = mutableListOf<Allay>()

    init {
        object : BukkitRunnable() {
            override fun run() {
                for (i in releasedSouls.indices.reversed()) {
                    val allay = releasedSouls[i]
                    soulTick(allay)
                }
            }
        }.runTaskTimer(SplokysMagicItems.pluginInstance, 20, 20)
    }


    override fun createItemStack(): ItemStack {
        return super.createItemStack().also { setSoulsCollected(it, 0) }
    }

    override fun useAbility(itemStack: ItemStack, player: Player) {
        SplokysMagicItems.pluginInstance.logger.info("right clicked")

        val soulsCollected = getSoulsCollected(itemStack)

        if (soulsCollected == 0) return

        player.playSound(player.location, Sound.BLOCK_CHAIN_FALL, 4f, 0f)
        player.playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 4f, 1.66f)

        for (i in 0 until soulsCollected) {
            spawnSoul(player.location, player.world)
        }

        setSoulsCollected(itemStack, 0)
    }

    override fun onAttackEntity(itemStack: ItemStack, player: Player, entity: LivingEntity) {
    }

    override fun onKillEntity(itemStack: ItemStack, player: Player, entity: LivingEntity) {

        setSoulsCollected(itemStack, getSoulsCollected(itemStack) + 1)
    }

    override fun cleanUp() {
        releasedSouls.forEach { it.remove() }
    }

    protected open fun spawnSoul(location: Location, world: World) {
        val allay = world.spawnEntity(location, EntityType.ALLAY) as Allay
        var spawnLocation = location.clone()
        for (i in 0 until maxSpawnTries) {
            val newLocation = spawnLocation.add(Vector(
                Random.nextDouble(-soulSpawnRadius, soulSpawnRadius),
                0.0,
                Random.nextDouble(-soulSpawnRadius, soulSpawnRadius)))
            spawnLocation = newLocation
            if (newLocation.block.isPassable) break
        }

        allay.teleport(spawnLocation)

        world.spawnParticle(Particle.REDSTONE, spawnLocation.x, spawnLocation.y, spawnLocation.z, 15, DustOptions(Color.AQUA, 2f))

        allay.setCanDuplicate(false)
        allay.equipment.setItemInMainHand(ItemStack(Material.NETHERITE_SWORD))
        releasedSouls.add(allay)
    }

    protected fun soulTick(allay: Allay) {
        if (allay.isDead) {
            releasedSouls.remove(allay)
            return
        }
        val world = allay.world
        val targetEntity = (world.getNearbyEntities(allay.location, 20.0, 20.0, 20.0) { it is Enemy }
            .minByOrNull { allay.location.distanceSquared(it.location) } ?: return) as LivingEntity
        allay.pathfinder.moveTo(targetEntity, 2.0)
        val rayTraceResult = world.rayTraceEntities(allay.location, (targetEntity.location.toVector().
        subtract(allay.location.toVector()).normalize()),releasedSoulAttackRange) {it is Enemy}
        if (/*targetEntity.location.distance(allay.location) <= .9*/ rayTraceResult?.hitEntity != null ) {
            val prevNoDamageTicks = targetEntity.noDamageTicks
            targetEntity.noDamageTicks = 0
            targetEntity.damage(releasedSoulDamage, allay)
            targetEntity.noDamageTicks = prevNoDamageTicks

        }
    }

    protected fun setSoulsCollected(itemStack: ItemStack, soulsCollected: Int) {
        val itemMeta = itemStack.itemMeta
        val soulsCollectedKey = NamespacedKey(SplokysMagicItems.pluginInstance, MagicItemKeys.SoulsCollected.key)
        itemMeta.persistentDataContainer.set(soulsCollectedKey, PersistentDataType.INTEGER, soulsCollected )
        itemStack.itemMeta = itemMeta

        updateLore(itemStack, 1, MiniMessage.miniMessage().deserialize("<yellow>Souls Collected:</yellow> <red>$soulsCollected</red>"))
    }

    protected fun getSoulsCollected(itemStack: ItemStack): Int {
        val itemMeta = itemStack.itemMeta
        val soulsCollectedKey = NamespacedKey(SplokysMagicItems.pluginInstance, MagicItemKeys.SoulsCollected.key)
        return  itemMeta.persistentDataContainer.getOrDefault(soulsCollectedKey, PersistentDataType.INTEGER, 0)
    }


}