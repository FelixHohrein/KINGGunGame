package de.payne.gungame.listener.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.payne.gungame.GunGame;
import de.payne.gungame.team.Team;
import de.payne.gungame.utils.LastDamageTimeScheduler;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if(LastDamageTimeScheduler.aktiveTimer.containsKey(player)) {
			Player damager = LastDamageTimeScheduler.lastDamager.get(player);
			GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setCurrentLevel(1);
			GunGame.getInstance().getGunGameManager().PlayerKilled(damager, 1, 1);
		}
		
		if(Team.hasTeam(player)){
			Team.getTeam(player).closeTeam();
		}
		
		GunGame.getInstance().getIngameList().remove(player);
		GunGame.getInstance().getSignBuilder().signUpdate(Bukkit.getOnlinePlayers().size()-1);
		GunGame.getInstance().getBuilders().remove(player);
		GunGame.getInstance().getScoreboardManager().getBoards().remove(player);
		
		//FÜR CACHING VORHER NOCH DATEN METHODE FÜR SYNCEN MIT DATABASE----------
		GunGame.getInstance().getStatisticTable().setValues(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()));
		GunGame.getInstance().getGungamePlayers().remove(player.getUniqueId());
		//-----------------------------------------------------------
	}
}