package de.payne.gungame.utils;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import lombok.Getter;

public final class LastDamageTimeScheduler {	
	
	//VICTIM, KILLER
	public static Map<Player, Player> lastDamager = Maps.newHashMap();
	//VICTIM, OBJEKT
	public static Map<Player, LastDamageTimeScheduler> aktiveTimer = Maps.newHashMap();
	
	private int durationInSeconds;

	@Getter
	private Player victim, killer;
	
	@Getter
	private final BukkitTask taskName;
	
	
	public LastDamageTimeScheduler(final int durationInSeconds, final Player victim, final Player killer) {
		this.durationInSeconds = durationInSeconds;
		
		this.victim = victim;
		this.killer = killer;
		
		this.taskName = this.newGameTimer();
		aktiveTimer.put(victim, this);
		lastDamager.put(victim, killer);
	}
	
	
	private final BukkitTask newGameTimer() {	
		BukkitTask gameTimer = new BukkitRunnable() {
			int countdown = durationInSeconds;
			
			@Override
			public void run() {
				

				if(countdown == 0) {

					lastDamager.remove(victim);
					aktiveTimer.remove(victim);
					this.cancel();
				}

				
				countdown--;
				
			}

		}.runTaskTimerAsynchronously(GunGame.getInstance(), 0, 20);
//		.runTaskTimer(GunGame.getInstance(), 0, 20);
		
		return gameTimer;
	}
}
