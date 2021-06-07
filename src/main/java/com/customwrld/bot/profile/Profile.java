package com.customwrld.bot.profile;

import com.customwrld.bot.Bot;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import com.customwrld.bot.profile.punishment.Punishment;
import com.customwrld.bot.profile.punishment.PunishmentType;
import org.bson.Document;

import java.util.*;

public class Profile {

    @Getter private static final MongoCollection<Document> collection = Bot.getInstance().getMongoDatabase().getCollection("discordProfiles");

    @Getter @Setter private String discordId;
    @Getter @Setter private Boolean ticketBanned;
    @Getter private final List<Punishment> punishments;
    @Getter @Setter private boolean loaded;

    public Profile(String discordId) {
        this.discordId = discordId;
        this.punishments = new ArrayList<>();
        load();
    }

    public Punishment getActivePunishmentByType(PunishmentType type) {
        for (Punishment punishment : punishments) {
            if (punishment.getType() == type && !punishment.isRemoved()) {
                return punishment;
            }
        }

        return null;
    }

    public void save() {
        Document document = new Document();
        document.put("discordId", discordId);
        document.put("ticketBanned", ticketBanned);

        List<HashMap<String, String>> simplePunishments = new ArrayList<>();
        for (Punishment punishment : punishments) {
            simplePunishments.add(punishment.simplify());
        }
        document.put("punishments", simplePunishments);

        collection.replaceOne(Filters.eq("discordId", discordId), document, new ReplaceOptions().upsert(true));
    }

    private void load() {
        Document document = collection.find(Filters.eq("discordId", discordId)).first();

        if (document != null) {
            ticketBanned = document.getBoolean("ticketBanned");

            Object obj = document.get("punishments");
            if (obj instanceof List) {
                List<?> simplePunishments = (List<?>) obj;
                for (Object obj2 : simplePunishments) {
                    if (!(obj2 instanceof Map))
                        continue;

                    Map<String, String> simplePunishment = new HashMap<>();
                    for (Map.Entry<?, ?> ent : ((Map<?, ?>) obj2).entrySet()) {
                        if (ent.getKey() instanceof String && ent.getValue() instanceof String) {
                            simplePunishment.put((String) ent.getKey(), (String) ent.getValue());
                        }
                    }
                    this.punishments.add(Punishment.fromSimple(simplePunishment));
                }
            }
        }
        loaded = true;
    }

}
