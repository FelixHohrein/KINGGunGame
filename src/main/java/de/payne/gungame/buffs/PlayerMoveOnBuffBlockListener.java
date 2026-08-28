package de.payne.gungame.buffs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.payne.gungame.GunGame;

public class PlayerMoveOnBuffBlockListener implements Listener {
	
    
    @EventHandler
    public final void onPlayerMoveEvent(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN); //get block typ
        
        if(!GunGame.getInstance().getIngameList().contains(player)){
        	return;
        }
        
        if(player.getWorld() != GunGame.getInstance().getCurrentMap().getSpawnLocation().getWorld()) {
        	return;
        }

        if(block.getType() != Material.GLASS) {
        	return;
        }

        if(GunGame.getInstance().getBuffManager().isCooldown(this.getBuffLocationObject(block.getLocation()))) {
	    	return;
	    }


        BuffTypes type = GunGame.getInstance().getBuffManager().randomBuff();
        GunGame.getInstance().getBuffCountdownManager().addAktivBuffScheduler(new Buff(type), player);
        GunGame.getInstance().getBuffManager().addCooldown(this.getBuffLocationObject(block.getLocation()));

    }
    
    
    private final BuffLocations getBuffLocationObject(final Location location) {

        for(BuffLocations locations : GunGame.getInstance().getBuffLocationsCurrentmap()) {
        	if(locations.getLocation().equals(location.getBlock().getLocation())) {
        		return locations;
        	}
        }
        return null;
    }
    
}