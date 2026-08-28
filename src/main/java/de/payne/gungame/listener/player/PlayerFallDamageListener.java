package de.payne.gungame.listener.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public final class PlayerFallDamageListener implements Listener {

    @EventHandler
    public final void onFallDamage(final EntityDamageEvent event){
        if(event.getEntity() instanceof Player && event.getCause() == DamageCause.FALL)
             event.setCancelled(true);
    }
}
