package net.acomputerdog.advplugin.cmd;

import net.acomputerdog.advplugin.AdvancedPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandLayer {
    private final AdvancedPlugin plugin;

    private final Map<String, CmdData> commandMap;

    public CommandLayer(AdvancedPlugin plugin, ConfigurationSection commands) {
        this.plugin = plugin;
        commandMap = new HashMap<>();

        this.registerCommands(commands);
    }

    public void registerCommands(ConfigurationSection commands) {
        for (String cmdName : commands.getKeys(false)) {
            ConfigurationSection section = commands.getConfigurationSection(cmdName);

            String perm = section.getString("permission", null);
            String usage = section.getString("usage", "");
            int maxArgs = section.getInt("max-args", -1);
            int minArgs = section.getInt("min-args", 0);
            boolean requiresPlayer = section.getBoolean("requires-player", false);

            CmdHandler cmdHandler = plugin.defineCommandHandlerFor(cmdName);
            if (cmdHandler == null) {
                plugin.getALogger().logWarn(() -> "Plugin did not define a command handler for: " + cmdName);
            }

            CmdData cmd = new CmdData(cmdName, perm, requiresPlayer, minArgs, maxArgs, usage, cmdHandler);

            if (commandMap.containsKey(cmdName)) {
                plugin.getALogger().logWarn(() -> "Duplicate command: " + cmdName);
            }

            commandMap.put(cmdName, cmd);
        }
    }

    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        CmdData cmd = commandMap.get(command.getName());
        if (cmd != null) {
            if (sender.hasPermission(cmd.requiredPermission)) {
                if (!cmd.requiresPlayer || sender instanceof Player) {
                    // TODO no magic numbers
                    if (args.length >= cmd.minimumArgs && (args.length <= cmd.maximumArgs || cmd.maximumArgs == -1)) {
                        if (cmd.commandHandler != null) {
                            handleCommand(cmd, sender, command, label, args);
                        } else {
                            sender.sendMessage(ChatColor.RED + "An error occurred while processing this command: no handler was registered.  Please report this to a server administrator.");
                            plugin.getALogger().logError(() -> "No handler for command: " + command.getName());
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Wrong number of arguments.  Usage: " + cmd.commandUsage);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Sorry, you do not have permission to use that command.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "An error occurred while processing this command: command was not registered.  Please report this to a server administrator.");
            plugin.getALogger().logError(() -> "Command not registered to this plugin: " + command.getName());
        }
    }

    private void handleCommand(CmdData cmdData, CommandSender sender, Command command, String label, String[] args) {
        Cmd cmd = new Cmd(this.plugin, sender, command, label, args);

        try {
            cmdData.commandHandler.runCommand(cmd);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An error occurred while processing this command: exception thrown in handler.  Please report this to a server administrator.");
            plugin.getALogger().logError(() -> "Exception thrown while executing command: " + command.getName(), e);
        }
    }

    public static class CmdData {
        private final String name;
        private final String requiredPermission;
        private final boolean requiresPlayer;
        private final int minimumArgs;
        private final int maximumArgs;
        private final String commandUsage;

        private final CmdHandler commandHandler;

        private CmdData(String name, String requiredPermission, boolean requiresPlayer, int minimumArgs, int maximumArgs, String commandUsage, CmdHandler commandHandler) {
            this.name = name;
            this.requiredPermission = requiredPermission;
            this.requiresPlayer = requiresPlayer;
            this.minimumArgs = minimumArgs;
            this.maximumArgs = maximumArgs;
            this.commandUsage = commandUsage;
            this.commandHandler = commandHandler;
        }

        public String getName() {
            return name;
        }

        public String getRequiredPermission() {
            return requiredPermission;
        }

        public boolean isRequiresPlayer() {
            return requiresPlayer;
        }

        public int getMinimumArgs() {
            return minimumArgs;
        }

        public int getMaximumArgs() {
            return maximumArgs;
        }

        public String getCommandUsage() {
            return commandUsage;
        }

        public CmdHandler getCommandHandler() {
            return commandHandler;
        }
    }
}
