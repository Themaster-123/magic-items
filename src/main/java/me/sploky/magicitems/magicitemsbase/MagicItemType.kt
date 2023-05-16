package me.sploky.magicitems.magicitemsbase

import me.sploky.magicitems.magicitemsbase.items.MagicItem
import me.sploky.magicitems.registries.MagicItemRegistry

enum class MagicItemType(val typeName: String, val keyName: String) {
    BLOCK_WAND("BlockWand", "block_wand"),
    SWORD_OF_THE_SPOOK("Sword of the Spook", "Sword_of_the_Spook"),
    NUKE("Nuke", "Nuke"),
    BIGGER_NUKE("Bigger Nuke", "Bigger_Nuke"),
    BOUNCY_BOW("Bouncy Bow", "Bouncy_Bow"),
    TOTEM_OF_DYING("Totem Of Dying", "Totem_Of_Dying");

    companion object {
        @Throws(IllegalArgumentException::class) @JvmStatic
        fun itemTypeFromName(name: String): MagicItemType {
            // returns the MagicItemType from the name param
            return itemTypeFromParam(name) { it.typeName }
        }
        @Throws(IllegalArgumentException::class) @JvmStatic
        fun itemTypeFromKeyName(name: String): MagicItemType {
            // returns the MagicItemType from the key param
            return itemTypeFromParam(name) { it.keyName }
        }

        @Throws(IllegalArgumentException::class) @JvmStatic
        private fun itemTypeFromParam(name: String, getParamF: (MagicItemType) -> String): MagicItemType {
            // returns the MagicItemType from a param
            return values().find {
                name.equals(getParamF(it).replace(" ", ""), true)
            } ?: throw IllegalArgumentException("MagicItemType does not exist")
        }
    }

    fun getMagicItem(): MagicItem {
        return MagicItemRegistry.getItem(this)
    }

}