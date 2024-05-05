package de.cric_hammel.magicStuff.listeners;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cric_hammel.magicStuff.Main;

public class TeleporterListener implements Listener {

	@EventHandler
	public void onPlayerUseTeleporter(PlayerInteractEvent event) {
		if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		Player player = event.getPlayer();
		if (Main.checkLore(player, Main.TELEPORTER_LORE, true)) {
			event.setCancelled(true);
			teleport(player);
		}
	}

	private void teleport(Player player) {
		World currentWorld = player.getWorld();
		Location currentLocation = player.getLocation();
		Entity armorStand1 = currentWorld.spawnEntity(currentLocation, EntityType.ARMOR_STAND);
		ArmorStand armorStand = (ArmorStand) armorStand1;
		armorStand.setInvisible(true);
		float yaw = player.getLocation().getYaw();
		float pitch = player.getLocation().getPitch();
		armorStand.setGravity(false);
		armorStand.setInvulnerable(true);
		armorStand.setRotation(yaw, pitch);

		boolean inBlock = false;
		int breakCounter = 0;
		while (!inBlock) {
			breakCounter++;
			if (breakCounter >= 1000) {
				armorStand.remove();
				break;
			}
			armorStand.teleport(armorStand.getLocation().add(armorStand.getLocation().getDirection()));
			if (!armorStand.getLocation().getBlock().isPassable()) {
				inBlock = true;
				armorStand.teleport(armorStand.getLocation().add(armorStand.getLocation().getDirection().multiply(-1)));
				player.teleport(armorStand);
				player.setFallDistance(0);
				player.getLocation().getDirection().zero();
				player.playEffect(EntityEffect.TELEPORT_ENDER);
				player.playSound(currentLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
				armorStand.remove();
			}
		}
	}

}
