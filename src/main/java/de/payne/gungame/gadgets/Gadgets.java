package de.payne.gungame.gadgets;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class Gadgets {
	
	@Getter
	private int id, levelToSet, kosten;
	@Getter
	@Setter
	private int amount;
	@Getter
	private String name;
	@Getter
	private ItemStack itemStack;

	//Konstruktor
	public Gadgets(UUID uuid, final GadgetTypes gadgetTypes) {
		this.getData(uuid, gadgetTypes);
	}

	
	private final void getData(final UUID uuid, final GadgetTypes gadgetTypes) {
		
		switch(gadgetTypes){
		
        case HOOK:
            this.id = 0;
            this.levelToSet = 0;//UNUSED HERE
            this.kosten = 500;
            this.amount = 5;
            this.name = "Der Haken";
            this.itemStack = this.createHook(uuid, this.amount, this.name);
            break;
            
        case INSTANTLEVELI:
            this.id = 1;
            this.levelToSet = 25;
            this.kosten = 2500;
            this.amount = 1;
            this.name = "Level 25 - Gold";
            this.itemStack = this.createInstantlevelI(uuid, this.amount, this.name);
            break;
            
        case INSTANTLEVELII:
            this.id = 2;
            this.levelToSet = 45;
            this.kosten = 4500;
            this.amount = 1;
            this.name = "Level 45 - Eisen";
            this.itemStack = this.createInstantlevelII(uuid, this.amount, this.name);
            break;
            
        case INSTANTLEVELIII:
            this.id = 3;
            this.levelToSet = 65;
            this.kosten = 6500;
            this.amount = 1;
            this.name = "Level 65 - Diamant";
            this.itemStack = this.createInstantlevelIII(uuid, this.amount, this.name);
            break;

        case SHOCKWAVE:
            this.id = 4;
            this.levelToSet = 0;
            this.kosten = 500;
            this.amount = 1;
            this.name = "Shockwave";
            this.itemStack = this.createShockwave(uuid, this.amount, this.name);
            break;
        case JOE:
        	this.id = 5;
        	this.levelToSet = 0;
        	this.kosten = 500;
        	this.amount = 1;
        	this.name = "Joe";
        	this.itemStack = this.createSheep(uuid, this.amount, this.name);
        	break;
        case BACKPORTER:
        	this.id = 6;
        	this.levelToSet = 0;
        	this.kosten = 500;
        	this.amount = 1;
        	this.name = "Backporter";
        	this.itemStack = this.createBackporter(uuid, this.amount, this.name);
        	break;
        default:
        	System.out.println("[GunGame] --> [GADGETS] > GADGETS NOT FOUND!");
        	break;
		}
	}
	
	
	//CREATES THE HOOK, ONLY USED IN GADGETS.JAR (HERE)
	private final ItemStack createHook(final UUID uuid, final int uses, final String name) {
		ItemStack itemStack = new ItemStack(Material.FISHING_ROD);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.displayName(Component.text(ChatColor.GOLD + name)); // FOR PAPER
//		itemMeta.setDisplayName(ChatColor.GOLD + name); FOR SPIGOT
		itemMeta.setUnbreakable(true);
		ArrayList<Component> lore = Lists.newArrayList();
//		ArrayList<String> lore = new ArrayList<String>(); FOR SPIGOT
		lore.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_USES, uuid, false), "#USES#", String.valueOf(uses)))); // FOR PAPER
//		lore.add(ChatColor.GRAY + "Benutzungen: " + ChatColor.GOLD + uses); FOR SPIGOT
		itemMeta.lore(lore);
//		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	//CREATES THE LEVELUP ITEM, ONLY USED IN GADGETS.JAR (HERE)
	private final ItemStack createInstantlevelI(final UUID uuid, final int uses, final String name) {
		ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.displayName(Component.text(ChatColor.GOLD + name)); // FOR PAPER
//		itemMeta.setDisplayName(ChatColor.GOLD + name); FOR SPIGOT
		itemMeta.setUnbreakable(true);
		ArrayList<Component> lore = Lists.newArrayList(); // FOR PAPER
//		ArrayList<String> lore = new ArrayList<String>(); // FOR SPIGOT
		lore.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_USES, uuid, false), "#USES#", String.valueOf(uses))));
//		lore.add(ChatColor.GRAY + "Benutzungen: " + ChatColor.GOLD + uses); FOR SPIGOT
		itemMeta.lore(lore); // FOR PAPER
//		itemMeta.setLore(lore); FOR SPIGOT
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	//CREATES THE LEVELUP ITEM, ONLY USED IN GADGETS.JAR (HERE)
	private final ItemStack createInstantlevelII(final UUID uuid, final int uses, final String name) {
		ItemStack itemStack = new ItemStack(Material.IRON_INGOT);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.displayName(Component.text(ChatColor.GOLD + name)); // FOR PAPER
//		itemMeta.setDisplayName(ChatColor.GOLD + name); FOR SPIGOT
		itemMeta.setUnbreakable(true);
		ArrayList<Component> lore = Lists.newArrayList(); // FOR PAPER
//		ArrayList<String> lore = Lists.newArrayList(); FOR SPIGOT
		lore.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_USES, uuid, false), "#USES#", String.valueOf(uses))));
//		lore.add(ChatColor.GRAY + "Benutzungen: " + ChatColor.GOLD + uses);
		itemMeta.lore(lore); // FOR PAPER
//		itemMeta.setLore(lore); FOR SPIGOT
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	//CREATES THE LEVELUP ITEM, ONLY USED IN GADGETS.JAR (HERE)
	private final ItemStack createInstantlevelIII(final UUID uuid, final int uses, final String name) {
		ItemStack itemStack = new ItemStack(Material.DIAMOND);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.displayName(Component.text(ChatColor.GOLD + name)); // FOR PAPER
//		itemMeta.setDisplayName(ChatColor.GOLD + name); FOR SPIGOT
		itemMeta.setUnbreakable(true);
		ArrayList<Component> lore = Lists.newArrayList(); // FOR PAPER
//		ArrayList<String> lore = new ArrayList<String>(); // FOR SPIGOT
		lore.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_USES, uuid, false), "#USES#", String.valueOf(uses))));
//		lore.add(ChatColor.GRAY + "Benutzungen: " + ChatColor.GOLD + uses);
		itemMeta.lore(lore); //FOR PAPER
//		itemMeta.setLore(lore); FOR SPIGOT
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	//CREATES THE LEVELUP ITEM, ONLY USED IN GADGETS.JAR (HERE)
	private final ItemStack createShockwave(final UUID uuid, final int uses, final String name) {
		ItemStack itemStack = new ItemStack(Material.SNOWBALL);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.displayName(Component.text(ChatColor.GOLD + name)); // FOR PAPER
//		itemMeta.setDisplayName(ChatColor.GOLD + name); // FOR SPIGOT
		itemMeta.setUnbreakable(true);
		ArrayList<Component> lore = Lists.newArrayList(); // FOR PAPER
//		ArrayList<String> lore = new ArrayList<String>(); // FOR SPIGOT
		lore.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_USES, uuid, false), "#USES#", String.valueOf(uses))));
//		lore.add(ChatColor.GRAY + "Benutzungen: " + ChatColor.GOLD + uses); FOR SPIGOT
		itemMeta.lore(lore); // FOR PAPER
//		itemMeta.setLore(lore); FOR SPIGOT
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	//CREATES THE SHEEP ITEM, ONLY USED IN GADGETS.JAR (HERE)
	private final ItemStack createSheep(final UUID uuid, final int uses, final String name) {
		ItemStack itemStack = new ItemStack(Material.SILVERFISH_SPAWN_EGG);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.displayName(Component.text(ChatColor.GOLD + name)); // FOR PAPER
//		itemMeta.setDisplayName(ChatColor.GOLD + name); // FOR SPIGOT
		itemMeta.setUnbreakable(true);
		ArrayList<Component> lore = Lists.newArrayList(); // FOR PAPER
//		ArrayList<String> lore = new ArrayList<String>(); FOR SPIGOT
		lore.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_USES, uuid, false), "#USES#", String.valueOf(uses))));
//		lore.add(ChatColor.GRAY + "Benutzungen: " + ChatColor.GOLD + uses); FOR SPIGOT
		itemMeta.lore(lore); // FOR PAPER
//		itemMeta.setLore(lore); FOR SPIGOT
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	//CREATES THE BackPorter ITEM, ONLY USED IN GADGETS.JAR (HERE)
	private final ItemStack createBackporter(final UUID uuid, final int uses, final String name) {
		ItemStack itemStack = new ItemStack(Material.CLOCK);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.displayName(Component.text(ChatColor.GOLD + name)); // FOR PAPER
//		itemMeta.setDisplayName(ChatColor.GOLD + name); // FOR SPIGOT
		itemMeta.setUnbreakable(true);
		ArrayList<Component> lore = Lists.newArrayList(); // FOR PAPER
//		ArrayList<String> lore = new ArrayList<String>(); FOR SPIGOT
		lore.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_USES, uuid, false), "#USES#", String.valueOf(uses))));
//		lore.add(ChatColor.GRAY + "Benutzungen: " + ChatColor.GOLD + uses); FOR SPIGOT
		itemMeta.lore(lore); // FOR PAPER
//		itemMeta.setLore(lore); FOR SPIGOT
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	//TO UPDATE THE USSES
	public final void updateItemStack(final UUID uuid) {
		
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		
		final List<Component> lore = Lists.newArrayList(); // FOR PAPER
//		final List<String> lore = Lists.newArrayList(); FOR SPIGOT
		lore.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_USES, uuid, false), "#USES#", String.valueOf(this.amount))));
//		lore.add(ChatColor.GRAY + "Benutzungen: "+ ChatColor.GOLD + this.amount); FOR SPIGOT
		
		itemMeta.lore(lore); // FOR PAPER
//		itemMeta.setLore(lore); FOR SPIGOT
		this.itemStack.setItemMeta(itemMeta);
	}
	
	//REMOVE USES OR HOLE GADGET IF ITS DONE
	public final void removeUses(final Player shooter, final int amount) {
		
		//WENN USES GRÖSSER ALS 0
		if(this.amount > 0) {
			//REMOVE USE IN GADGET OBEJECT
			this.amount = this.amount - amount;
			//WENN AMOUNT DANN = 0
			if(this.amount == 0) {
				shooter.getInventory().remove(this.itemStack);
				shooter.updateInventory();
				this.sendAktionBar(shooter, GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_NO_USES, shooter.getUniqueId(), false), "#GADGETNAME#", this.name));
				//WENN AMOUNT NICHT 0
			} else {
				this.sendAktionBar(shooter, GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_LESS_USES, shooter.getUniqueId(), false), "#GADGETNAME#", this.name), "#USES#", String.valueOf(this.amount)));
			}
		}
		//REFRESH THE ITEM THE PLAYER IS HOLDING
		this.updateItemStack(shooter.getUniqueId());
		GunGame.getInstance().getGunGameEngine().levelChange(shooter, GunGame.getInstance().getGungamePlayers().get(shooter.getUniqueId()).getCurrentLevel());
	}
	
	//send Aktionbar to player
	private final void sendAktionBar(final Player player, final String message) {
		player.sendActionBar(Component.text(message));
	}
}