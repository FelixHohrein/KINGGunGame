package de.payne.gungame;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public final class GunGameManager {
	
	public final void PlayerKilled(final Player player, final int addLevel, final int addKills) {
		
		//GET HIM TO THE NEXT LEVEL
		if(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel() < GunGame.getInstance().getLevelConfig().getLevel().size()) {
			GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setCurrentLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel() + addLevel);
			GunGame.getInstance().getGunGameEngine().levelChange(player, (GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel()));
			player.setLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
		}
		
		//UPDATE HIGHESTLEVEL 
		if(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel() > GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getHighestLevel()) {
			GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setHighestLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
		}
		//UPDATE KILLS/PUNKTE
		GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setKills(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getKills() + addKills);
		GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setTokens(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getTokens() + GunGame.getInstance().getSettings().getPointsToAdd());
		//GIVE REGENERATION EFFECT
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*5, 2, true, true));
	
		//UPDATE SCOREBOARD
		GunGame.getInstance().getScoreboardManager().updateBoard(player);
	}

	public final void PlayerDeath(final Player player, final int addDeaths) {
		//UPDATE DEATHS
		GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setDeaths(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getDeaths() + addDeaths); 
			
	}
	
	
	//RESET LOGIC FOR DEATH, USED IN PlayerDamageListenerDeathLogic
	public final void resetLevel(final Player player, final int levelToRemove) {
		if(levelToRemove == 0) {
			GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setCurrentLevel(1);
			GunGame.getInstance().getGunGameEngine().levelChange(player, GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel()); 
			player.setLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
		} else {
			GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setCurrentLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel()-levelToRemove);
			GunGame.getInstance().getGunGameEngine().levelChange(player, GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel()); 
			player.setLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
		}

	}
	
	//RETURNS THE LEVEL TO REMOVE FOR INDIVIDUAL REMOVEMENTS
	public final int getLevelToRemove(final Player player) {
		
		if(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel() <= 1) {
			return 0;
		}
		int abzug = Math.round(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel() * 0.33F);

		return abzug;
	}
}
