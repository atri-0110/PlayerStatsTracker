package com.atri0110.playerstatstracker.listeners;

import com.atri0110.playerstatstracker.data.PlayerDataManager;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.eventbus.EventHandler;
import org.allaymc.api.eventbus.event.block.BlockBreakEvent;
import org.allaymc.api.eventbus.event.block.BlockPlaceEvent;
import org.allaymc.api.eventbus.event.entity.EntityDieEvent;
import org.allaymc.api.eventbus.event.player.PlayerChatEvent;
import org.allaymc.api.eventbus.event.player.PlayerCommandEvent;
import org.allaymc.api.eventbus.event.player.PlayerMoveEvent;
import org.allaymc.api.math.location.Location3dc;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerEventListener {
    
    private final PlayerDataManager dataManager;
    private final Map<UUID, Location3dc> lastPositions;
    private final Map<UUID, Long> loginTimes;
    
    public PlayerEventListener(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
        this.lastPositions = new ConcurrentHashMap<>();
        this.loginTimes = new ConcurrentHashMap<>();
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getEntity() instanceof EntityPlayer player) {
            dataManager.recordBlockBroken(player.getUniqueId());
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getInteractInfo() != null && event.getInteractInfo().player() instanceof EntityPlayer player) {
            dataManager.recordBlockPlaced(player.getUniqueId());
        }
    }
    
    @EventHandler
    public void onEntityDie(EntityDieEvent event) {
        if (event.getEntity() instanceof EntityPlayer player) {
            dataManager.recordDeath(player.getUniqueId());
        }
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        dataManager.recordChatMessage(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPlayerCommand(PlayerCommandEvent event) {
        dataManager.recordCommandUsed(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        EntityPlayer player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        Location3dc current = event.getTo();
        
        Location3dc last = lastPositions.get(playerId);
        if (last != null) {
            double distance = current.distance(last);
            if (distance > 0.1) {
                dataManager.recordDistance(playerId, distance);
            }
        }
        
        lastPositions.put(playerId, current);
    }
    
    public void recordLogin(UUID playerId) {
        loginTimes.put(playerId, System.currentTimeMillis());
        dataManager.recordLogin(playerId);
    }
    
    public void recordLogout(UUID playerId) {
        Long loginTime = loginTimes.remove(playerId);
        if (loginTime != null) {
            long sessionMinutes = (System.currentTimeMillis() - loginTime) / (1000 * 60);
            dataManager.getPlayerStats(playerId).getPlayTimeMinutes().addAndGet(sessionMinutes);
        }
        dataManager.updatePlayTime(playerId);
        lastPositions.remove(playerId);
    }
}
