package de.cric_hammel.magicStuff.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import de.cric_hammel.magicStuff.Main;

public class MagicStuffCommand implements TabExecutor {

	public boolean onCommand(CommandSender sender, Command c, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				colorMessage(player, "&d-----<&5MagicStuff&dv&5" + Main.getPlugin().getDescription().getVersion() + "&d>-----");
				colorMessage(player, "&dUse &5/magicstuff help &dto see all available commands and items!");
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					Main.getPlugin().reloadConfig();
					colorMessage(player, Main.getMessageFromConfig("messages.config-reload"));
				} else if (args[0].equalsIgnoreCase("reset")) {
					File configFile = new File(Main.getPlugin().getDataFolder(), "config.yml");
					configFile.delete();
					Main.getPlugin().saveDefaultConfig();
					Main.getPlugin().reloadConfig();
					colorMessage(player, Main.getMessageFromConfig("messages.config-reset"));
				} else if (args[0].equalsIgnoreCase("help")) {
					colorMessage(player, "&dAvailable commands&c:");
					colorMessage(player, "&5/magicstuff help items&c: &9Shows a list of all items with a short description");
					colorMessage(player, "&5/magicstuff help commands&c: &9Shows this message");
					colorMessage(player, "&5/magicstuff reload&c: &9Reloads the config from the config file");
					colorMessage(player, "&5/magicstuff reset&c: &9Reset the config overwriting it with a default one");
					colorMessage(player, "");
					colorMessage(player, "&dAvailable items with their /give-commands&c:");
					for (Command command : PluginCommandYamlParser.parse(Main.getPlugin())) {
						if (!command.getName().equals("magicstuff")) {
							colorMessage(player, "&5/" + command.getName() + "&c: &9" + command.getDescription());
						}
					}
				}
			} else
				return false;
		} else
			sender.sendMessage(Main.getMessageFromConfig("messages.not-sent-by-player"));
		return true;
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
	
	
	public void colorMessage(Player p, String message) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
}
