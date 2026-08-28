package de.payne.gungame.gadgets;



import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.FishHook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.MESSAGE;
import de.payne.gungame.listener.player.PlayerSpawnProtectionDamageListener;
import de.payne.gungame.team.Team;
import net.md_5.bungee.api.ChatColor;

public final class Hook implements Listener {
	
	
	final PlayerSpawnProtectionDamageListener spawnProtection = new PlayerSpawnProtectionDamageListener();
	
	
	//RETURNS THE HOOK GADGET OBJECT FROM LIST WITH ID 0
	private final Gadgets getPlayersHookGadget(final Player player) {
		return GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getGadgets().get(0);
	}

	@EventHandler
	public final void projectileHitEvent(final ProjectileHitEvent event) {
		
		//HAKEN DER HOOK = ENTITY
		if(!(event.getEntity() instanceof FishHook)) {
			return;
		}
		FishHook hook = (FishHook) event.getEntity();
		
//WAS TUN WENN WASSER GETROFFEN WURDE!!!!!!!!!!!!!!!!
		//WENN BLOCK GETROFFEN WIRD 
		if(event.getHitBlock() != null || hook.isInWater()) {
			hook.setHookedEntity(null);
			hook.remove();
			return;
		}
		
		//HITTET ENTITY = PLAYER
		if(!(event.getHitEntity() instanceof Player)) {
			return;
		}
		final Player hooked = (Player) event.getHitEntity();

		//SHOOTER = PLAYER
		if(!(hook.getShooter() instanceof Player)) {
			return;
		}
		final Player shooter = (Player) hook.getShooter();
		
		//RETURNS IF PLAYER HAS NO PERMISSION
		if(!(shooter.hasPermission("gungame.user.hook"))) {
			return;
		}
		if(Team.hasTeam(shooter)) {
			if(Team.getTeam(shooter).getPlayer2().equals(hooked) || Team.getTeam(shooter).getPlayer1().equals(hooked)) {
				hook.setHookedEntity(null);
				hook.remove();
				return;
			}
		}
		
		//CHECK FOR SPAWNPROTECTION
		if(this.spawnProtection.checkSpawnProtection(shooter.getLocation()) || this.spawnProtection.checkSpawnProtection(hooked.getLocation())) {
			hook.setHookedEntity(null);
			hook.remove();
			return;
		}
		
		//CHECKT OB DAS ITEM DES SHOOTERS HAT NOCH USES ÜBRIG
		if(this.getPlayersHookGadget(shooter).getAmount() <= 0) {
			return;
		}
		
		//RETURNS IF THE DISTANCE BETWEEN SHOOTER AND HOOKED < 3
		if(shooter.getLocation().distance(hooked.getLocation()) < 3) {
			shooter.sendMessage(GunGame.getInstance().getTextEngine().getMessage(MESSAGE.GADGET_HOOK_TO_CLOSE, shooter.getUniqueId(), true));
			hook.setHookedEntity(null);
			hook.remove();
			return;
		}
		
		hooked.damage(0.1D, shooter);
		hook.setHookedEntity(null);
		hook.remove();
		this.pullEntityToLocation(hooked, shooter.getLocation());
		this.getPlayersHookGadget(shooter).removeUses(shooter, 1);
		hooked.getLocation().getWorld().playSound(hooked.getLocation(), Sound.ENTITY_MAGMA_CUBE_JUMP, 10f, 1f);
	}
	
	
	private void pullEntityToLocation(final Player hooked, Location loc) {		
		
		if(hooked.isSneaking()) {
			hooked.setSneaking(false);
		}
		Vector hooker = loc.toVector();						//B
		Vector hookeed = hooked.getLocation().toVector(); 	//A
		
		Vector divVec = new Vector(2, 2, 2);
		
		Vector bewegungsVector = hooker.subtract(hookeed).divide(divVec);
		
		hooked.setVelocity(bewegungsVector);
		
		
	}
}
