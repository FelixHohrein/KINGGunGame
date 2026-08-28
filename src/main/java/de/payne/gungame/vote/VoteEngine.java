package de.payne.gungame.vote;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import de.payne.gungame.signs.SignPhase;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;



public class VoteEngine {	

	public final void playerVote(final Player player, final String mapName) {
		//WENN KEIN AKTIVES VOTE VERFAHREN
		if(!GunGame.getInstance().getVoteHandler().isVoteVerfahrenStarted()) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_NOT_AKTIVE, player.getUniqueId(), true));
			return;
		}
//		WENN PLAYER BEREITS GEVOTED HAT
		if(GunGame.getInstance().getVoteHandler().getAlreadyVotedPlayers().containsKey(player)) {
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_ALREADY_VOTED, player.getUniqueId(), true), "#MAPNAME#", GunGame.getInstance().getVoteHandler().getAlreadyVotedPlayers().get(player).getMapname()));
			GunGame.getInstance().getVoteHandler().getAllMapsAndVotes(player);
			return;
		}
		
		// ADS A VOTE FOR THE MAP AND LIST THE PLAYER AS ALREADY VOTED
		GunGame.getInstance().getVoteHandler().addVote(mapName);
		GunGame.getInstance().getVoteHandler().addPlayerVoted(player, mapName);
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_SUCCESFULL, player.getUniqueId(), true), "#MAPNAME#", GunGame.getInstance().getVoteHandler().getPlayerVotedFor(player).getMapname()));

	}
	
	
	

	public final void mapWechsel(final int sekunden, final Player player) {
		
		if(player != null) {
			if(!GunGame.getInstance().getVoteHandler().isVoteVerfahrenStarted()) {
				if(GunGame.getInstance().getVoteHandler().hasCooldown(player)) {
					player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_COOLDOWN, player.getUniqueId(), true), "#ZEIT#", GunGame.getInstance().getVoteHandler().getCooldown(player).getRemainingTime()));
					return;
				}
			}
			GunGame.getInstance().getVoteHandler().addCooldown(player);
		}
		
		
		if(player != null) {
//			FOR PAPER
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_STARTED, player.getUniqueId(), true), "#PLAYER#", player.getName()));
				p.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_STARTED2, p.getUniqueId(), true));
			}
		}
		
		String allMaps = "";
		for(VotableMap vMap : GunGame.getInstance().getVoteHandler().getVotableMaps()) {
			allMaps += vMap.getMapname() + ", ";
		}
		Bukkit.getServer().sendMessage(Component.text(GunGame.getInstance().getPrefix() + ChatColor.GRAY + "Maps: " + ChatColor.GOLD + allMaps));
		
		new BukkitRunnable() {
			
			int countdown = sekunden;
			
			String verbleibendeZeit;
			String chatAusgabe;
			
			@Override
			public void run() {
				if(countdown > 60) {
					verbleibendeZeit = String.format("%02d:%02d", (countdown / 60),(countdown % 60));
					chatAusgabe = verbleibendeZeit + " Minuten.";
				} else {
					verbleibendeZeit = String.valueOf(countdown);
					chatAusgabe = verbleibendeZeit + " Sekunden.";
				}
				
				GunGame.getInstance().getVoteHandler().setVoteVerfahrenStarted(true);
				GunGame.getInstance().getVoteHandler().setVoteable(true);

				if(countdown % 30 == 0 && countdown != 0) {
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_MAPCHANGE_IN_SEC, p.getUniqueId(), true), "#ZEIT#", chatAusgabe));
					}
				}
				else if(countdown == 5) {
					
					GunGame.getInstance().getVoteHandler().setVoteable(false);
					
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_CLOSED, p.getUniqueId(), true));
					}
					
					VotableMap winMap = GunGame.getInstance().getVoteHandler().getVotableMap(GunGame.getInstance().getCurrentMap().getMapname());
					for(VotableMap vMap : GunGame.getInstance().getVoteHandler().getVotableMaps()) {
						if(vMap.getVotes() > winMap.getVotes()) {
							winMap = vMap;
						}
					}
					
					if (winMap == GunGame.getInstance().getVoteHandler().getVotableMap(GunGame.getInstance().getCurrentMap().getMapname())) {
						
						for(Player all : Bukkit.getOnlinePlayers()) {
							if(GunGame.getInstance().getVoteHandler().getAlreadyVotedPlayers().isEmpty()) {
								if(GunGame.getInstance().getIngameList().contains(all)) {
									all.sendTitle(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_NOBODY_VOTED_TITLE, all.getUniqueId(), false), GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_NOBODY_VOTED_SUBTITLE, all.getUniqueId(), false), 5, 40, 5);
								}else {
									all.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_NOBODY_VOTED_TITLE, all.getUniqueId(), true) + " " + GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_NOBODY_VOTED_SUBTITLE, all.getUniqueId(), false));
								}
							} else {
								if(!GunGame.getInstance().getIngameList().contains(all)) {
									all.sendTitle(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_NO_MAPCHANGE_TITLE, all.getUniqueId(), false), GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_NO_MAPCHANGE_SUBTITLE, all.getUniqueId(), false), 5, 40, 5);
								}else {
									all.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_NO_MAPCHANGE_TITLE, all.getUniqueId(), true) + " " + GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_NO_MAPCHANGE_SUBTITLE, all.getUniqueId(), false));
								}
							}

						}
						GunGame.getInstance().getVoteHandler().clearVotes();
						GunGame.getInstance().getVoteHandler().clearPlayerVoted();
						GunGame.getInstance().getVoteHandler().setVoteVerfahrenStarted(false);

						this.cancel();


					} else {
						for(Player all : GunGame.getInstance().getIngameList()) {
							
							all.sendTitle(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_MAPCHANGE_TITLE, all.getUniqueId(), false), "#ZEIT#", chatAusgabe), GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_MAPCHANGE_SUBTITLE, all.getUniqueId(), false), "#MAPNAME#", GunGame.getInstance().getCurrentMap().getMapname()), "#BUILDERNAME#", GunGame.getInstance().getCurrentMap().getBuilder()), 5, 20, 5);

						}
						GunGame.getInstance().getSignBuilder().signUpdatePhase(SignPhase.MAPCHANGE);
						GunGame.getInstance().setCurrentMap(winMap);
					}	
				}
				else if(countdown == 4 || countdown == 3 || countdown == 2 || countdown == 1) {
					for(Player all : GunGame.getInstance().getIngameList()) {
							
						all.sendTitle(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_MAPCHANGE_TITLE, all.getUniqueId(), false), "#ZEIT#", chatAusgabe), GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_MAPCHANGE_SUBTITLE, all.getUniqueId(), false), "#MAPNAME#", GunGame.getInstance().getCurrentMap().getMapname()), "#BUILDERNAME#", GunGame.getInstance().getCurrentMap().getBuilder()), 5, 20, 5);

					}				
				}
				else if(countdown == 0) {
					
					GunGame.getInstance().getSignBuilder().signUpdatePhase(SignPhase.ONLINE);
					GunGame.getInstance().getVoteHandler().setVoteVerfahrenStarted(false);
					GunGame.getInstance().getVoteHandler().getAlreadyVotedPlayers().clear();

					for(Player all : Bukkit.getOnlinePlayers()) {
						if(GunGame.getInstance().getIngameList().contains(all)) {
							all.teleport(GunGame.getInstance().getCurrentMap().getSpawnLocation());
							all.setHealth(20);
						}else {
							all.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.VOTE_MAPCHANGE, all.getUniqueId(), true), "#MAPNAME#", GunGame.getInstance().getCurrentMap().getMapname()));
						}

					}

					GunGame.getInstance().getVoteHandler().clearVotes();
										
					this.cancel();
				}
				countdown--;
			}	
		}.runTaskTimer(GunGame.getInstance(), 0, 20);
	}
	
}
