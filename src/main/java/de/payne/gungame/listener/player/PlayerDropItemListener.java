package de.payne.gungame.listener.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;



public final class PlayerDropItemListener implements Listener {

    @EventHandler
    public final void onDrop(final PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}