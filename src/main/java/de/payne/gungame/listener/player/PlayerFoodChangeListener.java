package de.payne.gungame.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public final class PlayerFoodChangeListener implements Listener {

	
    @EventHandler
    public final void onFoodChange(final FoodLevelChangeEvent event) {
        event.setCancelled(true); //Cancel this event
    }

}