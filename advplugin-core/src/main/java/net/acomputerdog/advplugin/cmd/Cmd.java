package net.acomputerdog.advplugin.cmd;

import net.acomputerdog.advplugin.AdvancedPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmd {
    private final AdvancedPlugin plugin;
    private final CommandSender user;
    private final Command command;
    private final String alias;
    private final String[] argsList;

    private String argsLine;

    public Cmd(AdvancedPlugin plugin, CommandSender user, Command command, String alias, String[] argsList) {
        this.plugin = plugin;
        this.user = user;
        this.command = command;
        this.alias = alias;
        this.argsList = argsList;
    }

    public AdvancedPlugin getPlugin() {
        return plugin;
    }

    public CommandSender getUser() {
        return user;
    }

    public Command getCommand() {
        return command;
    }

    public String getAlias() {
        return alias;
    }

    public String getArgsLine() {
        if (argsLine == null) {
            argsLine = String.join(" ", argsList);
        }
        return argsLine;
    }

    public String[] getArgsList() {
        return argsList;
    }

    public Player getPlayerUser() {
        return (Player)user;
    }
}
