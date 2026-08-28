package de.payne.gungame.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

public class SetBuffLocation implements CommandExecutor, TabCompleter {

	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.admin.setbufflocation")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		//returns if syntax error
		if(arguments.length != 2) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		if(!(GunGame.getInstance().isInteger(arguments[1]))) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_INTEGER, player.getUniqueId(), true));
			return true;
		}
		
		final Location location = player.getLocation().clone().subtract(0, 1, 0);
		final Block block = location.getBlock();

		if(block.getType() != Material.OBSIDIAN) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_SETBUFFLOCATION_OBSIDIAN, player.getUniqueId(), true));
			return true;
		}
		if(!GunGame.getInstance().getMapConfig().alreadyExists(arguments[0])) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_SETBUFFLOCATION_MAP_NOT_EXIST, player.getUniqueId(), true));
			return true;
		}
		GameMap map = null;
    	for(GameMap gameMap : GunGame.getInstance().getMapConfig().getGameMaps()){
    		if(gameMap.getSpawnLocation().getWorld().equals(player.getLocation().getWorld()) ) {
    			map = gameMap;
    		}
    	}
    	if(map == null) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_SETBUFFLOCATION_NOT_ON_MAP, player.getUniqueId(), true));
    		return true;
    	}
			GunGame.getInstance().getBuffLocationConfig().addBuffPosition(GunGame.getInstance().getMapConfig().getGameMap(arguments[0]), block.getLocation(), Integer.parseInt(arguments[1]));
			
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_SETBUFFLOCATION_SUCCESS, player.getUniqueId(), true));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_SETBUFFLOCATION_SUCCESS_NAME, player.getUniqueId(), true), "#NAME#", arguments[0]));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_SETBUFFLOCATION_SUCCESS_LOCATIONID, player.getUniqueId(), true), "#ID#", arguments[1]));
		
		return true;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("setbufflocation")) { 
        	final List<String> argList = Lists.newArrayList();
        	final Player player = (Player) sender;
            if(sender.hasPermission("gungame.admin.setbufflocation")) {
            	
                if (args.length == 1) {
                	for(GameMap gameMap : GunGame.getInstance().getMapConfig().getGameMaps()){
                		if(gameMap.getSpawnLocation().getWorld().equals(player.getLocation().getWorld()) ) {
                			argList.add(gameMap.getMapname());
                		}
                	}
                    return argList;

                }
                else if(args.length == 2) {
                	argList.add("Location ID (int)");
                	return argList;	
                }
                else if (args.length >= 3) {
            		argList.add("");
            		return argList;
                }
            }
         }
        return null;
    }
}
