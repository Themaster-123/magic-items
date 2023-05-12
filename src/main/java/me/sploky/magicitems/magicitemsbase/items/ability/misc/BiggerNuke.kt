package me.sploky.magicitems.magicitemsbase.items.ability.misc

import me.sploky.magicitems.magicitemsbase.MagicItemType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*

open class BiggerNuke: Nuke() {
    override val name: TextComponent
        get() = Component.text("Bigger Nuke", NamedTextColor.RED, TextDecoration.BOLD)
    override val description: List<TextComponent>
        get() = listOf(Component.text("WARNING!!! ", NamedTextColor.DARK_RED, TextDecoration.BOLD).append(
            Component.text("Will Crash The Server!", NamedTextColor.GOLD)
        ))
    override val itemType: MagicItemType
        get() = MagicItemType.BIGGER_NUKE
    override val explosionPower: Float
        get() = 150f
    override val explosionSoundPeriod: Long
        get() = 2


    override fun playExplosionSound(location: Location) {
        playExplosionSound(location, 50f)
    }

}