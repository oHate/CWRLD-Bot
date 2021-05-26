package com.customwrld.bot.tickets;

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

    @Getter private static Map<String, Ticket> activeTickets = new HashMap<>();
    @Getter private static final MongoCollection<Document> collection = Bot.getInstance().getMongoDatabase().getCollection("tickets");

    @Getter String discordId;
    @Getter @Setter String channelId;
    @Getter @Setter String reactionMessageId;

    public Ticket(String discordId, String channelId, String reactionMessageId) {
        this.discordId = discordId;
        this.channelId = channelId;
        this.reactionMessageId = reactionMessageId;
    }

    public boolean isValid() {
        return Bot.getInstance().getGuild().getTextChannelById(this.channelId) != null;
    }

    public static boolean canCreateTicket(String discordId) {
        Document document = collection.find(new Document("discordId", discordId)).first();

        if(document != null) {
            if(Bot.getInstance().getGuild().getTextChannelById(document.getString("channelId")) == null) {
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
            return new Ticket(document.getString("discordId"), document.getString("channelId"), document.getString("reactionMessageId"));
        }

        return null;
    }

    public void save() {
        Document document = new Document();
        document.put("discordId", discordId);
        document.put("channelId", channelId);
        document.put("reactionMessageId", reactionMessageId);

        collection.replaceOne(Filters.eq("discordId", discordId), document, new ReplaceOptions().upsert(true));
    }

    public void delete() {
        Document document = collection.find(new Document("discordId", discordId)).first();
        if(document == null) return;

        collection.deleteOne(document);
    }

}
