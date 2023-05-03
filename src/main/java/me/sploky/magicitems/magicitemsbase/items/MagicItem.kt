package me.sploky.magicitems.magicitemsbase.items

import me.sploky.magicitems.magicitemsbase.MagicItemType
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface MagicItem {
    val name: TextComponent;
    val description: List<TextComponent>;
    val material: Material;
    val itemType: MagicItemType

    fun  createItemStack(): ItemStack;
}