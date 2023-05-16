package me.sploky.magicitems.magicitemsbase.items

import me.sploky.magicitems.utils.datacontainer.MagicDataContainerUtils
import org.bukkit.inventory.ItemStack

abstract class MagicItemStack() : MagicItem {

    override fun createItemStack(): ItemStack {
        val itemStack = ItemStack(material)
        val itemMeta = itemStack.itemMeta
        itemMeta.displayName(name)
        itemMeta.lore(description)

        MagicDataContainerUtils.setMagicItemType(itemMeta.persistentDataContainer, itemType)
        itemStack.itemMeta = itemMeta
        return itemStack
    }
}