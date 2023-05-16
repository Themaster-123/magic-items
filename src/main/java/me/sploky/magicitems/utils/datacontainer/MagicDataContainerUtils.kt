package me.sploky.magicitems.utils.datacontainer

import me.sploky.magicitems.SplokysMagicItems
import me.sploky.magicitems.magicitemsbase.MagicItemType
import me.sploky.magicitems.magicitemsbase.items.MagicItem
import me.sploky.magicitems.namespace.MagicItemKeys
import me.sploky.magicitems.registries.MagicItemRegistry
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class MagicDataContainerUtils {
    companion object {
        @JvmStatic
        fun getMagicItemType(itemStack: ItemStack): MagicItemType? {
            val itemMeta = itemStack.itemMeta ?: return null
            return getMagicItemType(itemMeta.persistentDataContainer)
        }

        @JvmStatic
        fun getMagicItem(itemStack: ItemStack): MagicItem? {
            val itemType = getMagicItemType(itemStack) ?: return null
            return MagicItemRegistry.getItem(itemType)

        }

        @JvmStatic
        fun getMagicItemType(persistentDataContainer: PersistentDataContainer): MagicItemType? {
            val key = NamespacedKey(SplokysMagicItems.pluginInstance, MagicItemKeys.ItemType.key)
            if (!persistentDataContainer.has(key)) return null
            val nameData = persistentDataContainer.get(key, PersistentDataType.STRING) ?: return null

            return MagicItemType.itemTypeFromKeyName(nameData)
        }

        @JvmStatic
        fun getMagicItem(persistentDataContainer: PersistentDataContainer): MagicItem? {
            val itemType = getMagicItemType(persistentDataContainer) ?: return null
            return MagicItemRegistry.getItem(itemType)

        }

        @JvmStatic
        fun setMagicItemType(persistentDataContainer: PersistentDataContainer, itemType: MagicItemType) {
            val magicItemKey = NamespacedKey(SplokysMagicItems.pluginInstance, MagicItemKeys.ItemType.key)
            persistentDataContainer.set(magicItemKey, PersistentDataType.STRING, itemType.keyName)
        }
    }
}


