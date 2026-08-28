package de.payne.gungame.listener.block;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.payne.gungame.GunGame;

public final class BlockBreakListener implements Listener {
	
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
    	final Player player = event.getPlayer();
        if(player.hasPermission("gungame.admin.build") && GunGame.getInstance().getBuilders().contains(player)) {
        	event.setCancelled(false);
        } else { event.setCancelled(true);}
    }
}