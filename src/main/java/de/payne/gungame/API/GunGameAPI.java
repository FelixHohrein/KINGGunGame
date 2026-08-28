package de.payne.gungame.API;

import org.bukkit.entity.Player;

import de.payne.gungame.GunGame;
import de.payne.gungame.database.GunGamePlayer;

public class GunGameAPI {
	
	
	public GunGamePlayer getCachedPlayerData(final Player player) {
		if(GunGame.getInstance().getGungamePlayers().containsKey(player.getUniqueId())) {
			return GunGame.getInstance().getGungamePlayers().get(player.getUniqueId());
		} else {
			return null;
		}
	}
	
	public void updateScoreboard(final Player player) {
		if(player.isOnline()) {
			GunGame.getInstance().getScoreboardManager().updateBoard(player);
		}
	}

}
