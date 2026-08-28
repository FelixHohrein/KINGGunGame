package de.payne.gungame.gadgets;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public final class InstantLevel implements Listener{

	private final List<Player> cooldown = Lists.newArrayList();


	//RETURNS THE INSTANTLEVELUP GADGET OBJECT FROM LIST WITH GADGET ID
	private final Gadgets getPlayersLevelUpGadget(final Player player) {
		
		//WENN ITEMSTACK EQUALS = INSTANTLEVELUP1
		if(player.getInventory().getItemInMainHand().equals(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(1).getItemStack())){
			return GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(1);
		}
		//WENN ITEMSTACK EQUALS = INSTANTLEVELUP2
		else if(player.getInventory().getItemInMainHand().equals(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(2).getItemStack())){
			return GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(2);
		}
		//WENN ITEMSTACK EQUALS = INSTANTLEVELUP3
		else if(player.getInventory().getItemInMainHand().equals(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(3).getItemStack())){
			return GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(3);
		}
		return null;
	}
	
	
	@EventHandler
	public final void onPlayerInteractEntity(final PlayerInteractEvent event) {
		
		//DEFINES THE PLAYER
		final Player player = event.getPlayer();
		
		//WENN ER AM SPAWN IST
		if(player.getWorld().equals(GunGame.getInstance().getLobbySpawn().getSpawnLocation().getWorld())) {
			return;
		}
		
		//DEFINES THE GADGET WITH UPPER METHOD
		final Gadgets levelupGadget = this.getPlayersLevelUpGadget(player);
		
		//ABFRAGEN OB DER SPIELER DAS RICHTIGE ITEM IN DER HAND HÄLLT SIEHE METHODE OBEN
		if(levelupGadget == null) {
			return;
		}

		//WENN DIE ACTION KEIN RECHTSKLICK IN AIR ODER BLOCK IST RETURN
		if(!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		//WENN GADGET LEVEL = 0 - WARUM AUCH IMMER
		if(levelupGadget.getLevelToSet() == 0) {
			return;
		}
		
		if(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel() >= levelupGadget.getLevelToSet()) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_LEVELUP_FEHLER, player.getUniqueId(), true));
			return;
		}
		//UPDATE THE PLAYERS GADGET
		levelupGadget.removeUses(player, 1);
		levelupGadget.updateItemStack(player.getUniqueId());
		
		this.cooldown.add(player);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GunGame.getInstance(), new Runnable() {
			@Override
			public void run() {
				cooldown.remove(player);	
			}
			
		}, 20L);
		
		//DO WHAT THE GADGET SHOULD DO
		GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setCurrentLevel(levelupGadget.getLevelToSet());
		GunGame.getInstance().getGunGameEngine().levelChange(player, GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
		player.setLevel(levelupGadget.getLevelToSet());
		
		//SEND MESSAGE / PLAY SOUND
		player.sendActionBar(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_LEVELUP_SUCCESS, player.getUniqueId(), false), "#LEVEL#", String.valueOf(levelupGadget.getLevelToSet())))); // FOR PAPER
//		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "Du hast " + ChatColor.GOLD + "erfolgreich" + ChatColor.GRAY + " dein Level auf " + ChatColor.GOLD + levelupGadget.getLevelToSet() + ChatColor.GRAY + " erhöht.")); FOR SPIGOT
		player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 15f, 15f);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
