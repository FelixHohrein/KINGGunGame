 package de.payne.gungame.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import de.payne.gungame.language.LANGUAGE;
import de.payne.gungame.language.MESSAGE;

public final class TextMessageFile  {
	
	private final File messageFile;
	private final YamlConfiguration messageConfiguration;

	
	//Konstruktor
	public TextMessageFile(final Plugin plugin, final String fileName) {
		this.messageFile = new File(plugin.getDataFolder() + File.separator + fileName);
		
		if(!this.messageFile.exists()) {
			try {
				this.messageFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.messageConfiguration = YamlConfiguration.loadConfiguration(this.messageFile);

	}
	
	public final String getMessage(final Player player, final MESSAGE message) {
		String path = "message." + GunGame.getInstance().getGungamePlayers().get(player.getUniqueId()).getLanguage().name() + "." + message.name();
		return ChatColor.translateAlternateColorCodes('§', this.messageConfiguration.getString(path));
	}
	
	
	public final Map<MESSAGE, String> getMessages(LANGUAGE l){
		Map<MESSAGE, String> map = Maps.newHashMap();
				
		for(String s  : this.messageConfiguration.getConfigurationSection("message." + l.name()).getKeys(false)) {
			String path = "message." + l.name() + "." + s;
			
			map.put(MESSAGE.valueOf(s), ChatColor.translateAlternateColorCodes('§', this.messageConfiguration.getString(path)));
		}
		
		return map;
		
	}

}
