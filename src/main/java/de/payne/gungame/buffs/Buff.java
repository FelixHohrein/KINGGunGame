package de.payne.gungame.buffs;


import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Getter;
import lombok.Setter;

public class Buff {

	@Getter
	private String buffName;
	
	@Getter
	@Setter
	private int duration = 0;
	
	@Getter
	private PotionEffect potionEffect = null;
	@Getter
	private Enchantment enchantment = null;
	@Getter
	private int enchantmentLevel = 0;
	
	@Getter
	private int levelAdd = 0;
	
	
	public Buff(final BuffTypes buffType) {
		this.getData(buffType);
	}


	
	private final void getData(final BuffTypes buffType) {
				
		switch(buffType){
		
        case STÄRKE:
            this.buffName = "STÄRKE";
            this.duration = 30;
            this.potionEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*this.duration, 0);
            break;
            
        case STÄRKEII:
            this.buffName = "STÄRKEII";
            this.duration = 30;
            this.potionEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*this.duration, 1);
            break;
            
        case GESCHWINDIGKEIT:
            this.buffName = "GESCHWINDIGKEIT";
            this.duration = 30;
            this.potionEffect = new PotionEffect(PotionEffectType.SPEED, 20*this.duration, 0);
            break;
            
        case GESCHWINDIGKEITII:
            this.buffName = "GESCHWINDIGKEITII";
            this.duration = 30;
            this.potionEffect = new PotionEffect(PotionEffectType.SPEED, 20*this.duration, 1);
            break;
            
        case SPRUNGBOOST:
            this.buffName = "SPRUNGBOOST";
            this.duration = 30;
            this.potionEffect = new PotionEffect(PotionEffectType.JUMP, 20*this.duration, 2);
            break;
            
        case WASSERLÄUFER:
            this.buffName = "WASSERLÄUFER";
            this.duration = 30;
            break;
            
        case EXTRALEBEN:
            this.buffName = "EXTRALEBEN";
            this.duration = 120;
            this.potionEffect = new PotionEffect(PotionEffectType.HEALTH_BOOST, 20*this.duration, 1);  
            break;
            
        case EXTRALEBENII:
            this.buffName = "EXTRALEBENII";
            this.duration = 120;
            this.potionEffect = new PotionEffect(PotionEffectType.HEALTH_BOOST, 20*this.duration, 2); 
            break;
            
        case INSTANTLEVELUP:
            this.buffName = "INSTANTLEVELUP";
            this.levelAdd = 1;
            this.duration = 1;
            break;
            
        case INSTANTLEVELUPII:
        	this.buffName = "INSTANTLEVELUPII"; 
            this.levelAdd = 2;
            this.duration = 1;
            break;

        	
        case INSTANTLEVELUPIII:
        	this.buffName = "INSTANTLEVELUPIII";
            this.levelAdd = 3;
            this.duration = 1;
            break;

        default:
        	System.out.println("[GunGame] --> [BUFFS] > BUFF NOT FOUND!");
        	break;
		}
	}
	
}