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

@Getter @Setter
public class Profile {

    public static final MongoCollection<Document> collection = Bot.getBot().getMongoDatabase().getCollection("discord-profiles");

    private UUID minecraftId;
    private String discordId;
    private Boolean ticketBanned;
    private final List<Punishment> punishments;
    private boolean loaded;

    public Profile(UUID minecraftId) {
        this.minecraftId = minecraftId;
        this.punishments = new ArrayList<>();
        load();
    }
    
    public Profile(String discordId) {
        this.discordId = discordId;
        this.punishments = new ArrayList<>();
        load();
    }

    public Punishment getActivePunishmentByType(PunishmentType type) {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType() == type && !punishment.isRemoved()) {
                return punishment;
            }
        }

        return null;
    }

    public void save() {
        Document document = new Document();
        document.put("minecraftId", this.minecraftId);
        document.put("discordId", this.discordId);
        document.put("ticketBanned", this.ticketBanned);

        List<HashMap<String, String>> simplePunishments = new ArrayList<>();
        for (Punishment punishment : this.punishments) {
            simplePunishments.add(punishment.simplify());
        }
        document.put("punishments", simplePunishments);

        collection.replaceOne(Filters.eq("discordId", this.discordId), document, new ReplaceOptions().upsert(true));
    }

    private void load() {
        Document document = null;
        
        if(this.minecraftId == null && this.discordId != null) {
            document = collection.find(Filters.eq("discordId", this.discordId)).first();
        } else if(this.discordId == null && this.minecraftId != null) {
            document = collection.find(Filters.eq("minecraftId", this.minecraftId)).first();
        }
        
        if (document != null) {
            this.minecraftId = UUID.fromString(document.getString("minecraftId"));
            this.ticketBanned = document.getBoolean("ticketBanned");

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
        this.loaded = true;
    }

}
