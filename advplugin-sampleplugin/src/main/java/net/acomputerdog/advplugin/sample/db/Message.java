package net.acomputerdog.advplugin.sample.db;

import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@DatabaseTable(tableName = "PlayerMessage")
public class Message {

    @Id
    @Column(nullable = false, name = "playerUUID")
    UUID playerUUID;

    @Column(nullable = false, length = 128, name = "message")
    String message;

    @Column(name = "updateTime")
    Date updateTime;

    Message() {
        // no-args for ORM
    }

    public Message(UUID playerUUID, String message) {
        this.playerUUID = playerUUID;
        this.message = message;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getMessage() {
        return message;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
