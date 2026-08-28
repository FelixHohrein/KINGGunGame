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
import net.kyori.adventure.text.Component;

public final class CreateRanking implements CommandExecutor, TabCompleter {
	
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.admin.createranking")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		//sends message alle votable maps + returnt die methode
		if(arguments.length != 1) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		int rang;
		if(arguments[0].equalsIgnoreCase("1") || arguments[0].equalsIgnoreCase("2") || arguments[0].equalsIgnoreCase("3")) {
			rang = Integer.parseInt(arguments[0]);
		} else {
			player.sendMessage(Component.text(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_INTEGER, player.getUniqueId(), true)));
			return true;
		}
		
		GunGame.getInstance().getSettings().setTopPlayersPosition(rang, player.getLocation());
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_CREATERANKING_SUCCESS, player.getUniqueId(), true), "#RANK#", String.valueOf(rang)));
				

		return true;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("createranking")) { 
			final List<String> tabComplete = Lists.newArrayList();
    		if(args.length == 1) {
    			tabComplete.add("<1|2|3>");
    			return tabComplete;
    		} else if(args.length > 1) {
    			tabComplete.add("");
    			return tabComplete;
    		}
    	}
        return null;
    }
}