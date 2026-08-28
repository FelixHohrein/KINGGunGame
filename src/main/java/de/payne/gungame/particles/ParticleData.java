package de.payne.gungame.particles;

import java.util.Map;
import java.util.UUID;

import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.Maps;

public class ParticleData {

	private static Map<UUID, BukkitTask> trails = Maps.newHashMap();
	
	private final UUID uuid;
	
	public ParticleData(final UUID uuid) {
		this.uuid = uuid;
	}
	
	public final void setTask(BukkitTask bukkitTask) {
		trails.put(this.uuid, bukkitTask);
	}
	
	public final BukkitTask getTask() {
		return trails.get(this.uuid);
	}
	
	public final boolean hasTask() {
		if(trails.containsKey(this.uuid)) {
			return true;
		} else {
			return false;
		}
	}
	
	public final void removeTask() {
		trails.remove(this.uuid);
	}
	
	public void endTask() {
		if(trails.get(uuid).getTaskId() == 0) {
			return;
		}
		trails.get(uuid).cancel();
	}
	
	public static boolean hasFakeID(UUID uuid) {
		if(trails.containsKey(uuid)) {
			if(trails.get(uuid).getTaskId() == 1) {
				return true;
			}
		}
		return false;
	}
}

