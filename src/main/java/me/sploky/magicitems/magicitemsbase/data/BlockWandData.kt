package me.sploky.magicitems.magicitemsbase.data

import org.bukkit.Material
import org.bukkit.entity.BlockDisplay

data class BlockWandData(var heldBlock: Material = Material.AIR,
                         var currentBlockDisplay: BlockDisplay? = null,
                         override var abilityEnabled: Boolean = true, override var taskId: Int = -1
) : MagicItemData, AbilityData, ContinuousTaskAbilityData {
}