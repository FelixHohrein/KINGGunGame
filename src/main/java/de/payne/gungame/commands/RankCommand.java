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

public class RankCommand implements CommandExecutor, TabCompleter {

	
	//Fehler Chatausgaben
//	private final String permission = GunGame.getInstance().getPrefix() + ChatColor.RED + "Fehler! " + ChatColor.GRAY + "Du hast keine Berechtigung für diesen Befehl";
//	private final String syntax = GunGame.getInstance().getPrefix() + ChatColor.RED + "Fehler! " + ChatColor.GRAY + "Syntax: /rank | /rank <Spielername>";
	
	private final void sendStats(final Player player, final Player target) {
		player.sendMessage(ChatColor.DARK_GRAY + "=============="+ ChatColor.DARK_AQUA + "[GunGame - Stats]" + ChatColor.DARK_GRAY +"==============");
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_RANK_SENDSTATS_NAME, player.getUniqueId(), true), "#PLAYER#", target.getName()));
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_RANK_SENDSTATS_RANK, player.getUniqueId(), true), "#RANK#", String.valueOf(GunGame.getInstance().getStatisticTable().getRank(target.getUniqueId()))), "#MAXRANK#", String.valueOf(GunGame.getInstance().getStatisticTable().getSize())));
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_RANK_SENDSTATS_TOKENS, player.getUniqueId(), true), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getTokens())));
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_RANK_SENDSTATS_CURRENTLEVEL, player.getUniqueId(), true), "#LEVEL#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel())));
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_RANK_SENDSTATS_HIGHESTLEVEL, player.getUniqueId(), true), "#LEVEL#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getHighestLevel())));
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_RANK_SENDSTATS_KILLS, player.getUniqueId(), true), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getKills())));
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_RANK_SENDSTATS_DEATHS, player.getUniqueId(), true), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getDeaths())));
		GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).setKd(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).mathKD());
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_RANK_SENDSTATS_KD, player.getUniqueId(), true), "#KD#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getKd())));

	
	}
	
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.user.rank")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		if(arguments.length != 0 && arguments.length != 1) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		Player target;
		
		if(arguments.length == 1) {
			if(Bukkit.getPlayer(arguments[0]) != null) {
				target = Bukkit.getPlayer(arguments[0]);
			} else {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_RANK_NOT_ONLINE, player.getUniqueId(), true));
				return true;
			}
		} else {
			target = player;
		}	
		
		GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).setKd(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).mathKD());
		this.sendStats(player, target);
		return true;
	}
	
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("rank") || cmd.getName().equalsIgnoreCase("stats")) { 
        	final List<String> argList = Lists.newArrayList();
            
            if (args.length == 1 && sender.hasPermission("gungame.user.rank")) {
            	for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            		argList.add(onlinePlayer.getName());
            	}
                return argList;
            } 
            else if( args.length >= 2) {
            	argList.add("");
            	return argList;
            }
         }
        return null;
    }
}