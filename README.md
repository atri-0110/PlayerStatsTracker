# PlayerStatsTracker

A comprehensive player statistics tracking plugin for AllayMC servers.

## Overview

PlayerStatsTracker tracks detailed player statistics across your server, providing insights into player activity and engagement. The plugin features persistent storage, real-time tracking, and leaderboards for competitive elements.

## Features

### Tracked Statistics
- **Play Time** - Total time spent on the server
- **Blocks Broken** - Number of blocks mined/destroyed
- **Blocks Placed** - Number of blocks placed
- **Mobs Killed** - Total hostile mobs defeated
- **Player Kills** - PvP kills
- **Deaths** - Death count
- **Distance Traveled** - Total movement distance
- **Items Crafted** - Items created through crafting
- **Items Picked Up** - Items collected
- **Items Dropped** - Items dropped
- **Chat Messages** - Messages sent in chat
- **Commands Used** - Commands executed

### Commands

| Command | Description |
|---------|-------------|
| `/stats` | View your personal statistics |
| `/leaderboard <type>` | View top 10 leaderboard for specified stat |

### Leaderboard Types
- `playtime` - Most time played
- `blocks` - Combined blocks broken and placed
- `kills` - Combined mob and player kills
- `deaths` - Most deaths
- `distance` - Furthest distance traveled
- `crafted` - Most items crafted
- `chat` - Most chat messages

## Installation

1. Download the latest release from GitHub
2. Place the JAR file in your server's `plugins` folder
3. Start or restart the server
4. Player data will be stored in `plugins/PlayerStatsTracker/playerdata.json`

## Configuration

The plugin automatically creates a data file at:
```
plugins/PlayerStatsTracker/playerdata.json
```

No manual configuration is required. All settings are managed automatically.

## Permissions

Currently, all commands are available to all players. Permission nodes will be added in future updates.

## Technical Details

- **API Version**: 0.24.0
- **Java Version**: 21
- **Storage**: JSON-based persistent storage
- **Thread Safety**: All operations are thread-safe using ConcurrentHashMap

## Building from Source

```bash
./gradlew shadowJar
```

The compiled JAR will be in `build/libs/`.

## API Usage

Other plugins can access player statistics:

```java
PlayerStatsTracker plugin = PlayerStatsTracker.getInstance();
PlayerDataManager dataManager = plugin.getDataManager();
PlayerStats stats = dataManager.getPlayerStats(playerUUID);

long blocksBroken = stats.getBlocksBroken().get();
```

## Future Plans

- [ ] MySQL/Database support for larger servers
- [ ] Permission nodes for commands
- [ ] Web dashboard for statistics viewing
- [ ] Achievement system
- [ ] Statistic milestones and rewards
- [ ] Export to CSV/Excel

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Credits

- Author: atri-0110
- Built for: AllayMC Server Software

## Support

For issues and feature requests, please use the GitHub issue tracker.
