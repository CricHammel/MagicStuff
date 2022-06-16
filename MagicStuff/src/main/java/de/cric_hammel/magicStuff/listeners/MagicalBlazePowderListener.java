package de.cric_hammel.magicStuff.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import de.cric_hammel.magicStuff.Main;

public class MagicalBlazePowderListener implements Listener {
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER
				&& (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK
						|| event.getCause() == DamageCause.HOT_FLOOR || event.getCause() == DamageCause.LAVA)) {
			Player p = (Player) event.getEntity();
			if (Main.checkLore(p, Main.MAGICALBLAZEPOWDER_LORE, false)) {
				event.setCancelled(true);
//				p.playSound(p, Sound.ENTITY_CREEPER_HURT, 1f, 2f);
			}
		}
	}
}
