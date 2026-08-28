package de.payne.gungame.commands;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import de.payne.gungame.team.Team;
import net.kyori.adventure.text.Component;

public class TeamCommand implements CommandExecutor, TabCompleter {

	
	private static Map<Player, Player> offeneAnfragen = Maps.newHashMap();
	

//	private final void sendSyntax(final Player player) {
//		player.sendMessage(GunGame.getInstance().getPrefix() + ChatColor.RED + "Fehler! " + ChatColor.GRAY + "Syntax:");
//		player.sendMessage(GunGame.getInstance().getPrefix() + ChatColor.GOLD + "/team einladen <Spieler>" + ChatColor.GRAY + " lade einen anderen Spieler in dein Team ein");
//		player.sendMessage(GunGame.getInstance().getPrefix() + ChatColor.GOLD + "/team aufheben" + ChatColor.GRAY + " habe dein Team auf.");
//		player.sendMessage(GunGame.getInstance().getPrefix() + ChatColor.GOLD + "/team annehmen" + ChatColor.GRAY + " nehme eine Teamanfrage an.");
//
//	}

	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		// returns if sender isnt player
		if (!(commandSender instanceof Player)) {
			return true;
		}

		final Player player = (Player) commandSender;

		// returns if no permission
		if (!player.hasPermission("gungame.user.team")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}

		// returns if syntax error
		if (arguments.length != 1 && arguments.length != 2 ) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		

		if (arguments[0].equalsIgnoreCase("annehmen")) {
			
			if(Team.hasTeam(player)) {
				player.sendMessage(Component.text(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_ALREADY_IN_TEAM, player.getUniqueId(), true)));
			} else {
				
				Player anfragenderSpieler = null;
				for(Player p : offeneAnfragen.keySet()) {
					if(offeneAnfragen.get(p).equals(player)) {
						anfragenderSpieler = p;
					}
				}
				
				if(anfragenderSpieler != null) {
					anfragenderSpieler.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_SUCESSFULL, anfragenderSpieler.getUniqueId(), true), "#PLAYER#", player.getName()));
					player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_SUCESSFULL, player.getUniqueId(), true), "#PLAYER#", anfragenderSpieler.getName()));
					
					new Team(anfragenderSpieler.getUniqueId(), player.getUniqueId());
					offeneAnfragen.remove(anfragenderSpieler);
				} else {
					player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_NO_REQUEST, player.getUniqueId(), true));
				}
			}

			return true;
		}
		else if (arguments[0].equalsIgnoreCase("einladen")) {
			Player target = Bukkit.getPlayer(arguments[1]);
			if(player.equals(target)) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_NOT_WITH_YOURSELF, player.getUniqueId(), true));
				return true;
			}
			else if(target == null) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_PLAYER_NOT_EXIST, player.getUniqueId(), true));
				return true;
			}
			else if (Team.hasTeam(target)) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_TARGET_ALREADY_IN_TEAM, player.getUniqueId(), true));
				return true;
			}
			else if (Team.hasTeam(player)) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_ALREADY_IN_TEAM, player.getUniqueId(), true));
				return true;
			}
			else if(offeneAnfragen.containsKey(player)) {
				player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_ALREADY_AN_OPEN_REQUEST, player.getUniqueId(), true), "#PLAYER#", offeneAnfragen.get(player).getName()));
				return true;
			}
			else if (offeneAnfragen.containsValue(target)) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_ALREADY_AN_OPEN_REQUEST, player.getUniqueId(), true));
				return true;
			}
			
			if(!GunGame.getInstance().getCurrentMap().getTeamsErlaubt()) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_NOT_ALLOWED_ON_THIS_MAP, player.getUniqueId(), true));
				return true;
			} else {
				offeneAnfragen.put(player, target);
				this.anfragenTimeout(player, 30);
				target.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_REQUEST, player.getUniqueId(), true), "#PLAYER#", player.getName()));
				player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_REQUEST_SEND, player.getUniqueId(), true), "#PLAYER#", target.getName()));
				return true;
			}
			
		}
		else if (arguments[0].equalsIgnoreCase("aufheben")) {
			if(!Team.hasTeam(player)) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.TEAM_PLAYER_NO_TEAM, player.getUniqueId(), true));
			} else {
				Team.getTeam(player).closeTeam();
			}
		}

			
		return true;
	}
	
	private void anfragenTimeout(Player player, int timer) {
		new BukkitRunnable() {
			int countdown = timer;

			@Override
			public void run() {
				
				
				if(countdown == 0) {
					if(offeneAnfragen.containsKey(player)) {
						offeneAnfragen.remove(player);
					}
					this.cancel();
				}
				
				countdown--;
			}
			
		}.runTaskTimer(GunGame.getInstance(), 0, 20);
		
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("team")) { 
        	final List<String> argList = Lists.newArrayList();
            
        	if(sender.hasPermission("gungame.user.team")) {
        		
                if (args.length == 1) {
                	argList.add("einladen");
                	argList.add("annehmen");
                	argList.add("aufheben");
                    return argList;
                }
                else if (args.length == 2) {
                	//WENN ER /BUILD LIST DANACH DANN NULL
                	if(args[0].equalsIgnoreCase("einladen")){
                		for(Player p : Bukkit.getOnlinePlayers()) {
                			argList.add(p.getName());
                		}
                		return argList;
                	} else {
                		argList.add("");
                		return argList;
                	}
                }
                else if(args.length >= 3) {
            		argList.add("");
            		return argList;
                }
        	}

         }
        return null;
    }
}
