package com.atri0110.playerstatstracker.data;

import com.atri0110.playerstatstracker.PlayerStatsTracker;
import com.atri0110.playerstatstracker.stats.PlayerStats;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.allaymc.api.entity.interfaces.EntityPlayer;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    
    private final PlayerStatsTracker plugin;
    private final Map<UUID, PlayerStats> playerData;
    private final Gson gson;
    private final File dataFolder;
    private final File dataFile;
    
    public PlayerDataManager(PlayerStatsTracker plugin) {
        this.plugin = plugin;
        this.playerData = new ConcurrentHashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.dataFolder = PlayerStatsTracker.getInstance().getPluginContainer().dataFolder().toFile();
        this.dataFile = new File(dataFolder, "playerdata.json");
    }
    
    public void loadData() {
        if (!dataFile.exists()) {
            plugin.getPluginLogger().info("No existing player data found. Starting fresh.");
            return;
        }
        
        try (Reader reader = new FileReader(dataFile)) {
            Map<String, PlayerStats> loaded = gson.fromJson(reader, 
                new TypeToken<Map<String, PlayerStats>>(){}.getType());
            
            if (loaded != null) {
                loaded.forEach((uuid, stats) -> playerData.put(UUID.fromString(uuid), stats));
                plugin.getPluginLogger().info("Loaded data for " + playerData.size() + " players.");
            }
        } catch (IOException e) {
            plugin.getPluginLogger().error("Failed to load player data: " + e.getMessage());
        }
    }
    
    public void saveAllData() {
        dataFolder.mkdirs();
        
        Map<String, PlayerStats> toSave = new ConcurrentHashMap<>();
        playerData.forEach((uuid, stats) -> toSave.put(uuid.toString(), stats));
        
        try (Writer writer = new FileWriter(dataFile)) {
            gson.toJson(toSave, writer);
            plugin.getPluginLogger().info("Saved data for " + toSave.size() + " players.");
        } catch (IOException e) {
            plugin.getPluginLogger().error("Failed to save player data: " + e.getMessage());
        }
    }
    
    public PlayerStats getPlayerStats(UUID playerId) {
        return playerData.computeIfAbsent(playerId, k -> new PlayerStats());
    }
    
    public PlayerStats getPlayerStats(EntityPlayer player) {
        return getPlayerStats(player.getUniqueId());
    }
    
    public Map<UUID, PlayerStats> getAllPlayerData() {
        return new ConcurrentHashMap<>(playerData);
    }
    
    public void recordBlockBroken(UUID playerId) {
        getPlayerStats(playerId).getBlocksBroken().incrementAndGet();
    }
    
    public void recordBlockPlaced(UUID playerId) {
        getPlayerStats(playerId).getBlocksPlaced().incrementAndGet();
    }
    
    public void recordMobKilled(UUID playerId) {
        getPlayerStats(playerId).getMobsKilled().incrementAndGet();
    }
    
    public void recordPlayerKill(UUID playerId) {
        getPlayerStats(playerId).getPlayerKills().incrementAndGet();
    }
    
    public void recordDeath(UUID playerId) {
        getPlayerStats(playerId).getDeaths().incrementAndGet();
    }
    
    public void recordDistance(UUID playerId, double distance) {
        getPlayerStats(playerId).getDistanceTraveled().addAndGet((long) distance);
    }
    
    public void recordItemCrafted(UUID playerId) {
        getPlayerStats(playerId).getItemsCrafted().incrementAndGet();
    }
    
    public void recordItemPickedUp(UUID playerId) {
        getPlayerStats(playerId).getItemsPickedUp().incrementAndGet();
    }
    
    public void recordItemDropped(UUID playerId) {
        getPlayerStats(playerId).getItemsDropped().incrementAndGet();
    }
    
    public void recordChatMessage(UUID playerId) {
        getPlayerStats(playerId).getChatMessages().incrementAndGet();
    }
    
    public void recordCommandUsed(UUID playerId) {
        getPlayerStats(playerId).getCommandsUsed().incrementAndGet();
    }
    
    public void recordLogin(UUID playerId) {
        getPlayerStats(playerId).recordLogin();
    }
    
    public void updatePlayTime(UUID playerId) {
        getPlayerStats(playerId).updatePlayTime();
    }
}
