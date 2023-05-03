package me.sploky.magicitems.magicitemsbase.items

import me.sploky.magicitems.SplokysMagicItems
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

abstract class LoreUpdatableItemStack: MagicItemStack(), LoreUpdatable {
    override fun updateLore(itemStack: ItemStack, loreIndex: Int, component: Component) {
        val itemMeta = itemStack.itemMeta
        val lore = itemMeta.lore() ?: return
        if (lore.size <= loreIndex) {
            for (i in lore.size..loreIndex) {
                SplokysMagicItems.pluginInstance.logger.info(i.toString())
                lore.add(Component.text(""))
            }
        }
        lore[loreIndex] = component
        itemMeta.lore(lore)
        itemStack.itemMeta = itemMeta
    }
}