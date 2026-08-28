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
import net.md_5.bungee.api.ChatColor;

public class SetShopCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.admin.setshop")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		//sends message alle votable maps + returnt die methode
		if(arguments.length != 0) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		GunGame.getInstance().getShopNpc().createVillager(player.getLocation());
		GunGame.getInstance().getSettings().setShopPosition(player.getLocation());
		
		player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_SETSHOP_SUCCESS, player.getUniqueId(), true));
				
		return true;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
		if(cmd.getName().equalsIgnoreCase("setshop")) {
			if(args.length >= 1) {
		    	   final List<String> tabComplete = Lists.newArrayList();
		    	   tabComplete.add("");
		    	   return tabComplete;
			}

       }
		return null;
    }
}