package de.payne.gungame.listener.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.payne.gungame.GunGame;


public final class PlayerSpawnProtectionDamageListener implements Listener {
	
	
    @EventHandler
    public final void onDamage(EntityDamageByEntityEvent event) {
    	if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
        	final Player damager = (Player) event.getDamager();
        	final Player victim = (Player) event.getEntity();
        	if (checkSpawnProtection(damager.getLocation()) || checkSpawnProtection(victim.getLocation())) { //Is player in spawn protection area
                event.setCancelled(true); //Player get no damage
            }
    	}
    }
    
    //retuns true if player is inside spawn protection
    public final boolean checkSpawnProtection(final Location location) {
    			
    	if(GunGame.getInstance().getCurrentMap().getProtectionform().equalsIgnoreCase("kreis")) {
    		return this.checkKreisSpawnProtection(location);
    	} else if(GunGame.getInstance().getCurrentMap().getProtectionform().equalsIgnoreCase("viereck")) {
    		return this.checkViereckSpawnProtection(location);
    	}
    	return true;
    }
    
    //returnt true wenn der Spieler sich innerhalb des protection damage bekommt.
    private final boolean checkKreisSpawnProtection(final Location location) {    	
    	if(location.getWorld() != GunGame.getInstance().getCurrentMap().getSpawnLocation().getWorld()) {
    		return true;
    	}
    	return location.distance(GunGame.getInstance().getCurrentMap().getSpawnLocation()) < GunGame.getInstance().getCurrentMap().getRadius();
    }
    
    //returns true if player is inside the viereck
    private final boolean checkViereckSpawnProtection(final Location location) {
    	
    	if(location.getWorld() != GunGame.getInstance().getCurrentMap().getSpawnLocation().getWorld()) {
    		return true;
    	}
    	
    	final int xMax = GunGame.getInstance().getCurrentMap().getSpawnLocation().getBlockX()+GunGame.getInstance().getCurrentMap().getXAchse();
    	final int xMin = GunGame.getInstance().getCurrentMap().getSpawnLocation().getBlockX()-GunGame.getInstance().getCurrentMap().getXAchse(); 	
    	final int zMax = GunGame.getInstance().getCurrentMap().getSpawnLocation().getBlockZ()+GunGame.getInstance().getCurrentMap().getZAchse();
    	final int zMin = GunGame.getInstance().getCurrentMap().getSpawnLocation().getBlockZ()-GunGame.getInstance().getCurrentMap().getZAchse();

    	final int playerX = location.getBlockX();
    	final int playerZ = location.getBlockZ();
    	
    	if(playerX <= xMax && playerX >= xMin && playerZ <= zMax && playerZ >= zMin) {
    		return true;
    	}
    	return false;
    }
    
    

}