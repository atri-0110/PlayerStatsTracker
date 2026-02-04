package com.atri0110.playerstatstracker.commands;

import com.atri0110.playerstatstracker.data.PlayerDataManager;
import com.atri0110.playerstatstracker.stats.PlayerStats;
import org.allaymc.api.command.Command;
import org.allaymc.api.command.tree.CommandTree;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.server.Server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LeaderboardCommand extends Command {
    
    private final PlayerDataManager dataManager;
    
    public LeaderboardCommand(PlayerDataManager dataManager) {
        super("leaderboard", "View statistics leaderboards", "");
        this.dataManager = dataManager;
    }
    
    @Override
    public void prepareCommandTree(CommandTree tree) {
        tree.getRoot()
            .str("stat")
            .exec(context -> {
                if (!(context.getSender() instanceof EntityPlayer player)) {
                    context.getSender().sendMessage("This command can only be used by players");
                    return context.fail();
                }
                String stat = context.getResult(0);
                if (!isValidStatType(stat)) {
                    player.sendMessage("§cInvalid stat type. Valid types: playtime, blocks, kills, deaths, distance, chat");
                    return context.fail();
                }
                showLeaderboard(player, stat);
                return context.success();
            });
    }

    /**
     * Gets player name by UUID with caching support.
     * Falls back to UUID prefix for offline players with no cached name.
     */
    private String getPlayerNameCached(UUID uuid) {
        // For future implementation: add a name cache map
        // private final Map<UUID, String> nameCache = new ConcurrentHashMap<>();
        // return nameCache.computeIfAbsent(uuid, k -> getPlayerName(k));
        return getPlayerName(uuid);
    }
    
    private boolean isValidStatType(String type) {
        return List.of("playtime", "blocks", "kills", "deaths", "distance", "chat").contains(type);
    }
    
    private void showLeaderboard(EntityPlayer player, String statType) {
        Map<UUID, PlayerStats> allData = dataManager.getAllPlayerData();

        List<Map.Entry<UUID, Long>> sorted = allData.entrySet().stream()
            .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), getStatValue(entry.getValue(), statType)))
            .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
            .limit(10)
            .collect(Collectors.toList());

        String title = getStatTitle(statType);
        player.sendMessage("§6=== " + title + " Leaderboard ===");
        player.sendMessage("");

        int rank = 1;
        for (Map.Entry<UUID, Long> entry : sorted) {
            String playerName = getPlayerName(entry.getKey());
            String value = formatStatValue(statType, entry.getValue());

            String color = rank == 1 ? "§6" : rank == 2 ? "§7" : rank == 3 ? "§c" : "§f";
            player.sendMessage(color + "#" + rank + " §f" + playerName + " §7- " + value);
            rank++;
        }

        if (sorted.isEmpty()) {
            player.sendMessage("§7No data available yet");
        }

        player.sendMessage("");
        player.sendMessage("§6========================");
        player.sendMessage("§7Note: Only online players show full names");
    }
    
    private long getStatValue(PlayerStats stats, String statType) {
        return switch (statType) {
            case "playtime" -> stats.getPlayTimeMinutes().get();
            case "blocks" -> stats.getBlocksBroken().get() + stats.getBlocksPlaced().get();
            case "kills" -> stats.getPlayerKills().get();
            case "deaths" -> stats.getDeaths().get();
            case "distance" -> stats.getDistanceTraveled().get();
            case "chat" -> stats.getChatMessages().get();
            default -> 0;
        };
    }
    
    private String getStatTitle(String statType) {
        return switch (statType) {
            case "playtime" -> "Play Time";
            case "blocks" -> "Blocks";
            case "kills" -> "Kills";
            case "deaths" -> "Deaths";
            case "distance" -> "Distance";
            case "chat" -> "Chat Messages";
            default -> "Unknown";
        };
    }
    
    private String formatStatValue(String statType, long value) {
        return switch (statType) {
            case "playtime" -> {
                long hours = value / 60;
                long mins = value % 60;
                yield hours > 0 ? hours + "h " + mins + "m" : mins + "m";
            }
            case "distance" -> value >= 1000 ? String.format("%.1f km", value / 1000.0) : value + " blocks";
            default -> String.valueOf(value);
        };
    }
    
    private String getPlayerName(UUID uuid) {
        // First, try to find the player online
        final String[] name = {uuid.toString().substring(0, 8)};
        Server.getInstance().getPlayerManager().forEachPlayer(player -> {
            if (player.getLoginData().getUuid().equals(uuid)) {
                EntityPlayer entityPlayer = player.getControlledEntity();
                if (entityPlayer != null) {
                    name[0] = entityPlayer.getDisplayName();
                }
            }
        });
        return name[0];
    }

    /**
     * Loads cached player names from disk to show full names in leaderboards even for offline players.
     * Call this on plugin load to populate the name cache.
     */
    public void loadPlayerNameCache() {
        // For future implementation: load from saved player data files
        // This would allow leaderboards to show full names for offline players
    }
}
