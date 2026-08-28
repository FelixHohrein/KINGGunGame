package de.payne.gungame.signs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.block.Sign;
import de.payne.gungame.GunGame;
import net.kyori.adventure.text.Component;



public final class SignBuilder {
	
	private final Sign sign;
	
	public SignBuilder(Sign sign) {
		this.sign = sign;
	}
	
	public final void signUpdate(final int onlinePlayers) {
		this.sign.line(0, Component.text(GunGame.getInstance().getPrefix()));
		this.sign.line(1, this.sign.line(1));
		this.sign.line(2, Component.text("" + ChatColor.GOLD + GunGame.getInstance().getIngameList().size() + ChatColor.GRAY + "/" + ChatColor.GOLD + onlinePlayers));
		this.sign.line(3, Component.text(ChatColor.DARK_AQUA + GunGame.getInstance().getCurrentMap().getMapname()));
		this.sign.update();
		

	}
	
	public final void signUpdatePhase(SignPhase signPhase) {
		
		this.sign.line(0, Component.text(GunGame.getInstance().getPrefix()));
		this.sign.line(1, Component.text(ChatColor.GRAY + "[" + ChatColor.GOLD + signPhase + ChatColor.GRAY +"]"));
		this.sign.line(2, Component.text("" + ChatColor.GOLD + GunGame.getInstance().getIngameList().size() + ChatColor.GRAY + "/" + ChatColor.GOLD + Bukkit.getOnlinePlayers().size()));
		this.sign.line(3, Component.text(ChatColor.DARK_AQUA + GunGame.getInstance().getCurrentMap().getMapname()));
		this.sign.update();

	}
}
