package de.payne.gungame.shop;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;


public final class ShopGui {
	
	public final Inventory createShop (final Player player){
		
//		FOR PAPER
		final Inventory shop = Bukkit.createInventory(null, 9, Component.text(ChatColor.GOLD + "Gadget - Shop"));
		
		//------------------------------------------------------------------------------------------------------------------------------
		final ItemStack hook = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(0).getItemStack();
		final ItemMeta hookMeta = hook.getItemMeta();
		hookMeta.displayName(Component.text(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(0).getName()));
		
		
		final List<Component> hookList = Lists.newArrayList(); 
		hookList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_USES, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(5))));
		hookList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_COSTS, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(0).getKosten()))));
		
		hookMeta.lore(hookList); 
		
		hook.setItemMeta(hookMeta);
		
		shop.setItem(0, hook);
		//------------------------------------------------------------------------------------------------------------------------------
		final ItemStack instantLevelI = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(1).getItemStack();
		final ItemMeta instantLevelIMeta = instantLevelI.getItemMeta();
		
		instantLevelIMeta.displayName(Component.text(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(1).getName()));
		
		final List<Component> instantLevelIList = Lists.newArrayList(); 
		
		instantLevelIList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_USES, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(1))));
		instantLevelIList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_COSTS, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(1).getKosten()))));
		

		instantLevelIMeta.lore(instantLevelIList); //FOR PAPER
		
		instantLevelI.setItemMeta(instantLevelIMeta);
		
		shop.setItem(1, instantLevelI);
		//------------------------------------------------------------------------------------------------------------------------------
		final ItemStack instantLevelII = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(2).getItemStack();
		final ItemMeta instantLevelIIMeta = instantLevelII.getItemMeta();
		instantLevelIIMeta.displayName(Component.text(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(2).getName())); // FOR PAPER
//		instantLevelIIMeta.setDisplayName(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(2).getName()); FOR SPIGOT

		final List<Component> instantLevelIIList = Lists.newArrayList(); // FOR PAPER
//		final List<String> instantLevelIIList = Lists.newArrayList(); FOR SPIGOT
		
//		FOR PAPER
		instantLevelIIList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_USES, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(1))));
		instantLevelIIList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_COSTS, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(2).getKosten()))));
		
//		FOR SPIGOT
//		instantLevelIIList.add(ChatColor.GRAY + "1 Benutzung");
//		instantLevelIIList.add(ChatColor.GRAY + "Kosten: " + ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(2).getKosten());

//		FOR PAPER
		instantLevelIIMeta.lore(instantLevelIIList);
//		instantLevelIIMeta.setLore(instantLevelIIList); FOR SPIGOT
		
		instantLevelII.setItemMeta(instantLevelIIMeta);
		
		shop.setItem(2, instantLevelII);
		//------------------------------------------------------------------------------------------------------------------------------
		final ItemStack instantLevelIII = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(3).getItemStack();
		final ItemMeta instantLevelIIIMeta = instantLevelIII.getItemMeta();
//		FOR SPIGOT
//		instantLevelIIIMeta.setDisplayName(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(3).getName());
//		FOR PAPER
		instantLevelIIIMeta.displayName(Component.text(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(3).getName()));
		
//		final List<String> instantLevelIIIList = Lists.newArrayList(); FOR SPIGOT
//		FOR PAPER
		final List<Component> instantLevelIIIList = Lists.newArrayList();

//		FOR SPIGOT
//		instantLevelIIIList.add(ChatColor.GRAY + "1 Benutzung");
//		instantLevelIIIList.add(ChatColor.GRAY + "Kosten: " + ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(3).getKosten());

//		FOR PAPER
		instantLevelIIIList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_USES, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(1))));
		instantLevelIIIList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_COSTS, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(3).getKosten()))));

		instantLevelIIIMeta.lore(instantLevelIIIList); // FOR PAPER
//		instantLevelIIIMeta.setLore(instantLevelIIIList); FOR SPIGOT
		
		instantLevelIII.setItemMeta(instantLevelIIIMeta);
		
		shop.setItem(3, instantLevelIII);
		//------------------------------------------------------------------------------------------------------------------------------
		
		final ItemStack shockwave = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(4).getItemStack();
		final ItemMeta shockwaveMeta = shockwave.getItemMeta();
//		FOR PAPER
		shockwaveMeta.displayName(Component.text(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(4).getName()));
		
//		shockwaveMeta.setDisplayName(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(4).getName()); FOR SPIGOT

//		final List<String> shockwaveList = Lists.newArrayList(); FOR SPIGOT
		final List<Component> shockwaveList = Lists.newArrayList(); // FOR PAPER

//		FOR SPIGOT
//		shockwaveList.add(ChatColor.GRAY + "1 Benutzung");
//		shockwaveList.add(ChatColor.GRAY + "Kosten: " + ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(4).getKosten());

//		FOR PAPER
		shockwaveList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_USES, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(1))));
		shockwaveList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_COSTS, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(4).getKosten()))));
		
//		shockwaveMeta.setLore(shockwaveList); FOR SPIGOT
		shockwaveMeta.lore(shockwaveList); // FOR PAPER
		
		shockwave.setItemMeta(shockwaveMeta);
		
		shop.setItem(4, shockwave);
		
		//------------------------------------------------------------------------------------------------------------------------------
		
		final ItemStack joe = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(5).getItemStack();
		final ItemMeta joeMeta = joe.getItemMeta();
		
//		joeMeta.setDisplayName(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(5).getName()); FOR SPIGOT
		joeMeta.displayName(Component.text(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(5).getName())); // FOR PAPER
		
//		final List<String> joeList = Lists.newArrayList(); FOR SPIGOT
		final List<Component> joeList = Lists.newArrayList(); //FOR PAPER

//		FOR SPIGOT
//		joeList.add(ChatColor.GRAY + "1 Benutzung");
//		joeList.add(ChatColor.GRAY + "Kosten: " + ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(5).getKosten());

		//FOR PAPER
		joeList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_USES, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(1))));
		joeList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_COSTS, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(5).getKosten()))));
		
//		joeMeta.setLore(joeList); FOR SPIGOT
		joeMeta.lore(joeList); // FOR PAPER
		
		joe.setItemMeta(joeMeta);
		
		shop.setItem(5, joe);
		
		//------------------------------------------------------------------------------------------------------------------------------
		
		final ItemStack backporter = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(6).getItemStack();
		final ItemMeta backporterMeta = backporter.getItemMeta();
		
//		joeMeta.setDisplayName(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(5).getName()); FOR SPIGOT
		backporterMeta.displayName(Component.text(ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(6).getName())); // FOR PAPER
		
//		final List<String> joeList = Lists.newArrayList(); FOR SPIGOT
		final List<Component> backporterList = Lists.newArrayList(); //FOR PAPER

//		FOR SPIGOT
//		joeList.add(ChatColor.GRAY + "1 Benutzung");
//		joeList.add(ChatColor.GRAY + "Kosten: " + ChatColor.GOLD + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(5).getKosten());

		//FOR PAPER
		backporterList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_USES, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(1))));
		backporterList.add(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_COSTS, player.getUniqueId(), false), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(6).getKosten()))));
		backporterList.add(Component.text(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_EXPLANATION_BACKPORTER, player.getUniqueId(), false)));

//		joeMeta.setLore(joeList); FOR SPIGOT
		backporterMeta.lore(backporterList); // FOR PAPER
		
		backporter.setItemMeta(backporterMeta);
		
		shop.setItem(6, backporter);
		//------------------------------------------------------------------------------------------------------------------------------
		
		final ItemStack Platzhalter = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		final ItemMeta MPlatzhalter = Platzhalter.getItemMeta();
//		MPlatzhalter.setDisplayName(" ");  FOR SPIGOT
		MPlatzhalter.displayName(Component.text(" ")); //FOR PAPER
		Platzhalter.setItemMeta(MPlatzhalter);
		
		for (int i = 7; i <= 8; i++) {
			shop.setItem(i, Platzhalter);
		}
		//------------------------------------------------------------------------------------------------------------------------------
		
		return shop;
	}
}
