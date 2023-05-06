package me.sploky.magicitems.commands

import me.sploky.magicitems.registries.MagicItemRegistry
import me.sploky.magicitems.magicitemsbase.MagicItemType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.IllegalArgumentException

class CommandMagicGive: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            if (args == null || args.isEmpty()) {
                sender.sendMessage(Component.text(command.usage, NamedTextColor.RED))
                return true
            }

            // Gets the item from the args.
            val itemArg = args[0]
            val itemType: MagicItemType
            try {
                itemType = MagicItemType.itemTypeFromName(itemArg.uppercase())
            } catch (_:IllegalArgumentException) {
                sender.sendMessage(Component.text("Invalid Type", NamedTextColor.RED))
                return true
            }
            val item = MagicItemRegistry.getItem(itemType)

            // Gives the Player the Item.
            sender.inventory.addItem(item.createItemStack())
            // Sends Player confirmation text.
            sender.sendMessage(MiniMessage.miniMessage().
            deserialize("<green>Gave</green> <name> <green>to ${sender.name}</green>)", Placeholder.component("name", item.name)))
            return true
        }

        // Sender isn't Player class.
        sender.sendMessage(Component.text("Sender must be player", NamedTextColor.RED))
        return true
    }
}