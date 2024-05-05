package de.cric_hammel.magicStuff.listeners;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import de.cric_hammel.magicStuff.Main;
import de.cric_hammel.magicStuff.utilities.FoodItem;
import de.cric_hammel.magicStuff.utilities.PotionEffectWrapper;
import net.md_5.bungee.api.ChatColor;

public class AutoFeederListener implements Listener {

	private static final String INVENTORY_TITLE = ChatColor.translateAlternateColorCodes('&', "&4&k&l_&4&lAuto-feeder&4&k&l_");

	Map<String, String> inventories = Main.getPlugin().getAfInventories();

	@EventHandler
	public void onPlayerIneract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& Main.checkLore(event.getPlayer(), Main.AUTOFEEDER_LORE, true)) {
			event.getPlayer().openInventory(getInventory(event.getPlayer()));
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getView().getTitle().equals(INVENTORY_TITLE)) {
			saveInventory((Player) event.getPlayer(), event.getInventory());
		}
	}

	@EventHandler
	public void onInventoryEvent(InventoryClickEvent event) {
		if (event.getView().getTitle().equals(INVENTORY_TITLE)) {
			if (event.getCurrentItem() != null && !(event.getCurrentItem().getType().isEdible())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if ((event.getEntityType() == EntityType.PLAYER
				? Main.checkLore((Player) event.getEntity(), Main.AUTOFEEDER_LORE, false)
				: false) && event.getFoodLevel() < 20 && event.getItem() == null) {
			Player p = (Player) event.getEntity();
			ItemStack food = getFirstFood(getInventory(p), p);
			if (food == null) {
				return;
			}
			FoodItem fi = FoodItem.getFromEquiv(food.getType());
			if (21 - p.getFoodLevel() < fi.getFoodLevel()) {
				return;
			}
			new BukkitRunnable() {

				@Override
				public void run() {
					if (p.getFoodLevel() + fi.getFoodLevel() >= 20)
						p.setFoodLevel(20);
					else {
						p.setFoodLevel(p.getFoodLevel() + fi.getFoodLevel());
						FoodLevelChangeEvent e = new FoodLevelChangeEvent(p, p.getFoodLevel());
						Bukkit.getPluginManager().callEvent(e);
					}
					if (p.getSaturation() + fi.getSaturation() >= 20)
						p.setSaturation(20);
					else
						p.setSaturation(p.getSaturation() + fi.getSaturation());
					if (fi.getEffects() != null) {
						for (PotionEffectWrapper effect : fi.getEffects()) {
							if (effect.getAmplifier() == -1) {
								p.removePotionEffect(effect.getType());
							} else {
								p.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration() * 20,
										effect.getAmplifier() - 1));
							}
						}
					}
					p.playSound(p, Sound.ENTITY_PLAYER_BURP, 1, 1);
				}
			}.runTaskLater(Main.getPlugin(), 3);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (Main.checkLore(event.getPlayer(), Main.AUTOFEEDER_LORE, true)) {
			event.setCancelled(true);
		}
	}

	public Inventory getInventory(Player p) {
		String uuid = p.getUniqueId().toString();
		if (inventories.containsKey(uuid)) {
			try {
				String base64 = inventories.get(uuid);
				return fromBase64(base64);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Bukkit.createInventory(null, 9 * 3, INVENTORY_TITLE);
	}

	public void saveInventory(Player p, Inventory inv) {
		try {
			String uuid = p.getUniqueId().toString();
			String base64 = toBase64(inv);
			inventories.put(uuid, base64);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public String toBase64(Inventory inventory) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			// Write the size of the inventory
			dataOutput.writeInt(inventory.getSize());

			// Save every element in the list
			for (int i = 0; i < inventory.getSize(); i++) {
				dataOutput.writeObject(inventory.getItem(i));
			}

			// Serialize that array
			dataOutput.close();
			return new String(Base64.getEncoder().encode(outputStream.toByteArray()));
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public Inventory fromBase64(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt(), INVENTORY_TITLE);

			// Read the serialized inventory
			for (int i = 0; i < inventory.getSize(); i++) {
				inventory.setItem(i, (ItemStack) dataInput.readObject());
			}

			dataInput.close();
			return inventory;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	public ItemStack getFirstFood(Inventory inv, Player p) {
		for (ItemStack itemStack : inv.getStorageContents()) {
			if (itemStack != null ? itemStack.getType().isEdible() : false) {
				if (itemStack.getAmount() <= 1) {
					inv.remove(itemStack);
				} else {
					itemStack.setAmount(itemStack.getAmount() - 1);
				}
				saveInventory(p, inv);
				return itemStack.clone();
			}
		}
		return null;
	}
}
