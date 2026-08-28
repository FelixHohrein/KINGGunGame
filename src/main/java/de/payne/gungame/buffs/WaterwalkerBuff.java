package de.payne.gungame.buffs;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;

public final class WaterwalkerBuff implements Listener {

//	public final Map<Player, List<Block>> changedBlocks = Maps.newHashMap();
	
	
	@EventHandler
	public final void onPlayerMove(final PlayerMoveEvent event) {
		
		final Player player = event.getPlayer();
		final Location blockPlayer = player.getLocation().clone().subtract(0, 1, 0);
		final Block block = blockPlayer.getBlock();
		
		if(block.getType() != Material.WATER && block.getType() != Material.LAVA) {
			return;
		}
		
		if(!(GunGame.getInstance().getBuffedPlayers().containsKey(player))) {
			return;
		}
		
		final Buff buff = GunGame.getInstance().getBuffedPlayers().get(player);
		
		if(!(buff.getBuffName().equalsIgnoreCase("WASSERLÄUFER"))) {
			return;
		}
		
		if(buff.getDuration() <= 0) {
			GunGame.getInstance().getBuffManager().removeBuffs(player, buff);
			return;
		}
		
		this.setFloor(player, blockPlayer, 2);
	}
	
	private final void setFloor(final Player player, final Location center, final int radius) {

		List<Block> blocks = Lists.newArrayList();
		
		//FÜGT DIE BLÖCKE DER LISTE HINZU
		for (int xMod = -radius; xMod <= radius; xMod++) {
			for (int zMod = -radius; zMod <= radius; zMod++) {
				Block theBlock = center.getBlock().getRelative(xMod, 0, zMod);
				if(theBlock.isLiquid()) {
					if(theBlock.getWorld().getBlockAt(theBlock.getLocation().clone().add(0, 1, 0)).getType() == Material.AIR) {
						if(theBlock.getType() == Material.WATER) {
							theBlock.setType(Material.BLUE_STAINED_GLASS);
//							this.changedBlocks.get(player).add(theBlock);
							blocks.add(theBlock);
						} else if(theBlock.getType() == Material.LAVA) {
							theBlock.setType(Material.RED_STAINED_GLASS);
//							this.changedBlocks.get(player).add(theBlock);
							blocks.add(theBlock);
						}
					}
				}
			}
		}
		this.resetTimer(blocks, 3);
	}
	
	
	//STARTET DEN TIMER FÜR DAS RESETTEN
	private final void resetTimer(final List<Block> blocks, final int resetTime) {

		new BukkitRunnable() {

			@Override
			public void run() {
				resetBlocks(blocks);
				this.cancel();
			}
			
		}.runTaskLater(GunGame.getInstance(), 20*resetTime);
	}
	

	//ENTFERNT DIE BLÖCKE
	private final void resetBlocks(final List<Block> blocks) {
		for(Block block : blocks) {
			if(block.getType() == Material.BLUE_STAINED_GLASS) {
				block.setType(Material.WATER);
			} else if(block.getType() == Material.RED_STAINED_GLASS) {
				block.setType(Material.LAVA);
			}
		}
	}
	
}
