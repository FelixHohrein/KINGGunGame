package de.payne.gungame.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysicsListener implements Listener {

	
	@EventHandler
	public void blockPhysicsListener(BlockPhysicsEvent event) {
		event.setCancelled(true);
	}
}
