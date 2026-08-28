package de.payne.gungame.vote;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import de.payne.gungame.map.GameMap;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class VoteHandler {

	@Getter
	private boolean perPlayerCooldown;
	
	@Getter
	@Setter
	private boolean voteVerfahrenStarted, voteable;
	
	private List<VotableMap> voteableMaps = Lists.newArrayList();
	
	@Getter
	private Map<Player, VotableMap> alreadyVotedPlayers = Maps.newHashMap();
	
	private List<AktiveCooldowns> coolDownPerPlayer = Lists.newArrayList();
	
	
	public VoteHandler() {
		this.perPlayerCooldown = GunGame.getInstance().getSettings().isVotePerPlayer();

		for(GameMap gMap : GunGame.getInstance().getMapConfig().getVotableMaps()) {
			this.voteableMaps.add(new VotableMap(gMap));
		}
	}
	
	
//	
//	ALREADY VOTED PLAYERS LOGIC
//	
	public void addPlayerVoted(final Player player, final String mapName) {
		this.alreadyVotedPlayers.put(player, this.getVotableMap(mapName));
	}
	public void removePlayerVoted(final Player player) {
		this.alreadyVotedPlayers.remove(player);
	}
	public boolean hasPlayerAlreadyVoted(final Player player) {
		return this.alreadyVotedPlayers.containsKey(player);
	}
	public VotableMap getPlayerVotedFor(final Player player) {
		return this.alreadyVotedPlayers.get(player);
	}
	public void clearPlayerVoted() {
		this.alreadyVotedPlayers.clear();
	}
//	
//	VOTEABLE MAPS LOGIC
//	
	public void updateVotableMaps() {
		this.voteableMaps.clear();
		for(GameMap gMap : GunGame.getInstance().getMapConfig().getVotableMaps()) {
			this.voteableMaps.add(new VotableMap(gMap));
		}	
	}
	public VotableMap getVotableMap(final String mapName) {
		for(VotableMap vMap : this.voteableMaps) {
			if(vMap.getMapname().equalsIgnoreCase(mapName)) {
				return vMap;
			}
		}
		return null;
	}
	public List<VotableMap> getVotableMaps(){
		return this.voteableMaps;
	}
	public final void getAllMapsAndVotes(final Player player) {
	
		for(VotableMap vMap : this.voteableMaps) {
			player.sendMessage(Component.text(GunGame.getInstance().getPrefix() + ChatColor.GRAY + vMap.getMapname() + " - " + vMap.getVotes()));
		}
	}
//	
//	COOLDOWN LOGIC
//	
	public final boolean hasCooldown(final Player player) {
		if(this.perPlayerCooldown) {
			for(AktiveCooldowns aCooldown : this.coolDownPerPlayer) {
				if(aCooldown.getPlayer().equals(player)) {
					return true;
				}
			}
			return false;
		} else {
			if(this.coolDownPerPlayer.size() == 1) {
				return true;
			} else { 
				return false;
			}
		}
	}
	public final void addCooldown(final Player player) {
		this.coolDownPerPlayer.add(new AktiveCooldowns(player));
	}
	public final AktiveCooldowns getCooldown(final Player player) {
		if(this.perPlayerCooldown) {
			for(AktiveCooldowns aCooldown : this.coolDownPerPlayer) {
				if(aCooldown.getPlayer().equals(player)) {
					return aCooldown;
				}
			}
			return null;
		} else {
			return this.coolDownPerPlayer.get(0);
		}
	}
	public final void removeCooldown(final Player player) {
		if(this.perPlayerCooldown) {
			for(AktiveCooldowns aCooldown : this.coolDownPerPlayer) {
				if(aCooldown.getPlayer().equals(player)) {
					this.coolDownPerPlayer.remove(aCooldown);
				}
			}
		} else {
			this.coolDownPerPlayer.remove(0);
		}
	}
	public final void removeCooldown(final AktiveCooldowns aCooldown) {
		this.coolDownPerPlayer.remove(aCooldown);
	}
//	
//	VOTE LOGIC
//	
	public final void addVote(String mapName) {
		this.getVotableMap(mapName).setVotes(this.getVotableMap(mapName).getVotes() + 1);
	}
	public final void clearVotes() {
		this.getVotableMaps().forEach(vMap -> vMap.setVotes(0));
	}
	public final int getVotes(String mapName) {
		return this.getVotableMap(mapName).getVotes();
	}
}
