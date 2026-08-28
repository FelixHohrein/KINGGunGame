package de.payne.gungame.kopfgeld;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import de.payne.gungame.armorstand.SkullCreator;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public final class KopfgeldInventory implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
	    if(!event.getView().title().equals(Component.text(ChatColor.BOLD + "" + ChatColor.AQUA + "Ausgesetzte Kopfgelder"))) {
	    	return;
	    }
	    event.setCancelled(true);
	}
	
	
	

	public final Inventory createKopfgeld‹bersichtInventory() {
		final Inventory inv = Bukkit.createInventory(null, 54, Component.text(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Ausgesetzte Kopfgelder"));	
		
		int i = 0;
		if(!Kopfgeld.getKopfgelder().isEmpty()) {
			for(final Kopfgeld kopfgeld : Kopfgeld.getKopfgelder().values()) {
				
				if(kopfgeld.getId() < 0 || kopfgeld.getTarget() == null || kopfgeld.getSender() == null || kopfgeld.getAmount() < 1 || kopfgeld.getRemainingTime() == null) {
					continue;
				}
				
				inv.setItem(i, this.getPlayerHead(kopfgeld.getTarget(), kopfgeld.getSender(), kopfgeld.getAmount(), kopfgeld.getRemainingTime()));
				i++;
			}
		}	
		
		return inv;
	}
	
	
	private final ItemStack getPlayerHead(final OfflinePlayer target, final OfflinePlayer sender, final int amount, final String verbleibendeZeit) {
		
		final ItemStack playerHead = SkullCreator.itemFromUuid(target.getUniqueId());
		final ItemMeta playerHeadM = playerHead.getItemMeta();
		final List<Component> lore = Lists.newArrayList();
		
		lore.add(Component.text(ChatColor.GRAY + "Ausgesetzt von: " + ChatColor.GOLD + sender.getName()));
		lore.add(Component.text(ChatColor.GRAY + "Betrag: " + ChatColor.GOLD + amount));
		lore.add(Component.text(ChatColor.GRAY + "Verbleibende Zeit:" + ChatColor.GOLD + verbleibendeZeit));
		if(target.isOnline()) {
			lore.add(Component.text(ChatColor.GREEN + "online"));
		} else {
			lore.add(Component.text(ChatColor.RED + "offline"));
		}
		
		playerHeadM.displayName(Component.text(ChatColor.GOLD + target.getName()));
		playerHeadM.lore(lore);
		
		playerHead.setItemMeta(playerHeadM);
		
		return playerHead;
	}
}
