package me.sploky.magicitems.player

import me.sploky.magicitems.magicitemsbase.MagicItemType
import me.sploky.magicitems.magicitemsbase.data.MagicItemData
import org.bukkit.entity.Player
import java.util.UUID

class MagicPlayerData {
    val mutableMap = mutableMapOf<UUID, MutableMap<MagicItemType, MagicItemData>>();


}

