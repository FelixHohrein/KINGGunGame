package de.payne.gungame.language;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;

public class TextEngine {

	private static Map<LANGUAGE, Map<MESSAGE, String>> messages = Maps.newHashMap();

	
	public TextEngine(){
		messages.put(LANGUAGE.DE, this.messageDE());
		messages.put(LANGUAGE.EN, this.messageEN());
	}
	
	
	
	private final Map<MESSAGE, String> messageDE() {
		Map<MESSAGE, String> map = Maps.newHashMap();
		map.putAll(GunGame.getInstance().getTextMessageFile().getMessages(LANGUAGE.DE));
		
		
		return map;
	}
	private final Map<MESSAGE, String> messageEN() {
		Map<MESSAGE, String> map = Maps.newHashMap();
		map.putAll(GunGame.getInstance().getTextMessageFile().getMessages(LANGUAGE.EN));

		return map;
	}
	
	
	public final String changePlaceholders(String messageToReplace, String target, String replacement) {
		
		return messageToReplace.replace(target, replacement);
	}
	
	
	public final String getMessage(final MESSAGE message, final UUID uuid, final boolean withPrefix) {
		LANGUAGE language;
		if(GunGame.getInstance().getGungamePlayers().containsKey(uuid)) {
			language = GunGame.getInstance().getGungamePlayers().get(uuid).getLanguage();
		} else {
			language = GunGame.getInstance().getStatisticTable().getLanguage(uuid);
		}
		 
		if(withPrefix) {
			return (GunGame.getInstance().getPrefix() + messages.get(language).get(message));
		} else {
			return (messages.get(language).get(message));
		}
	}
}
