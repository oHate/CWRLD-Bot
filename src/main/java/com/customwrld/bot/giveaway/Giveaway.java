package com.customwrld.bot.giveaway;

import com.customwrld.bot.Bot;
import com.customwrld.bot.config.Config;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

import java.util.function.Consumer;

public class Giveaway {

    @Getter private static MongoCollection<Document> collection = Bot.getInstance().getMongoDatabase().getCollection("giveaways");

    @Getter @Setter Long ends;
    @Getter @Setter String channelId;
    @Getter @Setter String messageId;
    @Getter @Setter String prize;
    @Getter @Setter int seconds;
    @Getter @Setter private boolean finished;

    public Giveaway(Long ends, String channelId, String messageId, String prize) {
        this.ends = ends;
        this.channelId = channelId;
        this.messageId = messageId;
        this.prize = prize;
        this.seconds = (int) (ends - System.currentTimeMillis()) / 1000;
    }

    public void save() {
        Document document = new Document();
        document.put("ends", ends);
        document.put("channelId", channelId);
        document.put("messageId", messageId);
        document.put("prize", prize);
        document.put("finished", finished);

        collection.replaceOne(Filters.eq("messageId", messageId), document, new ReplaceOptions().upsert(true));
    }

    public static Giveaway getById(String messageId) {
        Document document = collection.find(new Document("messageId", messageId)).first();
        if(document == null) return null;

        return new Giveaway(document.getLong("ends"), document.getString("channelId"), document.getString("messageId"), document.getString("prize"));
    }

    public boolean hasEnded() {
        return (ends - System.currentTimeMillis()) <= 0;
    }

    public void validate(Consumer<Boolean> consumer) {
        TextChannel channel = Bot.getInstance().getGuild().getTextChannelById(this.channelId);
        if(channel != null) {
            channel.retrieveMessageById(messageId).queue((message) -> {
                    if(!hasEnded() && !finished) {
                        consumer.accept(true);
                    } else {
                        if(!isFinished()) {
                            setFinished(true);
                            save();
                            drawWinner(message);
                        }
                        consumer.accept(false);
                    }
            }, (failed) -> {
                delete();
                consumer.accept(false);
            });
        } else {
            delete();
            consumer.accept(false);
        }
    }

    public void delete() {
        Document document = collection.find(new Document("messageId", messageId)).first();
        if(document == null) return;

        collection.deleteOne(document);
    }

    public void drawWinner(Message message) {
        Config config = Bot.getInstance().getConfig();

        message.getReactions()
                .stream().filter(r -> r.getReactionEmote().getName().equals("\uD83C\uDF89"))
                .findAny().ifPresent(mr -> {
            List<User> users = new LinkedList<>();
            mr.retrieveUsers().queue(retrieved -> {
                retrieved.stream().distinct().filter(u -> !u.isBot()).forEach(users::add);

                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(config.getBotColor())
                        .setTitle(this.prize + " Giveaway")
                        .setFooter("Ended at");

                if(users.size() < 1) {
                    builder.setDescription("Nobody won the Giveaway.");
                    builder.setTimestamp(Instant.ofEpochMilli(this.ends).atZone(ZoneOffset.UTC));

                    message.editMessage(builder.build()).queue();
                } else {
                    String id = users.get((int) (Math.random() * users.size())).getId();

                    builder.setDescription("The giveaway has concluded! Congratulations to the Winner!");
                    builder.setTimestamp(Instant.ofEpochMilli(this.ends).atZone(ZoneOffset.UTC));

                    message.editMessage(builder.build()).queue();

                    EmbedBuilder winnerEmbed = new EmbedBuilder().setColor(config.getBotColor())
                            .setDescription("Congratulations to <@" + id + ">! You won the " + prize + " giveaway!");
                    message.getChannel().sendMessage(winnerEmbed.build()).queue();
                }
                setFinished(true);
                save();
            });

        });

    }

}
