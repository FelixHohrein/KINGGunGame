package de.payne.gungame.armorstand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import de.payne.gungame.database.GunGamePlayer;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;

public class Armorstand {

	@Getter
	private static Map<Integer, Armorstand> armorstands = Maps.newHashMap();
	
	@Getter
	private ArmorStand stand;
	@Setter
	@Getter
	private UUID uuid;
	@Getter
	private int rank;
	@Getter
	private Location location;
	
//	@Getter
//	private Sign sign;
	@Getter
	private Hologram holo;
	
	//REMOVE OLD METHOD MISSING!!!
	public Armorstand(final int rank, final UUID uuid, final Location location) {
		
		this.rank = rank;
		this.uuid = uuid;
		this.location = location;

		this.stand = this.getExistingOrCreateIfNotExistArmorStand();
		this.setEquipment();
		this.setPose();
		
//		this.sign = this.setSigns();
		this.holo = this.setHolos();
		
		armorstands.put(rank, this);
	} 
	

	public final void changePlayers(final UUID uuid) {
		this.uuid = uuid;
		OfflinePlayer oP = Bukkit.getOfflinePlayer(this.uuid);
		
		armorstands.get(this.rank).getStand().getEquipment().setHelmet(this.getPlayerHead(), false);
		
		armorstands.get(this.rank).getStand().setCustomName(oP.getName());
		this.updateHolos();
	}
	
	private final void updateHolos() {
		
		GunGamePlayer player = new GunGamePlayer(this.uuid);
		
		this.holo.clearLines();
		
		this.holo.insertTextLine(0, ChatColor.GOLD + player.getPlayerName());
		this.holo.insertTextLine(1, ChatColor.DARK_AQUA + "Kills: " + ChatColor.GOLD + player.getKills());
		player.setKd(player.mathKD());
		this.holo.insertTextLine(2, ChatColor.DARK_AQUA + "K/D: " + ChatColor.GOLD + player.getKd());
		this.holo.insertTextLine(3, ChatColor.DARK_AQUA + "Max Lvl: " + ChatColor.GOLD + player.getHighestLevel());
		
//		this.sign.line(0, Component.text(ChatColor.GOLD + player.getPlayerName()));
//		this.sign.line(1, Component.text(ChatColor.DARK_AQUA + "Kills: " + ChatColor.GOLD + player.getKills()));
//		player.setKd(player.mathKD());
//		this.sign.line(2, Component.text(ChatColor.DARK_AQUA + "K/D: " + ChatColor.GOLD + player.getKd()));
//		this.sign.line(3, Component.text(ChatColor.DARK_AQUA + "Höchstes Lvl: " + ChatColor.GOLD + player.getHighestLevel()));
//		this.sign.update();	
	}
	
	private final String getDirection(float yaw) {
		
	    if(yaw >= 315 || yaw <= 45) {
	        // towards north, positive z
	       return "+z";
	    } else if(yaw > 45 && yaw < 135) {
	        // towards west, negative x
	    	 return "-x";
	    } else if(yaw >= 135 && yaw <= 225) {
	        // towards south, negative z
	    	 return "-z";
	    } else if(yaw > 225 && yaw < 315) {
	       // towards east, positive x
	    	 return "+x";
	    }
		return null;
	}
		
	private final Hologram setHolos() {
		String direction = this.getDirection(this.stand.getLocation().getYaw());
		
		switch (direction) {

		case "+z":
			Location loc = this.location.clone().add(0, 1, 1);
			
			Hologram holo = HologramsAPI.createHologram(GunGame.getInstance(), loc);
			GunGamePlayer player = new GunGamePlayer(this.uuid);
			
			holo.insertTextLine(0, ChatColor.GOLD + player.getPlayerName());
			holo.insertTextLine(1, ChatColor.DARK_AQUA + "Kills: " + ChatColor.GOLD + player.getKills());
			player.setKd(player.mathKD());
			holo.insertTextLine(2, ChatColor.DARK_AQUA + "K/D: " + ChatColor.GOLD + player.getKd());
			holo.insertTextLine(3, ChatColor.DARK_AQUA + "Max Lvl: " + ChatColor.GOLD + player.getHighestLevel());
//			loc.getBlock().setType(Material.OAK_WALL_SIGN);
//			Block block = loc.getBlock();
//			Directional wallSignData = (Directional) block.getBlockData();
//			wallSignData.setFacing(BlockFace.SOUTH);
//			block.setBlockData(wallSignData);
			
//			return (Sign) block.getState();
			
			return holo;

		case "-z":
			Location loc2 = this.location.clone().add(0, 1, 0).subtract(0, 0, 1);
			
			Hologram holo2 = HologramsAPI.createHologram(GunGame.getInstance(), loc2);
			GunGamePlayer player2 = new GunGamePlayer(this.uuid);
			
			holo2.insertTextLine(0, ChatColor.GOLD + player2.getPlayerName());
			holo2.insertTextLine(1, ChatColor.DARK_AQUA + "Kills: " + ChatColor.GOLD + player2.getKills());
			player2.setKd(player2.mathKD());
			holo2.insertTextLine(2, ChatColor.DARK_AQUA + "K/D: " + ChatColor.GOLD + player2.getKd());
			holo2.insertTextLine(3, ChatColor.DARK_AQUA + "Max Lvl: " + ChatColor.GOLD + player2.getHighestLevel());
			
//			loc2.getBlock().setType(Material.OAK_WALL_SIGN);
//			Block block2 = loc2.getBlock();
//			Directional wallSignData2 = (Directional) block2.getBlockData();
//			wallSignData2.setFacing(BlockFace.NORTH);
//			block2.setBlockData(wallSignData2);
//			return (Sign) block2.getState();
			return holo2;

		case "+x":
			Location loc3 = this.location.clone().add(1, 1, 0);
			
			Hologram holo3 = HologramsAPI.createHologram(GunGame.getInstance(), loc3);
			GunGamePlayer player3 = new GunGamePlayer(this.uuid);
			
			holo3.insertTextLine(0, ChatColor.GOLD + player3.getPlayerName());
			holo3.insertTextLine(1, ChatColor.DARK_AQUA + "Kills: " + ChatColor.GOLD + player3.getKills());
			player3.setKd(player3.mathKD());
			holo3.insertTextLine(2, ChatColor.DARK_AQUA + "K/D: " + ChatColor.GOLD + player3.getKd());
			holo3.insertTextLine(3, ChatColor.DARK_AQUA + "Max Lvl: " + ChatColor.GOLD + player3.getHighestLevel());
			
//			loc3.getBlock().setType(Material.OAK_WALL_SIGN);
//			Block block3 = loc3.getBlock();
//			Directional wallSignData3 = (Directional) block3.getBlockData();
//			wallSignData3.setFacing(BlockFace.EAST);
//			block3.setBlockData(wallSignData3);
//			return (Sign) block3.getState();
			
			return holo3;

		case "-x":
			Location loc4 = this.location.clone().add(0, 1, 0).subtract(1, 0, 0);
			
			Hologram holo4 = HologramsAPI.createHologram(GunGame.getInstance(), loc4);
			GunGamePlayer player4 = new GunGamePlayer(this.uuid);
			
			holo4.insertTextLine(0, ChatColor.GOLD + player4.getPlayerName());
			holo4.insertTextLine(1, ChatColor.DARK_AQUA + "Kills: " + ChatColor.GOLD + player4.getKills());
			player4.setKd(player4.mathKD());
			holo4.insertTextLine(2, ChatColor.DARK_AQUA + "K/D: " + ChatColor.GOLD + player4.getKd());
			holo4.insertTextLine(3, ChatColor.DARK_AQUA + "Max Lvl: " + ChatColor.GOLD + player4.getHighestLevel());
			
//			loc4.getBlock().setType(Material.OAK_WALL_SIGN);
//			Block block4 = loc4.getBlock();
//			Directional wallSignData4 = (Directional) block4.getBlockData();
//			wallSignData4.setFacing(BlockFace.WEST);
//			block4.setBlockData(wallSignData4);
//			return (Sign) block4.getState();
			
			return holo4;
			
		}
		return null;
	}

	private final ArmorStand getExistingOrCreateIfNotExistArmorStand() {

		Collection<Entity> entities = this.location.getWorld().getNearbyEntities(this.location, 1, 2, 1, (entity) -> entity.getType() == EntityType.ARMOR_STAND);
		if(entities.isEmpty()) {
			return this.createArmorstand();
		} else {
			for(Entity entity : entities) {
				if(entities.size() != 1) {
					System.out.println("MEHR ALS 1 ARMORSTAND POSSIBLE????");
				} else {
					ArmorStand stand = (ArmorStand) entity;
					return stand;
				}
			}
		}
		return null;
	}
	private final ArmorStand createArmorstand() {
		final ArmorStand armorStand = this.location.getWorld().spawn(this.location, ArmorStand.class);
		
		OfflinePlayer oP = Bukkit.getOfflinePlayer(this.uuid);
		
		
		armorStand.setGravity(false);
		armorStand.setBasePlate(false);
		armorStand.setArms(true);
		armorStand.setVisible(true);
		armorStand.setCustomName(oP.getName());
		armorStand.setCustomNameVisible(true);
		
		armorStand.addEquipmentLock(EquipmentSlot.HEAD, LockType.REMOVING_OR_CHANGING);
		armorStand.addEquipmentLock(EquipmentSlot.CHEST, LockType.REMOVING_OR_CHANGING);
		armorStand.addEquipmentLock(EquipmentSlot.LEGS, LockType.REMOVING_OR_CHANGING);
		armorStand.addEquipmentLock(EquipmentSlot.FEET, LockType.REMOVING_OR_CHANGING);
		armorStand.addEquipmentLock(EquipmentSlot.HAND, LockType.REMOVING_OR_CHANGING);
		
		armorStand.setCanPickupItems(false);
		
		return armorStand;
	}
	
	private final void setPose() {
		switch (this.rank) {

		case 1:
			EulerAngle body = new EulerAngle(RadiansDegreesConvert.degreesToRadians(0f),RadiansDegreesConvert.degreesToRadians(0F),RadiansDegreesConvert.degreesToRadians(352F));
			EulerAngle head = new EulerAngle(RadiansDegreesConvert.degreesToRadians(16f),RadiansDegreesConvert.degreesToRadians(0F),RadiansDegreesConvert.degreesToRadians(8F));
			EulerAngle leftLeg = new EulerAngle(RadiansDegreesConvert.degreesToRadians(0f),RadiansDegreesConvert.degreesToRadians(0F),RadiansDegreesConvert.degreesToRadians(6F));
			EulerAngle rightLeg = new EulerAngle(RadiansDegreesConvert.degreesToRadians(307f),RadiansDegreesConvert.degreesToRadians(60F),RadiansDegreesConvert.degreesToRadians(24F));
			EulerAngle leftArm = new EulerAngle(RadiansDegreesConvert.degreesToRadians(273F),RadiansDegreesConvert.degreesToRadians(277F),RadiansDegreesConvert.degreesToRadians(0F));
            EulerAngle rightArm = new EulerAngle(RadiansDegreesConvert.degreesToRadians(259F),RadiansDegreesConvert.degreesToRadians(68F),RadiansDegreesConvert.degreesToRadians(36F));
           
            this.stand.setBodyPose(body);
            this.stand.setHeadPose(head);
            this.stand.setLeftLegPose(leftLeg);
            this.stand.setRightLegPose(rightLeg);
            this.stand.setLeftArmPose(leftArm);
            this.stand.setRightArmPose(rightArm);
			break;

		case 2:
			EulerAngle head2 = new EulerAngle(RadiansDegreesConvert.degreesToRadians(40f),RadiansDegreesConvert.degreesToRadians(333F),RadiansDegreesConvert.degreesToRadians(0F));
			EulerAngle leftArm2 = new EulerAngle(RadiansDegreesConvert.degreesToRadians(92F),RadiansDegreesConvert.degreesToRadians(96F),RadiansDegreesConvert.degreesToRadians(0F));
	        EulerAngle rightArm2 = new EulerAngle(RadiansDegreesConvert.degreesToRadians(267F),RadiansDegreesConvert.degreesToRadians(356F),RadiansDegreesConvert.degreesToRadians(96F));
	       
	        this.stand.setHeadPose(head2);
	        this.stand.setLeftArmPose(leftArm2);
	        this.stand.setRightArmPose(rightArm2);
			break;

		case 3:
			EulerAngle body3 = new EulerAngle(RadiansDegreesConvert.degreesToRadians(352f),RadiansDegreesConvert.degreesToRadians(58F),RadiansDegreesConvert.degreesToRadians(0F));
			EulerAngle head3 = new EulerAngle(RadiansDegreesConvert.degreesToRadians(315f),RadiansDegreesConvert.degreesToRadians(58F),RadiansDegreesConvert.degreesToRadians(0F));
			EulerAngle leftLeg3 = new EulerAngle(RadiansDegreesConvert.degreesToRadians(42f),RadiansDegreesConvert.degreesToRadians(56F),RadiansDegreesConvert.degreesToRadians(0F));
	        EulerAngle rightArm3 = new EulerAngle(RadiansDegreesConvert.degreesToRadians(217F),RadiansDegreesConvert.degreesToRadians(64F),RadiansDegreesConvert.degreesToRadians(0F));
	       
	        this.stand.setBodyPose(body3);
	        this.stand.setHeadPose(head3);
	        this.stand.setLeftLegPose(leftLeg3);
	        this.stand.setRightArmPose(rightArm3);
			break;
		default:
			this.stand = null;
			this.uuid = null;
			this.location = null;
			this.holo = null;
			break;
		}
	}
	
	private final void setEquipment() {
		switch (this.rank) {

		case 1:
			this.stand.getEquipment().setHelmet(this.getPlayerHead(), false);
			this.stand.getEquipment().setChestplate(this.CreateWithMaterial(Material.DIAMOND_CHESTPLATE, 0, 1, null, null, null, 0), false);
			this.stand.getEquipment().setLeggings(this.CreateWithMaterial(Material.DIAMOND_LEGGINGS, 0, 1, null, null, null, 0), false);
			this.stand.getEquipment().setBoots(this.CreateWithMaterial(Material.DIAMOND_BOOTS, 0, 1, null, null, null, 0), false);
			this.stand.getEquipment().setItemInMainHand(this.CreateWithMaterial(Material.DIAMOND_SWORD, 0, 1, null, null, null, 0), false);
			break;

		case 2:
			this.stand.getEquipment().setHelmet(this.getPlayerHead(), false);
			this.stand.getEquipment().setChestplate(this.CreateWithMaterial(Material.GOLDEN_CHESTPLATE, 0, 1, null, null, null, 0), false);
			this.stand.getEquipment().setLeggings(this.CreateWithMaterial(Material.GOLDEN_LEGGINGS, 0, 1, null, null, null, 0), false);
			this.stand.getEquipment().setBoots(this.CreateWithMaterial(Material.GOLDEN_BOOTS, 0, 1, null, null, null, 0), false);
			this.stand.getEquipment().setItemInMainHand(this.CreateWithMaterial(Material.GOLDEN_SWORD, 0, 1, null, null, null, 0), false);
			break;

		case 3:
			this.stand.getEquipment().setHelmet(this.getPlayerHead(), false);
			this.stand.getEquipment().setChestplate(this.CreateWithMaterial(Material.IRON_CHESTPLATE, 0, 1, null, null, null, 0), false);
			this.stand.getEquipment().setLeggings(this.CreateWithMaterial(Material.IRON_LEGGINGS, 0, 1, null, null, null, 0), false);
			this.stand.getEquipment().setBoots(this.CreateWithMaterial(Material.IRON_BOOTS, 0, 1, null, null, null, 0), false);
			this.stand.getEquipment().setItemInMainHand(this.CreateWithMaterial(Material.IRON_SWORD, 0, 1, null, null, null, 0), false);
			break;
		default:
			this.stand = null;
			this.uuid = null;
			this.location = null;
			this.holo = null;
			break;
		}
	}
	
    private final ItemStack CreateWithMaterial(Material material, int subid, int amount, String Displayname, ArrayList<Component> lore, Enchantment ench, int enchlvl) {
        @SuppressWarnings("deprecation")
		ItemStack is = new ItemStack(material, amount, (short) subid); 
        ItemMeta im = is.getItemMeta(); 
        if (Displayname != null) im.displayName(Component.text(Displayname)); 
        if (lore != null) im.lore(lore);
        if (ench != null) im.addEnchant(ench, enchlvl, true); 
        is.setItemMeta(im); 
        return is; 
    }
	
	private final ItemStack getPlayerHead() {
		return SkullCreator.itemFromUuid(this.uuid);
	}
	
}
