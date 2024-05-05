package de.cric_hammel.magicStuff.listeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.magicStuff.Main;

public class ScaffolderListener implements Listener {

	private static final String METADATA_KEY = "magicStuff_scaffolder";
	private static final String METADATA_COLOR_KEY = "magicStuff_scaffolder_color";

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();

		if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY()
				&& to.getBlockZ() == from.getBlockZ()) {
			return;
		}

		Player player = event.getPlayer();

		int color = 0;
		if (player.hasMetadata(METADATA_COLOR_KEY)) {
			List<MetadataValue> colorMetaData = player.getMetadata(METADATA_COLOR_KEY);
			color = (int) colorMetaData.get(0).value();
		}

		if (Main.checkLore(event.getPlayer(), Main.SCAFFOLDER_LORE, true)) {

			Material colorType = Main.MATERIALS[color++];
			if (color >= Main.MATERIALS.length) {
				color = 0;
			}
			player.setMetadata(METADATA_COLOR_KEY, new FixedMetadataValue(Main.getPlugin(), color));

			Block underPlayer;
			if (event.getPlayer().isSneaking()) {
				underPlayer = to.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
			} else {
				underPlayer = to.getBlock().getRelative(BlockFace.DOWN);
			}

//			Material previousType = underPlayer.getType();
			final BlockState state;
			if (!underPlayer.hasMetadata(METADATA_KEY)) {
				state = underPlayer.getState();
				underPlayer.setMetadata(METADATA_KEY, new FixedMetadataValue(Main.getPlugin(), state));
			} else {
				state = (BlockState) underPlayer.getMetadata(METADATA_KEY).get(0).value();
			}

			underPlayer.setType(colorType);
			new BukkitRunnable() {
				@Override
				public void run() {
					if (underPlayer.hasMetadata(METADATA_KEY)) {
						state.update(true);
						underPlayer.removeMetadata(METADATA_KEY, Main.getPlugin());
					}
				}
			}.runTaskLater(Main.getPlugin(), 20 * 2);
		}
	}
}