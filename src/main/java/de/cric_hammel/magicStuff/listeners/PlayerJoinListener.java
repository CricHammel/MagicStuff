package de.cric_hammel.magicStuff.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.cric_hammel.magicStuff.Main;

public class PlayerJoinListener implements Listener{

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().discoverRecipes(Main.recipes);
	}
}
