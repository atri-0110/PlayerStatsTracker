package com.atri0110.playerstatstracker.commands;

import com.atri0110.playerstatstracker.data.PlayerDataManager;
import com.atri0110.playerstatstracker.stats.PlayerStats;
import org.allaymc.api.command.Command;
import org.allaymc.api.command.tree.CommandTree;
import org.allaymc.api.entity.interfaces.EntityPlayer;

public class StatsCommand extends Command {
    
    private final PlayerDataManager dataManager;
    
    public StatsCommand(PlayerDataManager dataManager) {
        super("stats", "View your player statistics", "");
        this.dataManager = dataManager;
    }
    
    @Override
    public void prepareCommandTree(CommandTree tree) {
        tree.getRoot()
            .exec(context -> {
                if (!(context.getSender() instanceof EntityPlayer player)) {
                    context.getSender().sendMessage("This command can only be used by players");
                    return context.fail();
                }
                showStats(player);
                return context.success();
            });
    }
    
    private void showStats(EntityPlayer player) {
        PlayerStats stats = dataManager.getPlayerStats(player.getUniqueId());
        
        player.sendMessage("§6=== Player Statistics ===");
        player.sendMessage("§ePlayer: §f" + player.getDisplayName());
        player.sendMessage("");
        player.sendMessage("§bPlay Time: §f" + formatTime(stats.getPlayTimeMinutes().get()));
        player.sendMessage("§bBlocks Broken: §f" + stats.getBlocksBroken().get());
        player.sendMessage("§bBlocks Placed: §f" + stats.getBlocksPlaced().get());
        player.sendMessage("§bPlayer Kills: §f" + stats.getPlayerKills().get());
        player.sendMessage("§bDeaths: §f" + stats.getDeaths().get());
        player.sendMessage("§bDistance Traveled: §f" + formatDistance(stats.getDistanceTraveled().get()));
        player.sendMessage("§bChat Messages: §f" + stats.getChatMessages().get());
        player.sendMessage("§bCommands Used: §f" + stats.getCommandsUsed().get());
        player.sendMessage("");
        player.sendMessage("§6========================");
    }
    
    private String formatTime(long minutes) {
        long hours = minutes / 60;
        long mins = minutes % 60;
        if (hours > 0) {
            return hours + "h " + mins + "m";
        }
        return mins + "m";
    }
    
    private String formatDistance(long blocks) {
        if (blocks >= 1000) {
            return String.format("%.1f km", blocks / 1000.0);
        }
        return blocks + " blocks";
    }
}
