package de.payne.gungame.kopfgeld;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import lombok.Getter;

public class Kopfgeld {

	@Getter
	private static Map<Integer, Kopfgeld> kopfgelder = Maps.newHashMap();
	
	@Getter
	private final OfflinePlayer sender, target;
	@Getter
	private final UUID senderUUID, targetUUID;
	@Getter
	private final long timeStampInSeconds;
	@Getter
	private int amount, duration, id;
	
	
	//FROM COMMAND
	public Kopfgeld(final boolean fromCommand, final UUID sender, final UUID target, final int amount, final long timeStampInSeconds, final int duration, final int id) {
		
		if(fromCommand) {
			int lastDatabaseID = GunGame.getInstance().getStatisticTable().getLastID();
			
			int mapID = 0;
			for(int i : kopfgelder.keySet()) {
				if(i > mapID) {
					mapID = i;
				}				
			}
			
			for(int i = 0; ; i++) {
				if(i > lastDatabaseID && i > mapID) {
					this.id = i;
					break;
				}
			}
//			this.id = GunGame.getInstance().getStatisticTable().getLastID() +1;
		} else {
			this.id = id;
		}
		this.senderUUID = sender;
		this.targetUUID = target;
		
		this.timeStampInSeconds = timeStampInSeconds;
		this.duration = duration;
		this.sender = Bukkit.getOfflinePlayer(senderUUID);
		this.target = Bukkit.getOfflinePlayer(targetUUID);
		
		this.amount = amount;
		
		kopfgelder.put(this.id, this);
	}

	
	//RETURNS THE REMAINING SECONDS AS INT
    public final int getRemainingTimeInSeconds() {
    	long curentTimeInSeconds = System.currentTimeMillis() / 1000;
    	
    	if(curentTimeInSeconds - this.timeStampInSeconds > 604800) {
    		return -1;
    	}

    	int verbleibendeZeit = this.duration - Math.round(curentTimeInSeconds - this.timeStampInSeconds);
    	return verbleibendeZeit;
    }
	
    
    //RETURNS A STRING CONTAINS THE DURATION IN TEXT
	public final String getRemainingTime() {
		int remainingTime = this.getRemainingTimeInSeconds();
		
		String chatAusgabe = "";
		
		
		final int tage = remainingTime / 86400;
		final int stunden = (remainingTime % 86400) / 3600;
		final int minuten = (remainingTime % 3600) / 60;
		final int sekunden = (remainingTime % 3600) % 60;
				
		if(tage == 1) {
			chatAusgabe = " " + tage + " Tag";
		} else if (tage > 1) {
			chatAusgabe = " " + tage + " Tage";
		}
		if(stunden == 1) {
			chatAusgabe += " " + stunden + " Stunde";
		} else if (stunden > 1){
			chatAusgabe += " " + stunden + " Stunden";
		}
		if(minuten == 1) {
			chatAusgabe += " " + minuten + " Minute";
		} else if(minuten > 1) {
			chatAusgabe += " " + minuten + " Minuten";
		}
		if(sekunden == 1) {
			chatAusgabe += " " + sekunden + " Sekunde";
		} else if (sekunden > 1) {
			chatAusgabe += " " + sekunden + " Sekunden";
		}
		chatAusgabe += ".";

		return chatAusgabe;
	}
    
	public static final Kopfgeld getKopfgeldFromTarget(final Player target) {
		for(Kopfgeld kopfgeld : kopfgelder.values()) {
			if(target.equals(kopfgeld.getTarget())){
				return kopfgeld;
			}
		}
		return null;
	}
	
	public static final boolean isKopfgeldAusgesetzt(final Player target) {
		for(Kopfgeld kopfgeld : kopfgelder.values()) {
			if(target.equals(kopfgeld.getTarget())){
				return true;
			}
		}
		return false;
	}
	
	public static final int gesamtKopfgeldAusgesetzt(final Player player) {
		if(!isKopfgeldAusgesetzt(player)) {
			return 0;
		}
		
		int gesamtKopfgeld = 0;
		for(Kopfgeld kopfgeld : kopfgelder.values()) {
			if(player.equals(kopfgeld.getTarget())) {
				gesamtKopfgeld += kopfgeld.getAmount();
			}
		}
		return gesamtKopfgeld;
	}
	
	public static final Kopfgeld getKopfgeldFromSender(final Player sender) {
		for(Kopfgeld kopfgeld : kopfgelder.values()) {
			if(sender.equals(kopfgeld.getSender())){
				return kopfgeld;
			}
		}
		return null;
	}
}
