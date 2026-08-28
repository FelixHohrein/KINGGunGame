package de.payne.gungame.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;

public class HangingBreakListener implements Listener {

	
	@EventHandler
	public void hangingBreakListener(HangingBreakEvent event) {
		event.setCancelled(true);
	}
}
