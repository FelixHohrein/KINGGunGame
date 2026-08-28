package de.payne.gungame.buffs;


import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import lombok.Getter;

public class BuffManager {

	//später config hinzufügen
	private final int buffCooldownTime = 180; // 3 minuten
	private final int buffTimeToCollect = 300; // 5 minuten
	
	
	@Getter
	private Map<Integer, Integer> buffCooldown = Maps.newHashMap();
	@Getter
	private Map<Integer, Integer> buffTimetoCollectMap = Maps.newHashMap();
	
	//ADD BUFFS TO PLAYER
	public final void addBuffs(final Player player, final Buff buff) {
		this.removeBuffs(player, buff);
				
		GunGame.getInstance().getBuffedPlayers().put(player, buff);
		this.addBuffPotionEffect(player, buff);
		this.addBuffEnchantments(player, buff);
		this.addBuffLevel(player, buff);
		
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 15, 15);
	}
	
	//REMOVE ALL BUFFS FROM PLAYER
	public final void removeBuffs(final Player player, final Buff buff) {
		if(!GunGame.getInstance().getBuffedPlayers().containsKey(player)) {
			return;
		}

		this.removeBuffPotionEffect(player, buff);
		this.removeBuffEnchantments(player, buff);
		GunGame.getInstance().getBuffedPlayers().remove(player);
	}
	
	
	
	
	
	//METHODE TO ADD BUFF LEVELUP
	private final void addBuffLevel(final Player player, final Buff buff) {
		if(buff.getLevelAdd() > 0) {
			if(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel() >= GunGame.getInstance().getLevelConfig().getLevel().size()) {
				GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setTokens(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getTokens()+500);
				return;
			}
			GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).setCurrentLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel() + buff.getLevelAdd());
			GunGame.getInstance().getGunGameEngine().levelChange(player, GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
			player.setLevel(GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getCurrentLevel());
		}
	}
	
	//ADD BUFF POTIONEFFECTS
	private final void addBuffPotionEffect(final Player player, final Buff buff) {
		
		if(buff.getPotionEffect() == null) {
			return;
		}
		player.addPotionEffect(buff.getPotionEffect());
	}
	
	//ADD BUFFS ENCHANTMENTS
	private final void addBuffEnchantments(final Player player, final Buff buff) {

		if(buff.getEnchantment() == null) {
			return;
		}
//		
//		if(player.getInventory().getBoots() == null){
//			player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
//		}
//		player.getInventory().getBoots().addEnchantment(buff.getEnchantment(), buff.getEnchantmentLevel());	
	}
	
	//REMOVE BUFF POTIONEFFECTS
	private final void removeBuffPotionEffect(final Player player, final Buff buff) {
		
		if(buff.getPotionEffect() == null) {
			return;
		}
		for(PotionEffect potionEffect : player.getActivePotionEffects()) {
			player.removePotionEffect(potionEffect.getType());
		}
	}
	
	//REMOVE BUFF ENCHANTMENTS
	private final void removeBuffEnchantments(final Player player, final Buff buff) {
	
		if(buff.getEnchantment() == null) {
			return;
		}
	}
	
	//returns a random BuffType
	public final BuffTypes randomBuff() {	
		Random rnd = new Random();
		int pos = rnd.nextInt(GunGame.getInstance().getPosibleBuffs().size());
		BuffTypes randomBuffType = GunGame.getInstance().getPosibleBuffs().get(pos);
		return randomBuffType;
	}
	
	//RETURNS THE CURRENTTIME IN SECONDS
	public final int getCurrentTime() {
		return Math.round(System.currentTimeMillis() / 1000);
	}
	
	
	public final void autoBuffRefresh() {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(GunGame.getInstance(), new Runnable(){

			@Override
			public void run() {

				 for(BuffLocations locations : GunGame.getInstance().getBuffLocationsCurrentmap()) {
					 //CHECKS IF THE BUFF HAS AN COOLDOWN
					 if(isCooldown(locations)) {
						 continue;
						//WENN KEIN COOLDOWN
					 }
					 addTimeToCollect(locations);
					 
					 if(isTimeToCollect(locations)) {//WENN DER BUFF AUFGEHOBEN WERDEN KANN
						 replaceBlock(false, locations);
						 continue;
						 
					 } else { //WENN DIE ZEIT UM AUFHEBEN ABGELAUFEN IST
						 addCooldown(locations);
						 replaceBlock(true, locations);
					 } 

				}
			}
			
		}, 20*60, 20*5); //erstmalig nach 60 sekunden, alle 5 Sekunden
	}


	//CHECK IF THE BUFF HAS A ACTIVE COOLDOWN 
	public final boolean isCooldown(final BuffLocations buffLocation) {
					
		if(!this.getBuffCooldown().containsKey(buffLocation.getLocationID())) {
			return false;
		}
		
		int cooldownTime;
		cooldownTime = this.getBuffCooldown().get(buffLocation.getLocationID());
		if(this.getBuffCooldown().get(buffLocation.getLocationID()) == null) {
			cooldownTime = 0;
		}
		if(this.getCurrentTime() - cooldownTime < this.buffCooldownTime) {
			return true;
		}
		this.getBuffCooldown().remove(buffLocation.getLocationID());
		return false;
	}
	
	//ADD A COOLDOWN
	public final void addCooldown(final BuffLocations buffLocation) {
		if(this.isCooldown(buffLocation)) {
			return;
		}
		this.getBuffTimetoCollectMap().remove(buffLocation.getLocationID());
		this.replaceBlock(true, buffLocation);
		this.getBuffCooldown().put(buffLocation.getLocationID(), this.getCurrentTime());
	}

	
    //CHECK IF THE TIME TO COLLECT IS TRUE
	private final boolean isTimeToCollect(final BuffLocations buffLocation) {
		
		//WENN NICHT IN DER MAP BUFFTIMETOCOLLECT
		if(!this.getBuffTimetoCollectMap().containsKey(buffLocation.getLocationID())) {
			return false;
		}
		
		int cooldownTime;
		cooldownTime = this.getBuffTimetoCollectMap().get(buffLocation.getLocationID());
		if(this.getBuffTimetoCollectMap().get(buffLocation.getLocationID()) == null) {
			cooldownTime = 0;
		}
		//WENN DIE CURRENTTIME - TIME SAVED IN MAP GRÖSSER IST WIE DIE TIMETOCOLLECT WARTEZEIT
		if(this.getCurrentTime() - cooldownTime < this.buffTimeToCollect) {
			return true;
		}
		this.buffTimetoCollectMap.remove(buffLocation.getLocationID());
		return false;
	}
	
	//ADD A TIME TO COLLECT COOLDOWN
	private final void addTimeToCollect(final BuffLocations buffLocation) {
		if(this.isTimeToCollect(buffLocation)) {
			return;
		}
		this.getBuffTimetoCollectMap().put(buffLocation.getLocationID(), this.getCurrentTime());
	}

	
	private final void replaceBlock(final boolean toSolid, final BuffLocations buffLocation){
		if(toSolid) {
			buffLocation.getLocation().getBlock().setType(Material.OBSIDIAN);
		} else {
			buffLocation.getLocation().getBlock().setType(Material.GLASS);
		}
	}
}

