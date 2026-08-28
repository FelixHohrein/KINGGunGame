package de.payne.gungame.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.payne.gungame.GunGame;
import de.payne.gungame.gadgets.GadgetTypes;
import de.payne.gungame.gadgets.Gadgets;
import de.payne.gungame.kopfgeld.Kopfgeld;
import de.payne.gungame.language.LANGUAGE;

public class StatistikTable {
	
	private final MySqlDatabase mySqlDatabase;
	
	//Konstruktor
	public StatistikTable(final MySqlDatabase mySqlDatabase) {
		this.mySqlDatabase = mySqlDatabase;
	}
	
	//GETTING ALL VALUES FROM GUNGAME SAVED IN MAP
	public final Map<String, Integer> results (final UUID uuid) {
		Map<String, Integer> zwischenspeicher = Maps.newHashMap();

		this.mySqlDatabase.checkConnection();

		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("SELECT gg.ID, Tokens, Kills, Tode, HighestLevel, CurrentLevel FROM kgmg_gungame gg, kgmg_players play WHERE UUID= ? AND gg.ID = play.ID;")) {
			preparedStatement.setString(1, uuid.toString());
			ResultSet resultSet = preparedStatement.executeQuery();
			
            while (resultSet.next()) {
            	int id = resultSet.getInt("ID");
            	int Tokens = resultSet.getInt("Tokens");
            	int kills = resultSet.getInt("Kills");
            	int deaths = resultSet.getInt("Tode");
            	int highestLevel = resultSet.getInt("HighestLevel");
            	int currentLevel = resultSet.getInt("CurrentLevel");
            	zwischenspeicher.put("id", id);
            	zwischenspeicher.put("tokens", Tokens);
            	zwischenspeicher.put("kills", kills);
            	zwischenspeicher.put("deaths", deaths);
            	zwischenspeicher.put("highestLevel", highestLevel);
            	zwischenspeicher.put("currentLevel", currentLevel);
            }
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		return zwischenspeicher;
	}
	
	//GET THE FIRSTJOIN DATE
	public final String getFirstJoin(final UUID uuid) {
		
		this.mySqlDatabase.checkConnection();
		
		String firstJoin = "FEHLER";
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("SELECT Firstjoin FROM kgmg_players WHERE UUID= ?")) {
			preparedStatement.setString(1, uuid.toString());
			ResultSet resultSet = preparedStatement.executeQuery();
			
            while (resultSet.next()) {
            	firstJoin = resultSet.getString("Firstjoin");
            }
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		return firstJoin;
	}
	
	public final LANGUAGE getLanguage(final UUID uuid) {
		this.mySqlDatabase.checkConnection();
		String language = "FEHLER";
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("SELECT Language FROM kgmg_players WHERE UUID= ?")) {
			preparedStatement.setString(1, uuid.toString());
			ResultSet resultSet = preparedStatement.executeQuery();
			
            while (resultSet.next()) {
            	language = resultSet.getString("Language");
            }
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		return LANGUAGE.valueOf(language);
	}
	
	public final void setLanguage(final LANGUAGE l, final UUID uuid) {
		this.mySqlDatabase.checkConnection();
		
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("UPDATE kgmg_players SET Language = ? WHERE UUID = ?")) {
			preparedStatement.setString(1, String.valueOf(l));
			preparedStatement.setString(2, uuid.toString());
			preparedStatement.executeUpdate();
			
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	//SET THE GUNGAME VALUES (UPDATE)
	public final void setValues(final GunGamePlayer gunGamePlayer) {
		this.mySqlDatabase.checkConnection();
		
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("UPDATE kgmg_gungame gg, kgmg_players play SET Tokens = ?, Kills = ?, Tode = ?, HighestLevel = ?, CurrentLevel = ? WHERE UUID = ? AND gg.ID = play.ID;")) {
			preparedStatement.setInt(1, gunGamePlayer.getTokens());
			preparedStatement.setInt(2, gunGamePlayer.getKills());
			preparedStatement.setInt(3, gunGamePlayer.getDeaths());
			preparedStatement.setInt(4, gunGamePlayer.getHighestLevel());
			preparedStatement.setInt(5, gunGamePlayer.getCurrentLevel());
			preparedStatement.setString(6, gunGamePlayer.getUuid().toString());
			preparedStatement.executeUpdate();
			
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	//GETS THE GADGETS A PLAYER IS HOLDING
	public final List<Gadgets> getGadget(final UUID uuid){
		List <Gadgets> zwischenspeicher = Lists.newArrayList();
		this.mySqlDatabase.checkConnection();

		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("SELECT ggg.Hook, ggg.LevelupI, ggg.LevelupII, ggg.LevelupIII, ggg.Shockwave, ggg.Joe, ggg.Backporter from kgmg_gungame_gadgets ggg, kgmg_players play where UUID=? and ggg.ID = play.ID")) {																													
			preparedStatement.setString(1, uuid.toString());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
            while (resultSet.next()) {
            	
            final int amountHook = resultSet.getInt("Hook");
			final Gadgets hook = new Gadgets(uuid, GadgetTypes.HOOK);
			hook.setAmount(amountHook);
			hook.updateItemStack(uuid);
			
            final int amountInstantLevelI = resultSet.getInt("LevelupI");
			final Gadgets instantlevelI = new Gadgets(uuid, GadgetTypes.INSTANTLEVELI);
			instantlevelI.setAmount(amountInstantLevelI);
			instantlevelI.updateItemStack(uuid);
			
            final int amountInstantLevelII = resultSet.getInt("LevelupII");
			Gadgets instantlevelII = new Gadgets(uuid, GadgetTypes.INSTANTLEVELII);
			instantlevelII.setAmount(amountInstantLevelII);
			instantlevelII.updateItemStack(uuid);

			
            final int amountInstantLevelIII = resultSet.getInt("LevelupIII");
			Gadgets instantlevelIII = new Gadgets(uuid, GadgetTypes.INSTANTLEVELIII);
			instantlevelIII.setAmount(amountInstantLevelIII);
			instantlevelIII.updateItemStack(uuid);
			
			final int amountShockwave = resultSet.getInt("Shockwave");
			Gadgets shockwave = new Gadgets(uuid, GadgetTypes.SHOCKWAVE);
			shockwave.setAmount(amountShockwave);
			shockwave.updateItemStack(uuid);

			final int amountJoe = resultSet.getInt("Joe");
			Gadgets joe = new Gadgets(uuid, GadgetTypes.JOE);
			joe.setAmount(amountJoe);
			joe.updateItemStack(uuid);
		
			final int amountBackporter = resultSet.getInt("Backporter");
			Gadgets backporter = new Gadgets(uuid, GadgetTypes.BACKPORTER);
			backporter.setAmount(amountBackporter);
			backporter.updateItemStack(uuid);
			
			
			zwischenspeicher.add(0, hook);
			zwischenspeicher.add(1, instantlevelI);
			zwischenspeicher.add(2, instantlevelII);
			zwischenspeicher.add(3, instantlevelIII);
			zwischenspeicher.add(4, shockwave);
			zwischenspeicher.add(5, joe);
			zwischenspeicher.add(6, backporter);


            }
            
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		
		return zwischenspeicher;
	}
	
	//SETS THE NEW GADGETS OF AN PLAYER IS HOLDING WITH THE AMMOUNT AS VALUE
	public final void setGadgets(final GunGamePlayer gunGamePlayer) {
		this.mySqlDatabase.checkConnection();
		
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("UPDATE kgmg_gungame_gadgets ggg, kgmg_players play set Hook = ?, LevelupI = ?, LevelupII = ?, LevelupIII = ?, Shockwave = ?, Joe = ?, Backporter = ? where UUID = ? and ggg.ID = play.ID;")) {
			preparedStatement.setInt(1, gunGamePlayer.getGadgets().get(0).getAmount());
			preparedStatement.setInt(2, gunGamePlayer.getGadgets().get(1).getAmount());
			preparedStatement.setInt(3, gunGamePlayer.getGadgets().get(2).getAmount());
			preparedStatement.setInt(4, gunGamePlayer.getGadgets().get(3).getAmount());
			preparedStatement.setInt(5, gunGamePlayer.getGadgets().get(4).getAmount());
			preparedStatement.setInt(6, gunGamePlayer.getGadgets().get(5).getAmount());
			preparedStatement.setInt(7, gunGamePlayer.getGadgets().get(6).getAmount());
			preparedStatement.setString(8, gunGamePlayer.getUuid().toString());
			preparedStatement.executeUpdate();
			
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	//INSERTS NEW KOPFGELDER AND UPDATE EXISTS
	public final void setKopfgelder(final Kopfgeld kopfgelder) {
		this.mySqlDatabase.checkConnection();
		
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("INSERT INTO kgmg_gungame_kopfgeld (ID, SenderUUID, TargetUUID, Betrag, TimeStampInSeconds, Dauer) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE SenderUUID = ?, TargetUUID = ?, Betrag = ?, TimeStampInSeconds = ?, Dauer = ?")) {
			preparedStatement.setInt(1, kopfgelder.getId());
			preparedStatement.setString(2, kopfgelder.getSenderUUID().toString());
			preparedStatement.setString(3, kopfgelder.getTargetUUID().toString());
			preparedStatement.setInt(4, kopfgelder.getAmount());
			preparedStatement.setLong(5, kopfgelder.getTimeStampInSeconds());
			preparedStatement.setInt(6, kopfgelder.getDuration());
			
			//ON DUPLICATE KEY
			preparedStatement.setString(7, kopfgelder.getSenderUUID().toString());
			preparedStatement.setString(8, kopfgelder.getTargetUUID().toString());
			preparedStatement.setInt(9, kopfgelder.getAmount());
			preparedStatement.setLong(10, kopfgelder.getTimeStampInSeconds());
			preparedStatement.setInt(11, kopfgelder.getDuration());

			preparedStatement.executeUpdate();
			
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public final void deleteKopfgeld(Kopfgeld kopfgeld) {
		this.mySqlDatabase.checkConnection();
		
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("DELETE FROM kgmg_gungame_kopfgeld WHERE ID = ?")) {
			preparedStatement.setInt(1, kopfgeld.getId());
			preparedStatement.executeUpdate();
			
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	
	//GETTING ALL KOPFGELDER SAVED IN LIST
	public final List<Kopfgeld> getKopfgelder() {
		List<Kopfgeld> zwischenspeicher = Lists.newArrayList();

		this.mySqlDatabase.checkConnection();
		
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("SELECT ID, SenderUUID, TargetUUID, Betrag, TimeStampInSeconds, Dauer FROM kgmg_gungame_kopfgeld;")) {
			ResultSet resultSet = preparedStatement.executeQuery();
			
            while (resultSet.next()) {
            	int id = resultSet.getInt("ID");
            	UUID sender = UUID.fromString(resultSet.getString("SenderUUID"));
            	UUID target = UUID.fromString(resultSet.getString("TargetUUID"));
            	int amount = resultSet.getInt("Betrag");
            	long timeStampInSeconds = resultSet.getLong("TimeStampInSeconds");
            	int duration = resultSet.getInt("Dauer");
            	
            	Kopfgeld kopfgeld = new Kopfgeld(false, sender, target, amount, timeStampInSeconds, duration, id);
            	zwischenspeicher.add(kopfgeld);
            }
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		return zwischenspeicher;
	}
	
	public final int getLastID() {
		int lastID = -1;
		this.mySqlDatabase.checkConnection();
		
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("SELECT ID FROM kgmg_gungame_kopfgeld WHERE ID=(SELECT max(ID) FROM kgmg_gungame_kopfgeld);")) {
			ResultSet resultSet = preparedStatement.executeQuery();
			
            while (resultSet.next()) {    	
            	int id = resultSet.getInt("ID");
            	lastID = id;
            }
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		return lastID;
		
	}
	
	//RETURNS THE TOP 10 PLAYERS BY POINTS
	public final Map<Integer, String> getTopRanks() {
		Map<Integer, String> topRanks = Maps.newHashMap();
		this.mySqlDatabase.checkConnection();
		
		
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("SELECT UUID FROM kgmg_gungame gg, kgmg_players play WHERE gg.ID= play.ID ORDER BY Kills DESC LIMIT 10")){
			ResultSet resultSet = preparedStatement.executeQuery();
			int i = 1;
			while(resultSet.next()) {
				String uuid = resultSet.getString("UUID");
				topRanks.put(i, uuid);
				i++;
			}
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		return topRanks;
	}	
	
	
	public final int getRank(final UUID uuid) {
		this.mySqlDatabase.checkConnection();

		int rank = -1;
		
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("SELECT UUID FROM kgmg_gungame gg, kgmg_players play WHERE  gg.ID= play.ID ORDER BY Kills DESC;")) {
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String uuid2 = resultSet.getString("UUID");
				if(uuid2.equalsIgnoreCase(uuid.toString())) {
					rank = resultSet.getRow();
					break;
					//resultSet.getRow();
				}
			}
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		return rank;
	}
	
	//RETURNS THE SIZE OF THE TABLE
	public final int getSize() {
		this.mySqlDatabase.checkConnection();
		try(final PreparedStatement preparedStatement = GunGame.getInstance().getMySqlDatabase().getConnection().prepareStatement("SELECT COUNT(*) FROM kgmg_gungame")){
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return resultSet.getInt(1);
			}
			
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		return -1;
	}
	
	
}
