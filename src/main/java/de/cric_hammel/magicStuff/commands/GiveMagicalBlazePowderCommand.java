package de.cric_hammel.magicStuff.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.magicStuff.Main;

public class GiveMagicalBlazePowderCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command c, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				ItemStack powder = Main.createMagicalBlazePowderItem();
				player.getInventory().addItem(powder);
			} else
				return false;
		} else
			sender.sendMessage(Main.getMessageFromConfig("messages.not-sent-by-player"));
		return true;
	}
}
