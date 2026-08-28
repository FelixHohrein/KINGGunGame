package de.payne.gungame.commands;

import java.util.List;

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

public final class DeleteMapCommand implements CommandExecutor, TabCompleter {

	
	//Fehler Chatausgaben
//	final private String permission = GunGame.getInstance().getPrefix() + ChatColor.RED + "Fehler! " + ChatColor.GRAY + "Du hast keine Berechtigung für diesen Befehl";
//	final private String syntax = GunGame.getInstance().getPrefix() + ChatColor.RED + "Fehler! " + ChatColor.GRAY + "Syntax: /deletemap <Map name>";

	
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.admin.deletemap")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		//returns if syntax error
		if(arguments.length != 1) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		//return wenn die map bereits existiert
		if(!GunGame.getInstance().getMapConfig().alreadyExists(arguments[0])) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_DELETEMAP_MAP_NOT_EXISTS, player.getUniqueId(), true));
			
			int zähler = 1;
			for(GameMap VotableMaps : GunGame.getInstance().getMapConfig().getVotableMaps()) {
				player.sendMessage(GunGame.getInstance().getPrefix() + zähler + ": " + ChatColor.AQUA + VotableMaps.getMapname() + ChatColor.GRAY + " - " + ChatColor.AQUA + VotableMaps.getBuilder());
				zähler++;
			}
			return true;
		}
			GunGame.getInstance().getMapConfig().deleteMap(arguments[0]);
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_DELETEMAP_SUCCESS, player.getUniqueId(), true));
			
		return true;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("deletemap")) { 
        	final List<String> argList = Lists.newArrayList();
            if(sender.hasPermission("gungame.admin.deletemap")) {
            	
                if (args.length == 1) {
                	for(GameMap gameMap : GunGame.getInstance().getMapConfig().getGameMaps()){
                		argList.add(gameMap.getMapname());
                	}
                    return argList;
                }
                else if(args.length >= 2) {
            		argList.add("");
            		return argList;
                }
            }
         }
        return null;
    }
}
