package de.cric_hammel.magicStuff.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.cric_hammel.magicStuff.Main;

public class WOPListener implements Listener {

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		if (event.getHand() != EquipmentSlot.HAND || !Main.checkLore(p, Main.WOP_LORE, true)) {
			return;
		}
		Entity e = event.getRightClicked();
		if (!p.hasMetadata(Main.WOP_META_KEY_ISPOSSESSED) && !e.hasMetadata(Main.WOP_META_KEY_POSSESSES)) {
			if (!p.hasMetadata(Main.WOP_META_KEY_POSSESSES) && !e.hasMetadata(Main.WOP_META_KEY_ISPOSSESSED)) {
				p.setMetadata(Main.WOP_META_KEY_POSSESSES, new FixedMetadataValue(Main.getPlugin(), 1));
				e.setMetadata(Main.WOP_META_KEY_ISPOSSESSED, new FixedMetadataValue(Main.getPlugin(), 1));

				new BukkitRunnable() {

					@Override
					public void run() {
						if (!p.hasMetadata(Main.WOP_META_KEY_POSSESSES)
								&& !e.hasMetadata(Main.WOP_META_KEY_ISPOSSESSED)) {
							cancel();
						}

						if (e.isDead() || !p.isOnline()) {
							p.removeMetadata(Main.WOP_META_KEY_POSSESSES, Main.getPlugin());
							e.removeMetadata(Main.WOP_META_KEY_ISPOSSESSED, Main.getPlugin());
						}

						e.teleport(p.getLocation().clone().add(p.getLocation().getDirection().multiply(3.0))
								.setDirection(p.getLocation().getDirection().multiply(-1)));
					}
				}.runTaskTimer(Main.getPlugin(), 0, 1);

				for (Player player : Bukkit.getOnlinePlayers()) {
					player.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1f, 1.2f);
				}
				p.spawnParticle(Particle.PORTAL, e.getLocation(), 300, 0, 0, 0);
				event.setCancelled(true);
			} else if (p.hasMetadata(Main.WOP_META_KEY_POSSESSES) && e.hasMetadata(Main.WOP_META_KEY_ISPOSSESSED)) {
				p.removeMetadata(Main.WOP_META_KEY_POSSESSES, Main.getPlugin());
				e.removeMetadata(Main.WOP_META_KEY_ISPOSSESSED, Main.getPlugin());
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 1.2f);
				}
				p.spawnParticle(Particle.REVERSE_PORTAL, e.getLocation(), 300, 0, 0, 0);

				new BukkitRunnable() {

					@Override
					public void run() {
						Vector v = e.getLocation().toVector().add(p.getLocation().toVector().multiply(-1));
						v.setY(1.5);
						e.setVelocity(v);
					}
				}.runTaskLater(Main.getPlugin(), 1);
				event.setCancelled(true);
			}
		}
	}
}
