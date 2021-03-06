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

    public static DbLayer initializeDatabase(ConfigurationSection pluginYml, ConfigurationSection bukkitConfig) throws SQLException, ClassNotFoundException {
        String host = bukkitConfig.getString("host", "localhost");
        int port = bukkitConfig.getInt("port", 3306);
        String db = bukkitConfig.getString("db", "minecraft");
        String user = bukkitConfig.getString("user", "root");
        String pass = bukkitConfig.getString("pass", "");

        StringBuilder options = new StringBuilder();
        options.append("autoReconnect=true");
        if (bukkitConfig.contains("use-ssl")) {
            options.append("&useSSL=");
            options.append(bukkitConfig.getBoolean("use-ssl"));
        }

        String connString = String.format("jdbc:mysql://%s:%d/%s?%s", host, port, db, options.toString());

        ConnectionSource dbConnection = new JdbcPooledConnectionSource(connString, user, pass);

        DbLayer dbLayer = new DbLayer(dbConnection);

        dbLayer.createDAOs(pluginYml.getStringList("data_classes"));

        if (pluginYml.getBoolean("create_tables", false)) {
            dbLayer.createTables();
        }

        return dbLayer;
    }
}
