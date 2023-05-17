package me.sploky.magicitems.magicitemsbase.items.ability.wands

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.magicitemsbase.MagicItemType
import me.sploky.magicitems.magicitemsbase.data.BlockWandData
import me.sploky.magicitems.magicitemsbase.items.ability.CancelableAbilityItem
import me.sploky.magicitems.magicitemsbase.items.ExplosionItem
import me.sploky.magicitems.magicitemsbase.items.MagicItemStack
import me.sploky.magicitems.magicitemsbase.items.RangedItem
import me.sploky.magicitems.utils.math.MathUtils
import me.sploky.magicitems.player.getMagicData
import me.sploky.magicitems.player.setMagicData
import me.sploky.magicitems.utils.misc.TransformationUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.random.Random

open class BlockWand : MagicItemStack(), Wand, RangedItem, ExplosionItem, CancelableAbilityItem {
    override val name: TextComponent
        get() = Component.text("BlockWand", NamedTextColor.BLUE)

    override val description: List<TextComponent>
        get() = listOf(Component.text("Grabs any block You look at.", NamedTextColor.GOLD))

    override val material: Material
        get() = Material.DIAMOND_SHOVEL

    override val itemType: MagicItemType
        get() = MagicItemType.BLOCK_WAND

    override val maxDistance: Int
        get() = 10

    override val explosionPower: Float
        get() = 6f

    open val explosionDelay: Int
        get() = 30


    override fun useAbility(itemStack: ItemStack, player: Player) {
        val blockWandData = getBlockWandData(player)

        if (!blockWandData.abilityEnabled) return

        if (blockWandData.heldBlock == Material.AIR){
            pickUpBlock(itemStack, player, blockWandData)
        }
        else {
            shootBlock(itemStack, player, blockWandData)
        }

        player.setMagicData(itemType, blockWandData)
    }



    override fun cancelAbility(itemStack: ItemStack, player: Player) {
        val blockWandData = getBlockWandData(player)
        shootBlock(itemStack, player, blockWandData)

        player.setMagicData(itemType, blockWandData)
    }

    protected open fun pickUpBlock(itemStack: ItemStack, player: Player, blockWandData: BlockWandData) {
        cancelBlockHoldingTask(blockWandData)
        val targetBlock = player.getTargetBlockExact(maxDistance)

        blockWandData.heldBlock = targetBlock?.type ?: Material.AIR
        if (targetBlock == null)
            return

        val world = player.world

        world.playSound(targetBlock.location, targetBlock.blockSoundGroup.placeSound, 1f, 1f)

        blockWandData.abilityEnabled = false

        val blockDisplay = createBlockDisplay(world, targetBlock)
        blockWandData.currentBlockDisplay = blockDisplay

        blockDisplay.interpolationDuration = 5
        blockDisplay.interpolationDelay = -1

        var currentTick = 0
        blockWandData.taskId = Bukkit.getScheduler().
        scheduleSyncRepeatingTask(SplokysMagicItems.pluginInstance,
            {  blockHoldingTask(player, itemStack, blockWandData, currentTick); currentTick++ }, 3, 1)

        // turn Block into Air after 1 tick delay
        targetBlock.type = Material.AIR
        object : BukkitRunnable() {
            override fun run() {
                targetBlock.type = Material.AIR
            }

        }.runTaskLater(SplokysMagicItems.pluginInstance, 1)
    }

    protected open fun shootBlock(itemStack: ItemStack, player: Player, blockWandData: BlockWandData) {
        val blockDisplay = blockWandData.currentBlockDisplay ?: return
        val rayTraceResult = player.world.rayTraceBlocks(player.eyeLocation, player.eyeLocation.direction,99999.0, FluidCollisionMode.NEVER,
            true) ?: return

        cancelBlockHoldingTask(blockWandData)
        blockWandData.heldBlock = Material.AIR


        val hitPosition = rayTraceResult.hitPosition
        val oldTransformation = blockDisplay.transformation
        blockDisplay.isGlowing = true

        blockDisplay.interpolationDuration = 2
        blockDisplay.interpolationDelay = -1

        blockDisplay.transformation = TransformationUtils.createTransformation(blockDisplay.location.toVector(), hitPosition, oldTransformation.leftRotation)

        val world = player.world

        // play sounds on hit
        object : BukkitRunnable() {
            override fun run() {
                world.playSound(hitPosition.toLocation(world), rayTraceResult.hitBlock!!.blockSoundGroup.breakSound, 3f, 1f)
                world.playSound(hitPosition.toLocation(world), blockDisplay.block.soundGroup.breakSound, 3f, 1f)
            }
        }.runTaskLater(SplokysMagicItems.pluginInstance, 2)


        // explode after delay
        object : BukkitRunnable() {
            override fun run() {
                world.createExplosion(hitPosition.x, hitPosition.y, hitPosition.z, explosionPower, false, true)
                blockDisplay.remove()
            }
        }.runTaskLater(SplokysMagicItems.pluginInstance, explosionDelay.toLong())

    }

    protected open fun cancelBlockHoldingTask(blockWandData: BlockWandData) {

        if (blockWandData.taskId != -1) {
            Bukkit.getScheduler().cancelTask(blockWandData.taskId)

        }
        blockWandData.taskId = -1
        blockWandData.currentBlockDisplay = null
        blockWandData.abilityEnabled = true

    }

    protected open fun blockHoldingTask(player: Player, itemStack: ItemStack, blockWandData: BlockWandData, currentTick: Int) {
        val blockDisplay = blockWandData.currentBlockDisplay!!

        val blockDisplayRotation = MathUtils.eulerToQuaternion(
            Math.toRadians(player.eyeLocation.pitch.toDouble()).toFloat(),
            Math.toRadians(-player.eyeLocation.yaw.toDouble()).toFloat(),
            0f)

        val random = Random(currentTick)

        val blockDisplayPos = player.eyeLocation.toVector().add(player.eyeLocation.direction .multiply(3)).
        add(Vector(random.nextFloat(), random.nextFloat(), random.nextFloat()).multiply(0.001f))

        blockDisplay.interpolationDuration = 2
        blockDisplay.interpolationDelay = -1


        blockDisplay.transformation = TransformationUtils.createTransformation(blockDisplay.location.toVector(), blockDisplayPos, blockDisplayRotation)

        blockWandData.abilityEnabled = true
    }

    protected open fun createBlockDisplay(world: World, block: Block): BlockDisplay {
        val blockDisplay = world.spawnEntity(block.location, EntityType.BLOCK_DISPLAY) as BlockDisplay
        blockDisplay.block = block.blockData.clone()
        return blockDisplay
    }

    protected open fun getBlockWandData(player: Player): BlockWandData {
        return player.getMagicData(itemType).let { it as BlockWandData? } ?: BlockWandData()
    }

}