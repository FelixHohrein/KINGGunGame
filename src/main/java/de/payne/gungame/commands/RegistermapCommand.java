package de.payne.gungame.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import net.md_5.bungee.api.ChatColor;

public final class RegistermapCommand implements CommandExecutor, TabCompleter {

	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.admin.registermap")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		//returns if syntax error
		if(arguments.length != 8) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_ARGS, player.getUniqueId(), true));
			return true;
		}
		
		//return wenn die map bereits existiert
		if(GunGame.getInstance().getMapConfig().alreadyExists(arguments[0])) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_REGISTERMAP_MAP_ALREADY_EXIST, player.getUniqueId(), true));
			return true;
		}
		
		if((!GunGame.getInstance().isInteger(arguments[5])) || (!GunGame.getInstance().isInteger(arguments[6])) || (!GunGame.getInstance().isInteger(arguments[7]))) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_INTEGER, player.getUniqueId(), true));
			return true;
		}
			
			GunGame.getInstance().getMapConfig().registerMap(arguments[0], player.getLocation(), arguments[1], Boolean.parseBoolean(arguments[2]), Boolean.parseBoolean(arguments[3]), arguments[4], Integer.parseInt(arguments[5]), Integer.parseInt(arguments[6]), Integer.parseInt(arguments[7]) );	
			
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_REGISTERMAP_SUCCESS, player.getUniqueId(), true));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_REGISTERMAP_SUCCESS_NAME, player.getUniqueId(), true), "#NAME#", arguments[0]));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_REGISTERMAP_SUCCESS_BUILDER, player.getUniqueId(), true), "#BUILDER#", arguments[1]));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_REGISTERMAP_SUCCESS_VOTABLE, player.getUniqueId(), true), "#VOTABLE#", arguments[2]));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_REGISTERMAP_SUCCESS_TEAMS, player.getUniqueId(), true), "#TEAMS#", arguments[3]));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_REGISTERMAP_SUCCESS_PROTECTION, player.getUniqueId(), true), "#TYPE#", arguments[4]));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_REGISTERMAP_SUCCESS_RADIUS, player.getUniqueId(), true), "#RADIUS#", arguments[5]));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_REGISTERMAP_SUCCESS_X, player.getUniqueId(), true), "#ZAHL#", arguments[6]));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_REGISTERMAP_SUCCESS_Z, player.getUniqueId(), true), "#ZAHL#", arguments[7]));

		return true;
	}
	
//	/registermap <Map name> <Builders name> <votable[true/false]> <protectiontype[kreis/viereck]> <radius> <xAchse> <yAchse>";
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("registermap")) { 
        	final List<String> argList = Lists.newArrayList();
            if(sender.hasPermission("gungame.admin.registermap")) {
            	
                if (args.length == 1) {
                	argList.add("<MapName>");
                    return argList;
                }
                else if(args.length == 2) {
                	for(Player player : Bukkit.getOnlinePlayers()) {
                		argList.add(player.getName());
                	}
                	return argList;	
                }
                else if (args.length == 3) {
            		argList.add("true");
            		argList.add("false");
            		return argList;
                }
                else if (args.length == 4) {
            		argList.add("true");
            		argList.add("false");
            		return argList;
                }
                else if (args.length == 5) {
            		argList.add("kreis");
            		argList.add("viereck");
            		return argList;
                }
                else if (args.length == 6) {
            		argList.add("<Radius> int - null bei viereck");
            		return argList;
                }
                else if (args.length == 7) {
            		argList.add("<X-Achse> int - null bei kreis");
            		return argList;
                }
                else if (args.length == 8) {
            		argList.add("<Y-Achse> int - null bei kreis");
            		return argList;
                }
                else if (args.length >= 9) {
            		argList.add("");
            		return argList;
                }
            }
         }
        return null;
    }
}
