# 📋 Orbit Commands

Complete list of all available commands in the Orbit plugin.

---

## 🔧 Admin Commands

### `/orbit`
**Permission:** `orbit.admin`  
**Description:** Main admin command for orbit plugin management

**Subcommands:**
- `/orbit reload` - Reload configuration files
- `/orbit version` - Show plugin version information
- `/orbit about` - Display plugin information
- `/orbit pluginstats` - Show plugin statistics

**Usage Examples:**
```
/ess
/ess reload
/ess version
/ess about
/ess pluginstats
```

---

### `/serverinfo`
**Permission:** `orbit.serverinfo`  
**Description:** Display real-time server information including uptime, player count, and memory usage

**Usage:**
```
/serverinfo
```

**Output:**
- Server uptime
- Online players count
- Memory usage statistics

---

### `/lagg`
**Permission:** `orbit.lagg`  
**Description:** Monitor server performance and identify potential lag sources

**Usage:**
```
/lagg
```

**Output:**
- Current TPS (Ticks Per Second)
- Memory usage statistics
- World information (chunks, entities, tile entities)

---

### `/loop <command> <count> <delay>`
**Permission:** `orbit.loop`  
**Description:** Execute a command multiple times with a specified delay

**Parameters:**
- `<command>` - The command to execute
- `<count>` - Number of times to execute
- `<delay>` - Delay between executions (in seconds)

**Usage Examples:**
```
/loop say Hello 5 2
/loop time day 3 1
/loop weather clear 2 5
```

---

### `/killall [entitytype] [radius] [world]`
**Permission:** `orbit.killall`  
**Description:** Kill entities with various options including specific entity types, radius, and world targeting

**Parameters:**
- `[entitytype]` - Optional specific entity type to kill
- `[radius]` - Optional radius within which to kill entities
- `[world]` - Optional specific world to target

**Usage Examples:**
```
/killall
/killall zombie
/killall zombie 50
/killall zombie world
/killall zombie 50 world
/killall creeper 100 world_nether
```

**Features:**
- Never kills players (safe)
- Supports all entity types (zombie, creeper, item, etc.)
- Radius-based killing (1-1000 blocks)
- World-specific targeting
- Tab completion for entity types and worlds

---

## 🎮 Player Commands

### `/settings`
**Permission:** `orbit.settings`  
**Description:** View and manage your player settings

**Aliases:** `setting`, `preferences`, `prefs`

**Subcommands:**
- `/settings` - View your current settings
- `/settings view` - View your current settings
- `/settings view <player>` - View another player's settings (requires `orbit.settings.others`)
- `/settings sounds` - Toggle sound effects on/off
- `/settings sounds <player>` - Toggle sound effects for another player (requires `orbit.settings.others`)
- `/settings messages` - Toggle private messages on/off
- `/settings messages <player>` - Toggle private messages for another player (requires `orbit.settings.others`)
- `/settings reset` - Reset your settings to default values
- `/settings reset <player>` - Reset another player's settings to default values (requires `orbit.settings.others`)

**Usage Examples:**
```
/settings
/settings view
/settings view PlayerName
/settings sounds
/settings sounds PlayerName
/settings messages
/settings messages PlayerName
/settings reset
/settings reset PlayerName
```

**Settings:**
- **Sound Effects** - Control whether you hear plugin sound effects
- **Private Messages** - Control whether you can receive private messages

---

### `/msg <player> <message>`
**Permission:** `orbit.msg`  
**Description:** Send a private message to another player

**Aliases:** `m`, `t`, `tell`, `w`, `whisper`

**Parameters:**
- `<player>` - Target player name
- `<message>` - Message to send

**Usage Examples:**
```
/msg PlayerName Hello there!
/m PlayerName How are you?
/tell PlayerName This is a private message
```

---

### `/msgtoggle`
**Permission:** `orbit.msgtoggle`  
**Description:** Toggle private message reception on/off

**Usage:**
```
/msgtoggle
```

**Effect:** When toggled off, you won't receive private messages from other players.

---

### `/fly [player] [on/off]`
**Permission:** `orbit.fly`  
**Description:** Toggle flight mode for yourself or another player

**Parameters:**
- `[player]` - Optional target player (requires `orbit.fly.others`)
- `[on/off]` - Optional state to set

**Usage Examples:**
```
/fly
/fly on
/fly off
/fly PlayerName
/fly PlayerName on
/fly PlayerName off
```

---

### `/gamemode <mode> [player]`
**Permission:** `orbit.gamemode`  
**Description:** Change gamemode for yourself or another player

**Aliases:** `gm`

**Parameters:**
- `<mode>` - Gamemode (creative, survival, adventure, spectator)
- `[player]` - Optional target player

**Usage Examples:**
```
/gamemode creative
/gm survival
/gamemode adventure PlayerName
/gm spectator PlayerName
```

---

### `/speed <speed> [player]`
**Permission:** `orbit.speed`  
**Description:** Change movement speed for yourself or another player

**Parameters:**
- `<speed>` - Speed value (0.1 to 10.0)
- `[player]` - Optional target player (requires `orbit.speed.others`)

**Usage Examples:**
```
/speed 1.0
/speed 2.5
/speed 0.5 PlayerName
/speed 10.0 PlayerName
```

---

### `/enderchest [player]`
**Permission:** `orbit.enderchest`  
**Description:** Open ender chest for yourself or another player

**Aliases:** `ec`

**Parameters:**
- `[player]` - Optional target player

**Usage Examples:**
```
/enderchest
/ec
/enderchest PlayerName
/ec PlayerName
```

---

### `/playertime <time> [world]`
**Permission:** `orbit.playertime`  
**Description:** Set personal time for a specific world

**Parameters:**
- `<time>` - Time value (day, night, noon, or number)
- `[world]` - Optional world name

**Usage Examples:**
```
/playertime day
/playertime night
/playertime noon
/playertime 6000
/playertime day world_nether
```

---

### `/playerweather <weather> [world]`
**Permission:** `orbit.playerweather`  
**Description:** Set personal weather for a specific world

**Parameters:**
- `<weather>` - Weather type (clear, rain, thunder)
- `[world]` - Optional world name

**Usage Examples:**
```
/playerweather clear
/playerweather rain
/playerweather thunder
/playerweather clear world_the_end
```

---

## 🏠 Home Commands

### `/home`
**Permission:** `orbit.home`  
**Description:** Show all your homes

**Usage:**
```
/home
```

**Output:** Displays a clickable list of all your homes

---

### `/home <name>`
**Permission:** `orbit.home`  
**Description:** Teleport to your home

**Parameters:**
- `<name>` - Home name

**Usage Examples:**
```
/home spawn
/home base
/home farm
```

---

### `/home <player> <name>`
**Permission:** `orbit.home.visit`  
**Description:** Teleport to another player's home (staff only)

**Parameters:**
- `<player>` - Target player name
- `<name>` - Home name

**Usage Examples:**
```
/home PlayerName spawn
/home PlayerName base
```

---

### `/home list [player]`
**Permission:** `orbit.home.list` / `orbit.home.list.others`  
**Description:** List all your homes or another player's homes

**Parameters:**
- `[player]` - Optional target player (requires `orbit.home.list.others`)

**Usage Examples:**
```
/home list
/home list PlayerName
```

---

### `/sethome <name>`
**Permission:** `orbit.sethome`  
**Description:** Set a home at your current location

**Parameters:**
- `<name>` - Home name

**Usage Examples:**
```
/sethome spawn
/sethome base
/sethome farm
```

**Note:** Limited by maximum homes config (default: 3). Use `orbit.home.unlimited` to bypass limit.

---

### `/delhome` `/deletehome <name>`
**Permission:** `orbit.delhome`  
**Description:** Delete your home

**Parameters:**
- `<name>` - Home name to delete

**Usage Examples:**
```
/delhome oldhome
/deletehome unused
```

---

### `/delhome <player> <name>`
**Permission:** `orbit.delhome.others`  
**Description:** Delete another player's home (staff only)

**Parameters:**
- `<player>` - Target player name
- `<name>` - Home name to delete

**Usage Examples:**
```
/delhome PlayerName oldhome
/deletehome PlayerName unused
```

---

## 🌍 Base Commands

### `/teleport` `/tp <player>`
**Permission:** `orbit.teleport`  
**Description:** Teleport to another player

**Usage Examples:**
```
/tp PlayerName
/teleport PlayerName
```

---

### `/tp <player1> <player2>`
**Permission:** `orbit.teleport`  
**Description:** Teleport one player to another player

**Parameters:**
- `<player1>` - Player to teleport
- `<player2>` - Target player

**Usage Examples:**
```
/tp Player1 Player2
/teleport Player1 Player2
```

---

### `/time day`
**Permission:** `orbit.time`  
**Description:** Set world time to day (1000 ticks)

**Usage:**
```
/time day
```

---

### `/time night`
**Permission:** `orbit.time`  
**Description:** Set world time to night (13000 ticks)

**Usage:**
```
/time night
```

---

### `/time noon`
**Permission:** `orbit.time`  
**Description:** Set world time to noon (6000 ticks)

**Usage:**
```
/time noon
```

---

### `/weather clear`
**Permission:** `orbit.weather`  
**Description:** Set weather to clear

**Usage:**
```
/weather clear
```

---

### `/weather rain`
**Permission:** `orbit.weather`  
**Description:** Set weather to rain

**Usage:**
```
/weather rain
```

---

### `/weather thunder`
**Permission:** `orbit.weather`  
**Description:** Set weather to thunder

**Usage:**
```
/weather thunder
```

---

### `/top`
**Permission:** `orbit.top`  
**Description:** Teleport to the highest safe block at your current location

**Usage:**
```
/top
```

---

## 🗺️ Warp Commands

### `/warp <name>`
**Permission:** `orbit.warp`  
**Description:** Teleport to a warp location

**Parameters:**
- `<name>` - Warp name

**Usage Examples:**
```
/warp spawn
/warp home
/warp shop
```

**Note:** Some warps may require additional permissions (e.g., `orbit.warp.spawn`)

---

### `/setwarp <name>`
**Permission:** `orbit.setwarp`  
**Description:** Create a new warp at your current location

**Parameters:**
- `<name>` - Warp name

**Usage Examples:**
```
/setwarp spawn
/setwarp home
/setwarp shop
```

---

### `/delwarp` `/deletewarp <name>`
**Permission:** `orbit.delwarp`  
**Description:** Delete an existing warp

**Parameters:**
- `<name>` - Warp name to delete

**Usage Examples:**
```
/delwarp oldwarp
/deletewarp unused
```

---

## 📊 Permission Summary

### Admin Permissions
- `orbit.admin` - Access to all admin commands
- `orbit.serverinfo` - View server information
- `orbit.lagg` - Monitor server performance
- `orbit.loop` - Execute loop commands
- `orbit.killall` - Kill entities with various options

### Player Permissions
- `orbit.settings` - Manage own settings
- `orbit.settings.others` - Manage other players' settings
- `orbit.msg` - Send private messages
- `orbit.msgtoggle` - Toggle message reception
- `orbit.fly` - Toggle flight mode
- `orbit.fly.others` - Toggle flight for other players
- `orbit.gamemode` - Change gamemode
- `orbit.speed` - Change movement speed
- `orbit.speed.others` - Change speed for other players
- `orbit.enderchest` - Access ender chest
- `orbit.playertime` - Set personal time
- `orbit.playerweather` - Set personal weather

### Home Permissions
- `orbit.sethome` - Set homes
- `orbit.home` - Use homes
- `orbit.home.list` - List own homes
- `orbit.home.visit` - Visit other players' homes
- `orbit.home.list.others` - List other players' homes
- `orbit.delhome` - Delete own homes
- `orbit.delhome.others` - Delete other players' homes
- `orbit.home.unlimited` - Bypass maximum homes limit

### Base Permissions
- `orbit.teleport` - Teleport to players
- `orbit.time` - Change world time
- `orbit.weather` - Change world weather
- `orbit.top` - Teleport to highest block

### Warp Permissions
- `orbit.warp` - Use warps
- `orbit.warp.<warpname>` - Use specific warp
- `orbit.setwarp` - Create warps
- `orbit.delwarp` - Delete warps

---

## 🎯 Command Categories

| Category | Commands | Description |
|----------|----------|-------------|
| **Admin** | 5 commands | Server management and monitoring |
| **Player** | 9 commands | Player-specific features and utilities |
| **Home** | 7 commands | Personal home system management |
| **Base** | 9 commands | Basic world and teleportation commands |
| **Warp** | 3 commands | Warp system management |

---

## 💡 Tips

- Use tab completion for command suggestions
- Most commands support multiple aliases for convenience
- Check permissions before using commands on other players
- Warp permissions can be granular (e.g., `orbit.warp.spawn`)
- Personal time/weather settings only affect your view
- Use `/ess` for quick admin access
- Homes are per-player and stored in database or YAML
- Killall command never affects players (safe to use)
- Use `orbit.home.unlimited` for VIP players who need more homes
- Player settings are stored in the main player database
- Default settings can be configured in `config.yml`

---

**Total Commands:** 33 commands across 5 categories 