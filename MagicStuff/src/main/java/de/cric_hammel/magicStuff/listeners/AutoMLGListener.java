package de.cric_hammel.magicStuff.listeners;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.magicStuff.Main;

public class AutoMLGListener implements Listener {
	@EventHandler
	public void onPlayerFillBucket(PlayerBucketFillEvent event) {
		if (Main.checkLore(event.getPlayer(), Main.AUTOMLG_LORE, true)) {
			if (event.getItemStack().getType() == Material.WATER_BUCKET) {
				ItemStack bucket = Main.createAutoMLGItem();
				bucket.setType(Material.WATER_BUCKET);
				event.setItemStack(bucket);
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerEmptyBucket(PlayerBucketEmptyEvent event) {
		if (Main.checkLore(event.getPlayer(), Main.AUTOMLG_LORE, true)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			Player p = (Player) event.getEntity();
			if (event.getCause() == DamageCause.FALL && Main.checkLore(p, Main.AUTOMLG_LORE, false)) {
				ItemStack bucketFull = Main.createAutoMLGItem();
				bucketFull.setType(Material.WATER_BUCKET);
				Map<Integer, ? extends ItemStack> map = p.getInventory().all(bucketFull);
				boolean hasFullBucket = false;
				for (Entry<Integer, ? extends ItemStack> entry : map.entrySet()) {
					ItemStack val = entry.getValue();
					if (val.equals(bucketFull)) {
						val.setType(Material.BUCKET);
						hasFullBucket = true;
					}
				}
				if (hasFullBucket) {
					Block b = p.getLocation().getBlock();
					b.setType(Material.WATER);
					World w = p.getWorld();
					w.playSound(p.getLocation(), Sound.ITEM_BUCKET_EMPTY, 1, 1);
					event.setCancelled(true);
					new BukkitRunnable() {

						@Override
						public void run() {
							b.setType(Material.AIR);
						}
					}.runTaskLater(Main.getPlugin(), 5);
				}
			}
		}
	}
}
