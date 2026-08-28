package de.payne.gungame.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import de.payne.gungame.map.GameMap;
import de.payne.gungame.signs.SignPhase;
import de.payne.gungame.vote.VotableMap;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class ForcemapCommand implements CommandExecutor, TabCompleter {

		
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.admin.forcemap")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		//returns mit allen votable maps aus maps.yml
		if(arguments.length == 0) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_FORCEMAP_ALLMAPS, player.getUniqueId(), true));
			int zähler = 1;
			for(GameMap VotableMaps : GunGame.getInstance().getMapConfig().getVotableMaps()) {
				player.sendMessage(GunGame.getInstance().getPrefix() + zähler + ": " + ChatColor.AQUA + VotableMaps.getMapname() + ChatColor.GRAY + " - " + ChatColor.AQUA + VotableMaps.getBuilder());
				zähler++;
			}
			return true;
		}
		
		//returns if syntax fehler
		if(arguments.length > 1) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		//returns if the arguements are null
		if(!GunGame.getInstance().getMapConfig().alreadyExists(arguments[0])) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_ARGS, player.getUniqueId(), true));
			return true;
		}

		if(arguments[0].equalsIgnoreCase(GunGame.getInstance().getCurrentMap().getMapname())) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_FORCEMAP_FEHLER_CURRENTLY_PLAYED, player.getUniqueId(), true));
			return true;
		}

		player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_FORCEMAP_MAPCHANGE_SOON, player.getUniqueId(), true));
		
		new BukkitRunnable() {

			int countdown = 15;
			
			@Override
			public void run() {
				
				if(countdown == 10) {
					Bukkit.getServer().sendMessage(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_FORCEMAP_SUCCESS, player.getUniqueId(), true), "#SENDER#", player.getName()), "#MAPNAME#", arguments[0])));
				}
				else if (countdown == 5) {
					GunGame.getInstance().getSignBuilder().signUpdatePhase(SignPhase.MAPCHANGE);
					
					GunGame.getInstance().setCurrentMap(GunGame.getInstance().getMapConfig().getGameMap(arguments[0]));
				}
				
				else if(countdown == 4 || countdown == 3 || countdown == 2 || countdown == 1) {
					for(Player all : GunGame.getInstance().getIngameList()) {
						all.sendTitle(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_FORCEMAP_TITLE, player.getUniqueId(), false), "#ZEIT#", String.valueOf(countdown)), GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_FORCEMAP_SUBTITLE, player.getUniqueId(), false), "#MAPNAME#", GunGame.getInstance().getCurrentMap().getMapname()), "#BUILDERNAME#", GunGame.getInstance().getCurrentMap().getBuilder()), 5, 20, 5);// in ticks 20 = 1 sekunde
					}				
				}
				
				
				if(countdown ==0) {
										
					for(Player all : Bukkit.getOnlinePlayers()) {
						if(GunGame.getInstance().getIngameList().contains(all)) {
							all.teleport(GunGame.getInstance().getCurrentMap().getSpawnLocation());
							all.setHealth(20);
						}else {
							all.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_FORCEMAP_DONE, player.getUniqueId(), true), "#MAPNAME#", GunGame.getInstance().getCurrentMap().getMapname()));
						}

					}	
					GunGame.getInstance().getSignBuilder().signUpdatePhase(SignPhase.ONLINE);
					this.cancel();
				}

				countdown--;
			}

		}.runTaskTimer(GunGame.getInstance(), 0, 20);
		
		return true;
	}

	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("forcemap")) { 
        	final List<String> argList = Lists.newArrayList();
            
            if (args.length == 1 && sender.hasPermission("gungame.admin.forcemap")) {
            	for(VotableMap votemaps : GunGame.getInstance().getVoteHandler().getVotableMaps()) {
            		argList.add(votemaps.getMapname());
            	}

                return argList;
            }
            else if (args.length >= 2) {
            	argList.add("");
            	return argList;
            }
         }
        return null;
    }
}
