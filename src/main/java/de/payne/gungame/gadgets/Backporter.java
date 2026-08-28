package de.payne.gungame.gadgets;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import de.payne.gungame.listener.player.PlayerSpawnProtectionDamageListener;
import de.payne.gungame.particles.Effects;
import de.payne.gungame.particles.ParticleData;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public final class Backporter implements Listener {

	private final PlayerSpawnProtectionDamageListener spawnProtection = new PlayerSpawnProtectionDamageListener();
	private final List<Player> cooldown = Lists.newArrayList();

	
	
	//RETURNS THE Backporter GADGET OBJECT FROM LIST WITH GADGET ID
	private final Gadgets getBackportGadget(final Player player) {
		return GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(6);
	}
	
	
	@EventHandler
	public final void onPlayerInteract(final PlayerInteractEvent event) {

		final Player player = event.getPlayer();

		if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}
		
		if(this.cooldown.contains(player)) {
			return;
		}
		
		if(player.getInventory().getItemInMainHand().getType() != Material.CLOCK) {
			return;
		}

		if(this.spawnProtection.checkSpawnProtection(player.getLocation())) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_BACKPORTER_ALREADY_AT_SPAWN, player.getUniqueId(), true));
			event.setCancelled(true);
			return;
		}
		
		Gadgets backporter = this.getBackportGadget(player);

		if(backporter.getAmount() <= 0) {
			return;
		}
		
		event.setCancelled(true);
		this.doBackportPlayer(player, backporter);

		
		this.cooldown.add(player);	
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GunGame.getInstance(), new Runnable() {
			@Override
			public void run() {
				cooldown.remove(player);	
			}
			
		}, 20L);
	}
	
	

	
	private final void doBackportPlayer(final Player player, final Gadgets backporter) {
		Location loc1 = player.getLocation().clone();
		Effects backportEffect = new Effects(player);
		ParticleData particleData = new ParticleData(player.getUniqueId());
		
		new BukkitRunnable() {
			
			int countdown = 3;
			@Override
			public void run() {
				if(countdown > 0) {
					if(loc1.getBlockX() == player.getLocation().getBlockX() && loc1.getBlockY() == player.getLocation().getBlockY() && loc1.getBlockZ() == player.getLocation().getBlockZ()) {
						backportEffect.startBackporter();
						player.sendActionBar(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_BACKPORTER_DO_NOT_MOVE, player.getUniqueId(), false), "#ZEIT#", String.valueOf(countdown))));			
						
						} else {
							player.sendActionBar(Component.text(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_BACKPORTER_DISABLED, player.getUniqueId(), false)));			
							particleData.endTask();
							particleData.removeTask();
							this.cancel();
						}
					
				}else if(countdown == 0) {
					backporter.removeUses(player, 1);
					backporter.updateItemStack(player.getUniqueId());
					particleData.endTask();
					particleData.removeTask();

					player.teleport(GunGame.getInstance().getCurrentMap().getSpawnLocation());
					this.cancel();
				}
				countdown--;
			}
			
		}.runTaskTimer(GunGame.getInstance(), 0, 20);
		
	}
	
}
