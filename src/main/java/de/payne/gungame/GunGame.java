package de.payne.gungame;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.payne.gungame.armorstand.Armorstand;
import de.payne.gungame.armorstand.HolographicDisplays;
import de.payne.gungame.buffs.Buff;
import de.payne.gungame.buffs.BuffCountdownManager;
import de.payne.gungame.buffs.BuffLocations;
import de.payne.gungame.buffs.BuffManager;
import de.payne.gungame.buffs.BuffTypes;
import de.payne.gungame.buffs.PlayerMoveOnBuffBlockListener;
import de.payne.gungame.buffs.WaterwalkerBuff;
import de.payne.gungame.commands.AdminMenuCommand;
import de.payne.gungame.commands.BuildCommand;
import de.payne.gungame.commands.CreateLowerRanking;
import de.payne.gungame.commands.CreateRanking;
import de.payne.gungame.commands.DeleteMapCommand;
import de.payne.gungame.commands.ForcemapCommand;
import de.payne.gungame.commands.HelpCommand;
import de.payne.gungame.commands.KopfgeldCommand;
import de.payne.gungame.commands.LeaveCommand;
import de.payne.gungame.commands.RankCommand;
import de.payne.gungame.commands.RegistermapCommand;
import de.payne.gungame.commands.SetBuffLocation;
//import de.payne.gungame.commands.SetLanguageCommand;
import de.payne.gungame.commands.SetLobbyCommand;
import de.payne.gungame.commands.SetShopCommand;
import de.payne.gungame.commands.TeamCommand;
import de.payne.gungame.commands.VoteCommand;
import de.payne.gungame.config.BuffLocationConfig;
import de.payne.gungame.config.LevelConfig;
import de.payne.gungame.config.MapConfig;
import de.payne.gungame.config.TextMessageFile;
import de.payne.gungame.database.GunGamePlayer;
import de.payne.gungame.database.MySqlDatabase;
import de.payne.gungame.database.StatistikTable;
import de.payne.gungame.gadgets.Backporter;
import de.payne.gungame.gadgets.Hook;
import de.payne.gungame.gadgets.InstantLevel;
import de.payne.gungame.gadgets.JoeGadget;
import de.payne.gungame.gadgets.Shockwave;
import de.payne.gungame.kopfgeld.Kopfgeld;
import de.payne.gungame.kopfgeld.KopfgeldInventory;
import de.payne.gungame.language.TextEngine;
import de.payne.gungame.listener.block.BlockBreakListener;
import de.payne.gungame.listener.block.BlockPhysicsListener;
import de.payne.gungame.listener.block.BlockPlaceListener;
import de.payne.gungame.listener.block.HangingBreakListener;
import de.payne.gungame.listener.block.HangingPlaceListener;
import de.payne.gungame.listener.block.LeavesDecayListener;
import de.payne.gungame.listener.block.ProtectFarmlandListener;
import de.payne.gungame.listener.block.WeatherChangeListener;
import de.payne.gungame.listener.player.PlayerArmorStandManipulateListener;
import de.payne.gungame.listener.player.PlayerBucketEmptyListener;
import de.payne.gungame.listener.player.PlayerBucketFillListener;
import de.payne.gungame.listener.player.PlayerDamageListenerDeathLogic;
import de.payne.gungame.listener.player.PlayerDropItemListener;
import de.payne.gungame.listener.player.PlayerFallDamageListener;
import de.payne.gungame.listener.player.PlayerFoodChangeListener;
import de.payne.gungame.listener.player.PlayerJoinListener;
import de.payne.gungame.listener.player.PlayerMoveListener;
import de.payne.gungame.listener.player.PlayerQuitListener;
import de.payne.gungame.listener.player.PlayerSpawnLocationListener;
import de.payne.gungame.listener.player.PlayerSpawnProtectionDamageListener;
import de.payne.gungame.map.GameMap;
import de.payne.gungame.scoreboard.ScorboardManager;
import de.payne.gungame.shop.ShopGui;
import de.payne.gungame.shop.ShopVillager;
import de.payne.gungame.shop.VillagerInteractListener;
import de.payne.gungame.signs.SignBuilder;
import de.payne.gungame.signs.SignListener;
import de.payne.gungame.signs.SignPhase;
import de.payne.gungame.team.Team;
import de.payne.gungame.vote.VoteEngine;
import de.payne.gungame.vote.VoteHandler;
import lombok.Getter;
import net.kyori.adventure.text.Component;



public final class GunGame extends JavaPlugin {
	
	@Getter
	private GunGameSettings settings;
	@Getter
	private MySqlDatabase mySqlDatabase;
	@Getter
	private GameMap currentMap;
	@Getter
	private GameMap lobbySpawn;
	@Getter
	private MapConfig mapConfig;
	@Getter
	private LevelConfig levelConfig;
	@Getter
	private TextMessageFile textMessageFile;
	@Getter
	private TextEngine textEngine;
//	@Getter
//	private VoteManager voteManager;
	@Getter
	private StatistikTable statisticTable;
	@Getter
	private String prefix;
	@Getter
	private Component prefixComponent;
	@Getter
	private GunGameEngine gunGameEngine;
	@Getter
	private GunGameManager gunGameManager;
	@Getter
	private List<Player> ingameList = Lists.newArrayList();
	@Getter
	private SignBuilder signBuilder;
	@Getter
	private VoteEngine voteEngine;
	@Getter
	private Map <UUID, GunGamePlayer> gungamePlayers = Maps.newConcurrentMap();
    private int lastMapswitch;
    @Getter
    private List<Player> builders = Lists.newArrayList();
	@Getter
	private final Map <Player, Buff> buffedPlayers = Maps.newHashMap();
	@Getter
	private BuffLocationConfig buffLocationConfig;
	@Getter
	private List<BuffLocations> buffLocationsCurrentmap = Lists.newArrayList();
	@Getter
	private List<BuffTypes> posibleBuffs = Lists.newArrayList();
	@Getter
	private BuffManager buffManager = new BuffManager();
	@Getter
	private BuffCountdownManager buffCountdownManager = new BuffCountdownManager();
	@Getter
	private ScorboardManager scoreboardManager = new ScorboardManager();
	@Getter
	private ShopGui shopGui = new ShopGui();
	@Getter
	private ShopVillager shopNpc = new ShopVillager();
	@Getter
	private HolographicDisplays holographicDisplays;
	@Getter
	private VoteHandler voteHandler;
	
	
	
	@Override
	public final void onEnable() {
		this.settings = new GunGameSettings(this);
		this.mySqlDatabase = new MySqlDatabase(this, this.settings);
		this.statisticTable = new StatistikTable(this.mySqlDatabase);
		
		this.mapConfig = new MapConfig(this, "maps.yml");
		this.levelConfig = new LevelConfig(this, "level.yml");
		this.buffLocationConfig = new BuffLocationConfig(this, "bufflocations.yml");
		this.textMessageFile = new TextMessageFile(this, "messages.yml");
		this.textEngine = new TextEngine();
		
//		this.voteManager = new VoteManager();
		this.signBuilder = new SignBuilder(this.settings.getSign());
		this.gunGameEngine = new GunGameEngine();
		this.gunGameManager = new GunGameManager();
		this.voteEngine = new VoteEngine();
		
		//MySQL connection afbauen und table erstellen if not exists
		this.mySqlDatabase.openConnection();
		this.mySqlDatabase.createTable();

		//fills the level.yml with examples
		this.levelConfig.setLevelNormal();
		//loads the hashmap with the current playing mod from config
		this.levelConfig.loadItemsToMap();
		
		this.voteHandler = new VoteHandler();
		//puts into the VoteMap (ArrayList) the Name of the registerd Maps as GameMap objekt, and the Votable Map Objekt for the registerd Map
		//nur wenn auch maps in der config enthalten sind, sonst if methode false
		if(this.mapConfig.mapsInsideFile()) {
			this.currentMap = this.mapConfig.getGameMaps().get(1);
			
//			for(final GameMap registeredMaps : this.mapConfig.getVotableMaps()) {
//				this.voteManager.getVoteMap().put(registeredMaps.getMapname(), new VotableMap(registeredMaps));
//			}
		}
		
		if(this.buffLocationConfig.isMapBuffExists(currentMap)) {
			for(final BuffLocations buffLocations : this.buffLocationConfig.getBuffPositions(this.currentMap.getMapname())) {
				this.buffLocationsCurrentmap.add(buffLocations);
			}
		}		
		//sets the lobbyspawn from config to variable
		if(this.mapConfig.alreadyExists("Lobby")) {
			this.lobbySpawn = this.mapConfig.getGameMap("Lobby");
		}
		
		//FOR ARMORSTANDS
		if (this.statisticTable.getSize() >= 3) {
			if (this.settings.topPlayersExists()) {
				if (this.settings.getArmorstandPositions().size() == 3) {
					Map<Integer, Location> rankLoc = this.settings.getArmorstandPositions();
					for (int i = 1; i <= 3; i++) {
						UUID uuid = UUID.fromString(this.statisticTable.getTopRanks().get(i));
						Location loc = rankLoc.get(i);
						new Armorstand(i, uuid, loc);
					}
				}
			}
		}
		
		//DISABLE PLUGIN IF HOLOGRAPHIC DISPLAY IS NOT INSTALLED
		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			getLogger().severe("*** HolographicDisplays ist nicht installiert. ***");
			getLogger().severe("*** Das Plugin wird deaktiviert... ***");
			this.setEnabled(false);
			return;
		}
		
		//CREATES SHOP ON SERVER START
		if(this.settings.isShopExists()) {
			this.shopNpc.createVillager(this.settings.getShopPosition());
		}

		//CREATES TOP WALL HOLOGRAPHIC DISPLAYS
		if(this.settings.topPlayersLowerExists()){
			this.holographicDisplays = new HolographicDisplays();
		}
		
		//CACHES THE KOPFGELDER IN LIST
		for(Kopfgeld kopfgeld : this.statisticTable.getKopfgelder()) {
			Kopfgeld.getKopfgelder().put(kopfgeld.getId(), kopfgeld);
		}
		
		
		//FOR CACHING DATA -> WENN RELOAD ALLE ONLINEPLAYERS IN MAP-----
		if(Bukkit.getOnlinePlayers().size() > 0) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				//CREATING GUNGAMEPLAYER OBJECT
				this.gungamePlayers.put(player.getUniqueId(), new GunGamePlayer(player.getUniqueId()));
				//setting correct level
				player.setLevel(this.gungamePlayers.get(player.getUniqueId()).getCurrentLevel());
				//SETTING UP THE SCOREBOARD
				this.scoreboardManager.createBoard(player);
				this.scoreboardManager.updateBoard(player);
			}
		}
		//---------------------------------------------------------------
		
		//add all BuffTypes to possibleBuffs ArrayList
		for(BuffTypes nextBuff : BuffTypes.values()) {
			this.posibleBuffs.add(nextBuff);
		}
		
		this.lastMapswitch = Math.round(System.currentTimeMillis() / 1000);
		this.prefix = this.settings.getPrefix();
		this.prefixComponent = Component.text(this.prefix);
		this.registerCommands();
		this.registerListeners();
		this.changeTime();
		this.autoMapswitch();
		this.buffManager.autoBuffRefresh();
		this.signBuilder.signUpdatePhase(SignPhase.ONLINE);
		this.getLogger().info("Das Plugin wurde erfolgreich gestartet!");
	}
	
	@Override
	public final void onDisable() {

		for(Player player : Bukkit.getOnlinePlayers()) {
			
			if(this.ingameList.contains(player)) {
				player.teleport(this.lobbySpawn.getSpawnLocation());
				player.getInventory().clear();
				player.setLevel(this.gungamePlayers.get(player.getUniqueId()).getCurrentLevel());
			}
			else if(this.builders.contains(player)) {
				player.teleport(this.lobbySpawn.getSpawnLocation());
				player.getInventory().clear();
				player.setLevel(this.gungamePlayers.get(player.getUniqueId()).getCurrentLevel());
			}
			
			this.statisticTable.setValues(this.gungamePlayers.get(player.getUniqueId()));
			this.statisticTable.setGadgets(this.gungamePlayers.get(player.getUniqueId()));
		}
		
		//saves all kopfgelder inside database
		for(Kopfgeld kopfgeld : Kopfgeld.getKopfgelder().values()) {
			this.statisticTable.setKopfgelder(kopfgeld);
		}
		
		this.signBuilder.signUpdatePhase(SignPhase.OFFLINE);

		this.mySqlDatabase.closeConnection();
	}
	
	private final void registerCommands() {
	//USER COMMANDS
	getCommand("help").setExecutor(new HelpCommand());
	getCommand("help").setTabCompleter(new HelpCommand());//null

	getCommand("leave").setExecutor(new LeaveCommand());
	getCommand("leave").setTabCompleter(new LeaveCommand());//null

	getCommand("vote").setExecutor(new VoteCommand());
	getCommand("vote").setTabCompleter(new VoteCommand());  // all votable maps

	//USER COMMANDS
//	getCommand("setlanguage").setExecutor(new SetLanguageCommand());
//	getCommand("setlanguage").setTabCompleter(new SetLanguageCommand());//null
	
//	getCommand("votemap").setExecutor(new VoteMapCommand());
//	getCommand("votemap").setTabCompleter(new VoteMapCommand());//start command
	
	getCommand("rank").setExecutor(new RankCommand());
	getCommand("rank").setTabCompleter(new RankCommand());//start command

	getCommand("kopfgeld").setExecutor(new KopfgeldCommand());
	getCommand("kopfgeld").setTabCompleter(new KopfgeldCommand());

	getCommand("team").setExecutor(new TeamCommand());
	getCommand("team").setTabCompleter(new TeamCommand());
	
	//SOMETHING BETWEEN
	getCommand("forcemap").setExecutor(new ForcemapCommand());
	getCommand("forcemap").setTabCompleter(new ForcemapCommand());
	
	getCommand("build").setExecutor(new BuildCommand());
	getCommand("build").setTabCompleter(new BuildCommand());

	//ADMIN COMMANDS
	getCommand("setlobby").setExecutor(new SetLobbyCommand());
	getCommand("setlobby").setTabCompleter(new SetLobbyCommand());

	getCommand("setshop").setExecutor(new SetShopCommand());
	getCommand("setshop").setTabCompleter(new SetShopCommand());

	getCommand("adminmenu").setExecutor(new AdminMenuCommand());
	getCommand("adminmenu").setTabCompleter(new AdminMenuCommand());

	getCommand("createranking").setExecutor(new CreateRanking());
	getCommand("createranking").setTabCompleter(new CreateRanking());

	getCommand("createlowerranking").setExecutor(new CreateLowerRanking());
	getCommand("createlowerranking").setTabCompleter(new CreateLowerRanking());
	
	getCommand("setbufflocation").setExecutor(new SetBuffLocation());
	getCommand("setbufflocation").setTabCompleter(new SetBuffLocation());
	
	getCommand("registermap").setExecutor(new RegistermapCommand());
	getCommand("registermap").setTabCompleter(new RegistermapCommand());

	getCommand("deletemap").setExecutor(new DeleteMapCommand());
	getCommand("deletemap").setTabCompleter(new DeleteMapCommand());

	}
	
	private final void registerListeners() {
	//PLAYEREVENTS
		
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerSpawnLocationListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
		getServer().getPluginManager().registerEvents(new KopfgeldInventory(), this);

		getServer().getPluginManager().registerEvents(new PlayerDamageListenerDeathLogic(), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveOnBuffBlockListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerFallDamageListener(), this);
		
		getServer().getPluginManager().registerEvents(new WaterwalkerBuff(), this);
		getServer().getPluginManager().registerEvents(new PlayerArmorStandManipulateListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerBucketEmptyListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerBucketFillListener(), this);

		//GADGETS
		getServer().getPluginManager().registerEvents(new Hook(), this);
		getServer().getPluginManager().registerEvents(new InstantLevel(), this);
		getServer().getPluginManager().registerEvents(new VillagerInteractListener(), this);
		getServer().getPluginManager().registerEvents(new Shockwave(), this);
		getServer().getPluginManager().registerEvents(new JoeGadget(), this);
		getServer().getPluginManager().registerEvents(new Backporter(), this);

		getServer().getPluginManager().registerEvents(new PlayerSpawnProtectionDamageListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);

		getServer().getPluginManager().registerEvents(new PlayerFoodChangeListener(), this);

	//WORLD EVENTS
		getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);
		getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
		getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
		getServer().getPluginManager().registerEvents(new ProtectFarmlandListener(), this);
		getServer().getPluginManager().registerEvents(new BlockPhysicsListener(), this);
		getServer().getPluginManager().registerEvents(new HangingPlaceListener(), this);
		getServer().getPluginManager().registerEvents(new HangingBreakListener(), this);
		getServer().getPluginManager().registerEvents(new LeavesDecayListener(), this);
	//ELSE
		getServer().getPluginManager().registerEvents(new SignListener(), this);

	}
	
	public final void setCurrentMap(final GameMap currentMap) {
		
		this.currentMap = currentMap;
		
		//UPDATES DATABASE WITH CACHED STUFF
		for(Player p : Bukkit.getOnlinePlayers()) {
			this.statisticTable.setValues(this.gungamePlayers.get(p.getUniqueId()));
			this.statisticTable.setGadgets(this.gungamePlayers.get(p.getUniqueId()));
			this.scoreboardManager.updateBoard(p);

		}
		
		if(!this.currentMap.getTeamsErlaubt()) {
			for(Team teams : Team.getTeams()) {
				teams.closeTeam();
			}
		}

		// UPDATES ARMORSTAND
		for (int i = 1; i <= 3; i++) {
			Armorstand stand = Armorstand.getArmorstands().get(i);
			UUID uuid = UUID.fromString(this.statisticTable.getTopRanks().get(i));
			stand.changePlayers(uuid);
		}
		//UPDATE HOLOS
		if(this.holographicDisplays != null) {
			this.holographicDisplays.setValues();
		}

		// AUTOMAPSWITCH CURRENTLY NOT WORKING
		this.lastMapswitch = Math.round(System.currentTimeMillis() / 1000);

		
		
		this.buffLocationsCurrentmap.clear();
		this.buffManager.getBuffCooldown().clear();
		this.buffManager.getBuffTimetoCollectMap().clear();
		
		if(this.buffLocationConfig.isMapBuffExists(currentMap)) {
			for(final BuffLocations buffLocations : this.buffLocationConfig.getBuffPositions(this.currentMap.getMapname())) {
				this.buffLocationsCurrentmap.add(buffLocations);
			}
		} 
	}
	
	
    private final void changeTime() {
    	Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				if(currentMap.getMapname().equals("Village")) {
					currentMap.getSpawnLocation().getWorld().setTime(18000);
				} else {
		               currentMap.getSpawnLocation().getWorld().setTime(0);
				}
               lobbySpawn.getSpawnLocation().getWorld().setTime(18000);
			}
    		
    	}, 2000L, 2000L); //alle 2000 ticks 20 ticks/sekunde = alle 100 sekunden
    }    
    
    private final void autoMapswitch() {
    	
    	Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
        	int currentTime = Math.round(System.currentTimeMillis() / 1000);

			@Override
			public void run() {
				if(currentTime - lastMapswitch > GunGame.getInstance().getSettings().getVoteAutoTaskTime()) {
					voteEngine.mapWechsel(settings.getVoteTime(), null);
				}
				
			}
    		
    	}, 100, 600L); // alle 30 Sekunden
    }
	
    //returns true if string = integer
    public final boolean isInteger(String string) {
	    try { 
	        Integer.parseInt(string); 
	    } catch(NumberFormatException exception) { 
	    	System.out.println("[GunGame] - [PARSING] > String is not an Integer");
	        return false; 
	    }
	    return true;
	}
    
	public static GunGame getInstance() {
		return JavaPlugin.getPlugin(GunGame.class);
	}
	
}
