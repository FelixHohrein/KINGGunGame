package de.payne.gungame.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import de.payne.gungame.listener.player.PlayerSpawnProtectionDamageListener;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class LeaveCommand implements CommandExecutor, TabCompleter {

//	final private String error = GunGame.getInstance().getPrefix() + ChatColor.RED + "Fehler! " + ChatColor.GRAY + "Bitte reconnecte und informiere einen Admin.";
//	final private String notIngame = GunGame.getInstance().getPrefix() + ChatColor.RED + "Fehler! " + ChatColor.GRAY + "Du bist nicht im GunGame.";
//	final private String auﬂerhalbSpawn = GunGame.getInstance().getPrefix() + ChatColor.GRAY + "Du kannst das GunGame nur am Spawn verlassen.";


	private final PlayerSpawnProtectionDamageListener damageListener = new PlayerSpawnProtectionDamageListener();
	
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}
		
		final Player player = (Player) commandSender;
		
		//returns if no permission
		if(!player.hasPermission("gungame.user.leave")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		//returns if syntax error
		if(arguments.length != 0) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		if(!GunGame.getInstance().getIngameList().contains(player)) {
			
			if(!player.getLocation().getWorld().equals(GunGame.getInstance().getCurrentMap().getSpawnLocation().getWorld())) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_LEAVE_NOT_INGAME, player.getUniqueId(), true));
				return true;
			} else {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_LEAVE_ERROR, player.getUniqueId(), true));
				return true;
			}
		}
		
		if(!this.damageListener.checkSpawnProtection(player.getLocation())) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_LEAVE_NOT_IN_SPAWN, player.getUniqueId(), true));
			return true;
		}
		
		GunGame.getInstance().getIngameList().remove(player);
		player.teleport(GunGame.getInstance().getLobbySpawn().getSpawnLocation());
		GunGame.getInstance().getSignBuilder().signUpdate(Bukkit.getOnlinePlayers().size());
		player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_LEAVE_SUCCESS, player.getUniqueId(), true));
		player.getInventory().clear();
		player.setLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
		player.setGameMode(GameMode.ADVENTURE);
		
		return true;
	}
	
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if(cmd.getName().equalsIgnoreCase("leave")) {
			if(args.length >= 1) {
		    	   final List<String> tabComplete = Lists.newArrayList();
		    	   tabComplete.add("");
		    	   return tabComplete;
			}
		}
		return null;
    }
}
