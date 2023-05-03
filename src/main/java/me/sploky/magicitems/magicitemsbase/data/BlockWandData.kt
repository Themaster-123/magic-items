package me.sploky.magicitems.magicitemsbase.data

import org.bukkit.Material
import org.bukkit.entity.BlockDisplay

data class BlockWandData(var heldBlock: Material = Material.AIR,
                         var holdingBlockTask: Int = -1,
                         var currentBlockDisplay: BlockDisplay? = null,
                         override var abilityDisabled: Boolean = false
) : MagicItemData, AbilityData {
}