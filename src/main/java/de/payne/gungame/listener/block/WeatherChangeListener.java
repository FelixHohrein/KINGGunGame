package de.payne.gungame.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public final class WeatherChangeListener implements Listener {

	@EventHandler
	public final void onWeatherChange(final WeatherChangeEvent event) {
		if(event.toWeatherState()) {
			event.setCancelled(true);
		}
	}
}