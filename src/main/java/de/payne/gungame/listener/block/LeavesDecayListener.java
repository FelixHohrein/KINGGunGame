package de.payne.gungame.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeavesDecayListener implements Listener {

	
	@EventHandler
	public void leavesDecayListener(LeavesDecayEvent event) {
		event.setCancelled(true);
	}
}
