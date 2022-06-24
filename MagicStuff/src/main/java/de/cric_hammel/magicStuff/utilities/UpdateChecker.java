package de.cric_hammel.magicStuff.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.plugin.java.JavaPlugin;

public class UpdateChecker {

	private final JavaPlugin plugin;
	private final int resourceId;

	public UpdateChecker(JavaPlugin plugin, int resourceId) {
		this.plugin = plugin;
		this.resourceId = resourceId;
	}

	public String getVersion() {
		String urlString = "https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId;
		try {
			URLConnection connection = new URL(urlString).openConnection();
			connection.setUseCaches(false);
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String version = reader.readLine();
			return version;
		} catch (IOException exception) {
			plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
		}
		return null;
	}
}
