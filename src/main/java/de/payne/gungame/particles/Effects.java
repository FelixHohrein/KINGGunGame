package de.payne.gungame.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.payne.gungame.GunGame;

public class Effects {

	private final Player player;
	
	
	public Effects(final Player player) {
		this.player = player;
	}
	
	
	public final void startBackporter() {
		ParticleData particleData = new ParticleData(player.getUniqueId());
				
		if(!particleData.hasTask()){
			particleData.setTask(this.getBackporterTask());
		}
	}
	
	public BukkitTask getBackporterTask() {

		BukkitTask task = new BukkitRunnable() {
			
			double var = 0;
			Location loc, first, second;			
						
			@Override
			public void run() {

				var += Math.PI / 16;
				
				loc = player.getLocation();
				first = loc.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
				second = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));
				
				player.getWorld().spawnParticle(Particle.TOTEM, first, 0);
				player.getWorld().spawnParticle(Particle.TOTEM, second, 0);
				
			}
			
		}.runTaskTimer(GunGame.getInstance(), 0, 1);
		
		
		return task;
	}
}
