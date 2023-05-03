package me.sploky.magicitems.player

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.magicitemsbase.MagicItemType
import me.sploky.magicitems.magicitemsbase.data.MagicItemData
import org.bukkit.entity.Player

fun Player.getMagicData(magicItemType: MagicItemType): MagicItemData? {
    val playerDataMap = SplokysMagicItems.pluginInstance.magicPlayerData.mutableMap;
    val typeDataMap = playerDataMap[this.uniqueId] ?: return null;
    return typeDataMap[magicItemType];

}

fun Player.setMagicData(magicItemType: MagicItemType, magicItemData: MagicItemData) {
    val playerDataMap = SplokysMagicItems.pluginInstance.magicPlayerData.mutableMap;
    val typeDataMap = playerDataMap.getOrPut(this.uniqueId) { mutableMapOf() }

    typeDataMap[ magicItemType] = magicItemData;
}