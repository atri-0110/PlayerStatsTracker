package com.atri0110.playerstatstracker;

import com.atri0110.playerstatstracker.commands.StatsCommand;
import com.atri0110.playerstatstracker.commands.LeaderboardCommand;
import com.atri0110.playerstatstracker.data.PlayerDataManager;
import com.atri0110.playerstatstracker.listeners.PlayerEventListener;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.registry.Registries;
import org.allaymc.api.server.Server;

public class PlayerStatsTracker extends Plugin {
    
    private static PlayerStatsTracker instance;
    private PlayerDataManager dataManager;
    private PlayerEventListener eventListener;
    
    public static PlayerStatsTracker getInstance() {
        return instance;
    }
    
    @Override
    public void onLoad() {
        instance = this;
        this.pluginLogger.info("PlayerStatsTracker is loading...");
        
        this.dataManager = new PlayerDataManager(this);
        this.dataManager.loadData();
        
        this.pluginLogger.info("PlayerStatsTracker data loaded successfully!");
    }
    
    @Override
    public void onEnable() {
        this.pluginLogger.info("PlayerStatsTracker is enabling...");
        
        this.eventListener = new PlayerEventListener(this.dataManager);
        Server.getInstance().getEventBus().registerListener(this.eventListener);
        
        registerCommands();
        
        this.pluginLogger.info("PlayerStatsTracker enabled successfully!");
        this.pluginLogger.info("Tracking player statistics...");
    }
    
    @Override
    public void onDisable() {
        this.pluginLogger.info("PlayerStatsTracker is disabling...");
        
        if (this.dataManager != null) {
            this.dataManager.saveAllData();
        }
        
        if (this.eventListener != null) {
            Server.getInstance().getEventBus().unregisterListener(this.eventListener);
        }
        
        this.pluginLogger.info("PlayerStatsTracker disabled. All data saved!");
    }
    
    private void registerCommands() {
        Registries.COMMANDS.register(new StatsCommand(this.dataManager));
        Registries.COMMANDS.register(new LeaderboardCommand(this.dataManager));
        
        this.pluginLogger.info("Commands registered: /stats, /leaderboard");
    }
    
    public PlayerDataManager getDataManager() {
        return this.dataManager;
    }
}
