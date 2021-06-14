package com.customwrld.bot.flame;

import com.customwrld.bot.Bot;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.*;

@Getter @Setter
public class Rank {

    @Getter private static MongoCollection<Document> collection = Bot.getBot().getMongoDatabase().getCollection("ranks");

    private final UUID uuid;
    private String name;
    private String prefix;

    public Rank(UUID uuid) {
        this.uuid = uuid;
    }

    public static Rank getByUuid(UUID uuid) {
        Document document = collection.find(Filters.eq("_id", uuid.toString())).first();

        if (document != null) {
            Rank rank = new Rank(UUID.fromString(document.getString("_id")));

            rank.setPrefix(document.getString("prefix"));
            rank.setName(document.getString("name"));

            return rank;
        }

        return null;
    }

}