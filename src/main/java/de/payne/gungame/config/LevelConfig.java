 package de.payne.gungame.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import lombok.Getter;
import net.kyori.adventure.text.Component;



public final class LevelConfig {
	
	private final File levelFile;
	private final YamlConfiguration levelConfiguration;
	@Getter
	private final Map<Integer, ItemStack[]> level = Maps.newHashMap();
	
	//Konstruktor
	public LevelConfig(final Plugin plugin, final String fileName) {
		this.levelFile = new File(plugin.getDataFolder() + File.separator + fileName);
//		this.levelConfiguration = YamlConfiguration.loadConfiguration(this.levelFile);
		
		if(!this.levelFile.exists()) {
			try {
				this.levelFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.levelConfiguration = YamlConfiguration.loadConfiguration(this.levelFile);

	}
	
	//saves the File
	private final void saveFile() {
		try {
			this.levelConfiguration.save(this.levelFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//loads all levels with items into the Hashmap
	public final void loadItemsToMap(){
		//returnt wenn  der Wert aus use nicht mit einem Level-tree darunter übereinstimmt
		final String path = this.levelConfiguration.getString("use");
		
		if(!(this.levelConfiguration.contains(path))){
			 GunGame.getInstance().getLogger().info("Der 'use' Wert aus der level.yml konnte keinen unterwerten zugeordnet werden!");
			return;

		}
		
		for(final String keys : this.levelConfiguration.getConfigurationSection(path).getKeys(false)) {
			@SuppressWarnings("unchecked")
			List<ItemStack> zwischenspeicher = (List<ItemStack>) levelConfiguration.get(path + "." + keys);

			for(ItemStack is : zwischenspeicher){
				ItemMeta im = is.getItemMeta();
				im.setUnbreakable(true);
				is.setItemMeta(im);
			}
			
			final ItemStack[] items = zwischenspeicher.toArray(new ItemStack[0]);
			final int level = Integer.parseInt(keys);
			this.level.put(level, items);
		}
	}

	
	//creates an Itemstack in one line while using this method
    private final ItemStack CreateWithMaterial(Material material, int subid, int amount, String Displayname, ArrayList<Component> lore, Enchantment ench, int enchlvl) {
        @SuppressWarnings("deprecation")
		ItemStack is = new ItemStack(material, amount, (short) subid); 
        ItemMeta im = is.getItemMeta(); 
        if (Displayname != null) im.displayName((Component.text(Displayname))); 
        if (lore != null) im.lore(lore);
        if (ench != null) im.addEnchant(ench, enchlvl, true); 
        is.setItemMeta(im); 
        return is; 
    }
    
    //creates items with methode above and set it in the map "level" with key 1-30 and value itemstack[] if use in config = example
	public final void setLevelNormal() {
		//returnt wenn der path use aus config nicht example ist
		if(!(this.levelConfiguration.getString("use").equalsIgnoreCase("example"))) {
			return;
		}
		if(this.levelConfiguration.contains("example")) {
			return;
		}
        this.level.put(1, new ItemStack[]{
                this.CreateWithMaterial(Material.STONE_SWORD, 0, 1, "§bLevel 1", null, null, 0),
        });
        this.level.put(2, new ItemStack[]{
        		this.CreateWithMaterial(Material.STONE_SWORD, 0, 1, "§bLevel 2", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_HELMET, 0, 1, "§bLevel 2", null, null, 0),
        });
        this.level.put(3, new ItemStack[]{
        		this.CreateWithMaterial(Material.STONE_SWORD, 0, 1, "§bLevel 3", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_HELMET, 0, 1, "§bLevel 3", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_CHESTPLATE, 0, 1, "§bLevel 3", null, null, 0),
        });
        this.level.put(4, new ItemStack[]{     
        		this.CreateWithMaterial(Material.STONE_SWORD, 0, 1, "§bLevel 4", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_HELMET, 0, 1, "§bLevel 4", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_CHESTPLATE, 0, 1, "§bLevel 4", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_LEGGINGS, 0, 1, "§bLevel 4", null, null, 0),
        });
        this.level.put(5, new ItemStack[]{    
        		this.CreateWithMaterial(Material.STONE_SWORD, 0, 1, "§bLevel 5", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_HELMET, 0, 1, "§bLevel 5", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_CHESTPLATE, 0, 1, "§bLevel 5", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_LEGGINGS, 0, 1, "§bLevel 5", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_BOOTS, 0, 1, "§bLevel 5", null, null, 0),
        });
        this.level.put(6, new ItemStack[]{
        		this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, "§bLevel 6", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_HELMET, 0, 1, "§bLevel 6", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_CHESTPLATE, 0, 1, "§bLevel 6", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_LEGGINGS, 0, 1, "§bLevel 6", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_BOOTS, 0, 1, "§bLevel 6", null, null, 0),
        });
        this.level.put(7, new ItemStack[]{
        		this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, "§bLevel 7", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_HELMET, 0, 1, "§bLevel 7", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_CHESTPLATE, 0, 1, "§bLevel 7", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_LEGGINGS, 0, 1, "§bLevel 7", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_BOOTS, 0, 1, "§bLevel 7", null, null, 0),
        });
        this.level.put(8, new ItemStack[]{
        		this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, "§bLevel 8", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_HELMET, 0, 1, "§bLevel 8", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_CHESTPLATE, 0, 1, "§bLevel 8", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_LEGGINGS, 0, 1, "§bLevel 8", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_BOOTS, 0, 1, "§bLevel 8", null, null, 0),
        });
        this.level.put(9, new ItemStack[]{
        		this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, "§bLevel 9", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_HELMET, 0, 1, "§bLevel 9", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_CHESTPLATE, 0, 1, "§bLevel 9", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_LEGGINGS, 0, 1, "§bLevel 9", null, null, 0),
                this.CreateWithMaterial(Material.LEATHER_BOOTS, 0, 1, "§bLevel 9", null, null, 0),
        });
        this.level.put(10, new ItemStack[]{
        		this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, "§bLevel 10", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_HELMET, 0, 1, "§bLevel 10", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_CHESTPLATE, 0, 1, "§bLevel 10", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_LEGGINGS, 0, 1, "§bLevel 10", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_BOOTS, 0, 1, "§bLevel 10", null, null, 0),
        });
        this.level.put(11, new ItemStack[]{
        		this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, "§bLevel 11", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.GOLDEN_HELMET, 0, 1, "§bLevel 11", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_CHESTPLATE, 0, 1, "§bLevel 11", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_LEGGINGS, 0, 1, "§bLevel 11", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_BOOTS, 0, 1, "§bLevel 11", null, null, 0),
        });
        this.level.put(12, new ItemStack[]{
                this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, "§bLevel 12", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.CHAINMAIL_HELMET, 0, 1, "§bLevel 12", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_CHESTPLATE, 0, 1, "§bLevel 12", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_LEGGINGS, 0, 1, "§bLevel 12", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_BOOTS, 0, 1, "§bLevel 12", null, null, 0),
        });
        this.level.put(13, new ItemStack[]{
                this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, "§bLevel 13", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.CHAINMAIL_HELMET, 0, 1, "§bLevel 13", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_CHESTPLATE, 0, 1, "§bLevel 13", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_LEGGINGS, 0, 1, "§bLevel 13", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_BOOTS, 0, 1, "§bLevel 13", null, null, 0),
        });
        this.level.put(14, new ItemStack[]{
                this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, "§bLevel 14", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.CHAINMAIL_HELMET, 0, 1, "§bLevel 14", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_CHESTPLATE, 0, 1, "§bLevel 14", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_LEGGINGS, 0, 1, "§bLevel 14", null, null, 0),
                this.CreateWithMaterial(Material.GOLDEN_BOOTS, 0, 1, "§bLevel 14", null, null, 0),
        });
        this.level.put(15, new ItemStack[]{
                this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, "§bLevel 15", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.CHAINMAIL_HELMET, 0, 1, "§bLevel 15", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_CHESTPLATE, 0, 1, "§bLevel 15", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_LEGGINGS, 0, 1, "§bLevel 15", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_BOOTS, 0, 1, "§bLevel 15", null, null, 0),
        });
        this.level.put(16, new ItemStack[]{
                this.CreateWithMaterial(Material.IRON_SWORD, 0, 1, "§bLevel 16", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_HELMET, 0, 1, "§bLevel 16", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_CHESTPLATE, 0, 1, "§bLevel 16", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_LEGGINGS, 0, 1, "§bLevel 16", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_BOOTS, 0, 1, "§bLevel 16", null, null, 0),
        });
        this.level.put(17, new ItemStack[]{
                this.CreateWithMaterial(Material.IRON_SWORD, 0, 1, "§bLevel 17", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.IRON_HELMET, 0, 1, "§bLevel 17", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.CHAINMAIL_CHESTPLATE, 0, 1, "§bLevel 17", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_LEGGINGS, 0, 1, "§bLevel 17", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_BOOTS, 0, 1, "§bLevel 17", null, null, 0),
        });
        this.level.put(18, new ItemStack[]{
                this.CreateWithMaterial(Material.IRON_SWORD, 0, 1, "§bLevel 18", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.IRON_HELMET, 0, 1, "§bLevel 18", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_CHESTPLATE, 0, 1, "§bLevel 18", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.CHAINMAIL_LEGGINGS, 0, 1, "§bLevel 18", null, null, 0),
                this.CreateWithMaterial(Material.CHAINMAIL_BOOTS, 0, 1, "§bLevel 18", null, null, 0),
        });
        this.level.put(19, new ItemStack[]{
                this.CreateWithMaterial(Material.IRON_SWORD, 0, 1, "§bLevel 19", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.IRON_HELMET, 0, 1, "§bLevel 19", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_CHESTPLATE, 0, 1, "§bLevel 19", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_LEGGINGS, 0, 1, "§bLevel 19", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.CHAINMAIL_BOOTS, 0, 1, "§bLevel 19", null, null, 0),
        });
        this.level.put(20, new ItemStack[]{
                this.CreateWithMaterial(Material.IRON_SWORD, 0, 1, "§bLevel 20", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.IRON_HELMET, 0, 1, "§bLevel 20", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_CHESTPLATE, 0, 1, "§bLevel 20", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_LEGGINGS, 0, 1, "§bLevel 20", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_BOOTS, 0, 1, "§bLevel 20", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
        });
        this.level.put(21, new ItemStack[]{
                this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, "§bLevel 21", null, null, 0),
                this.CreateWithMaterial(Material.IRON_HELMET, 0, 1, "§bLevel 21", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_CHESTPLATE, 0, 1, "§bLevel 21", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_LEGGINGS, 0, 1, "§bLevel 21", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_BOOTS, 0, 1, "§bLevel 21", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
        });
        this.level.put(22, new ItemStack[]{
                this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, "§bLevel 22", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_HELMET, 0, 1, "§bLevel 22", null, null, 0),
                this.CreateWithMaterial(Material.IRON_CHESTPLATE, 0, 1, "§bLevel 22", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_LEGGINGS, 0, 1, "§bLevel 22", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_BOOTS, 0, 1, "§bLevel 22", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
        });
        this.level.put(23, new ItemStack[]{
                this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, "§bLevel 23", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_HELMET, 0, 1, "§bLevel 23", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_CHESTPLATE, 0, 1, "§bLevel 23", null, null, 0),
                this.CreateWithMaterial(Material.IRON_LEGGINGS, 0, 1, "§bLevel 23", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.IRON_BOOTS, 0, 1, "§bLevel 23", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
        });
        this.level.put(24, new ItemStack[]{
                this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, "§bLevel 24", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_HELMET, 0, 1, "§bLevel 24", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_CHESTPLATE, 0, 1, "§bLevel 24", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_LEGGINGS, 0, 1, "§bLevel 24", null, null, 0),
                this.CreateWithMaterial(Material.IRON_BOOTS, 0, 1, "§bLevel 24", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
        });
        this.level.put(25, new ItemStack[]{
                this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, "§bLevel 25", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_HELMET, 0, 1, "§bLevel 25", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_CHESTPLATE, 0, 1, "§bLevel 25", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_LEGGINGS, 0, 1, "§bLevel 25", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_BOOTS, 0, 1, "§bLevel 25", null, null, 0),
        });
        this.level.put(26, new ItemStack[]{
                this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, "§bLevel 26", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.DIAMOND_HELMET, 0, 1, "§bLevel 26", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_CHESTPLATE, 0, 1, "§bLevel 26", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_LEGGINGS, 0, 1, "§bLevel 26", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_BOOTS, 0, 1, "§bLevel 26", null, null, 0),
        });
        this.level.put(27, new ItemStack[]{
                this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, "§bLevel 27", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.DIAMOND_HELMET, 0, 1, "§bLevel 27", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.DIAMOND_CHESTPLATE, 0, 1, "§bLevel 27", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_LEGGINGS, 0, 1, "§bLevel 27", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_BOOTS, 0, 1, "§bLevel 27", null, null, 0),
        });
        this.level.put(28, new ItemStack[]{
                this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, "§bLevel 28", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.DIAMOND_HELMET, 0, 1, "§bLevel 28", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.DIAMOND_CHESTPLATE, 0, 1, "§bLevel 28", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.DIAMOND_LEGGINGS, 0, 1, "§bLevel 28", null, null, 0),
                this.CreateWithMaterial(Material.DIAMOND_BOOTS, 0, 1, "§bLevel 28", null, null, 0),
        });
        this.level.put(29, new ItemStack[]{
                this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, "§bLevel 29", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.DIAMOND_HELMET, 0, 1, "§bLevel 29", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.DIAMOND_CHESTPLATE, 0, 1, "§bLevel 29", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.DIAMOND_LEGGINGS, 0, 1, "§bLevel 29", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.DIAMOND_BOOTS, 0, 1, "§bLevel 29", null, null, 0),
        });
        this.level.put(30, new ItemStack[]{
                this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, "§bLevel 30", null, Enchantment.DAMAGE_ALL, 1),
                this.CreateWithMaterial(Material.DIAMOND_HELMET, 0, 1, "§bLevel 30", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.DIAMOND_CHESTPLATE, 0, 1, "§bLevel 30", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.DIAMOND_LEGGINGS, 0, 1, "§bLevel 30", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                this.CreateWithMaterial(Material.DIAMOND_BOOTS, 0, 1, "§bLevel 30", null, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
        });
        
       
        this.levelConfiguration.set("example", level);
        this.saveFile();
    }
}
