package de.cric_hammel.magicStuff.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.magicStuff.Main;

public class WolfSwordListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR
				&& Main.checkLore(event.getPlayer(), Main.WOLFSWORD_LORE, true)) {

			new BukkitRunnable() {
				ItemStack sword = event.getPlayer().getInventory().getItemInMainHand();
				int damage = ((Damageable) sword.getItemMeta()).getDamage();
				World w = event.getPlayer().getWorld();

				@Override
				public void run() {
					Location loc = event.getPlayer().getLocation();
					Wolf wolf = (Wolf) w.spawnEntity(loc, EntityType.WOLF);
					wolf.setTamed(true);
					wolf.setOwner(event.getPlayer());
					new BukkitRunnable() {
						@Override
						public void run() {
							if (!wolf.isDead()) {
								wolf.remove();
							}
						}
					}.runTaskLater(Main.getPlugin(), 20 * 30);
					damage++;
					if (damage >= 249) {
						ItemMeta swordMeta = sword.getItemMeta();
						Damageable d = (Damageable) swordMeta;
						d.setDamage(249);
						sword.setItemMeta(d);
						cancel();
					}
				}
			}.runTaskTimer(Main.getPlugin(), 0, 10);
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1f, 0.8f);
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity().getKiller() != null) {
			Player p = event.getEntity().getKiller();
			if (Main.checkLore(p, Main.WOLFSWORD_LORE, true)) {
				ItemStack sword = p.getInventory().getItemInMainHand();
				ItemMeta swordMeta = sword.getItemMeta();
				Damageable d = (Damageable) swordMeta;
				d.setDamage(d.getDamage() > 0 ? d.getDamage() - 1 : 0);
				sword.setItemMeta(d);
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.2f);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDamageItem(PlayerItemDamageEvent event) {
		if (Main.checkLore(event.getPlayer(), Main.WOLFSWORD_LORE, true)) {
			event.setCancelled(true);
		}
	}
}
