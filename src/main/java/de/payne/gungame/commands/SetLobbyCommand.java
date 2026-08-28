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

public class SetLobbyCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.admin.setlobby")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		//returns if syntax error
		if(arguments.length != 1) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
			
			GunGame.getInstance().getMapConfig().registerMap("Lobby", player.getLocation(), arguments[0], false, false, "kreis", 500, 0, 0);
			
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_SETLOBBY_SUCCESS, player.getUniqueId(), true));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_SETLOBBY_SUCCESS_NAME, player.getUniqueId(), true), "#NAME#", "Lobby"));
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_SETLOBBY_SUCCESS_BUILDER, player.getUniqueId(), true), "#BUILDER#", arguments[0]));
		
		return true;
	}
	
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("setlobby")) { 
        	final List<String> argList = Lists.newArrayList();
            
            if (args.length == 1 && sender.hasPermission("gungame.admin.setlobby")) {
            	for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            		argList.add(onlinePlayer.getName());
            	}
                return argList;
            }
            else if(args.length >= 2) {
            	argList.add("");
            	return argList;
            }
         }
        return null;
    }
}
