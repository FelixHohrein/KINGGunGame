package de.payne.gungame.listener.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.payne.gungame.GunGame;
import de.payne.gungame.utils.LastDamageTimeScheduler;


public final class PlayerMoveListener implements Listener {
	
	private final PlayerSpawnProtectionDamageListener damageListener = new PlayerSpawnProtectionDamageListener();
    
    @EventHandler
    public final void onPlayerMoveEvent(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Block block = player.getLocation().getBlock(); //get block typ
        
        if(player.isDead()){
        	return;
        }
        if(!GunGame.getInstance().getIngameList().contains(player)) {
        	return;
        }
        
        if(player.getWorld() != GunGame.getInstance().getCurrentMap().getSpawnLocation().getWorld()) {
        	return;
        }

        if(block.getType() != Material.WATER && block.getType() != Material.LAVA) {
        	return;
        }
        
        if(this.damageListener.checkSpawnProtection(player.getLocation())) {
        	return;
        }

        if(LastDamageTimeScheduler.aktiveTimer.containsKey(player)) {
        	
        	if(!LastDamageTimeScheduler.lastDamager.get(player).isOnline()) {
        		player.damage(100, player);  
        		return;
        	} 
        	
            player.damage(100, LastDamageTimeScheduler.lastDamager.get(player));  
            return;
        }
        
        player.damage(100, player);  
    }
}