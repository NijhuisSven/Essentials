# ✨ Essentials Plugin

A modern, extensible Minecraft Paper plugin with handy commands, database support (MySQL & SQLite), and more!

---

## 🚀 Features

- ⚡ **Fast and stable Paper plugin**
- 🗃️ **Database support:** MySQL & SQLite
- 🧑‍💻 **Extensible command structure** (Aikar Commands)
- 🕒 **Uptime & server info**
- 💬 **Private messaging & toggles**
- 🔄 **Player data management**

---

## 🛠️ Installation

1. **Download** the jar and place it in your `plugins/` folder.
2. Start the server once to generate the config.
3. Edit `config.yml` for your database (MySQL or SQLite).
4. Restart the server.

---

## ⚙️ Configuration

```yaml
# config.yml
# Database Configuration
database:
  type: sqlite # or 'mysql'
  mysql:
    host: localhost
    port: 3306
    database: essentials
    username: root
    password: password
```

---

## 💡 Main Commands

| Command         | Permission                | Description                  |
|-----------------|--------------------------|------------------------------|
| `/serverinfo`   | essentials.serverinfo     | 📊 Show server info & uptime |
| `/msg`          | essentials.msg            | 💬 Send private message      |
| `/msgtoggle`    | essentials.msgtoggle      | 🔕 Toggle private messages   |
| `/time`         | essentials.time           | ⏰ Set world time            |
| `/fly`          | essentials.fly            | 🕊️ Toggle fly mode          |
| ...             | ...                      | ...                          |

---

## 🧩 Extending

- Commands are auto-registered via the `@AutoRegister` annotation.
- Add your own command in the correct folder (`baseCommands`, `playerCommands`, etc).

---

## 🗃️ Database

- Supports **MySQL** (with MariaDB driver) and **SQLite**.
- Models are auto-migrated with [Storm ORM](https://github.com/Mindgamesnl/storm).

---

## 🤝 Contributing

1. Fork this project
2. Create a feature branch
3. Open a pull request

---

## 📝 License

MIT License — see LICENSE file.

---

**Made with ❤️ by Svenn**
