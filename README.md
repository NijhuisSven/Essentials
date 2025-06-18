# ⚡ Orbit Plugin

A **lightweight** and **blazing fast** Minecraft Paper plugin with essential commands, efficient database support, and minimal resource usage!

---

## 🚀 Features

- ⚡ **Ultra-fast Paper plugin** (Minecraft 1.21.4) - Minimal performance impact
- 🗃️ **Lightweight database support:** MySQL & SQLite with Storm ORM
- 🧑‍💻 **Efficient command structure** (Aikar Commands Framework) - Zero overhead
- 🔄 **Smart auto-registration** via `@AutoRegister` annotation - No manual setup
- 🕒 **Optimized server monitoring** - Real-time info with minimal CPU usage
- 💬 **Fast private messaging** - Instant message delivery
- 🎮 **Efficient player management** - Memory-optimized data storage
- 🌍 **Lightweight warp system** - YAML or Database with smart caching
- ⚡ **Optimized teleportation** - Fast countdown with movement detection
- 🎯 **Streamlined player commands** - Gamemode, fly, speed & more
- 🌤️ **Efficient time & weather** - Instant world modifications
- 📦 **Quick ender chest access** - No delays or lag

---

## 🛠️ Installation

1. **Download** the lightweight jar and place it in your `plugins/` folder
2. Start the server once to generate the minimal configuration
3. Edit `config.yml` for your database settings (MySQL or SQLite)
4. Restart the server - Ready to use instantly!

---

## ⚙️ Configuration

```yaml
# config.yml - Minimal and efficient configuration
# Database Configuration
database:
  # Database type (mysql or sqlite)
  type: sqlite
  
  # MySQL Configuration (only used if type = mysql)
  mysql:
    host: localhost
    port: 3306
    database: orbit
    username: root
    password: password

# Warp Configuration
warps:
  # Storage type (yaml or database)
  storage: yaml
  
  # Teleportation settings
  teleport:
    # Delay in seconds before teleporting (0 to disable)
    delay: 3
    
    # Cancel teleport if player moves
    cancel-on-move: true
```

---

## 📋 Commands

**📖 Complete command documentation available in [COMMANDS.md](COMMANDS.md)**

The plugin includes **24 commands** across 4 categories:
- 🔧 **Admin Commands** - Server management and monitoring
- 🎮 **Player Commands** - Player-specific features and utilities  
- 🌍 **Base Commands** - Basic world and teleportation commands
- 🗺️ **Warp Commands** - Warp system management

---

## 🧩 Extending

### Adding New Commands - Zero Overhead
1. Create a new Java class in the appropriate folder:
   - `admin/` - For admin commands
   - `base/` - For base commands  
   - `player/` - For player commands
   - `warp/` - For warp-related commands

2. Add the `@AutoRegister` annotation to your class:
```java
@AutoRegister
@CommandAlias("yourcommand")
@CommandPermission("orbit.yourcommand")
@Description("Description of your command")
public class YourCommand extends BaseCommand {
    // Your command implementation
}
```

3. The command will be automatically registered on server start - **No performance impact!**

### Database Models - Optimized
- Use Storm ORM for efficient database models
- Models are automatically migrated with minimal overhead
- Supports MySQL and SQLite with connection pooling

---

## 🗃️ Database

- **MySQL** support with MariaDB driver - Optimized connections
- **SQLite** for ultra-fast local storage
- **Storm ORM** for efficient automatic migrations
- **HikariCP** connection pooling - Maximum performance
- **Smart caching** for warps (YAML or Database)

---

## 🔧 Technical Details

- **Minecraft Version:** 1.21.4
- **Java Version:** 21
- **Framework:** Paper API - Optimized for performance
- **Command Framework:** Aikar Commands Framework - Zero overhead
- **Database ORM:** Storm ORM - Lightweight and fast
- **Build Tool:** Gradle with Shadow plugin - Minimal jar size

---

## ⚡ Performance Benefits

- **Minimal memory usage** - Optimized data structures
- **Fast command execution** - No unnecessary overhead
- **Efficient database queries** - Smart caching and pooling
- **Lightweight listeners** - Only essential event handling
- **Optimized teleportation** - Fast countdown with movement detection
- **Smart auto-registration** - No manual command setup required

---

## 🤝 Contributing

1. Fork this project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

MIT License — see LICENSE file.

---

**Made with ❤️ by Svenn - Built for speed and efficiency!**
