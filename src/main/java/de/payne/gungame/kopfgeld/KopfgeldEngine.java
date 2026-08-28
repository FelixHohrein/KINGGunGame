package de.payne.gungame.kopfgeld;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.payne.gungame.GunGame;


public final class KopfgeldEngine {

	
	
	public void openKopfgeldInventory(final Player player) {
		this.checkDuration();
		KopfgeldInventory kopfgeldInventory = new KopfgeldInventory();
		player.openInventory(kopfgeldInventory.createKopfgeld‹bersichtInventory());
	}
	
	
	
	private void checkDuration() {
		if(Kopfgeld.getKopfgelder().isEmpty()) {
			return;
		}

		for(Kopfgeld kopfgelder : Kopfgeld.getKopfgelder().values()) {
			if(kopfgelder.getRemainingTimeInSeconds() <= 0) {
				Kopfgeld.getKopfgelder().remove(kopfgelder.getId());
				GunGame.getInstance().getStatisticTable().deleteKopfgeld(kopfgelder);
				
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(kopfgelder.getTargetUUID());
				if(offlinePlayer.isOnline()) {
					Player player = Bukkit.getPlayer(kopfgelder.getTargetUUID());
					GunGame.getInstance().getScoreboardManager().updateBoard(player);
				}
			}
		}	
	}
}
