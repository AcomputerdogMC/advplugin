package net.acomputerdog.advplugin.sample;

import com.j256.ormlite.dao.Dao;
import net.acomputerdog.advplugin.AdvancedPluginFull;
import net.acomputerdog.advplugin.async.AsyncTask;
import net.acomputerdog.advplugin.cmd.CmdHandler;
import net.acomputerdog.advplugin.sample.db.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class SamplePlugin extends AdvancedPluginFull {
    Dao<Message, UUID> messageDao;

    @Override
    public void onEnable() {
        super.onEnable();

        try {
            messageDao = getDatabase().lookupDao(Message.class);

            long count = messageDao.countOf();

            getALogger().logDetail(() -> "Number of messages in database: " + count);
        } catch (SQLException e) {
            getALogger().logError(() -> "Error looking up messages in database", e);
        }
    }

    @Override
    public CmdHandler defineCommandHandlerFor(String commandName) {
        switch (commandName) {
            case "getmessage":
                return defineGetMessageHandler();
            case "setmessage":
                return defineSetMessageHandler();
            default: {
                getALogger().logWarn(() -> "Attempted to define command handler for unknown command: "+ commandName);
                return null;
            }
        }
    }

    private CmdHandler defineGetMessageHandler() {
        return cmd -> {
            UUID uuid;
            if (cmd.getArgsList().length == 0) {
                if (cmd.getUser() instanceof Player) {
                    uuid = ((Player) cmd.getUser()).getUniqueId();
                } else {
                    cmd.getUser().sendMessage(ChatColor.RED + "This command can only be used by a player, or a player name / UUID must be specified.");
                    return;
                }
            } else {
                String nameOrUUID = cmd.getArgsList()[0];
                Player p = getServer().getPlayer(nameOrUUID);
                if (p != null) {
                    uuid = p.getUniqueId();
                } else {
                    try {
                        uuid = UUID.fromString(nameOrUUID);
                    } catch (IllegalArgumentException e) {
                        cmd.getUser().sendMessage(ChatColor.RED + "Unable to find a player by that name or UUID.  Maybe they are offline?");
                        return;
                    }
                }
            }

            AsyncTask<Message> task = runAsyncTask(t -> {
                t.result = messageDao.queryForId(uuid);
                if (t.result == null) {
                    t.fail = true;
                }
            }, t -> {
                if (t.exception != null) {
                    cmd.getUser().sendMessage(ChatColor.RED + "A database error occurred.  Please report this to a server administrator.");
                    getALogger().logError("Exception occurred while looking up player message", t.exception);
                } else {
                    cmd.getUser().sendMessage(ChatColor.YELLOW + "That player does not have a message.");
                }
            }, t -> {
                cmd.getUser().sendMessage(ChatColor.AQUA + t.result.getMessage());
            });
        };
    }

    private CmdHandler defineSetMessageHandler() {
        return cmd -> runAsyncTask(t -> {
            UUID uuid = cmd.getPlayerUser().getUniqueId();

            Message message = messageDao.queryForId(uuid);
            if (message == null) {
                message = new Message(uuid, cmd.getArgsLine());
                messageDao.create(message);
            } else {
                message.setMessage(cmd.getArgsLine());
                message.setUpdateTime(new Date());
                messageDao.update(message);
            }
        },t -> {
            if (t.fail) {
                cmd.getUser().sendMessage(ChatColor.RED + "A database error occurred.  Please report this to a server administrator.");
                getALogger().logError("Exception occurred while updating player message", t.exception);
            } else {
                cmd.getUser().sendMessage(ChatColor.AQUA + "Message updated.");
            }
        });
    }
}
