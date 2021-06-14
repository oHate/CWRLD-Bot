package com.customwrld.bot.flame;

import com.customwrld.bot.Bot;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.*;

@Getter @Setter
public class Grant {

    @Getter private static final MongoCollection<Document> collection = Bot.getBot().getMongoDatabase().getCollection("flame-grants");

    private final UUID id;
    private final UUID uuid;
    private UUID rank;
    private UUID addedBy;
    private long addedAt;
    private String addedReason;
    private long duration;
    private UUID removedBy;
    private long removedAt;
    private String removedReason;
    private boolean removed;

    public Grant(UUID id, UUID uuid, UUID rank, UUID addedBy, long addedAt, String addedReason, long duration) {
        this.id = id;
        this.uuid = uuid;
        this.rank = rank;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
    }

//    public boolean isPermanent() {
//        return duration == Integer.MAX_VALUE;
//    }
//
//    public boolean hasExpired() {
//        return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
//    }
//
//    public String getExpiresAtDate() {
//        if (duration == Integer.MAX_VALUE) {
//            return "Never";
//        }
//
//        return TimeUtil.dateToString(new Date(addedAt + duration));
//    }
//
//    public String getDurationText() {
//        if (removed) {
//            return "Removed";
//        }
//
//        if (isPermanent()) {
//            return "Permanent";
//        }
//
//        return TimeUtil.millisToRoundedTime(duration);
//    }

    public static Grant getById(String id) {
        Document document = collection.find(Filters.eq("_id", id)).first();

        if (document != null) {
            Grant grant = new Grant(UUID.fromString(document.getString("_id")),
                    UUID.fromString(document.getString("player")),
                    UUID.fromString(document.getString("rank")),
                    document.getString("addedBy") != null ? UUID.fromString(document.getString("addedBy")) : null,
                    document.getLong("addedAt"),
                    document.getString("addedReason"),
                    document.getLong("duration"));

            grant.setRemoved(document.getBoolean("removed"));

            if (grant.isRemoved()) {
                grant.setRemovedAt(document.getLong("removedAt"));
                grant.setRemovedBy(document.getString("removedBy") != null ? UUID.fromString(document.getString("removedBy")) : null);
                grant.setRemovedReason(document.getString("removedReason"));
            }

            return grant;
        }

        return null;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Grant && ((Grant) object).uuid.equals(uuid);
    }

}