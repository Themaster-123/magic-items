package me.sploky.magicitems.magicitemsbase

enum class MagicItemType(val typeName: String, val keyName: String) {
    BLOCK_WAND("BlockWand", "block_wand"),
    SWORD_OF_THE_SPOOK("Sword of the Spook", "Sword_of_the_Spook");

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
                name.equals(getParamF(it).replace(" ", ""), true);
            } ?: throw IllegalArgumentException("MagicItemType does not exist");
        }
    }

}