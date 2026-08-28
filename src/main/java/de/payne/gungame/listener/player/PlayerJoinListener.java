package de.payne.gungame.listener.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.payne.gungame.GunGame;
import de.payne.gungame.database.GunGamePlayer;
import de.payne.gungame.language.MESSAGE;



public class PlayerJoinListener implements Listener {
	
	
	@EventHandler
	 public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();	
		
		GunGame.getInstance().getMySqlDatabase().registerPlayer(player);
		//-------FOR CACHING DATA------------
		GunGamePlayer ggplayer = new GunGamePlayer(player.getUniqueId());
		GunGame.getInstance().getGungamePlayers().put(player.getUniqueId(), ggplayer);
		//-----------------------------------
		
		if(!GunGame.getInstance().getMySqlDatabase().isPlayerExisting(player)) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.FIRST_JOIN, p.getUniqueId(), true), "#PLAYER#", player.getName()));
			}
		}
		
		player.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.WELCOME, player.getUniqueId(), true), "#PLAYER#", player.getName()));
		player.getInventory().clear();
				
		player.setGameMode(GameMode.ADVENTURE);
		player.setLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
		player.setFoodLevel(20);
//		this.fancyRadio(player.getLocation());
		
		
		GunGame.getInstance().getScoreboardManager().createBoard(player);
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.setHealth(20);
		}
		
		
		if(!GunGame.getInstance().getSettings().signPathExists()) {
			return;	
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				GunGame.getInstance().getSignBuilder().signUpdate(Bukkit.getOnlinePlayers().size());
			}
		}.runTaskLater(GunGame.getInstance(), 20);// 20 ticks 1 sekunde
	}
	
//	USED IN LOBBY NOW
//    private final void fancyRadio(Location loc) {
//        
//        new BukkitRunnable(){
//            double t = Math.PI/4;
//            
//            public void run(){
//                t = t + 0.1*Math.PI;
//                for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
//                    double x = t*Math.cos(theta);
//                    double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
//                    double z = t*Math.sin(theta);
//                    loc.add(x,y,z);
//                    loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 0);
//                    loc.subtract(x,y,z);
// 
//                    theta = theta + Math.PI/64;
// 
//                    x = t*Math.cos(theta);
//                    y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
//                    z = t*Math.sin(theta);
//                    loc.add(x,y,z);
//                    
//                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 0);
//                    loc.subtract(x,y,z);
//                }
//                if (t > 20){
//                    this.cancel();
//                }
//            }
// 
//        }.runTaskTimer(GunGame.getInstance(), 0, 1);
//    }
}
