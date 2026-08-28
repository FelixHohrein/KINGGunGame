package de.payne.gungame.scoreboard;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import de.payne.gungame.kopfgeld.Kopfgeld;
import de.payne.gungame.language.MESSAGE;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public final class ScorboardManager {

	@Getter
	private final Map<Player, Scoreboard> boards = Maps.newHashMap();
	
	
	
	public final void updateAllPlayers() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			this.updateBoard(p);
		}
	}
	
	public final void updateBoard(final Player player) {
					
		final int kills = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getKills();
		final int deaths = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getDeaths();
		GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setKd(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).mathKD());
		final double kd = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getKd();
					
		final int rank = GunGame.getInstance().getStatisticTable().getRank(player.getUniqueId());
		final int vonRank = GunGame.getInstance().getStatisticTable().getSize();
					
		final int tokens = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getTokens();
		final int highestlevel = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getHighestLevel();
					
		final String currentMapName = GunGame.getInstance().getCurrentMap().getMapname();

//		NEW FOR PAPER
		this.boards.get(player).getTeam("currentmap").suffix(Component.text(ChatColor.GOLD + " " + currentMapName));
		this.boards.get(player).getTeam("tokens").suffix(Component.text(ChatColor.GOLD + " " + tokens));
		this.boards.get(player).getTeam("highestLevel").suffix(Component.text(ChatColor.GOLD + " " + highestlevel));
		this.boards.get(player).getTeam("kills").suffix(Component.text(ChatColor.GOLD + " " + kills));
		this.boards.get(player).getTeam("deaths").suffix(Component.text(ChatColor.GOLD + " " + deaths));
		this.boards.get(player).getTeam("kd").suffix(Component.text(ChatColor.GOLD + " " + kd));
		this.boards.get(player).getTeam("rank").suffix(Component.text(ChatColor.GOLD + " " + rank + ChatColor.GRAY + " von " + ChatColor.GOLD + vonRank));
		
		if(GunGame.getInstance().getCurrentMap().getTeamsErlaubt()) {
			this.boards.get(player).getTeam("teamserlaubt").suffix(Component.text(ChatColor.GREEN + " Ja"));
		} else {
			this.boards.get(player).getTeam("teamserlaubt").suffix(Component.text(ChatColor.RED + " Nein"));
		}
		
		if(Kopfgeld.isKopfgeldAusgesetzt(player)){
			this.boards.get(player).getTeam("kopfgeldA").suffix(Component.text(ChatColor.RED + " " + Kopfgeld.gesamtKopfgeldAusgesetzt(player)));
		} else {
			this.boards.get(player).getTeam("kopfgeldA").suffix(Component.text(ChatColor.GREEN + " 0"));
		}
		
		this.boards.get(player).getObjective(DisplaySlot.BELOW_NAME).displayName(Component.text(ChatColor.RED + "\u2764"));
	    
//		
//		BELOW NAME DISPLAY SLOT UPDATE USED BY TEAMS
//		
		
//		de.payne.gungame.team.Team team = de.payne.gungame.team.Team.hasTeam(player) ? de.payne.gungame.team.Team.getTeam(player) : null;
//
//		if(team != null) {
//			
//			if (team.getPlayer1().equals(player)) {
////				this.boards.get(player).getObjective("kgmg_GG-Team1").displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer1().getName()));
//				this.boards.get(player).getTeam("objTeam").displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer1().getName()));
//			
//			} else if (team.getPlayer2().equals(player)) {
////				this.boards.get(player).getObjective("kgmg_GG-Team1").displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer2().getName()));
//				this.boards.get(player).getTeam("objTeam").displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer2().getName()));
//
//			}
//		} else {
////			this.boards.get(player).getObjective("kgmg_GG-Team1").displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Kein Team"));
//			this.boards.get(player).getTeam("objTeam").displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Kein Team"));
//		}
		
		
//		if(de.payne.gungame.team.Team.hasTeam(player)) {
//			
//			de.payne.gungame.team.Team team = de.payne.gungame.team.Team.getTeam(player);
//			if(team.getPlayer1().equals(player)) {
//				this.boards.get(player).getObjective(DisplaySlot.BELOW_NAME).displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit "+ ChatColor.AQUA + team.getPlayer1().getName()));
//			} else if(team.getPlayer2().equals(player)) {
//				this.boards.get(player).getObjective(DisplaySlot.BELOW_NAME).displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit "+ ChatColor.AQUA + team.getPlayer2().getName()));
//			}
//		} else {
//			this.getBoards().get(player).getObjective(DisplaySlot.BELOW_NAME).displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Kein Team"));
//		}
	}
		
	
	//CREATES THE BOARD USED IN PLAYERJOIN
	public final void createBoard(final Player player) {
		
		final int kills = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getKills();
		final int deaths = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getDeaths();
		final double kd = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getKd();
		final int rank = GunGame.getInstance().getStatisticTable().getRank(player.getUniqueId());
		final int vonRank = GunGame.getInstance().getStatisticTable().getSize();
		final int tokens = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getTokens();
		final int highestlevel = GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getHighestLevel();
		final String currentMapName = GunGame.getInstance().getCurrentMap().getMapname();
		
	ScoreboardManager manager = Bukkit.getScoreboardManager();
	Scoreboard board = manager.getNewScoreboard();
	Objective objective = board.registerNewObjective("kgmg_GG-Stats", "dummy", Component.text(ChatColor.GOLD + "GunGame" + ChatColor.GRAY + "-" + ChatColor.GOLD + "Stats"));
	objective.setDisplaySlot(DisplaySlot.SIDEBAR);

	//CurrentMap
	Team currentMap= board.registerNewTeam("currentmap");
	currentMap.prefix(Component.text(ChatColor.GRAY + "Map:"));
	currentMap.suffix(Component.text(ChatColor.GOLD + " " + currentMapName));
	currentMap.addEntry(ChatColor.DARK_PURPLE.toString());
	objective.getScore(ChatColor.DARK_PURPLE.toString()).setScore(8);
	
	//TeamsErlaubt
	Team teamsErlaubt= board.registerNewTeam("teamserlaubt");
	teamsErlaubt.prefix(Component.text(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SCOREBOARD_TEAMS, player.getUniqueId(), false)));
	if(GunGame.getInstance().getCurrentMap().getTeamsErlaubt()) {
		teamsErlaubt.suffix(Component.text(ChatColor.GREEN + " Ja"));
	} else {
		teamsErlaubt.suffix(Component.text(ChatColor.RED + " Nein"));
	}
	teamsErlaubt.addEntry(ChatColor.ITALIC.toString());
	objective.getScore(ChatColor.ITALIC.toString()).setScore(7);
	
	//tokens
	Team tokense = board.registerNewTeam("tokens");
	tokense.prefix(Component.text(ChatColor.GRAY + "Tokens:"));
	tokense.suffix(Component.text(ChatColor.GOLD + " " + tokens));
	tokense.addEntry(ChatColor.AQUA.toString());
	objective.getScore(ChatColor.AQUA.toString()).setScore(6);
	
	//Höchstes Level
	Team highestLevel = board.registerNewTeam("highestLevel");
	
	highestLevel.prefix(Component.text(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SCOREBOARD_HIGHESTLEVEL, player.getUniqueId(), false)));
	highestLevel.suffix(Component.text(ChatColor.GOLD + " " + highestlevel));
	highestLevel.addEntry(ChatColor.BLACK.toString());
	objective.getScore(ChatColor.BLACK.toString()).setScore(5);
	
	//kills
	Team kiills = board.registerNewTeam("kills");
	kiills.prefix(Component.text(ChatColor.GRAY + "Kills:"));
	kiills.suffix(Component.text(ChatColor.GOLD + " " + kills));
	kiills.addEntry(ChatColor.BLUE.toString());
	objective.getScore(ChatColor.BLUE.toString()).setScore(4);
	
	//Deaths
	Team deathss = board.registerNewTeam("deaths");
	deathss.prefix(Component.text(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SCOREBOARD_DEATHS, player.getUniqueId(), false)));
	deathss.suffix(Component.text(ChatColor.GOLD + " " + deaths));
	deathss.addEntry(ChatColor.BOLD.toString());
	objective.getScore(ChatColor.BOLD.toString()).setScore(3);
	
	//kd
	Team kdr = board.registerNewTeam("kd");
	kdr.prefix(Component.text(ChatColor.GRAY + "K/D:"));
	kdr.suffix(Component.text(ChatColor.GOLD + " " + kd));
	kdr.addEntry(ChatColor.DARK_AQUA.toString());
	objective.getScore(ChatColor.DARK_AQUA.toString()).setScore(2);
	
	//rank
	Team rang = board.registerNewTeam("rank");
	rang.prefix(Component.text(ChatColor.GRAY + "Rang:"));
	rang.suffix(Component.text(ChatColor.GOLD + " " + rank + ChatColor.GRAY + " / " + ChatColor.GOLD + vonRank));
	rang.addEntry(ChatColor.WHITE.toString());
	objective.getScore(ChatColor.WHITE.toString()).setScore(1);
	
	//rank
	Team kopfgeld = board.registerNewTeam("kopfgeldA");
	kopfgeld.prefix(Component.text(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.SCOREBOARD_KOPFGELD, player.getUniqueId(), false)));
	if(Kopfgeld.isKopfgeldAusgesetzt(player)){
		kopfgeld.suffix(Component.text(ChatColor.RED + " " + Kopfgeld.gesamtKopfgeldAusgesetzt(player)));
	} else {
		kopfgeld.suffix(Component.text(ChatColor.GREEN + " 0"));
	}
	kopfgeld.addEntry(ChatColor.DARK_GRAY.toString());
	objective.getScore(ChatColor.DARK_GRAY.toString()).setScore(0);
	
	
	
	
//	---------------------------------------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------
	
	
    Objective objective2 = board.registerNewObjective("showhealth", Criterias.HEALTH, Component.text(ChatColor.RED + "\u2764"));
    objective2.setDisplaySlot(DisplaySlot.BELOW_NAME);
    
    
    
	
//	de.payne.gungame.team.Team team = de.payne.gungame.team.Team.hasTeam(player) ? de.payne.gungame.team.Team.getTeam(player) : null;
//	
//	Objective objective1 = board.registerNewObjective("kgmg_GG-Team1", "dummy", Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Kein Team"));
//	objective1.setDisplaySlot(DisplaySlot.BELOW_NAME);
//
//	Team obj1Team = board.registerNewTeam("objTeam");
//	obj1Team.displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Kein Team"));
//	
//	obj1Team.addEntry(ChatColor.BOLD + "" + ChatColor.GRAY + "Kein Team");
//	objective1.getScore(ChatColor.BOLD + "" + ChatColor.GRAY + "Kein Team").setScore(0);
//	
//	
//	if(team != null) {
//		
//		if (team.getPlayer1().equals(player)) {
////			objective1.displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer1().getName()));
//			obj1Team.addEntry(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer1().getName());
//			objective1.getScore(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer1().getName()).setScore(0);
//
//			obj1Team.displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer1().getName()));
//
//		} else if (team.getPlayer2().equals(player)) {
////			objective1.displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer2().getName()));
//			
//			obj1Team.addEntry(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer2().getName());
//			objective1.getScore(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer2().getName()).setScore(0);
//			
//			obj1Team.displayName(Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit " + ChatColor.AQUA + team.getPlayer2().getName()));
//
//		}
//	}

	//FOR TEAMS------------------------------------------------------------------
//	if(de.payne.gungame.team.Team.hasTeam(player)){
//		de.payne.gungame.team.Team team = de.payne.gungame.team.Team.getTeam(player);
//		
//		Objective objective1 = null;
//		
//		if(team.getPlayer1().equals(player)) {
//			objective1 = board.registerNewObjective("kgmg_GG-Team1", "dummy", Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit "+ ChatColor.AQUA + team.getPlayer1().getName()));
//		
//		} else if(team.getPlayer2().equals(player)) {
//			objective1 = board.registerNewObjective("kgmg_GG-Team1", "dummy", Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Verbündet mit "+ ChatColor.AQUA + team.getPlayer2().getName()));
//		}
//		objective1.setDisplaySlot(DisplaySlot.BELOW_NAME);
//	} else {
//		Objective objective1 = board.registerNewObjective("kgmg_GG-Team1", "dummy", Component.text(ChatColor.BOLD + "" + ChatColor.GRAY + "Kein Team"));
//		objective1.setDisplaySlot(DisplaySlot.BELOW_NAME);
//	}
	//----------------------------------------------------------------------------
	
	
	boards.put(player, board);
	player.setScoreboard(board);
	}
}
