package me.sploky.magicitems.magicitemsbase.items

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.namespace.MagicItemKeys
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

abstract class MagicItemStack() : MagicItem {

    override fun createItemStack(): ItemStack {
        val itemStack = ItemStack(material);
        val itemMeta = itemStack.itemMeta;
        itemMeta.displayName(name)
        itemMeta.lore(description);
        val magicItemKey = NamespacedKey(SplokysMagicItems.pluginInstance, MagicItemKeys.ItemType.key);
        // Sets the name of the MagicItemType in the PDC of the item
        itemMeta.persistentDataContainer.set(magicItemKey, PersistentDataType.STRING, itemType.keyName)
        itemStack.itemMeta = itemMeta;
        return itemStack;
    }
}