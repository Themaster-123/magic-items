package me.sploky.magicitems.registries

import me.sploky.magicitems.magicitemsbase.MagicItemType
import me.sploky.magicitems.magicitemsbase.items.Cleanable
import me.sploky.magicitems.magicitemsbase.items.MagicItem

class MagicItemRegistry {
    companion object {
        @JvmStatic
        private val items = mutableMapOf<MagicItemType, MagicItem>()

        @JvmStatic
        fun registerItem(magicItem: MagicItem) {
            items[magicItem.itemType] = magicItem

            if (magicItem is Cleanable) {
                CleanableRegistry.registerElement(magicItem)
            }
        }

        @JvmStatic @Throws(IllegalArgumentException::class)
        fun getItem(type: MagicItemType): MagicItem {
            return items[type] ?: throw IllegalArgumentException("Type does not exist")
        }
    }


}