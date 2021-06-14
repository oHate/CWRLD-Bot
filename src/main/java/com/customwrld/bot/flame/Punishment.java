package com.customwrld.bot.flame;

import com.customwrld.bot.Bot;
import com.customwrld.bot.util.TimeUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

@Getter
@Setter
public class Punishment {

    @Getter
    private static final MongoCollection<Document> collection = Bot.getBot().getMongoDatabase().getCollection("punishments");

    private final String id;
    private UUID player;
    private Type type;
    private UUID addedBy;
    private Long addedAt;
    private String addedReason;
    private Long duration;
    private UUID removedBy;
    private Long removedAt;
    private String removedReason;
    private boolean removed;

    private Punishment(String id, UUID player, Type type, UUID addedBy, Long addedAt, String addedReason, Long duration) {
        this.id = id;
        this.player = player;
        this.type = type;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
    }

    public static Punishment getById(String id) {
        Document document = collection.find(Filters.eq("_id", id)).first();

        if (document != null) {
            Punishment punishment = new Punishment(document.getString("_id"),
                    UUID.fromString(document.getString("player")),
                    Type.valueOf(document.getString("type")),
                    document.getString("addedBy") != null ? UUID.fromString(document.getString("addedBy")) : null,
                    document.getLong("addedAt"),
                    document.getString("addedReason"),
                    document.getLong("duration"));

            punishment.setRemoved(document.getBoolean("removed"));

            if (punishment.isRemoved()) {
                punishment.setRemovedAt(document.getLong("removedAt"));
                punishment.setRemovedBy(document.getString("removedBy") != null ? UUID.fromString(document.getString("removedBy")) : null);
                punishment.setRemovedReason(document.getString("removedReason"));
            }

            return punishment;
        }

        return null;
    }

    public boolean isPermanent() {
        return type == Type.BLACKLIST || duration == Integer.MAX_VALUE;
    }

    public boolean isActive() {
        return !this.removed && (this.isPermanent() || this.getMillisRemaining() < 0L);
    }

    public long getMillisRemaining() {
        return (addedAt + duration) - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
    }

    public String getDurationText() {
        if (isPermanent() || duration == 0) {
            return "Permanent";
        } else {
            return TimeUtil.millisToRoundedTime(duration);
        }
    }

    public String getTimeRemaining() {
        if (removed) {
            return "Removed";
        }

        if (isPermanent()) {
            return "Permanent";
        }

        if (hasExpired()) {
            return "Expired";
        }

        return TimeUtil.millisToRoundedTime((addedAt + duration) - System.currentTimeMillis());
    }

    public String getContext() {
        if (!(type == Type.BAN || type == Type.MUTE)) {
            return removed ? type.getUndoContext() : type.getContext();
        }

        if (isPermanent()) {
            return (removed ? type.getUndoContext() : "permanently " + type.getContext());
        } else {
            return (removed ? type.getUndoContext() : "temporarily " + type.getContext());
        }
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Punishment && ((Punishment) object).id.equals(this.id);
    }

    @Getter
    @AllArgsConstructor
    public enum Type {

        BLACKLIST("Blacklist", "blacklisted", "unblacklisted", true, true),
        BAN("Ban", "banned", "unbanned", true, true),
        MUTE("Mute", "muted", "unmuted", false, true),
        WARN("Warning", "warned", null, false, false);

        private final String readable;
        private final String context;
        private final String undoContext;
        private final boolean ban;
        private final boolean removable;

    }

}