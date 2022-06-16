package de.cric_hammel.magicStuff.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.magicStuff.Main;

public class GiveScaffolderCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command c, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("magicStuff.givescaffolder")) {
				if (args.length == 0) {
					ItemStack scaffolder = Main.createScaffolderItem();
					player.getInventory().addItem(scaffolder);
				} else
					player.sendMessage(Main.getMessageFromConfig("messages.wrong-arguments", c.getName()));
			} else
				player.sendMessage(Main.getMessageFromConfig("messages.no-permission", c.getName()));
		} else
			sender.sendMessage(Main.getMessageFromConfig("messages.not-sent-by-player", c.getName()));
		return false;
	}
}