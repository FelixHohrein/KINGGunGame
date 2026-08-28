package de.payne.gungame.listener.block;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import de.payne.gungame.GunGame;

public final class BlockPlaceListener implements Listener {
	
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
    	Player player = event.getPlayer();
        if(player.hasPermission("gungame.admin.build") && GunGame.getInstance().getBuilders().contains(player)) {
        	event.setCancelled(false);
        } else { event.setCancelled(true);}
    }
}