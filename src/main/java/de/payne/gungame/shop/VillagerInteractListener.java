package de.payne.gungame.shop;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import de.payne.gungame.GunGame;
import de.payne.gungame.gadgets.Gadgets;
import de.payne.gungame.language.MESSAGE;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public final class VillagerInteractListener implements Listener{

	
	@EventHandler /*(priority = EventPriority.HIGH)*/
	public final void villagerInteract(final PlayerInteractEntityEvent event) {
		
		
		if(!(event.getRightClicked() instanceof Villager)) {
			event.setCancelled(true);
			return;
		}
		final Villager villager = (Villager) event.getRightClicked();
		
		final Player player = event.getPlayer();
		
		//WENN ER NICHT SPAWN IST
		if(!(player.getWorld().equals(GunGame.getInstance().getLobbySpawn().getSpawnLocation().getWorld()))) {
			return;
		}
		
		if(!event.getPlayer().hasPermission("gungame.user.shop")) {
			return;
		}
		
		if(!villager.getCustomName().equalsIgnoreCase(ChatColor.GOLD + "Gadget - Shop")) {
			return;
		}
		
		player.openInventory(GunGame.getInstance().getShopGui().createShop(player));
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 15, 15);

	}
	
	@EventHandler
	public final void onInventoryClick(final InventoryClickEvent event) {
		
		 if (!(event.getWhoClicked() instanceof Player)){
			 return;
		 }
		 
		final Player player = (Player) event.getWhoClicked();																
	    final ItemStack clicked = event.getCurrentItem();
	    
	    //WENN DAS INVENTAR NICHT DER GADGET SHOP IST RETURN
//	    if (!event.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Gadget - Shop")) { // FOR SPIGOT
	    if(!event.getView().title().equals(Component.text(ChatColor.GOLD + "Gadget - Shop"))) { //FOR PAPER
//
//	    	
//	    	
//	    	
//	    	
//BUILDER INVENTORY ZUGRIFF AUF ANDERE INVENTARE ERLAUBEN
            if(player.hasPermission("gungame.admin.build") && GunGame.getInstance().getBuilders().contains(player)) {
            	event.setCancelled(false);
            } else { event.setCancelled(true);}
	    	return;
	    }

	    //WENN CLICK OUTSIDE ODER NULL
	    if (event.getSlotType().equals(SlotType.OUTSIDE) || event.getCurrentItem() == null) {
	    	return;
	    }
	    
	    //WENN KEINE RECHTE RETURN
	    if(!player.hasPermission("gungame.user.shop")) {
	    	return;
	    }
	    event.setCancelled(true); //DAMIT KEINE ITEMS AUS DEM INV GENOMMEN WERDEN KÖNNEN
	    //WENN AUF GESPERRTEN SLOT KLICKT ODER AUF SEINEN KOPF
	    if (clicked.getType() == Material.BLACK_STAINED_GLASS_PANE) { 
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 15, 15);
			return;
		}	
	    //
	    // KAUF INTERAKTION
	    //
	    //FISHING ROD / DER HAKEN

		if (clicked.equals(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(0).getItemStack())) {
			this.transaktionDurchführen(player, 0);
		} 
		//InstantLevelUpI
		else if(clicked.equals(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(1).getItemStack())) {
			this.transaktionDurchführen(player, 1);

		} 
		//InstantLevelUpII
		else if (clicked.equals(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(2).getItemStack())) {
			this.transaktionDurchführen(player, 2);

		}
		//InstantLevelUpIII
		else if (clicked.equals(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(3).getItemStack())) {
			this.transaktionDurchführen(player, 3);
		}
		//SHOCKWAVE
		else if (clicked.equals(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(4).getItemStack())) {
			this.transaktionDurchführen(player, 4);
		}
		//JOE
		else if (clicked.equals(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(5).getItemStack())) {
			this.transaktionDurchführen(player, 5);
		}
		//Backporter
		else if (clicked.equals(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(6).getItemStack())) {
			this.transaktionDurchführen(player, 6);
		}
	}
	
	//FÜHRT DIE TRANSAKTION DURCH
	private final void transaktionDurchführen(final Player player, final int gadgetId) {
		//WENN ER GENÜGEND PUNKTE HAT
		if(this.isPointsEnaugh(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(gadgetId), player)) {
			//ZIEHT DEM SPIELER DIE KOSTEN DES GADGETS AB
			GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setTokens(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getTokens() - GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(gadgetId).getKosten());
			//GIBT DEM SPIELER DAS ITEM BZW DIE ANZAHL AN USES DAZU
			if(gadgetId == 0) {//WENN DER HAKEN AMOUNT +5
				GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(gadgetId).setAmount(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(gadgetId).getAmount() + 5);
			} else { // WENN ILU´s dann AMOUNT +1
				GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(gadgetId).setAmount(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(gadgetId).getAmount() + 1);
			}
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPHUI_BUY_SUCCESS, player.getUniqueId(), true), "#GADGETNAME#", GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(gadgetId).getName()));
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 15, 15);
			player.closeInventory();
			GunGame.getInstance().getScoreboardManager().updateBoard(player);
			return;
		} else {//WENN ER NICHT GENÜGEND PUNKTE HAT
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SHOPGUI_BUY_NOT_ENAUGH_TOKENS, player.getUniqueId(), true), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(gadgetId).getKosten() - GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getTokens())));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 15, 15);
			return;
		}
	}
	
	//RETURNS TRUE IF THE PLAYER HAS ENOUGH POINTS FOR THE GADGET
	private final boolean isPointsEnaugh(final Gadgets gadget, final Player player) {
		
		if(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getTokens() >= gadget.getKosten()){
			return true;
		}
		return false;   	
	}
}
