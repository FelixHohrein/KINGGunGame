package de.payne.gungame.team;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public final class Team {

	@Getter
	private final static List<Team> teams = Lists.newArrayList();
	
	@Getter
	private final UUID uuid1, uuid2;
	@Getter
	private final Player player1, player2;
	
	
	
	public Team(final UUID uuid1, final UUID uuid2) {
		this.uuid1 = uuid1;
		this.uuid2 = uuid2;
		this.player1 = Bukkit.getPlayer(uuid1);
		this.player2 = Bukkit.getPlayer(uuid2);

		teams.add(this);
		
//		GunGame.getInstance().getScoreboardManager().updateBoard(player1);
//		GunGame.getInstance().getScoreboardManager().updateBoard(player2);
		
		GunGame.getInstance().getScoreboardManager().updateAllPlayers();
	}
	
	
	public void closeTeam() {
		player1.sendMessage(Component.text(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_DISSOLVED, player1.getUniqueId(), true)));
		player2.sendMessage(Component.text(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_DISSOLVED, player2.getUniqueId(), true)));
		teams.remove(this);
//		GunGame.getInstance().getScoreboardManager().updateBoard(player1);
//		GunGame.getInstance().getScoreboardManager().updateBoard(player2);

		GunGame.getInstance().getScoreboardManager().updateAllPlayers();

	}

	
	public static final Team getTeam(final Player player) {
		for(Team team : teams) {
			if(team.player1.equals(player) || team.player2.equals(player)) {
				return team;
			}
		}
		return null;
	}
	
	public static final boolean hasTeam(final Player player) {
		
		if(getTeam(player) == null) {
			return false;
		} else {
			return true;
		}
		
	}
}
