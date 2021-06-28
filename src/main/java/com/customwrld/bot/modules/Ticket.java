package com.customwrld.bot.modules;

import com.customwrld.bot.Bot;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class Ticket {

    @Getter private static final Map<String, Ticket> activeTickets = new HashMap<>();
    @Getter private static final MongoCollection<Document> collection = Bot.getBot().getMongoDatabase().getCollection("tickets");

    @Getter String discordId;
    @Getter @Setter String channelId;

    public Ticket(String discordId, String channelId) {
        this.discordId = discordId;
        this.channelId = channelId;
    }

    public boolean isValid() {
        return Bot.getBot().getGuild().getTextChannelById(this.channelId) != null;
    }

    public static boolean canCreateTicket(String discordId) {
        Document document = collection.find(new Document("discordId", discordId)).first();

        if(document != null) {
            if(Bot.getBot().getGuild().getTextChannelById(document.getString("channelId")) == null) {
                collection.deleteOne(document);
                Ticket.getActiveTickets().remove(discordId);
                return true;
            }
            return false;
        } else {
            Ticket.getActiveTickets().remove(discordId);
            return true;
        }
    }

    public static Ticket getById(String discordId) {
        Document document = collection.find(new Document("discordId", discordId)).first();

        if(document != null) {
            return new Ticket(document.getString("discordId"), document.getString("channelId"));
        }

        return null;
    }

    public void save() {
        Document document = new Document();
        document.put("discordId", discordId);
        document.put("channelId", channelId);

        collection.replaceOne(Filters.eq("discordId", discordId), document, new ReplaceOptions().upsert(true));
    }

    public void delete() {
        Document document = collection.find(new Document("discordId", discordId)).first();
        if(document == null) return;

        collection.deleteOne(document);
    }

}
