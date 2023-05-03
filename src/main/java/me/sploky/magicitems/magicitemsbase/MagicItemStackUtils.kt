package me.sploky.magicitems.magicitemsbase

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.magicitemsbase.items.MagicItem
import me.sploky.magicitems.namespace.MagicItemKeys
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class MagicItemStackUtils {
    companion object {
        @JvmStatic
        fun getMagicItemType(itemStack: ItemStack): MagicItemType? {
            val itemMeta = itemStack.itemMeta ?: return null;
            val key = NamespacedKey(SplokysMagicItems.pluginInstance, MagicItemKeys.ItemType.key)
            if (!itemMeta.persistentDataContainer.has(key)) return null;
            val nameData = itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING) ?: return null;

            return MagicItemType.itemTypeFromKeyName(nameData);
        }

        @JvmStatic
        fun getMagicItem(itemStack: ItemStack): MagicItem? {
            val itemType = getMagicItemType(itemStack) ?: return null;
            return MagicItemRegistry.getItem(itemType);

        }
    }
}


