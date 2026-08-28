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

public final class AdminMenuCommand implements CommandExecutor, TabCompleter {

	
//	private final void sendSyntax(final Player player) {
//		player.sendMessage(ChatColor.DARK_GRAY + "=============="+ ChatColor.DARK_AQUA + "[Admin-Befehle]" + ChatColor.DARK_GRAY +"==============");
//		player.sendMessage(ChatColor.DARK_AQUA + "[Level]" + ChatColor.DARK_GRAY +"===================================");
//		player.sendMessage(ChatColor.GRAY+ "/adminmenu" + ChatColor.GOLD + " setlevel " + ChatColor.GRAY + "<Spielername> <Anzahl>"+ ChatColor.GRAY + " - Setzt das Level eines Spielers.");
//		player.sendMessage(ChatColor.GRAY+ "/adminmenu" + ChatColor.GOLD + " addlevel " + ChatColor.GRAY + "<Spielername> <Anzahl>"+ ChatColor.GRAY + " - Fügt dem Spieler Level hinzu.");
//		player.sendMessage(ChatColor.GRAY+ "/adminmenu" + ChatColor.GOLD + " removelevel " + ChatColor.GRAY + "<Spielername> <Anzahl>"+ ChatColor.GRAY + " - Zieht dem Spieler Level ab.");
//		player.sendMessage(ChatColor.GRAY+ "/adminmenu" + ChatColor.GOLD + " resetlevel " + ChatColor.GRAY + "<Spielername>"+ ChatColor.GRAY + " - Setzt das Level eines Spielers auf 0.");
//		player.sendMessage(ChatColor.DARK_AQUA + "[Punkte]" + ChatColor.DARK_GRAY +"==================================");
//		player.sendMessage(ChatColor.GRAY+ "/adminmenu" + ChatColor.GOLD + " settokens " + ChatColor.GRAY + "<Spielername> <Anzahl>"+ ChatColor.GRAY + " - Setzt die Tokens eines Spielers.");
//		player.sendMessage(ChatColor.GRAY+ "/adminmenu" + ChatColor.GOLD + " addtokens " + ChatColor.GRAY + "<Spielername> <Anzahl>"+ ChatColor.GRAY + " - Fügt dem Spieler Tokens hinzu.");
//		player.sendMessage(ChatColor.GRAY+ "/adminmenu" + ChatColor.GOLD + " removetokens " + ChatColor.GRAY + "<Spielername> <Anzahl>"+ ChatColor.GRAY + " - Zieht dem Spieler Tokens ab.");
//		player.sendMessage(ChatColor.GRAY+ "/adminmenu" + ChatColor.GOLD + " resettokens " + ChatColor.GRAY + "<Spielername>"+ ChatColor.GRAY + " - Setzt die Tokens eines Spielers auf 0.");
//		player.sendMessage(ChatColor.DARK_GRAY + "=========================================");
//
//	}	
	
	@Override
	public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
		//returns if sender isnt player
		if(!(commandSender instanceof Player)) {
			return true;
		}

		final Player player = (Player) commandSender;

		//returns if no permission
		if(!(player.hasPermission("gungame.admin.adminmenu"))) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		//sends message alle votable maps + returnt die methode
		if(arguments.length != 2 && arguments.length != 3) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
			return true;
		}
		
		if(Bukkit.getPlayer(arguments[1]) == null) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_ARGS, player.getUniqueId(), true));
			return true;
		}
				
		final Player target = Bukkit.getPlayer(arguments[1]);
		
		if(!(GunGame.getInstance().isInteger(arguments[2]))) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_INTEGER, player.getUniqueId(), true));
			return true;
		}
		final int amount = Integer.parseInt(arguments[2]);
			
		
		//level
		if(arguments[0].equalsIgnoreCase("setlevel")) {
			if(arguments.length !=3) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
				return true;
			}
			if(amount > GunGame.getInstance().getLevelConfig().getLevel().size() || amount < 1) {
				player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_SETLEVEL_FEHLER, player.getUniqueId(), true), "#LEVEL#", String.valueOf(GunGame.getInstance().getLevelConfig().getLevel().size())));
				return true;
			}
			GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).setCurrentLevel(amount);
			GunGame.getInstance().getGunGameEngine().levelChange(target, GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel());
			target.setLevel(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel());
			
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_SETLEVEL_SUCCESS_SENDER, player.getUniqueId(), true), "#TARGET#", target.getName()), "#AMOUNT#", String.valueOf(amount)));
			target.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_SETLEVEL_SUCCESS_TARGET, target.getUniqueId(), true), "#SENDER#", player.getName()), "#AMOUNT#", String.valueOf(amount)));
			GunGame.getInstance().getScoreboardManager().updateBoard(target);
			
			return true;
		} 
		else if(arguments[0].equalsIgnoreCase("addlevel")) {
			if(arguments.length !=3) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
				return true;
			}
			if(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel() + amount > GunGame.getInstance().getLevelConfig().getLevel().size()) {
				player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_ADDLEVEL_FEHLER, player.getUniqueId(), true), "#TARGET#", target.getName()), "#LEVEL#", String.valueOf(GunGame.getInstance().getLevelConfig().getLevel().size() - GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel())));
				return true;
			}
			GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).setCurrentLevel(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel() + amount);
			GunGame.getInstance().getGunGameEngine().levelChange(target, GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel());
			target.setLevel(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel());
			
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_ADDLEVEL_SUCCESS_SENDER, player.getUniqueId(), true), "#TARGET#", target.getName()), "#AMOUNT#", String.valueOf(amount)), "#LEVEL#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel())));
			target.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_ADDLEVEL_SUCCESS_TARGET, target.getUniqueId(), true), "#AMOUNT#", String.valueOf(amount)), "#SENDER#", player.getName()));
			GunGame.getInstance().getScoreboardManager().updateBoard(target);

			return true;
		}
		else if(arguments[0].equalsIgnoreCase("removelevel")) {
			if(arguments.length !=3) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
				return true;
			}
			if(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel()-amount <= 0) {
				player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_REMOVELEVEL_FEHLER, player.getUniqueId(), true), "#TARGET#", target.getName()), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel()-1)));
				return true;
			}
			GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).setCurrentLevel(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel()-amount);
			GunGame.getInstance().getGunGameEngine().levelChange(target, GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel());
			target.setLevel(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel());
			
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_REMOVELEVEL_SUCCESS_SENDER, player.getUniqueId(), true), "#TARGET#", target.getName()), "#AMOUNT#", String.valueOf(amount)), "#LEVEL#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel())));
			target.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_REMOVELEVEL_SUCCESS_TARGET, target.getUniqueId(), true), "#AMOUNT#", String.valueOf(amount)), "#SENDER#", player.getName()));
			GunGame.getInstance().getScoreboardManager().updateBoard(target);

			return true;
		}
		else if(arguments[0].equalsIgnoreCase("resetlevel")) {
			if(arguments.length !=2) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
				return true;
			}
			GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).setCurrentLevel(0);
			GunGame.getInstance().getGunGameEngine().levelChange(target, GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getCurrentLevel());
			target.setLevel(1);
			
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_RESETLEVEL_SUCCESS_SENDER, player.getUniqueId(), true), "#TARGET#", target.getName()));
			target.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_RESETLEVEL_SUCCESS_TARGET, target.getUniqueId(), true), "#SENDER#", player.getName()));
			GunGame.getInstance().getScoreboardManager().updateBoard(target);

			return true;
		}
		
		//points
		else if(arguments[0].equalsIgnoreCase("settokens")) {
			if(arguments.length !=3) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
				return true;
			}
			GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).setTokens(amount);
			
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_SETTOKENS_SUCCESS_SENDER, player.getUniqueId(), true), "#TARGET#", target.getName()), "#AMOUNT#", String.valueOf(amount)));
			target.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_SETTOKENS_SUCCESS_TARGET, target.getUniqueId(), true), "#SENDER#", player.getName()), "#AMOUNT#", String.valueOf(amount)));
			GunGame.getInstance().getScoreboardManager().updateBoard(target);

			return true;
		}
		else if(arguments[0].equalsIgnoreCase("addtokens")) {
			if(arguments.length !=3) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
				return true;
			}
			GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).setTokens(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getTokens() + amount);
			
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_ADDTOKENS_SUCCESS_SENDER, player.getUniqueId(), true), "#TARGET#", target.getName()), "#AMOUNT#", String.valueOf(amount)), "#PUNKTE#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getTokens())));
			target.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_ADDTOKENS_SUCCESS_TARGET, target.getUniqueId(), true), "#AMOUNT#", String.valueOf(amount)), "#SENDER#", player.getName()));
			GunGame.getInstance().getScoreboardManager().updateBoard(target);

			return true;	
		} 
		else if(arguments[0].equalsIgnoreCase("removetokens")) {
			if(arguments.length !=3) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
				return true;
			}
			if(amount > GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getTokens()) {
				player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_REMOVETOKENS_FEHLER, player.getUniqueId(), true), "#TARGET#", target.getName()), "#AMOUNT#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getTokens())));
				return true;
			}
			GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).setTokens(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getTokens()-amount);
			
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_REMOVETOKENS_SUCCESS_SENDER, player.getUniqueId(), true), "#TARGET#", target.getName()), "#AMOUNT#", String.valueOf(amount)), "#Punkte#", String.valueOf(GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).getTokens())));
			target.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_REMOVETOKENS_SUCCESS_TARGET, target.getUniqueId(), true), "#AMOUNT#", String.valueOf(amount)), "#SENDER#", player.getName()));
			GunGame.getInstance().getScoreboardManager().updateBoard(target);

			return true;
		}
		else if(arguments[0].equalsIgnoreCase("resettokens")) {
			if(arguments.length !=2) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
				return true;
			}
			GunGame.getInstance().getGungamePlayers().get(target.getUniqueId()).setTokens(0);
			
			player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_RESETTOKENS_SUCCESS_SENDER, player.getUniqueId(), true), "#TARGET#", target.getName()));
			target.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_ADMIN_RESETTOKENS_SUCCESS_TARGET, target.getUniqueId(), true), "#SENDER#", player.getName()));
			GunGame.getInstance().getScoreboardManager().updateBoard(target);

			return true;
		} else {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));
		}

		return true;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("adminmenu")) { 
        	final List<String> argList = Lists.newArrayList();
        	
            if(sender.hasPermission("gungame.admin.adminmenu")) {
            	
                if (args.length == 1) {
                	argList.add("setlevel");
                	argList.add("addlevel");
                	argList.add("removelevel");
                	argList.add("resetlevel");
                	argList.add("settokens");
                	argList.add("addtokens");
                	argList.add("removetokens");
                	argList.add("resettokens");
                    return argList;

                }
                else if(args.length == 2) {
                	for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                		argList.add(onlinePlayer.getName());
                	}
                	return argList;	
                }
                else if (args.length == 3) {
                	if(args[0].equalsIgnoreCase("resetlevel") || args[0].equalsIgnoreCase("resettokens")) {
                		argList.add("");
                		return argList;
                	} else {
                		argList.add("<Anzahl>");
                		return argList;
                	}
                } 
                else if (args.length >= 4) {
            		argList.add("");
            		return argList;
                }
            }
         }
        return null;
    }
}