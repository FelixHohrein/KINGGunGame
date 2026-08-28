package de.payne.gungame.vote;


import org.bukkit.entity.Player;


import de.payne.gungame.GunGame;
import lombok.Getter;

public class AktiveCooldowns {

	
	@Getter
	private Player player;
	
	private long timeStampInSeconds;
	private int duration;
	
	
	public AktiveCooldowns(final Player player) {
		
		this.player = GunGame.getInstance().getVoteHandler().isPerPlayerCooldown() ? player : null;
		this.timeStampInSeconds = Math.round(System.currentTimeMillis() / 1000);
		this.duration = GunGame.getInstance().getSettings().getVoteDelay();
		
		
		
	}
	
	//RETURNS THE REMAINING SECONDS AS INT
    private final int getRemainingTimeInSeconds() {
    	long curentTimeInSeconds = System.currentTimeMillis() / 1000;
    	int verbleibendeZeit = this.duration - Math.round(curentTimeInSeconds - this.timeStampInSeconds);
    	
    	if(verbleibendeZeit <= 1) {
    		GunGame.getInstance().getVoteHandler().removeCooldown(this);
    	}
    	
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
	
}
