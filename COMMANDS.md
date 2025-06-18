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

## 🎮 Player Commands

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

### Player Permissions
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
| **Admin** | 4 commands | Server management and monitoring |
| **Player** | 8 commands | Player-specific features and utilities |
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

---

**Total Commands:** 24 commands across 4 categories 