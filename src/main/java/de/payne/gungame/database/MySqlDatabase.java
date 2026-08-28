package de.payne.gungame.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import de.payne.gungame.GunGameSettings;
import lombok.Getter;

public class MySqlDatabase {
	
	private final GunGameSettings settings;
	@Getter
	private Connection connection;
	private Plugin databaseOwner;
		
	//Konstruktor
	public MySqlDatabase(final Plugin databaseOwner, final GunGameSettings settings) {
		this.settings = settings;
		this.databaseOwner = databaseOwner;
	}
	

	//connect if connection allowed und not connected
	public final void openConnection() {
		if(!this.settings.isConnectionAllowed()) {
			return;
		}
		
		if(this.isConnected()) {
			return;
		}

		try {
			 this.connection = DriverManager.getConnection("jdbc:mysql://" + this.settings.getHost() + ":" + this.settings.getPort() + "/" +
	                    this.settings.getDatabase() + "?autoReconnect=true", this.settings.getUsername(), this.settings.getPassword());
			 this.databaseOwner.getLogger().info("MySQL --> Verbunden!");
			
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		
	}
	
	//disconnect a connected connection
	public final void closeConnection() {
        if (!isConnected()) {
        	return;
        }
        
        if(!this.settings.isConnectionAllowed()) {
        	return;
        }
        try {
            this.connection.close();
            this.databaseOwner.getLogger().info("MySQL --> Getrennt!");

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

	}
	
	//returns true if connected
	public final boolean isConnected() {
		return (this.connection == null ? false : true);
	}
	
	//open a new connection if no connection
	public final void checkConnection() {
		if(!this.isConnected()) {
			this.openConnection();
		}
	}
	
	//createTable if not exist 
	public final void createTable() {
		if(!this.settings.isConnectionAllowed()) {
			return;
		}
		this.checkConnection();
		
		try(final PreparedStatement preparedStatement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS kgmg_gungame ("
				+ "ID INT NOT NULL,"
				+ "Tokens INT,"
				+ "Kills INT,"
				+ "Tode INT,"
				+ "HighestLevel INT,"
				+ "CurrentLevel INT,"
				+ "PRIMARY KEY (ID),"
				+ "foreign key(ID) references kgmg_players(ID))")){
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
	        exception.printStackTrace();
		    }
		
		this.createGadgetTable();
		this.createKopfgeldTable();
		}
	
	private final void createGadgetTable() {
		
		if(!this.settings.isConnectionAllowed()) {
			return;
		}
		this.checkConnection();
		
		try(final PreparedStatement preparedStatement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS kgmg_gungame_gadgets ("
				+ "ID INT NOT NULL,"
				+ "Hook INT,"
				+ "LevelupI INT,"
				+ "LevelupII INT,"
				+ "LevelupIII INT,"
				+ "Shockwave INT,"
				+ "Joe INT,"
				+ "Backporter INT,"
				+ "PRIMARY KEY (ID),"
				+ "foreign key(ID) references kgmg_players(ID))")){
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
	        exception.printStackTrace();
		    }
	}
	
	private final void createKopfgeldTable() {
		
		if(!this.settings.isConnectionAllowed()) {
			return;
		}
		this.checkConnection();
		
		try(final PreparedStatement preparedStatement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS kgmg_gungame_kopfgeld ("
				+ "ID INT AUTO_INCREMENT,"
				+ "SenderUUID VARCHAR(40),"
				+ "TargetUUID VARCHAR(40),"
				+ "Betrag INT,"
				+ "TimeStampInSeconds BIGINT,"
				+ "PRIMARY KEY (ID),"
				+ "Dauer INT)")){
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
	        exception.printStackTrace();
		    }
	}
	
	
	//register a new player
	public final void registerPlayer(final Player player) {
		this.checkConnection();
		
		if (this.isPlayerExisting(player)) {
			return;
		}
		
		final int id = this.getDatabaseID(player);
		
		try(final PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO kgmg_gungame (ID, Tokens, Kills, Tode, HighestLevel, CurrentLevel) VALUES (?, ?, ?, ?, ?, ?)")) {
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, 100);
			preparedStatement.setInt(3, 0);
			preparedStatement.setInt(4, 0);
			preparedStatement.setInt(5, 1);
			preparedStatement.setInt(6, 1);
    		preparedStatement.execute();
			
		} catch (SQLException exception) {
            exception.printStackTrace();
        }
		
		this.registerGadgetPlayer(player);
	}
	
	//register a new player Gadget
	private final void registerGadgetPlayer(final Player player) {
		this.checkConnection();
		
		final int id = this.getDatabaseID(player);
		
		try(final PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO kgmg_gungame_gadgets (ID, Hook, LevelupI, LevelupII, LevelupIII, Shockwave, Joe, Backporter) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, 0);
			preparedStatement.setInt(3, 0);
			preparedStatement.setInt(4, 0);
			preparedStatement.setInt(5, 0);
			preparedStatement.setInt(6, 0);
			preparedStatement.setInt(7, 0);
			preparedStatement.setInt(8, 0);

    		preparedStatement.execute();
			
		} catch (SQLException exception) {
            exception.printStackTrace();
        }
	}
	
	private final int getDatabaseID(final Player player) {
		this.checkConnection();
		try(final PreparedStatement preparedStatement = this.connection.prepareStatement("select ID from kgmg_players where UUID= ?")) {
			preparedStatement.setString(1, player.getUniqueId().toString());
			
			ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }
			
		} catch (SQLException exception) {
            exception.printStackTrace();
        }
		return -1;
	}

	//returns true if player exists
	public final boolean isPlayerExisting(final Player player) {
		this.checkConnection();
		
		try(final PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT gg.ID FROM kgmg_players play, kgmg_gungame gg WHERE UUID= ? AND gg.ID = play.ID")){
			preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
    		if(resultSet.next()) {
    			return true;
    		}
		} catch (SQLException exception) {
            exception.printStackTrace();
        }
		return false;
	}	
}
