package de.payne.gungame;



import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.payne.gungame.gadgets.Gadgets;



public final class GunGameEngine {

    public final boolean levelChange(final Player player, final int Level) {
        if (Level <= GunGame.getInstance().getLevelConfig().getLevel().size()) {
            final ItemStack[] Items = GunGame.getInstance().getLevelConfig().getLevel().get(Level);
            player.getInventory().clear();
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null); 
            for (final ItemStack Item : Items) {
                if (this.isArmor(Item) != null) {
                    if (this.isArmor(Item).equals("helmet")) {
                        player.getInventory().setHelmet(Item);
                    } else if (this.isArmor(Item).equals("chestplate")) {
                        player.getInventory().setChestplate(Item);
                    } else if (this.isArmor(Item).equals("leggings")) { 
                        player.getInventory().setLeggings(Item); 
                    } else if (this.isArmor(Item).equals("boots")) { 
                        player.getInventory().setBoots(Item); 
                    }
                    
                    //FÜGT IHM NACH KILL MIT EXISTIERENDEN FROSTWALKER BUFF NEU HINZU DAUER BLEIBT GLEICH
                    if(GunGame.getInstance().getBuffedPlayers().containsKey(player)) {
                        if(GunGame.getInstance().getBuffedPlayers().get(player).getEnchantment() != null) {
                    		if(player.getInventory().getBoots() == null){
                    			player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
                    		}
                    		player.getInventory().getBoots().addEnchantment(GunGame.getInstance().getBuffedPlayers().get(player).getEnchantment(),GunGame.getInstance().getBuffedPlayers().get(player).getEnchantmentLevel());	
                        }
                    } 
                } else {
                    player.getInventory().addItem(Item);
                }
            }
            
            
            //FÜR JEDES EINZELNE GADGET
            for(Gadgets gadget : GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets()) {

            	//WENN AMOUNT == 0, keine USES vorhanden

            	if(gadget.getAmount() != 0) {
                	gadget.updateItemStack(player.getUniqueId());
                	player.getInventory().addItem(gadget.getItemStack());  
            	}   	              
            }
            player.updateInventory();
            return true;
        } else {
            return false;
        }
    }
    
    private final String isArmor( final ItemStack Item) {
        switch (Item.getType().toString()) { //Check item type
            case "LEATHER_HELMET":
                return "helmet";
            case "LEATHER_CHESTPLATE":
                return "chestplate";
            case "LEATHER_LEGGINGS":
                return "leggings";
            case "LEATHER_BOOTS":
                return "boots";
            case "CHAINMAIL_HELMET":
                return "helmet";
            case "CHAINMAIL_CHESTPLATE":
                return "chestplate";
            case "CHAINMAIL_LEGGINGS":
                return "leggings";
            case "CHAINMAIL_BOOTS":
                return "boots";
            case "GOLDEN_HELMET":
                return "helmet";
            case "GOLDEN_CHESTPLATE":
                return "chestplate";
            case "GOLDEN_LEGGINGS":
                return "leggings";
            case "GOLDEN_BOOTS":
                return "boots";
            case "IRON_HELMET":
                return "helmet";
            case "IRON_CHESTPLATE":
                return "chestplate";
            case "IRON_LEGGINGS":
                return "leggings";
            case "IRON_BOOTS":
                return "boots";
            case "DIAMOND_HELMET":
                return "helmet";
            case "DIAMOND_CHESTPLATE":
                return "chestplate";
            case "DIAMOND_LEGGINGS":
                return "leggings";
            case "NETHERITE_HELMET":
                return "helmet";
            case "NETHERITE_CHESTPLATE":
                return "chestplate";
            case "NETHERITE_LEGGINGS":
                return "leggings";
            case "DIAMOND_BOOTS":
                return "boots";
            case "NETHERITE_BOOTS":
                return "boots";  
            default:
                return null; // wenn keine rüstung
        }
    }
}
