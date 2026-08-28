package de.payne.gungame.listener.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.payne.gungame.GunGame;

public class ProtectFarmlandListener implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		if(GunGame.getInstance().getBuilders().contains(event.getPlayer())) {
			event.setCancelled(false);
			return;
		}
		
		Block blockFarmland = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);

		if (event.getAction() == (Action.PHYSICAL) && blockFarmland.getType() == Material.FARMLAND) {
			if(blockFarmland.getType() != Material.DARK_OAK_PRESSURE_PLATE){

				if (blockFarmland.getType() == Material.FARMLAND) {
					if (event.getPlayer() == null) {
						event.setCancelled(true);
						event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
						blockFarmland.setType(blockFarmland.getType());
						blockFarmland.setBlockData(blockFarmland.getBlockData());
					}

				}
			}
		}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

			if (
//ALL DOOR큦					
					  event.getClickedBlock().getType() == Material.OAK_DOOR
					| event.getClickedBlock().getType() == Material.ACACIA_DOOR
					| event.getClickedBlock().getType() == Material.BIRCH_DOOR
					| event.getClickedBlock().getType() == Material.DARK_OAK_DOOR
					| event.getClickedBlock().getType() == Material.JUNGLE_DOOR
					| event.getClickedBlock().getType() == Material.SPRUCE_DOOR
					| event.getClickedBlock().getType() == Material.CRIMSON_DOOR
					| event.getClickedBlock().getType() == Material.IRON_DOOR
					| event.getClickedBlock().getType() == Material.WARPED_DOOR
//ALL TRAPDOOR큦
					| event.getClickedBlock().getType() == Material.OAK_TRAPDOOR
					| event.getClickedBlock().getType() == Material.ACACIA_TRAPDOOR
					| event.getClickedBlock().getType() == Material.BIRCH_TRAPDOOR
					| event.getClickedBlock().getType() == Material.CRIMSON_TRAPDOOR
					| event.getClickedBlock().getType() == Material.DARK_OAK_TRAPDOOR
					| event.getClickedBlock().getType() == Material.IRON_TRAPDOOR
					| event.getClickedBlock().getType() == Material.JUNGLE_TRAPDOOR
					| event.getClickedBlock().getType() == Material.SPRUCE_TRAPDOOR
					| event.getClickedBlock().getType() == Material.WARPED_TRAPDOOR
//FENCE GATE큦
					| event.getClickedBlock().getType() == Material.OAK_FENCE_GATE
					| event.getClickedBlock().getType() == Material.ACACIA_FENCE_GATE
					| event.getClickedBlock().getType() == Material.BIRCH_FENCE_GATE
					| event.getClickedBlock().getType() == Material.DARK_OAK_FENCE_GATE
					| event.getClickedBlock().getType() == Material.JUNGLE_FENCE_GATE
					| event.getClickedBlock().getType() == Material.SPRUCE_FENCE_GATE
					| event.getClickedBlock().getType() == Material.CRIMSON_FENCE_GATE
					| event.getClickedBlock().getType() == Material.SPRUCE_FENCE_GATE
					| event.getClickedBlock().getType() == Material.WARPED_FENCE_GATE
//BUTTON큦
					| event.getClickedBlock().getType() == Material.OAK_BUTTON
					| event.getClickedBlock().getType() == Material.STONE_BUTTON
					| event.getClickedBlock().getType() == Material.ACACIA_BUTTON
					| event.getClickedBlock().getType() == Material.BIRCH_BUTTON
					| event.getClickedBlock().getType() == Material.CRIMSON_BUTTON
					| event.getClickedBlock().getType() == Material.DARK_OAK_BUTTON
					| event.getClickedBlock().getType() == Material.JUNGLE_BUTTON
					| event.getClickedBlock().getType() == Material.POLISHED_BLACKSTONE_BUTTON
					| event.getClickedBlock().getType() == Material.SPRUCE_BUTTON
					| event.getClickedBlock().getType() == Material.WARPED_BUTTON
//REDSTONE STUFF
					| event.getClickedBlock().getType() == Material.LEVER
					| event.getClickedBlock().getType() == Material.COMPARATOR
					| event.getClickedBlock().getType() == Material.DAYLIGHT_DETECTOR
					| event.getClickedBlock().getType() == Material.HOPPER
					| event.getClickedBlock().getType() == Material.DROPPER
					| event.getClickedBlock().getType() == Material.DISPENSER
//CHEST큦
					| event.getClickedBlock().getType() == Material.CHEST
					| event.getClickedBlock().getType() == Material.TRAPPED_CHEST
					| event.getClickedBlock().getType() == Material.CHEST_MINECART
					| event.getClickedBlock().getType() == Material.ENDER_CHEST
//NOTEBLOCK STUFF
					| event.getClickedBlock().getType() == Material.NOTE_BLOCK
					| event.getClickedBlock().getType() == Material.JUKEBOX
//BED큦
					| event.getClickedBlock().getType() == Material.RED_BED
					| event.getClickedBlock().getType() == Material.BLACK_BED
					| event.getClickedBlock().getType() == Material.BLUE_BED
					| event.getClickedBlock().getType() == Material.BROWN_BED
					| event.getClickedBlock().getType() == Material.CYAN_BED
					| event.getClickedBlock().getType() == Material.GRAY_BED
					| event.getClickedBlock().getType() == Material.GREEN_BED
					| event.getClickedBlock().getType() == Material.LIGHT_BLUE_BED
					| event.getClickedBlock().getType() == Material.LIGHT_GRAY_BED
					| event.getClickedBlock().getType() == Material.MAGENTA_BED
					| event.getClickedBlock().getType() == Material.LIME_BED
					| event.getClickedBlock().getType() == Material.ORANGE_BED
					| event.getClickedBlock().getType() == Material.PINK_BED
					| event.getClickedBlock().getType() == Material.PURPLE_BED
					| event.getClickedBlock().getType() == Material.WHITE_BED
					| event.getClickedBlock().getType() == Material.YELLOW_BED
//OTHER USEABLE큦
					| event.getClickedBlock().getType() == Material.ANVIL
					| event.getClickedBlock().getType() == Material.FURNACE
					| event.getClickedBlock().getType() == Material.FURNACE_MINECART
					| event.getClickedBlock().getType() == Material.BLAST_FURNACE
					| event.getClickedBlock().getType() == Material.CARTOGRAPHY_TABLE
					| event.getClickedBlock().getType() == Material.CRAFTING_TABLE
					| event.getClickedBlock().getType() == Material.ENCHANTING_TABLE
					| event.getClickedBlock().getType() == Material.FLETCHING_TABLE
					| event.getClickedBlock().getType() == Material.SMITHING_TABLE
					| event.getClickedBlock().getType() == Material.WRITABLE_BOOK
					| event.getClickedBlock().getType() == Material.BREWING_STAND
					| event.getClickedBlock().getType() == Material.BEACON
					| event.getClickedBlock().getType() == Material.GRINDSTONE
					| event.getClickedBlock().getType() == Material.BELL
					| event.getClickedBlock().getType() == Material.BARREL
					| event.getClickedBlock().getType() == Material.COMPOSTER) 
			{
				if(GunGame.getInstance().getBuilders().contains(event.getPlayer())){
					event.setCancelled(false);
				} else {
					event.setCancelled(true);
				}
			}
		}

	}

}
