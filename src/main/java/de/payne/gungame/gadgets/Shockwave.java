package de.payne.gungame.gadgets;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import de.payne.gungame.GunGame;
import de.payne.gungame.listener.player.PlayerSpawnProtectionDamageListener;
import de.payne.gungame.team.Team;

public class Shockwave implements Listener {

	
	final PlayerSpawnProtectionDamageListener spawnProtection = new PlayerSpawnProtectionDamageListener();
	
	
	//RETURNS THE SHOCKWAVE GADGET OBJECT FROM LIST WITH ID 0
	private final Gadgets getPlayersShockwaveGadget(final Player player) {
		return GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(4);
	}
	
	
	
	@EventHandler
	public final void projectileHitEvent(final ProjectileHitEvent event) {
		
		//ENTITY INSTANCE OF SNOWBALL
		if(!(event.getEntity() instanceof Snowball)) {
			return;
		}
		
		final Snowball snowball = (Snowball) event.getEntity();
		
		if(!(snowball.getShooter() instanceof Player)) {
			return;
		}
		
		final Player shooter = (Player) snowball.getShooter();
		
		Location location = null;
		if(event.getHitBlock() != null) {
			location = event.getHitBlock().getLocation();
		} else if(event.getHitEntity() != null) {
			location = event.getHitEntity().getLocation();
		}
		
		if(location == null) {
			return;
		}
		//CHECK FOR SPAWNPROTECTION
		if(this.spawnProtection.checkSpawnProtection(location) || this.spawnProtection.checkSpawnProtection(shooter.getLocation())) {
			snowball.remove();
			GunGame.getInstance().getGunGameEngine().levelChange(shooter, GunGame.getInstance().getGungamePlayers().get(shooter.getUniqueId()).getCurrentLevel());
			return;
		}
		
		for(Entity entities : snowball.getNearbyEntities(5, 5, 5)){
			if(!(entities instanceof Player)) {
				continue;
			}
			Player p = (Player) entities;

			//SKIP DEN SHOOTER UND WENN INSIDE SPAWN
			if(p.equals(shooter) || this.spawnProtection.checkSpawnProtection(p.getLocation())) {
				continue;
			}
			
			if(Team.hasTeam(shooter)) {
				if(Team.getTeam(shooter).getPlayer1().equals(p) || Team.getTeam(shooter).getPlayer2().equals(p)) {
					continue;
				}
			}

			p.damage(0.1D, shooter);
			this.pullPlayerToLocation(p, location);
			
		}
		
//		this.spawnPrimedTNT(location);
		location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 5);
		this.getPlayersShockwaveGadget(shooter).removeUses(shooter, 1);
		this.getPlayersShockwaveGadget(shooter).updateItemStack(shooter.getUniqueId());
	}
	
	//PUSH OTHERS AWAY
	private final void pullPlayerToLocation(final Player player, final Location location){
		
		if(player.getLocation().distance(location) < 1) {
			Location location2 = location.add(1, 1, 1);
			double g = -0.08;
			double d = 1;
			double t = d;
			double vector_x = (1.0+0.07*t) * (player.getLocation().getX()-location2.getX())/t;
			double vector_y = (1.0+0.03*t) * (player.getLocation().getY()-location2.getY())/t -0.5*g*t;
			double vector_z = (1.0+0.07*t) * (player.getLocation().getZ()-location2.getZ())/t;
			
			Vector vector = player.getVelocity();
			
			vector.setX(vector_x);
			vector.setY(vector_y);
			vector.setZ(vector_z);
			
			player.setVelocity(vector);
			
		} else {
			double g = -0.08;
			double d = player.getLocation().distance(location);
			double t = d;
			double vector_x = (1.0+0.07*t) * (player.getLocation().getX()-location.getX())/t;
			double vector_y = (1.0+0.03*t) * (player.getLocation().getY()-location.getY())/t -0.5*g*t;
			double vector_z = (1.0+0.07*t) * (player.getLocation().getZ()-location.getZ())/t;
			
			Vector vector = player.getVelocity();
			
			vector.setX(vector_x);
			vector.setY(vector_y);
			vector.setZ(vector_z);
			
			player.setVelocity(vector);
		}

	}

}
