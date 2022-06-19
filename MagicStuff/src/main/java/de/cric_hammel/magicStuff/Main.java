package de.cric_hammel.magicStuff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.cric_hammel.magicStuff.commands.GiveAutoFeederCommand;
import de.cric_hammel.magicStuff.commands.GiveAutoMLGCommand;
import de.cric_hammel.magicStuff.commands.GiveMagicalBlazePowderCommand;
import de.cric_hammel.magicStuff.commands.GivePickarangCommand;
import de.cric_hammel.magicStuff.commands.GiveScaffolderCommand;
import de.cric_hammel.magicStuff.commands.GiveTeleporterCommand;
import de.cric_hammel.magicStuff.commands.GiveWOPCommand;
import de.cric_hammel.magicStuff.commands.GiveWolfSwordCommand;
import de.cric_hammel.magicStuff.commands.MagicStuffCommand;
import de.cric_hammel.magicStuff.listeners.AutoFeederListener;
import de.cric_hammel.magicStuff.listeners.AutoMLGListener;
import de.cric_hammel.magicStuff.listeners.MagicalBlazePowderListener;
import de.cric_hammel.magicStuff.listeners.PickarangListener;
import de.cric_hammel.magicStuff.listeners.PlayerJoinListener;
import de.cric_hammel.magicStuff.listeners.ScaffolderListener;
import de.cric_hammel.magicStuff.listeners.TeleporterListener;
import de.cric_hammel.magicStuff.listeners.WOPListener;
import de.cric_hammel.magicStuff.listeners.WolfSwordListener;


/**
 * @author Cric_Hammel
 * 
 * Some stuff with magic
 */
public class Main extends JavaPlugin {

	private static Main plugin;

	private File afConfigFile;
	private FileConfiguration afConfig;
	
	public static final List<NamespacedKey> recipes = new ArrayList<>();

	public static final String SCAFFOLDER_LORE = "Lets you scaffold!";

	public static final String TELEPORTER_LORE = "Lets you teleport around!";

	public static final String AUTOMLG_LORE = "Keep this in your inventory to survive any jump!";

	public static final String WOLFSWORD_LORE = "Collect souls to create your army of wolves!";

	public static final String MAGICALBLAZEPOWDER_LORE = "Keep this in your inventory to gain immunity to fire!";

	public static final String PICKARANG_LORE = "Lets you break blocks from a distance!";
	public static final String PICKARANG_META_KEY = "magicstuff_pickarang";

	public static final String WOP_LORE = "Right-click an entity to possess it!";
	public static final String WOP_META_KEY_ISPOSSESSED = "magicstuff_isPossessed";
	public static final String WOP_META_KEY_POSSESSES = "magicstuff_possesses";

	public static final String AUTOFEEDER_LORE = "Put items in it to be feeded automatically!";
	public static final String AUTOFEEDER_CONFIG_KEY = "inventories";
	private Map<String, String> afInventories = new HashMap<>();

	public static final Material[] MATERIALS = { Material.RED_STAINED_GLASS, Material.ORANGE_STAINED_GLASS,
			Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.GREEN_STAINED_GLASS,
			Material.LIGHT_BLUE_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.BLUE_STAINED_GLASS,
			Material.MAGENTA_STAINED_GLASS, Material.PINK_STAINED_GLASS };

	public void onEnable() {
		plugin = this;

		plugin.saveDefaultConfig();

		afConfigFile = new File(getDataFolder(), "auto_feeder.yml");
		if (!afConfigFile.exists()) {
			saveResource("auto_feeder.yml", false);
		}
		afConfig = YamlConfiguration.loadConfiguration(afConfigFile);
		if (afConfig.contains(AUTOFEEDER_CONFIG_KEY)) {
			for (String uuid : afConfig.getConfigurationSection(AUTOFEEDER_CONFIG_KEY).getKeys(false)) {
				afInventories.put(uuid, afConfig.getConfigurationSection(AUTOFEEDER_CONFIG_KEY).getString(uuid));
			}
		}

		// Commands
		getCommand("givescaffolder").setExecutor(new GiveScaffolderCommand());
		getCommand("giveteleporter").setExecutor(new GiveTeleporterCommand());
		getCommand("giveautomlg").setExecutor(new GiveAutoMLGCommand());
		getCommand("givewolfsword").setExecutor(new GiveWolfSwordCommand());
		getCommand("givemagicalblazepowder").setExecutor(new GiveMagicalBlazePowderCommand());
		getCommand("givepickarang").setExecutor(new GivePickarangCommand());
		getCommand("givewop").setExecutor(new GiveWOPCommand());
		getCommand("magicstuff").setExecutor(new MagicStuffCommand());
		getCommand("magicstuff").setTabCompleter(new MagicStuffCommand());
		getCommand("giveautofeeder").setExecutor(new GiveAutoFeederCommand());

		// Events
		PluginManager p = Bukkit.getPluginManager();
		p.registerEvents(new PlayerJoinListener(), plugin);
		p.registerEvents(new ScaffolderListener(), plugin);
		p.registerEvents(new TeleporterListener(), plugin);
		p.registerEvents(new AutoMLGListener(), plugin);
		p.registerEvents(new WolfSwordListener(), plugin);
		p.registerEvents(new MagicalBlazePowderListener(), plugin);
		p.registerEvents(new PickarangListener(), plugin);
		p.registerEvents(new WOPListener(), plugin);
		p.registerEvents(new AutoFeederListener(), plugin);

		// Scaffolder
		ItemStack scaffolder = createScaffolderItem();
		NamespacedKey keyS = new NamespacedKey(getPlugin(), "scaffolder");
		recipes.add(keyS);
		ShapedRecipe rS = new ShapedRecipe(keyS, scaffolder);
		rS.shape("sss", "sns", "sss");
		rS.setIngredient('s', Material.SCAFFOLDING);
		rS.setIngredient('n', Material.NETHER_STAR);
		Bukkit.addRecipe(rS);

		// Teleporter
		ItemStack teleporter = createTeleporterItem();
		NamespacedKey keyT = new NamespacedKey(getPlugin(), "teleporter");
		recipes.add(keyT);
		ShapedRecipe rT = new ShapedRecipe(keyT, teleporter);
		rT.shape("ded", "ene", "ded");
		rT.setIngredient('d', Material.DIAMOND_BLOCK);
		rT.setIngredient('e', Material.ELYTRA);
		rT.setIngredient('n', Material.NETHER_STAR);
		Bukkit.addRecipe(rT);

		// Auto-MLG
		ItemStack bucket = createAutoMLGItem();
		NamespacedKey keyB = new NamespacedKey(plugin, "auto-mlg");
		recipes.add(keyB);
		ShapedRecipe rB = new ShapedRecipe(keyB, bucket);
		rB.shape("idi", "ibi", "iei");
		rB.setIngredient('i', Material.IRON_BLOCK);
		rB.setIngredient('d', Material.DISPENSER);
		rB.setIngredient('b', Material.BUCKET);
		rB.setIngredient('e', Material.ENDER_EYE);
		Bukkit.addRecipe(rB);

		// Wolf-Sword
		ItemStack sword = createWolfSwordItem();
		NamespacedKey keyW = new NamespacedKey(plugin, "wolf-sword");
		recipes.add(keyW);
		ShapedRecipe rW = new ShapedRecipe(keyW, sword);
		rW.shape("sgs", "sgs", "sts");
		rW.setIngredient('s', Material.SOUL_SAND);
		rW.setIngredient('g', Material.GHAST_TEAR);
		rW.setIngredient('t', Material.STICK);
		Bukkit.addRecipe(rW);

		// Magical-Blaze-Powder
		ItemStack powder = createMagicalBlazePowderItem();
		NamespacedKey keyP = new NamespacedKey(plugin, "magical-blaze-powder");
		recipes.add(keyP);
		ShapedRecipe rP = new ShapedRecipe(keyP, powder);
		rP.shape("bbb", "chc", "mmm");
		rP.setIngredient('b', Material.BLAZE_ROD);
		rP.setIngredient('c', Material.MAGMA_CREAM);
		rP.setIngredient('h', Material.HEART_OF_THE_SEA);
		rP.setIngredient('m', Material.MAGMA_BLOCK);
		Bukkit.addRecipe(rP);

		// Pickarang
		ItemStack pick = createPickarangItem();
		NamespacedKey keyPi = new NamespacedKey(plugin, "pickarang");
		recipes.add(keyPi);
		ShapelessRecipe rPi = new ShapelessRecipe(keyPi, pick);
		rPi.addIngredient(1, Material.NETHERITE_PICKAXE);
		rPi.addIngredient(1, Material.TRIDENT);
		Bukkit.addRecipe(rPi);

		// WOP
		ItemStack wop = createWOPItem();
		NamespacedKey keyWOP = new NamespacedKey(plugin, "wand-of-possessing");
		recipes.add(keyWOP);
		ShapedRecipe rWOP = new ShapedRecipe(keyWOP, wop);
		rWOP.shape("eee", "dbd", "nnn");
		rWOP.setIngredient('b', Material.BLAZE_ROD);
		rWOP.setIngredient('d', Material.DRAGON_BREATH);
		rWOP.setIngredient('e', Material.ENDER_EYE);
		rWOP.setIngredient('n', Material.NETHERITE_INGOT);
		Bukkit.addRecipe(rWOP);

		// Auto-feeder
		ItemStack af = createAutoFeederItem();
		NamespacedKey keyAf = new NamespacedKey(plugin, "auto-feeder");
		recipes.add(keyAf);
		ShapedRecipe rAf = new ShapedRecipe(keyAf, af);
		rAf.shape("ccc", "cdc", "ccc");
		rAf.setIngredient('c', Material.CAKE);
		rAf.setIngredient('d', Material.DISPENSER);
		Bukkit.addRecipe(rAf);
	}

	public void onDisable() {
		if (!afInventories.isEmpty()) {
			if (!afConfig.contains(AUTOFEEDER_CONFIG_KEY)) {
				afConfig.createSection(AUTOFEEDER_CONFIG_KEY);
			}

			for (Map.Entry<String, String> entry : afInventories.entrySet()) {
				String key = entry.getKey();
				String val = entry.getValue();
				afConfig.getConfigurationSection(AUTOFEEDER_CONFIG_KEY).set(key, val);
			}
			try {
				afConfig.save(afConfigFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Main getPlugin() {
		return plugin;
	}

	public FileConfiguration getAfConfig() {
		return afConfig;
	}

	public Map<String, String> getAfInventories() {
		return afInventories;
	}

	public static String getMessageFromConfig(String path, String command) {
		return ChatColor.translateAlternateColorCodes('&',
				plugin.getConfig().getString(path).replace("%command%", "/" + command));
	}

	public static ItemStack createScaffolderItem() {
		ItemStack scaffolder = new ItemStack(Material.SCAFFOLDING);
		ItemMeta scaffolderMeta = scaffolder.getItemMeta();
		scaffolderMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&k&l_&4&lThe Scaffolder&4&k&l_"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(SCAFFOLDER_LORE);
		scaffolderMeta.setLore(lore);
		scaffolderMeta.addEnchant(Enchantment.DURABILITY, 10, true);
		scaffolderMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		scaffolder.setItemMeta(scaffolderMeta);
		return scaffolder;
	}

	public static ItemStack createTeleporterItem() {
		ItemStack teleporter = new ItemStack(Material.DIAMOND_HOE);
		ItemMeta teleporterMeta = teleporter.getItemMeta();
		teleporterMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&k&l_&4&lThe Teleporter&4&k&l_"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(TELEPORTER_LORE);
		teleporterMeta.setLore(lore);
		teleporterMeta.addEnchant(Enchantment.DURABILITY, 10, true);
		teleporterMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		teleporter.setItemMeta(teleporterMeta);
		return teleporter;
	}

	public static ItemStack createAutoMLGItem() {
		ItemStack bucket = new ItemStack(Material.BUCKET);
		ItemMeta bucketMeta = bucket.getItemMeta();
		bucketMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&k&l_&4&Auto-MLG&4&k&l_"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(AUTOMLG_LORE);
		bucketMeta.setLore(lore);
		bucketMeta.addEnchant(Enchantment.DURABILITY, 10, true);
		bucketMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		bucket.setItemMeta(bucketMeta);
		return bucket;
	}

	public static ItemStack createWolfSwordItem() {
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		ItemMeta swordMeta = sword.getItemMeta();
		swordMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&k&l_&4&lWolf Swordr&4&k&l_"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(WOLFSWORD_LORE);
		swordMeta.setLore(lore);
		swordMeta.addEnchant(Enchantment.DURABILITY, 10, true);
		swordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		Damageable d = (Damageable) swordMeta;
		d.setDamage(249);
		sword.setItemMeta(d);
		return sword;
	}

	public static ItemStack createMagicalBlazePowderItem() {
		ItemStack powder = new ItemStack(Material.BLAZE_POWDER);
		ItemMeta powderMeta = powder.getItemMeta();
		powderMeta
				.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&k&l_&4&lMagical Blaze Powder&4&k&l_"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(MAGICALBLAZEPOWDER_LORE);
		powderMeta.setLore(lore);
		powderMeta.addEnchant(Enchantment.DURABILITY, 10, true);
		powderMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		powder.setItemMeta(powderMeta);
		return powder;
	}

	public static ItemStack createPickarangItem() {
		ItemStack pick = new ItemStack(Material.NETHERITE_PICKAXE);
		ItemMeta pickMeta = pick.getItemMeta();
		pickMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&k&l_&4&lPickarang&4&k&l_"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(PICKARANG_LORE);
		pickMeta.setLore(lore);
		pickMeta.addEnchant(Enchantment.DURABILITY, 10, true);
		pickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		pick.setItemMeta(pickMeta);
		return pick;
	}

	public static Entity createPickarangEntity(World w, Location loc, Player p) {
		ArmorStand a = (ArmorStand) w.spawnEntity(loc, EntityType.ARMOR_STAND);
		a.setGravity(false);
		a.setInvisible(true);
		a.setInvulnerable(true);
		a.setSilent(true);
		EntityEquipment e = a.getEquipment();
		e.setItemInMainHand(createPickarangItem());
		a.setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());
		a.setMetadata(PICKARANG_META_KEY, new FixedMetadataValue(plugin, 1));
		return a;
	}

	public static ItemStack createWOPItem() {
		ItemStack wand = new ItemStack(Material.BLAZE_ROD);
		ItemMeta wandMeta = wand.getItemMeta();
		wandMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&k&l_&4&lWand of possessing&4&k&l_"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(WOP_LORE);
		wandMeta.setLore(lore);
		wandMeta.addEnchant(Enchantment.DURABILITY, 10, true);
		wandMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		wand.setItemMeta(wandMeta);
		return wand;
	}

	public static ItemStack createAutoFeederItem() {
		ItemStack af = new ItemStack(Material.FLOWER_POT);
		ItemMeta afMeta = af.getItemMeta();
		afMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&k&l_&4&lAuto-feeder&4&k&l_"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(AUTOFEEDER_LORE);
		afMeta.setLore(lore);
		afMeta.addEnchant(Enchantment.DURABILITY, 10, true);
		afMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		af.setItemMeta(afMeta);
		return af;
	}

	public static boolean checkLore(Player p, String lore, boolean inHand) {
		if (inHand) {
			ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
			if (itemInMainHand.hasItemMeta() && itemInMainHand.getItemMeta().hasLore()
					&& itemInMainHand.getItemMeta().getLore().get(0).equals(lore)) {
				return true;
			} else {
				return false;
			}
		} else {
			for (ItemStack i : p.getInventory().getContents()) {
				if (i != null && i.hasItemMeta() && i.getItemMeta().hasLore()
						&& i.getItemMeta().getLore().get(0).equals(lore)) {
					return true;
				}
			}
			return false;
		}
	}
}
