package de.cric_hammel.magicStuff.utilities;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

public enum FoodItem {

	APPLE(Material.APPLE, 4, 2.4f, null), BAKED_POTATO(Material.BAKED_POTATO, 5, 6f, null),
	BEETROOT(Material.BEETROOT, 1, 1.2f, null), BEETROOT_SOUP(Material.BEETROOT_SOUP, 6, 7.2f, null),
	BREAD(Material.BREAD, 5, 6f, null), CAKE(Material.CAKE, 14, 2.8f, null), CARROT(Material.CARROT, 3, 3.6f, null),
	CHORUS_FRUIT(Material.CHORUS_FRUIT, 4, 2.4f, null), COOKED_CHICKEN(Material.COOKED_CHICKEN, 6, 7.2f, null),
	COOKED_COD(Material.COOKED_COD, 5, 6, null), COOKED_MUTTON(Material.COOKED_MUTTON, 6, 9.5f, null),
	COOKED_PORKCHOP(Material.COOKED_PORKCHOP, 8, 12.8f, null), COOKED_RABBIT(Material.COOKED_RABBIT, 5, 6f, null),
	COOKED_SALMON(Material.COOKED_SALMON, 6, 9.5f, null), COOKIE(Material.COOKIE, 2, 0.4f, null),
	DRIED_KELP(Material.DRIED_KELP, 1, 0.6f, null),
	ENCHANTED_GOLDEN_APPLE(Material.ENCHANTED_GOLDEN_APPLE, 4, 9.6f,
			new PotionEffectWrapper[] { new PotionEffectWrapper(PotionEffectType.REGENERATION, 2, 20),
					new PotionEffectWrapper(PotionEffectType.ABSORPTION, 4, 120),
					new PotionEffectWrapper(PotionEffectType.DAMAGE_RESISTANCE, 1, 300),
					new PotionEffectWrapper(PotionEffectType.FIRE_RESISTANCE, 1, 300) }),
	GOLDEN_APPLE(Material.GOLDEN_APPLE, 4, 9.6f,
			new PotionEffectWrapper[] { new PotionEffectWrapper(PotionEffectType.REGENERATION, 2, 5),
					new PotionEffectWrapper(PotionEffectType.ABSORPTION, 1, 120) }),
	GLOW_BERRIES(Material.GLOW_BERRIES, 2, 0.4f, null), GOLDEN_CARROT(Material.GOLDEN_CARROT, 6, 14.4f, null),
	HONEY_BOTTLE(Material.HONEY_BOTTLE, 6, 1.2f,
			new PotionEffectWrapper[] { new PotionEffectWrapper(PotionEffectType.POISON, -1, 0) }),
	MELON_SLICE(Material.MELON_SLICE, 2, 1.2f, null), MUSHROOM_STEW(Material.MUSHROOM_STEW, 6, 7.2f, null),
	POISONOUS_POTATO(Material.POISONOUS_POTATO, 2, 1.2f,
			new PotionEffectWrapper[] { new PotionEffectWrapper(PotionEffectType.POISON, 1, 5) }),
	POTATO(Material.POTATO, 1, 0.6f, null),
	PUFFERFISH(Material.PUFFERFISH, 1, 0.2f,
			new PotionEffectWrapper[] { new PotionEffectWrapper(PotionEffectType.HUNGER, 3, 15),
					new PotionEffectWrapper(PotionEffectType.CONFUSION, 1, 15),
					new PotionEffectWrapper(PotionEffectType.POISON, 2, 60) }),
	PUMPKIN_PIE(Material.PUMPKIN_PIE, 8, 4.8f, null), RABBIT_STEW(Material.RABBIT_STEW, 10, 12f, null),
	RAW_BEEF(Material.BEEF, 3, 1.8f, null),
	RAW_CHICKEN(Material.CHICKEN, 2, 1.2f,
			new PotionEffectWrapper[] { new PotionEffectWrapper(PotionEffectType.HUNGER, 1, 30) }),
	RAW_COD(Material.COD, 2, 0.4f, null), RAW_MUTTON(Material.MUTTON, 2, 1.2f, null),
	RAW_RABBIT(Material.RABBIT, 3, 1.8f, null), RAW_SALMON(Material.SALMON, 2, 0.4f, null),
	ROTTEN_FLESH(Material.ROTTEN_FLESH, 4, 0.8f,
			new PotionEffectWrapper[] { new PotionEffectWrapper(PotionEffectType.HUNGER, 1, 30) }),
	SPIDER_EYE(Material.SPIDER_EYE, 2, 1.2f,
			new PotionEffectWrapper[] { new PotionEffectWrapper(PotionEffectType.POISON, 1, 5) }),
	STEAK(Material.COOKED_BEEF, 8, 12.8f, null), SUSPICIOUS_STEW(Material.SUSPICIOUS_STEW, 13, 21.2f, null),
	SWEET_BERRIES(Material.SWEET_BERRIES, 2, 0.4f, null), TROPICAL_FISH(Material.TROPICAL_FISH, 1, 0.2f, null);

	private final Material equiv;
	private final int foodLevel;
	private final float saturation;
	private final PotionEffectWrapper[] effects;

	FoodItem(Material equiv, int foodLevel, float saturation, PotionEffectWrapper[] effects) {
		this.equiv = equiv;
		this.foodLevel = foodLevel;
		this.saturation = saturation;
		this.effects = effects;

	}

	public Material getEquiv() {
		return equiv;
	}

	public int getFoodLevel() {
		return foodLevel;
	}

	public float getSaturation() {
		return saturation;
	}

	public PotionEffectWrapper[] getEffects() {
		return effects;
	}

	public static FoodItem getFromEquiv(Material equiv) {
		for (FoodItem f : FoodItem.values()) {
			if (f.getEquiv() == equiv) {
				return f;
			}
		}
		return null;
	}
}
