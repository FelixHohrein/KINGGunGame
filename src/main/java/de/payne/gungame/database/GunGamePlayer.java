package de.payne.gungame.database;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.gadgets.Gadgets;
import de.payne.gungame.language.LANGUAGE;
import lombok.Getter;
import lombok.Setter;


public final class GunGamePlayer {

	StatistikTable statistikTable = new StatistikTable(GunGame.getInstance().getMySqlDatabase());

	@Getter
	private UUID uuid;
	
	@Getter
	private int id;
	
	@Getter
	private String playerName, firstJoin;
	
	@Getter
	@Setter
	private double kd;
	
	@Getter
	@Setter
	private int tokens, kills, deaths, currentLevel, highestLevel;
	
	@Getter
	@Setter
	private List<Gadgets> Gadgets = Lists.newArrayList();
	
	@Getter
	@Setter
	private LANGUAGE language;

	
	
	public GunGamePlayer(UUID uuid) {
		Map<String, Integer> zwischenspeicher = statistikTable.results(uuid);

		this.id = zwischenspeicher.get("id");
		this.tokens = zwischenspeicher.get("tokens");
		this.kills = zwischenspeicher.get("kills");
		this.deaths = zwischenspeicher.get("deaths");
		this.currentLevel = zwischenspeicher.get("currentLevel");
		this.highestLevel = zwischenspeicher.get("highestLevel");
		this.kd = this.mathKD();
		this.playerName = Bukkit.getOfflinePlayer(uuid).getName(); //Bukkit.getPlayer(uuid).getName();
		this.firstJoin = statistikTable.getFirstJoin(uuid);
		this.Gadgets = statistikTable.getGadget(uuid);
		this.uuid = uuid;
		this.language = this.statistikTable.getLanguage(this.uuid);
	}
	

	public final double mathKD() {
		final double kills = this.kills;
		final double deaths = this.deaths;
			if (kills == 0 && deaths == 0) {
				return 0;
			}
			if (deaths == 0) {
				return kills;
			}	
		final double killDeathRate = Math.round((kills / deaths) * 100) / 100.0;
		return killDeathRate;
	}
}
