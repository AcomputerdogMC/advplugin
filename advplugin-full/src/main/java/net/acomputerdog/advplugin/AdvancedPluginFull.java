package net.acomputerdog.advplugin;

import net.acomputerdog.advplugin.db.DbLayer;

import java.sql.SQLException;

public abstract class AdvancedPluginFull extends AdvancedPlugin {
    private DbLayer dbLayer;

    public DbLayer getDatabase() {
        return dbLayer;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        try {
            if (getPluginYml().contains("adv-database")) {
                dbLayer = DbLayer.initializeDatabase(
                        getPluginYml().getConfigurationSection("adv-database"),
                        getConfig().getConfigurationSection("database"));
            } else {
                dbLayer = null;
            }
        } catch (SQLException | ClassNotFoundException e) {
            getALogger().logFatal("Error connecting to database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        if (dbLayer != null) {
            dbLayer.shutdown();
            dbLayer = null;
        }

        super.onDisable();
    }
}
