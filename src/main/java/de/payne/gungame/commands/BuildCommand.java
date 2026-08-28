package de.payne.gungame.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import de.payne.gungame.map.GameMap;
import net.md_5.bungee.api.ChatColor;

public class BuildCommand implements CommandExecutor, TabCompleter {


//	private final void sendSyntax(final Player player) {
//		player.sendMessage(GunGame.getInstance().getPrefix() + ChatColor.RED + "Fehler! " + ChatColor.GRAY + "Syntax:");
//		player.sendMessage(GunGame.getInstance().getPrefix() + ChatColor.GOLD + "/build list" + ChatColor.GRAY + " zeigt dir alle Spieler im Bearbeitungsmodus");
//		player.sendMessage(GunGame.getInstance().getPrefix() + ChatColor.GOLD + "/build <Map name> <true|false>" + ChatColor.GRAY + " um den Bearbeitungsmodus zu aktivieren");
//	}
	
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.admin.build")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));

			return true;
		}
		
		//returns if syntax error
		if(arguments.length > 2 || arguments.length < 1) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		if(arguments[0].equalsIgnoreCase("list")) {
			if(GunGame.getInstance().getBuilders().size() == 0) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_BUILD_NO_PLAYERS_IN_LIST, player.getUniqueId(), true));

			}else {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_BUILD_PLAYERS_IN_LIST, player.getUniqueId(), true));
				String s = "";
				for(Player p : GunGame.getInstance().getBuilders()){
					s += ChatColor.GOLD + p.getName() + ChatColor.GRAY + ",";
				}
				player.sendMessage(GunGame.getInstance().getPrefix() + s);
			}

			return true;
		}
		
		//WENN ES DIE MAP NICHT GIBT
		if(!GunGame.getInstance().getMapConfig().alreadyExists(arguments[0])) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_BUILD_MAP_NOT_FOUND, player.getUniqueId(), true));
			
			int zähler = 1;
			for(GameMap allMaps : GunGame.getInstance().getMapConfig().getGameMaps()) {
				player.sendMessage(GunGame.getInstance().getPrefix() + zähler + ": " + ChatColor.AQUA + allMaps.getMapname() + ChatColor.GRAY + " - " + ChatColor.AQUA + allMaps.getBuilder());
				zähler++;
			}
			return true;
		}
		
		if(arguments.length == 1) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		if(player.getLocation().equals(GunGame.getInstance().getCurrentMap().getSpawnLocation())) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_BUILD_NOT_IN_GUNGAME, player.getUniqueId(), true));
			return true;
		}
		
		//WENN DIE MAP AKTUELL GESPIELT WIRD
		if(GunGame.getInstance().getCurrentMap().getMapname().equals(arguments[0])) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_BUILD_NOT_CURRENTMAP, player.getUniqueId(), true));
			return true;
		}
		
		//WENN ARG 2 = TRUE
		if(Boolean.parseBoolean(arguments[1])) {
			
			//WENN ES DIE LOBBY IST
			if(GunGame.getInstance().getLobbySpawn().getMapname().equals(arguments[0])) {
				player.teleport(GunGame.getInstance().getMapConfig().getGameMap(arguments[0]).getSpawnLocation());
				Bukkit.getScheduler().runTaskLater(GunGame.getInstance(), new Runnable() {
					@Override
					public void run() {
						player.setGameMode(GameMode.CREATIVE);						
					}
					
				}, 20);
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_BUILD_MODE_AKTIVATED, player.getUniqueId(), true));
				GunGame.getInstance().getBuilders().add(player);
				return true;
			} else {
				GunGame.getInstance().getMapConfig().getGameMap(arguments[0]).setVotable(false);
				GunGame.getInstance().getMapConfig().setVotable(arguments[0], false);
//				GunGame.getInstance().getVoteManager().updateVotableMaps();
				GunGame.getInstance().getVoteHandler().updateVotableMaps();
				
				player.teleport(GunGame.getInstance().getMapConfig().getGameMap(arguments[0]).getSpawnLocation());
				Bukkit.getScheduler().runTaskLater(GunGame.getInstance(), new Runnable() {
					@Override
					public void run() {
						player.setGameMode(GameMode.CREATIVE);						
					}
					
				}, 20);
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_BUILD_MODE_AKTIVATED, player.getUniqueId(), true));
				GunGame.getInstance().getBuilders().add(player);
				return true;
			}
		//WENN ARG 2 = FALSE
		} else {
			if(!GunGame.getInstance().getBuilders().contains(player)) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_BUILD_MODE_DISABLED, player.getUniqueId(), true));
				return true;
			}
			//WENN ES DIE LOBBY IST
			if(GunGame.getInstance().getLobbySpawn().getMapname().equals(arguments[0])) {
				player.teleport(GunGame.getInstance().getLobbySpawn().getSpawnLocation());
				player.setGameMode(GameMode.ADVENTURE);
				
				player.getInventory().clear();
				player.setLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
				
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_BUILD_MODE_DISABLED, player.getUniqueId(), true));
				GunGame.getInstance().getBuilders().remove(player);
				return true;
				
			} else {
				
				player.teleport(GunGame.getInstance().getLobbySpawn().getSpawnLocation());
				player.setGameMode(GameMode.ADVENTURE);
				
				player.getInventory().clear();
				player.setLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());

				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_BUILD_MODE_DISABLED, player.getUniqueId(), true));
				GunGame.getInstance().getBuilders().remove(player);
				
				if(GunGame.getInstance().getMapConfig().getGameMap(arguments[0]).getSpawnLocation().getWorld().getPlayers().size() == 0) {
					GunGame.getInstance().getMapConfig().getGameMap(arguments[0]).setVotable(true);
					GunGame.getInstance().getMapConfig().setVotable(arguments[0], true);
					GunGame.getInstance().getVoteHandler().updateVotableMaps();

					return true;
				}
				return true;
			}
		}
	}
	
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("build")) { 
        	final List<String> argList = Lists.newArrayList();
            
        	if(sender.hasPermission("gungame.admin.build")) {
        		
                if (args.length == 1) {
                	argList.add("list");
                	for(GameMap gameMap : GunGame.getInstance().getMapConfig().getGameMaps()) {
                		argList.add(gameMap.getMapname());
                	}
                    return argList;
                }
                else if (args.length == 2) {
                	//WENN ER /BUILD LIST DANACH DANN NULL
                	if(args[0].equalsIgnoreCase("list")){
                		argList.add("");
                		return argList;
                	} else {
                    	argList.add("true");
                    	argList.add("false");
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