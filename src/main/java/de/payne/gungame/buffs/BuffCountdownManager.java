package de.payne.gungame.buffs;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import lombok.Getter;
import net.kyori.adventure.text.Component;

public class BuffCountdownManager {

	@Getter
	private final Map<Buff, BukkitTask> aktivBuffs = Maps.newHashMap();
	
	//STARTET EINEN NEUEN SCHEDULER MIT DUARION UND FÜGT DER MAÜ HINZU
	public final void addAktivBuffScheduler(final Buff buff, final Player player) {
		
		this.removeAktivBuffScheduler(GunGame.getInstance().getBuffedPlayers().get(player), player);
		GunGame.getInstance().getBuffManager().addBuffs(player, buff);
		
		aktivBuffs.put(buff,new BukkitRunnable() {
			int countdown = buff.getDuration();
			
			@Override
			public void run() {

				if(countdown > 0) {
					if(buff.getBuffName().equalsIgnoreCase("INSTANTLEVELUP") || buff.getBuffName().equalsIgnoreCase("INSTANTLEVELUPII") || buff.getBuffName().equalsIgnoreCase("INSTANTLEVELUPIII")) {
						if(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel() >= GunGame.getInstance().getLevelConfig().getLevel().size()) {
							sendAktionBar(player, GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.BUFF_HIGHEST_LEVEL_REACHED, player.getUniqueId(), false), "#POINTS#", "500"));
						} else {
							sendAktionBar(player, GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.BUFF_LEVEL_UP, player.getUniqueId(), false), "#BUFFTYPE#", buff.getBuffName()), "#LEVEL#", String.valueOf(buff.getLevelAdd())));
						}
					} else {
						sendAktionBar(player, GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.BUFF_OTHERS, player.getUniqueId(), false), "#BUFFTYPE#", buff.getBuffName()), "#ZEIT#", String.valueOf(countdown)));
					}
				}
				
				if(countdown == 0) {
					if(buff.getBuffName().equalsIgnoreCase("INSTANTLEVELUP") || buff.getBuffName().equalsIgnoreCase("INSTANTLEVELUPII") || buff.getBuffName().equalsIgnoreCase("INSTANTLEVELUPIII")) {
						if(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel() >= GunGame.getInstance().getLevelConfig().getLevel().size()) {
							sendAktionBar(player, GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.BUFF_HIGHEST_LEVEL_REACHED, player.getUniqueId(), false), "#POINTS#", "500"));
						} else {
							sendAktionBar(player, GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.BUFF_LEVEL_UP, player.getUniqueId(), false), "#BUFFTYPE#", buff.getBuffName()), "#LEVEL#", String.valueOf(buff.getLevelAdd())));
						}
					} else {
						sendAktionBar(player, GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.BUFF_TIME_OVER, player.getUniqueId(), false), "#BUFFTYPE#", buff.getBuffName()));
					}
					GunGame.getInstance().getBuffManager().removeBuffs(player, buff);
					
					removeAktivBuffScheduler(buff, player);
				}

				countdown--;
			}
		}.runTaskTimer(GunGame.getInstance(), 0, 20));

	}
	
	//REMOVES AKTIV SCHEDULERS AUS MAP UND STOPT VORHER WENN NOTWENDIG
	public final void removeAktivBuffScheduler(final Buff buff, final Player player) {
		
		if(!this.aktivBuffs.containsKey(buff)) {
			return;
		}
		if(!this.aktivBuffs.get(buff).isCancelled()) {
			this.aktivBuffs.get(buff).cancel();
		}
		if(this.aktivBuffs.containsKey(buff)) {
			this.aktivBuffs.remove(buff);
		}
		GunGame.getInstance().getBuffManager().removeBuffs(player, buff);
	}
	
	
	//send Aktionbar to player
	private final void sendAktionBar(final Player player, final String message) {
		player.sendActionBar(Component.text(message));
	}
	
}
