package de.payne.gungame.shop;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import de.payne.gungame.GunGame;
import net.md_5.bungee.api.ChatColor;

public final class ShopVillager {

	public final void createVillager(final Location location) {
		
		this.deleteOld();
		
		final Villager villager = location.getWorld().spawn(location, Villager.class);
		
		villager.setCanPickupItems(false);
		villager.setAI(false);
		villager.setGravity(false);
		villager.setInvisible(false);
		villager.setBreed(false);
		villager.setCollidable(false);
		villager.setInvulnerable(true);
		villager.setSilent(true);
		villager.setCustomName(ChatColor.GOLD + "Gadget - Shop");
		villager.setCustomNameVisible(true);
	}
	
	
	private final void deleteOld() {
		
		Location shopLocation = GunGame.getInstance().getSettings().getShopPosition();
		
		Collection<Entity> entities = shopLocation.getWorld().getNearbyEntities(shopLocation, 1, 1, 1, (entity) -> entity.getType() == EntityType.VILLAGER);
		for(Entity villager : entities) {
			villager.remove();
		}
	}
}
