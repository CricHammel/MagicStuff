package de.cric_hammel.magicStuff.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import de.cric_hammel.magicStuff.Main;

public class MagicStuffCommand implements TabExecutor {

	public boolean onCommand(CommandSender sender, Command c, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("magicStuff.magicstuff")) {
				if (args.length == 0) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&d-----<&5MagicStuff&dv&5" + Main.getPlugin().getDescription().getVersion() + "&d>-----"));
					player.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&dUse &5/magicstuff help &dto see all available commands!"));
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {
						Main.getPlugin().reloadConfig();
						player.sendMessage(Main.getMessageFromConfig("messages.config-reload", c.getName()));
					} else if (args[0].equalsIgnoreCase("reset")) {
						File configFile = new File(Main.getPlugin().getDataFolder(), "config.yml");
						configFile.delete();
						Main.getPlugin().saveDefaultConfig();
						Main.getPlugin().reloadConfig();
						player.sendMessage(Main.getMessageFromConfig("messages.config-reset", c.getName()));
					} else if (args[0].equalsIgnoreCase("help")) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dAvailable options&c:"));
						player.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&5/magicstuff help&c: &5 Shows this message"));
						player.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&5/magicstuff reload&c: &5 Reloads the config from the config file"));
						player.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&5/magicstuff reset&c: &5 Reset the config overwriting it with a default one"));
					}
				} else
					player.sendMessage(Main.getMessageFromConfig("messages.wrong-arguments",
							c.getName() + " <help|reload|reset>"));
			} else
				player.sendMessage(Main.getMessageFromConfig("messages.no-permission", c.getName()));
		} else
			sender.sendMessage(Main.getMessageFromConfig("messages.not-sent-by-player", c.getName()));
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command c, String label, String[] args) {
		List<String> completions = new ArrayList<>();

		if (args.length == 1) {
			completions.add("help");
			completions.add("reload");
			completions.add("reset");
		}
		return completions;
	}
}
