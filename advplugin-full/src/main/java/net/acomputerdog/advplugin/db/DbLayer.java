package net.acomputerdog.advplugin.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DbLayer {
    private final ConnectionSource connectionSource;
    private final List<Class<?>> dataClasses;

    public DbLayer(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        dataClasses = new ArrayList<>();
    }

    public void createDAOs(List<String> classNames) throws ClassNotFoundException, SQLException {
        for (String clsName : classNames) {
            Class<?> cls = Class.forName(clsName);

            DaoManager.createDao(this.connectionSource, cls);

            dataClasses.add(cls);
        }
    }

    public void createTables() throws SQLException {
        for (Class<?> cls : dataClasses) {
            TableUtils.createTableIfNotExists(this.connectionSource, cls);
        }
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public List<Class<?>> getDataClasses() {
        return Collections.unmodifiableList(dataClasses);
    }

    public <D extends Dao<T, ?>, T> D lookupDao(Class<T> dataClass) throws SQLException {
        return DaoManager.createDao(this.connectionSource, dataClass);
    }

    public void shutdown() {
        dataClasses.clear();
        connectionSource.closeQuietly();
    }

    public static DbLayer initializeDatabase(ConfigurationSection advConfig, ConfigurationSection bukkitConfig) throws SQLException, ClassNotFoundException {
        if (advConfig.getBoolean("enabled", false)) {
            String host = bukkitConfig.getString("host");
            String port = bukkitConfig.getString("port");
            String db = bukkitConfig.getString("db");
            String user = bukkitConfig.getString("user");
            String pass = bukkitConfig.getString("pass");
            String connString = String.format("jdbc:mysql://%s:%s/%s", host, port, db);

            ConnectionSource dbConnection = new JdbcPooledConnectionSource(connString, user, pass);

            DbLayer dbLayer = new DbLayer(dbConnection);

            dbLayer.createDAOs(advConfig.getStringList("data_classes"));

            if (advConfig.getBoolean("create_tables", false)) {
                dbLayer.createTables();
            }

            return dbLayer;
        } else {
            return null;
        }
    }
}
