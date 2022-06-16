package de.cric_hammel.magicStuff.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.magicStuff.Main;

public class PickarangListener implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& Main.checkLore(event.getPlayer(), Main.PICKARANG_LORE, true)) {
			ItemStack pick = event.getPlayer().getInventory().getItemInMainHand();
			event.getPlayer().getInventory().setItemInMainHand(null);
			Entity a = Main.createPickarangEntity(event.getPlayer().getWorld(), event.getPlayer().getLocation(),
					event.getPlayer());
			new BukkitRunnable() {
				List<ItemStack> drops = new ArrayList<>();
				int distance = 0;

				@Override
				public void run() {
					a.teleport(a.getLocation().add(a.getLocation().getDirection()));
					Location aLoc = a.getLocation();
					aLoc.setY(a.getLocation().getY() + 1);
					a.getWorld().spawnParticle(Particle.END_ROD, aLoc, 1, 0, 0, 0, 0);
					distance++;
					if (distance >= 100) {
						a.remove();
						Damageable d = (Damageable) pick.getItemMeta();
						d.setDamage(d.getDamage() + 1);
						pick.setItemMeta(d);
						for (Map.Entry<Integer, ItemStack> entry : event.getPlayer().getInventory().addItem(pick)
								.entrySet()) {
							ItemStack val = entry.getValue();
							event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), val);
						}
						for (Map.Entry<Integer, ItemStack> entry : event.getPlayer().getInventory()
								.addItem((ItemStack[]) drops.toArray(new ItemStack[0])).entrySet()) {
							ItemStack val = entry.getValue();
							event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), val);
						}
						cancel();
						return;
					}
					Location loc = a.getLocation();
					loc.setY(loc.getY() + 1);
					Block block = loc.getBlock();
					if (!(block.getType() == Material.AIR)) {
						for (ItemStack b : block.getDrops(pick)) {
							drops.add(b);
						}
						if (!block.isPassable()) {
							Damageable d = (Damageable) pick.getItemMeta();
							d.setDamage(d.getDamage() + 1);
							pick.setItemMeta(d);
							for (Map.Entry<Integer, ItemStack> entry : event.getPlayer().getInventory().addItem(pick)
									.entrySet()) {
								ItemStack val = entry.getValue();
								event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), val);
							}
							for (Map.Entry<Integer, ItemStack> entry : event.getPlayer().getInventory()
									.addItem((ItemStack[]) drops.toArray(new ItemStack[0])).entrySet()) {
								ItemStack val = entry.getValue();
								event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), val);
							}
							block.setType(Material.AIR);
							a.remove();
							cancel();
							return;
						}
						block.setType(Material.AIR);
					}
				}
			}.runTaskTimer(Main.getPlugin(), 0, 2);

		}
	}

	@EventHandler
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		if (event.getEntityType() == EntityType.PLAYER && (int) (event.getItem().hasMetadata(Main.PICKARANG_META_KEY)
				? event.getItem().getMetadata(Main.PICKARANG_META_KEY).get(0).value()
				: 0) == 1) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerManipulateArmorStand(PlayerArmorStandManipulateEvent event) {
		if ((int) (event.getRightClicked().hasMetadata(Main.PICKARANG_META_KEY)
				? event.getRightClicked().getMetadata(Main.PICKARANG_META_KEY).get(0).value()
				: 0) == 1) {
			event.setCancelled(true);
		}
	}
}
