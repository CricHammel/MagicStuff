package de.cric_hammel.magicStuff.utilities;

import org.bukkit.potion.PotionEffectType;

public class PotionEffectWrapper {
	private PotionEffectType type;
	private int amplifier;
	private int duration;

	public PotionEffectWrapper(PotionEffectType type, int amplifier, int duration) {
		this.type = type;
		this.amplifier = amplifier;
		this.duration = duration;
	}

	public PotionEffectType getType() {
		return type;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public int getDuration() {
		return duration;
	}
}