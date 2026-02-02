package com.atri0110.playerstatstracker.stats;

import lombok.Data;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Player statistics data container with thread-safe counters.
 */
@Data
public class PlayerStats {
    
    private final AtomicLong playTimeMinutes = new AtomicLong(0);
    private final AtomicLong blocksBroken = new AtomicLong(0);
    private final AtomicLong blocksPlaced = new AtomicLong(0);
    private final AtomicLong mobsKilled = new AtomicLong(0);
    private final AtomicLong playerKills = new AtomicLong(0);
    private final AtomicLong deaths = new AtomicLong(0);
    private final AtomicLong distanceTraveled = new AtomicLong(0);
    private final AtomicLong itemsCrafted = new AtomicLong(0);
    private final AtomicLong itemsPickedUp = new AtomicLong(0);
    private final AtomicLong itemsDropped = new AtomicLong(0);
    private final AtomicLong chatMessages = new AtomicLong(0);
    private final AtomicLong commandsUsed = new AtomicLong(0);
    
    private long lastLoginTime = 0;
    private long totalSessions = 0;
    
    /**
     * Records a player login.
     */
    public void recordLogin() {
        this.lastLoginTime = System.currentTimeMillis();
        this.totalSessions++;
    }
    
    /**
     * Updates play time based on last login.
     */
    public void updatePlayTime() {
        if (this.lastLoginTime > 0) {
            long sessionTime = System.currentTimeMillis() - this.lastLoginTime;
            long sessionMinutes = sessionTime / (1000 * 60);
            this.playTimeMinutes.addAndGet(sessionMinutes);
            this.lastLoginTime = System.currentTimeMillis();
        }
    }
}
