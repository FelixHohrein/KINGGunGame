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

public final class HelpCommand implements CommandExecutor, TabCompleter {

	//Help User Chatausgabe
	private final void sendUserHelp(final Player player) { 
		player.sendMessage(ChatColor.DARK_GRAY + "=============="+ ChatColor.DARK_AQUA + "[KING-GunGame]" + ChatColor.DARK_GRAY +"==============");
		player.sendMessage(ChatColor.DARK_AQUA + "[Allgemeines]" + ChatColor.DARK_GRAY +"==============================");
		player.sendMessage(ChatColor.GOLD + "/help"+ ChatColor.GRAY + " - zeigt dir alle Befehle.");
		player.sendMessage(ChatColor.GOLD + "/rank"+ ChatColor.GRAY + " - zeige die platzierung im Ranking.");
		player.sendMessage(ChatColor.GOLD + "/leave"+ ChatColor.GRAY + " - bringt dich aus dem Game in die Lobby.");

		player.sendMessage(ChatColor.DARK_AQUA + "[Game]" + ChatColor.DARK_GRAY +"====================================");
		player.sendMessage(ChatColor.GOLD + "/vote"+ ChatColor.GRAY + " - vote für eine Map während einer Abstimmung.");
		player.sendMessage(ChatColor.GOLD + "/votemap start"+ ChatColor.GRAY + " - starte das votemap Verfahren.");
		
		player.sendMessage(ChatColor.DARK_AQUA + "[Credits]" + ChatColor.DARK_GRAY +"==================================");
		player.sendMessage(ChatColor.GRAY + "made by " + ChatColor.GOLD + "Felix Payne"+ ChatColor.GRAY + " visit KING-Gaming.eu on ts3.");

		player.sendMessage(ChatColor.DARK_GRAY + "==========================================");
	}
	
	//Admin help Chatausgabe
	private final void sendAdminHelp(final Player player) {
		player.sendMessage(ChatColor.DARK_GRAY + "=============="+ ChatColor.DARK_AQUA + "[KING-GunGame]" + ChatColor.DARK_GRAY +"==============");
		player.sendMessage(ChatColor.DARK_AQUA + "[Allgemeines]" + ChatColor.DARK_GRAY +"=============================");
		player.sendMessage(ChatColor.GOLD + "/help"+ ChatColor.GRAY + " - zeigt dir alle Befehle.");
		player.sendMessage(ChatColor.GOLD + "/leave"+ ChatColor.GRAY + " - bringt dich aus dem Game in die Lobby.");
		player.sendMessage(ChatColor.GOLD + "/rank"+ ChatColor.GRAY + " - zeige die platzierung im Ranking");

		
		player.sendMessage(ChatColor.DARK_AQUA + "[Setup]" + ChatColor.DARK_GRAY +"===================================");
		player.sendMessage(ChatColor.GOLD + "/setlobby"+ ChatColor.GRAY + " - setzt den Lobby Spawn punkt.");
		player.sendMessage(ChatColor.GOLD + "/registermap"+ ChatColor.GRAY + " - setzt den spawnpunkt für die Game Maps.");
		player.sendMessage(ChatColor.GOLD + "/setbufflocation"+ ChatColor.GRAY + " - setzt die Bufflocations (pro GameMap zu setzen).");
		player.sendMessage(ChatColor.GOLD + "/createranking"+ ChatColor.GRAY + " - erstellt das Ranking.");
		player.sendMessage(ChatColor.GOLD + "/setshop"+ ChatColor.GRAY + " - erstellt den shop.");
		player.sendMessage(ChatColor.GOLD + "/deletemap"+ ChatColor.GRAY + " - löscht eine beliebige Map (Nur für das Plugin)");
		player.sendMessage(ChatColor.GOLD + "/build"+ ChatColor.GRAY + " - bringt dich in den builder modus.");

		
		player.sendMessage(ChatColor.DARK_AQUA + "[Game]" + ChatColor.DARK_GRAY +"====================================");
		player.sendMessage(ChatColor.GOLD + "/votemap"+ ChatColor.GRAY + " - starte eine Abstimmung.");
		player.sendMessage(ChatColor.GOLD + "/vote"+ ChatColor.GRAY + " - vote für eine Map während einer Abstimmung.");
		player.sendMessage(ChatColor.GOLD + "/forcemap"+ ChatColor.GRAY + " - bestimme die nächste Map.");
		player.sendMessage(ChatColor.GOLD + "/adminmenu"+ ChatColor.GRAY + " - ändere das Level eines Spielers.");
		
		player.sendMessage(ChatColor.GOLD + "[Credits]" + ChatColor.DARK_GRAY +"=================================");
		player.sendMessage(ChatColor.GRAY + "made by " + ChatColor.GOLD + "Felix Payne"+ ChatColor.GRAY + " visit KING-Gaming.eu on ts3");

		player.sendMessage(ChatColor.DARK_GRAY + "==========================================");
	}
	
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;

		//returns if syntax error
		if(arguments.length > 0) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		//send admin help to sender
		if(arguments.length == 0 && player.hasPermission("gungame.admin.help")) {
			sendAdminHelp(player);
			return true;
		}
		
		//send user help to sender
		if(arguments.length == 0 && player.hasPermission("gungame.user.help")) {
			sendUserHelp(player);
			return true;
		}
		return true;
	}

	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if(cmd.getName().equalsIgnoreCase("help")) {
			if(args.length >= 1) {
		    	   final List<String> tabComplete = Lists.newArrayList();
		    	   tabComplete.add("");
		    	   return tabComplete;
			}
		}
		return null;
    }
}
