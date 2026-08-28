package de.payne.gungame.gadgets;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.listener.player.PlayerSpawnProtectionDamageListener;
import de.payne.gungame.team.Team;

public final class JoeGadget implements Listener {
 
	private final PlayerSpawnProtectionDamageListener spawnProtection = new PlayerSpawnProtectionDamageListener();
	
	private final List<Player> cooldown = Lists.newArrayList();

	//RETURNS THE Exploding Sheep GADGET OBJECT FROM LIST WITH ID 5
	private final Gadgets getPlayersSilverfishGadget(final Player player) {
		return GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(5);
	}

	@EventHandler
	public final void onPlayerInteract(final PlayerInteractEvent event) {

		final Player player = event.getPlayer();

		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		if(this.cooldown.contains(player)) {
			return;
		}
		
		if(player.getInventory().getItemInMainHand().getType() != Material.SILVERFISH_SPAWN_EGG) {
			return;
		}

		if(this.spawnProtection.checkSpawnProtection(player.getLocation())) {
			event.setCancelled(true);
			return;
		}
		
		Gadgets joeGadget = this.getPlayersSilverfishGadget(player);

		if(joeGadget.getAmount() <= 0) {
			return;
		}
		
		event.setCancelled(true);

		joeGadget.removeUses(player, 1);
		joeGadget.updateItemStack(player.getUniqueId());
		Location location = event.getClickedBlock().getLocation();
		
		final Silverfish silverfish = this.spawnSilverfish(joeGadget, player, location);
		this.silverfishEngine(player, silverfish, 20);

		this.cooldown.add(player);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GunGame.getInstance(), new Runnable() {
			@Override
			public void run() {
				cooldown.remove(player);	
			}
			
		}, 20L);
	}
	
	//USED IN SILVERFISH ENGINE
	private final Player getNearestPlayer(final Player player, final Silverfish silverfish) {
		
		List<Player> onlinePlayersOnMap = Lists.newArrayList();
		
		for(Player next : Bukkit.getOnlinePlayers()) {
			if(next.getWorld().equals(GunGame.getInstance().getCurrentMap().getSpawnLocation().getWorld()) && silverfish.getWorld().equals(GunGame.getInstance().getCurrentMap().getSpawnLocation().getWorld())) {
				if(Team.hasTeam(player)) {
					if(Team.getTeam(player).getPlayer1().equals(next) || Team.getTeam(player).getPlayer2().equals(next)) {
						continue;
					}
				}
				onlinePlayersOnMap.add(next);
			}
		}
		
		onlinePlayersOnMap.remove(player);
		
		if(onlinePlayersOnMap.size() <= 0) {
			return null;
		}
		
		Player nearestPlayer = onlinePlayersOnMap.get(0);
		
		for(Player nextPlayer : onlinePlayersOnMap) {

			//WENN SPIELER ZUM FISH WENIGER WIE 20 BLÖCKE ENTFERNT
			if(nextPlayer.getLocation().distance(silverfish.getLocation()) <= 20) {
				//WENN DIE DISTANCE KLEINER WIE DIE DES NEARESTPLAYER IST ERSTETZT NEARESTPLAYER
				if(nearestPlayer.getLocation().distance(silverfish.getLocation()) > nextPlayer.getLocation().distance(silverfish.getLocation())){
					nearestPlayer = nextPlayer;
				}
			}

		}
		
		return nearestPlayer;
	}
		
	//USED IN INTERACT TO SPAWN ENTITY
	private final Silverfish spawnSilverfish(final Gadgets joe, final Player player, final Location location) {
		
		Silverfish silverfish = player.getWorld().spawn(location.clone().add(0, 1, 0), Silverfish.class);
		
		silverfish.setNoDamageTicks(20*50);
		silverfish.setCustomName(joe.getName());
		silverfish.setCustomNameVisible(false);
		
		silverfish.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*50, 5));
		
		return silverfish;
	}
	
	//THE ENGINE OF THE SILVERFISH EXPLODE/SETTING TARGET/ DAMAGE PLAYEER/ DESPAWN
	private final void silverfishEngine(final Player player, final Silverfish silverfish, final int timeInSeconds) {
		new BukkitRunnable() {

			
			int countdown = timeInSeconds*2;
			
			@Override
			public void run() {
				
				Player nearestPlayer = getNearestPlayer(player, silverfish);
				
				silverfish.setTarget(nearestPlayer);
				
				if(silverfish.getTarget() != null) {
							
					//WENN SILVERFISH SEIN ZIEL GEFUNDEN HAT UND DORT IST
					if(silverfish.getLocation().distance(nearestPlayer.getLocation()) <= 3) {
						
						if(silverfish.isValid()) {
							silverfish.remove();
						}
						
						silverfish.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, silverfish.getLocation(), 5);
						nearestPlayer.damage(nearestPlayer.getHealth()/2, player);

						this.cancel();
					}
					
					//WENN FISCH IM WASSSER
					if(silverfish.isInWater()) {
						if(silverfish.isValid()) {
							silverfish.remove();
						}
						this.cancel();
					}
					
					//WENN COUNTDOWN == 0
					if(countdown == 0) {
						
						if(silverfish.isValid()) {
							silverfish.remove();
						}
						
						this.cancel();
					}
					
				} else {
					silverfish.teleport(player.getLocation());
				}
				
				countdown--;
			}
			
		}.runTaskTimer(GunGame.getInstance(), 0, 10);
	}
}
