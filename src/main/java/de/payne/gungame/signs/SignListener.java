package de.payne.gungame.signs;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;



public class SignListener implements Listener {

	
	@Getter
	private Sign sign;
	
	@EventHandler
	public final void onsignwrite(SignChangeEvent event) {
		Player player = (Player)event.getPlayer();
		
		if(!player.hasPermission("gungame.admin.signwrite")) {
			return;
		}
		
		if(!((event.line(0).equals(Component.text("[GunGame]")) && event.line(1).equals(Component.text("join"))))) {
			return; 
		}
		
		Component line0 = GunGame.getInstance().getPrefixComponent();
		Component line1 = Component.text(ChatColor.GRAY + "[" + ChatColor.GOLD + SignPhase.ONLINE + ChatColor.GRAY +"]");
		Component line2 = Component.text("" + ChatColor.GOLD + GunGame.getInstance().getIngameList().size() + ChatColor.GRAY + "/" + ChatColor.GOLD + Bukkit.getOnlinePlayers().size());
		Component line3 = Component.text(ChatColor.DARK_AQUA + GunGame.getInstance().getCurrentMap().getMapname());
		
		event.line(0, line0);
		event.line(1, line1);
		event.line(2, line2);
		event.line(3, line3);

		
		Sign sign = (Sign) event.getBlock().getState();
		GunGame.getInstance().getSettings().setSignPosition(sign);
		
		player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.JOINSIGN_CREATE_SUCCESS, player.getUniqueId(), true));
	}
	
	
	@EventHandler
	public final void onclicksign(PlayerInteractEvent event) {
		Player player = (Player)event.getPlayer();
		
		if(!player.hasPermission("gungame.user.signclick")) {
			return;
		}

		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Block clicked = event.getClickedBlock();
		
		if(clicked.getType() != Material.OAK_WALL_SIGN) {
			return;
		}

		sign = (Sign) clicked.getState();


		if(!(sign.line(0).equals(GunGame.getInstance().getPrefixComponent()))) {
			return;
		}
		
		
		if(!(sign.line(1).equals(Component.text(ChatColor.GRAY + "[" + ChatColor.GOLD + SignPhase.ONLINE + ChatColor.GRAY +"]")))) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.JOINSIGN_NOT_REACHABLE, player.getUniqueId(), true));
			return;
		}

		if(GunGame.getInstance().getIngameList().size() == Bukkit.getOnlinePlayers().size()) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.JOINSIGN_FEHLER, player.getUniqueId(), true));
			return;
		}
		
		if(GunGame.getInstance().getIngameList().contains(player)) {
			player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.JOINSIGN_FEHLER, player.getUniqueId(), true));
			return;
		}

		
		GunGame.getInstance().getIngameList().add(player);
		GunGame.getInstance().getSignBuilder().signUpdate(Bukkit.getOnlinePlayers().size());
		
		player.teleport(GunGame.getInstance().getCurrentMap().getSpawnLocation());
		player.setGameMode(GameMode.ADVENTURE);
		player.removePotionEffect(PotionEffectType.SPEED);
		player.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.JOINSIGN_JOIN_SUCCESS, player.getUniqueId(), true));
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.JOINSIGN_JOIN_SUCCESS_PLAYERS, player.getUniqueId(), true), "#AMOUNT#", String.valueOf(GunGame.getInstance().getIngameList().size())));

		GunGame.getInstance().getGunGameEngine().levelChange(player, GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
		player.setLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
		    				

		}	
	
}
