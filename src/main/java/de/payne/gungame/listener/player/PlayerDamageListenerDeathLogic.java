package de.payne.gungame.listener.player;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.google.common.collect.Lists;

import de.payne.gungame.GunGame;
import de.payne.gungame.kopfgeld.Kopfgeld;
import de.payne.gungame.language.MESSAGE;
import de.payne.gungame.team.Team;
import de.payne.gungame.utils.LastDamageTimeScheduler;
import lombok.Getter;
import net.kyori.adventure.text.Component;

public final class PlayerDamageListenerDeathLogic implements Listener {

	private PlayerSpawnProtectionDamageListener damageListener = new PlayerSpawnProtectionDamageListener();

	@Getter
	private final List<Player> deadList = Lists.newArrayList();

	@EventHandler
	public void onBlockDamage(final EntityDamageByBlockEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onDamage(final EntityDamageEvent event) {
		if (event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FIRE
				|| event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.HOT_FLOOR
				|| event.getCause() == DamageCause.DROWNING || event.getCause() == DamageCause.STARVATION) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFinalDamage(final EntityDamageByEntityEvent event) {

		if (event.getEntity() instanceof Player) {

			final Player victim = (Player) event.getEntity();

			if (event.getDamager() instanceof Player) {

				if (!event.isCancelled()) {

					// CHECK IF TEAM OR NOT
					if (Team.hasTeam(victim)) {
						Team team = Team.getTeam(victim);
						if (team.getPlayer2().equals(event.getDamager())
								|| team.getPlayer1().equals(event.getDamager())) {
							if (!(victim.isInWater() || victim.isInLava())) {
								event.setCancelled(true);
								return;
							}
						}
					}

					if (event.getFinalDamage() < victim.getHealth()) {
						final Player killer = (Player) event.getDamager();
						if (damageListener.checkSpawnProtection(victim.getLocation())
								|| damageListener.checkSpawnProtection(killer.getLocation())) {
							event.setCancelled(true);
							return;
						}
						new LastDamageTimeScheduler(10, victim, killer);
					}

					if (event.getFinalDamage() >= victim.getHealth()) {
						final Player killer = (Player) event.getDamager();

						if (damageListener.checkSpawnProtection(victim.getLocation())
								|| damageListener.checkSpawnProtection(killer.getLocation())) {
							return;
						}

						this.deathLogic(killer, victim);
						event.setCancelled(true);
					}

				}
				// WENN DER DAMAGER ODER GEDAMAGED PLAYER KEIN PLAYER IST
			} else {

				if (event.getFinalDamage() >= victim.getHealth()) {

					if (LastDamageTimeScheduler.aktiveTimer.containsKey(victim)) {
						this.deathLogic(LastDamageTimeScheduler.lastDamager.get(victim), victim);
					} else {
						this.deathLogic(victim, victim);
					}
				}

				event.setCancelled(true);
			}
		}
	}

	private final void deathLogic(final Player killer, final Player victim) {

		if (!GunGame.getInstance().getIngameList().contains(victim)) {
			this.respawnLogic(victim, GunGame.getInstance().getLobbySpawn().getSpawnLocation());
			return;
		}

		if (this.getDeadList().contains(victim)) {
			System.out.println(GunGame.getInstance().getPrefix() + "Error: Spieler war bereits in der Dead-ArrayList!");
			this.respawnLogic(victim, GunGame.getInstance().getCurrentMap().getSpawnLocation());

			for (Player p : GunGame.getInstance().getIngameList()) {
				p.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(
						GunGame.getInstance().getTextEngine().getMessage(MESSAGE.PLAYER_KILLED, p.getUniqueId(), true),
						"#TARGET#", victim.getName()));
			}
			return;
		}
		// Needed for levelup System zeile 79 here
		final int victimlevel = GunGame.getInstance().getGungamePlayers().get(victim.getUniqueId()).getCurrentLevel();

		this.deadList.add(victim);
		GunGame.getInstance().getGunGameManager().PlayerDeath(victim, 1); // UPDATE STATS

		if (LastDamageTimeScheduler.aktiveTimer.containsKey(victim)) {
			if (!LastDamageTimeScheduler.aktiveTimer.get(victim).getTaskName().isCancelled()) {
				LastDamageTimeScheduler.aktiveTimer.get(victim).getTaskName().cancel();
			}
			LastDamageTimeScheduler.aktiveTimer.remove(victim);
			LastDamageTimeScheduler.lastDamager.remove(victim);
		}

		if (victim != killer) {

			if (victimlevel > (GunGame.getInstance().getGungamePlayers().get(killer.getUniqueId()).getCurrentLevel()
					+ 50)) {
				GunGame.getInstance().getGunGameManager().PlayerKilled(killer, 5, 1); // UPDATE STATS

			} else if (victimlevel > (GunGame.getInstance().getGungamePlayers().get(killer.getUniqueId())
					.getCurrentLevel() + 30)) {
				GunGame.getInstance().getGunGameManager().PlayerKilled(killer, 3, 1); // UPDATE STATS

			} else if (victimlevel > (GunGame.getInstance().getGungamePlayers().get(killer.getUniqueId())
					.getCurrentLevel() + 10)) {
				GunGame.getInstance().getGunGameManager().PlayerKilled(killer, 2, 1); // UPDATE STATS
			} else {
				GunGame.getInstance().getGunGameManager().PlayerKilled(killer, 1, 1); // UPDATE STATS
			}

			if (Kopfgeld.isKopfgeldAusgesetzt(victim)) {
				for (Kopfgeld kg : Kopfgeld.getKopfgelder().values()) {
					if (kg.getTargetUUID().equals(victim.getUniqueId())) {
						GunGame.getInstance().getGungamePlayers().get(killer.getUniqueId()).setTokens(
								GunGame.getInstance().getGungamePlayers().get(killer.getUniqueId()).getTokens()
										+ kg.getAmount());
						Kopfgeld.getKopfgelder().remove(kg.getId());
						GunGame.getInstance().getStatisticTable().deleteKopfgeld(kg);
					}
				}
				for (Player p : GunGame.getInstance().getIngameList()) {
					p.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(
							GunGame.getInstance().getTextEngine()
									.changePlaceholders(GunGame.getInstance().getTextEngine()
											.changePlaceholders(GunGame.getInstance().getTextEngine().getMessage(
													MESSAGE.PLAYER_KILLED_BY_PLAYER_KOPFGELD, p.getUniqueId(), true),
													"#KILLER#", killer.getName()),
											"#VICTIM#", victim.getName()),
							"#AMOUNT#", String.valueOf(Kopfgeld.gesamtKopfgeldAusgesetzt(victim))));
				}
			} else {
				for (Player p : GunGame.getInstance().getIngameList()) {
					p.sendMessage(
							GunGame.getInstance().getTextEngine()
									.changePlaceholders(GunGame.getInstance().getTextEngine().changePlaceholders(
											GunGame.getInstance().getTextEngine()
													.getMessage(MESSAGE.PLAYER_KILLED_BY_PLAYER, p.getUniqueId(), true),
											"#VICTIM#", victim.getName()), "#KILLER#", killer.getName()));
				}
			}
		} else {
			for (Player p : GunGame.getInstance().getIngameList()) {
				p.sendMessage(GunGame.getInstance().getTextEngine().changePlaceholders(
						GunGame.getInstance().getTextEngine().getMessage(MESSAGE.PLAYER_KILLED, p.getUniqueId(), true),
						"#TARGET#", victim.getName()));
			}
		}

		this.respawnLogic(victim, GunGame.getInstance().getCurrentMap().getSpawnLocation());
	}

	private final void respawnLogic(final Player player, final Location location) {
		if (!player.isOnline()) {
			return;
		}
		player.setFireTicks(0);
		player.teleport(location);
		GunGame.getInstance().getGunGameManager().resetLevel(player,
				GunGame.getInstance().getGunGameManager().getLevelToRemove(player));
		player.setHealth(20);
		player.setFoodLevel(20);
		GunGame.getInstance().getBuffCountdownManager()
				.removeAktivBuffScheduler(GunGame.getInstance().getBuffedPlayers().get(player), player);

		if (this.getDeadList().contains(player)) {
			this.getDeadList().remove(player);
		}
		// UPDATE SCOREBOARD
		GunGame.getInstance().getScoreboardManager().updateBoard(player);
	}
}