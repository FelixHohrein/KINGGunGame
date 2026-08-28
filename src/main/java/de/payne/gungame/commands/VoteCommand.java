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
import de.payne.gungame.vote.VotableMap;
import net.md_5.bungee.api.ChatColor;

public class VoteCommand implements CommandExecutor, TabCompleter {

	
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.user.vote")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		//sends message keine abstimmung aktuell und returnt die methode
		if(!GunGame.getInstance().getVoteHandler().isVoteVerfahrenStarted()) {
			GunGame.getInstance().getVoteEngine().mapWechsel(GunGame.getInstance().getSettings().getVoteTime(), player);
			return true;
		} else {
			//sends message alle votable maps + returnt die methode
			if(arguments.length == 0) {
				GunGame.getInstance().getVoteHandler().getAllMapsAndVotes(player);
				return true;
			}
			
			//returns if syntax error
			if(arguments.length != 1) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
				return true;
			}
			
			//MAP NOT EXISTS
			if(GunGame.getInstance().getVoteHandler().getVotableMap(arguments[0]) == null) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
				return true;
			}
			
			GunGame.getInstance().getVoteEngine().playerVote(player, arguments[0]);
		}

		return true;
	}
	
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("vote")) { 
        	final List<String> argList = Lists.newArrayList();
            
            if (args.length == 1 && sender.hasPermission("gungame.user.vote")) {
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