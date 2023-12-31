package me.sploky.magicitems

import me.sploky.magicitems.commands.CommandMagicGive
import me.sploky.magicitems.listeners.AbilityListener
import me.sploky.magicitems.listeners.BowAbilityListener
import me.sploky.magicitems.listeners.MeleeAbilityListener
import me.sploky.magicitems.magicitemsbase.items.ability.misc.BiggerNuke
import me.sploky.magicitems.magicitemsbase.items.ability.misc.Nuke
import me.sploky.magicitems.magicitemsbase.items.ability.misc.TotemOfDying
import me.sploky.magicitems.registries.MagicItemRegistry
import me.sploky.magicitems.magicitemsbase.items.ability.wands.BlockWand
import me.sploky.magicitems.magicitemsbase.items.ability.weapons.BouncyBow
import me.sploky.magicitems.magicitemsbase.items.ability.weapons.SwordOfTheSpook
import me.sploky.magicitems.player.MagicPlayerData
import me.sploky.magicitems.registries.CleanableRegistry
import me.sploky.magicitems.watchers.HeldItemWatcher
import me.sploky.magicitems.registries.WatcherRegistry
import org.bukkit.plugin.java.JavaPlugin


class SplokysMagicItems : JavaPlugin() {
    companion object {
        @JvmStatic
        lateinit var pluginInstance: SplokysMagicItems
    }

    val magicPlayerData = MagicPlayerData()

    override fun onEnable() {
        // Plugin startup logic
        pluginInstance = this
        logger.info("Magic Items Enabled.")

        registerCommands()
        registerItems()
        registerEvents()
        registerWatchers()
    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("Magic Items Disabled.")
        CleanableRegistry.cleanElements()
    }

    private fun registerCommands() {
        this.getCommand("magicgive")!!.setExecutor(CommandMagicGive())
    }

    private fun registerItems() {
        MagicItemRegistry.registerItem(BlockWand())
        MagicItemRegistry.registerItem(SwordOfTheSpook())
        MagicItemRegistry.registerItem(Nuke())
        MagicItemRegistry.registerItem(BiggerNuke())
        MagicItemRegistry.registerItem(BouncyBow())
        MagicItemRegistry.registerItem(TotemOfDying())
    }

    private fun registerEvents() {
        val pluginManager = server.pluginManager

        pluginManager.registerEvents(AbilityListener(), this)
        pluginManager.registerEvents(MeleeAbilityListener(), this)
        pluginManager.registerEvents(BowAbilityListener(), this)
    }

    private fun registerWatchers() {
        WatcherRegistry.registerWatcher(HeldItemWatcher())
    }
}