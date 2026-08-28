package de.payne.gungame.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.kopfgeld.Kopfgeld;
import de.payne.gungame.kopfgeld.KopfgeldEngine;
import de.payne.gungame.language.MESSAGE;
import net.kyori.adventure.text.Component;
public class KopfgeldCommand implements CommandExecutor, TabCompleter {

	
	private KopfgeldEngine kopfgeldEngine;

//	private final void sendSyntax(final Player player) {
//		player.sendMessage(Component.text(GunGame.getInstance().getPrefix() + ChatColor.GRAY + "/kopfgeld menu"));
//		player.sendMessage(Component.text(GunGame.getInstance().getPrefix() + ChatColor.GRAY +"/kopfgeld aussetzen <Spielername> <Betrag (GunGame Tokens)> <Dauer in Sekunden>"));
//	}
	
	public KopfgeldCommand() {
		this.kopfgeldEngine = new KopfgeldEngine();
	}
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] arguments) {
		// returns if sender isnt player
		if (!(commandSender instanceof Player)) {
			return true;
		}

		final Player player = (Player) commandSender;

		// returns if no permission
		if (!player.hasPermission("gungame.user.kopfgeld")) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_PERMISSION, player.getUniqueId(), true));
			return true;
		}
		
		if(!(arguments.length == 1 || arguments.length == 4)) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WRONG_SYNTAX, player.getUniqueId(), true));

			return true;
		}
		
		if(arguments[0].equalsIgnoreCase("aussetzen")) {

			// WENN DER BETRAG KEIN INT ist
			if(!(GunGame.getInstance().isInteger(arguments[2]))) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_INTEGER, player.getUniqueId(), true));
				return true;
			}
			int betrag = Integer.parseInt(arguments[2]);
			
			//RETURNS WENN NICHT GENÜGEND TOKENS
			if(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getTokens() < betrag) {
				player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_KOPFGELD_NOT_ENAUGHT_TOKENS, player.getUniqueId(), true), "#AMOUNT#", String.valueOf(betrag)));
				return true;
			}
			
			// WENN DIE DAUER != INT
			if(!(GunGame.getInstance().isInteger(arguments[3]))) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.NO_INTEGER, player.getUniqueId(), true));
				return true;
			}
			
			int dauer = Integer.parseInt(arguments[3]);
			
			if(dauer > 604800) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_KOPFGELD_MAX_DAYS, player.getUniqueId(), true));
				return true;
			}
			
			
			if(Bukkit.getPlayer(arguments[1]) == null) {
				player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_KOPFGELD_PLAYER_NOT_ONLINE, player.getUniqueId(), true));
				return true;
			}
			
			Player target = Bukkit.getPlayer(arguments[1]);

			Kopfgeld kopfgeld = new Kopfgeld(true, player.getUniqueId(), target.getUniqueId(), betrag, (System.currentTimeMillis()/1000), dauer, 0);
			GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setTokens(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getTokens()-betrag);
			
			GunGame.getInstance().getScoreboardManager().updateBoard(player);
			GunGame.getInstance().getScoreboardManager().updateBoard(target);
			
			player.sendMessage(Component.text(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.COMMAND_KOPFGELD_SUCCESS, player.getUniqueId(), true), "#TARGET#", target.getName()),  "#AMOUNT#", String.valueOf(betrag)),  "#ZEIT#", String.valueOf(kopfgeld.getRemainingTimeInSeconds()))));
			return true;
			
			
			
		} else if(arguments[0].equalsIgnoreCase("menu")) {

			this.kopfgeldEngine.openKopfgeldInventory(player);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 15, 15);
		}
		

		
		return true;
	}

	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
       
    	if (cmd.getName().equalsIgnoreCase("kopfgeld")) { 
        	final List<String> argList = Lists.newArrayList();
        	
            if(sender.hasPermission("gungame.user.kopfgeld")) {
            	
                if (args.length == 1) {
                	argList.add("aussetzen");
                	argList.add("menu");
                    return argList;

                }
                else if(args.length == 2) {
                	if(args[0].equalsIgnoreCase("aussetzen")) {
                    	for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    		argList.add(onlinePlayer.getName());
                    	}
                	} else if(args[0].equalsIgnoreCase("menu")) {
                		argList.add("");
                	}

                	return argList;	
                }
                
                else if (args.length == 3) {
                	if(args[0].equalsIgnoreCase("aussetzen")) {
                		argList.add("<Betrag>");
                		return argList;
                	} else {
                		argList.add("");
                		return argList;
                	}
                } 
                else if (args.length == 4) {
                	if(args[0].equalsIgnoreCase("aussetzen")) {
                		argList.add("<Dauer in Sekunden)>");
                		return argList;
                	} else {
                		argList.add("");
                		return argList;
                	}
                }
                else if (args.length >= 5) {
            		argList.add("");
            		return argList;
                }
            }
         }
        return null;
    }
	
}
