package net.acomputerdog.advplugin;

import net.acomputerdog.advplugin.async.AsyncTask;
import net.acomputerdog.advplugin.async.AsyncTask.AsyncBlock;
import net.acomputerdog.advplugin.async.AsyncTask.SyncBlock;
import net.acomputerdog.advplugin.cmd.CmdHandler;
import net.acomputerdog.advplugin.cmd.CommandLayer;
import net.acomputerdog.advplugin.log.ALogger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AdvancedPlugin extends JavaPlugin {

    private YamlConfiguration pluginYml;
    private ALogger aLogger;
    private CommandLayer commandLayer;

    public ALogger getALogger() {
        return aLogger;
    }

    public Configuration getPluginYml() {
        return pluginYml;
    }

    public CommandLayer getCommandLayer() {
        return commandLayer;
    }

    public <T> AsyncTask<T> runAsyncTask(AsyncBlock<T> async) {
        return runAsyncTask(new AsyncTask<>(this, async));
    }

    public <T> AsyncTask<T> runAsyncTask(AsyncBlock<T> async, SyncBlock<T> sync) {
        return runAsyncTask(new AsyncTask<>(this, async, sync));
    }

    public <T> AsyncTask<T> runAsyncTask(AsyncBlock<T> async, SyncBlock<T> onError, SyncBlock<T> onSuccess) {
        return runAsyncTask(new AsyncTask<>(this, async, onError, onSuccess));
    }

    public <T> AsyncTask<T> runAsyncTask(AsyncTask<T> task) {
        Bukkit.getScheduler().runTaskAsynchronously(this, task);
        return task;
    }

    public CmdHandler defineCommandHandlerFor(String commandName) {
        return null;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        pluginYml = YamlConfiguration.loadConfiguration(getTextResource("plugin.yml"));

        aLogger = new ALogger(getLogger());
        if (pluginYml.contains("min-log-level")) {
            aLogger.setLogLevel(pluginYml.getString("min-log-level"));
        }

        commandLayer = new CommandLayer(this, pluginYml.getConfigurationSection("commands"));
    }

    @Override
    public void onDisable() {
        aLogger = null;
        commandLayer = null;
        pluginYml = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        commandLayer.onCommand(sender, command, label, args);
        return true;
    }
}
