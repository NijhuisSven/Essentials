package nl.nijhuissven.essentials.database;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.Where;
import com.craftmend.storm.connection.hikaricp.HikariDriver;
import com.craftmend.storm.connection.sqlite.SqliteFileDriver;
import com.craftmend.storm.parser.types.TypeRegistry;
import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import nl.nijhuissven.essentials.Essentials;
import nl.nijhuissven.essentials.config.DatabaseType;
import nl.nijhuissven.essentials.config.GlobalConfiguration;
import nl.nijhuissven.essentials.database.adapters.FixedBooleanAdapter;
import nl.nijhuissven.essentials.models.PlayerModel;
import nl.nijhuissven.essentials.models.WarpModel;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class Database {
    private final Storm storm;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public Database(GlobalConfiguration config, File dataFolder) {
        Storm tempStorm = null;
        try {
            if (config.databaseType() == DatabaseType.MYSQL) {
                HikariConfig hikari = new HikariConfig();
                hikari.setDriverClassName("org.mariadb.jdbc.Driver");
                hikari.setJdbcUrl("jdbc:mariadb://" + config.mySqlHost() + ":" + config.mySqlPort() + "/" + config.mySqlDatabase());
                hikari.setUsername(config.mySqlUsername());
                hikari.setPassword(config.mySqlPassword());
                hikari.setMaximumPoolSize(6);
                tempStorm = new Storm(new HikariDriver(hikari));
            } else {
                tempStorm = new Storm(new SqliteFileDriver(new File(dataFolder, "data.db")));
            }
            TypeRegistry.registerAdapter(Boolean.class, new FixedBooleanAdapter());
            tempStorm.registerModel(new PlayerModel());
            tempStorm.registerModel(new WarpModel());
            tempStorm.runMigrations();
        } catch (Exception e) {
            Essentials.logger().severe("Database initialization failed: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
        this.storm = tempStorm;
    }

    public void saveModel(StormModel model) {
        try {
            storm.save(model);
        } catch (SQLException e) {
            Essentials.logger().severe("Couldn't save model: " + e.getMessage());
        }
    }

    public <T extends StormModel> List<T> query(Class<T> clazz, String field, Object value) {
        Collection<T> result = new ArrayList<>();
        try {
            if (field == null || value == null) {
                result = storm.buildQuery(clazz)
                        .execute()
                        .join();
            } else {
                result = storm.buildQuery(clazz)
                        .where(field, Where.EQUAL, value)
                        .execute()
                        .join();
            }
        } catch (Exception e) {
            Essentials.logger().severe("Database query failed: " + e.getMessage());
        }
        return new ArrayList<>(result);
    }

    public void disconnect() {
        executorService.shutdown();
    }

    public void deleteModel(StormModel model) {
        try {
            storm.delete(model);
        } catch (SQLException e) {
            Essentials.logger().severe("Couldn't delete model: " + e.getMessage());
        }
    }
} 