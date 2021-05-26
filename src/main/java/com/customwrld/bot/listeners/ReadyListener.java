package com.customwrld.bot.listeners;

import com.customwrld.bot.Bot;
import com.customwrld.bot.commandapi.CommandManager;
import com.customwrld.bot.config.Config;
import com.customwrld.bot.giveaway.Giveaway;
import com.customwrld.bot.profile.Profile;
import com.customwrld.bot.punishment.Punishment;
import com.customwrld.bot.punishment.PunishmentType;
import com.customwrld.bot.tickets.Ticket;
import com.customwrld.bot.timers.BanTimer;
import com.customwrld.bot.timers.GiveawayTimer;
import com.customwrld.bot.timers.MuteTimer;
import com.customwrld.bot.util.Logger;
import com.customwrld.bot.util.enums.LogType;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        Bot bot = Bot.getInstance();
        Config config = bot.getConfig();

        bot.setGuild(event.getJDA().getGuildById(config.getBotGuild()));

        if(bot.getGuild() == null) {
            Logger.log(LogType.ERROR, "Guild was marked as NULL! Is the bot in that Guild?");
            System.exit(0);
            return;
        }

        bot.setCommandManager(new CommandManager("com.customwrld.customwrldbot.commands"));
        Logger.log(LogType.INFO,"Command Manager has been loaded and activated!");

        loadMongo(bot, config);
        Logger.log(LogType.INFO,"MongoDB has been loaded and activated!");

        initializeTickets(bot.getGuild(), config);
        Logger.log(LogType.INFO,"Support Tickets have been loaded and activated!");

        initializeGiveaways(bot.getGuild());
        Logger.log(LogType.INFO,"Giveaways have been loaded and activated!");

        initializePunishments(bot.getGuild(), config);
        Logger.log(LogType.INFO,"Punishments have been loaded and activated!");

        Logger.log(LogType.READY, event.getJDA().getSelfUser().getAsTag() + " started in " + (System.currentTimeMillis() - bot.getStart()) + "ms.");
    }

    private void loadMongo(Bot bot, Config config) {
        bot.setMongoClient(new MongoClient(config.getMongoHost(), config.getMongoPort()));
        bot.setMongoDatabase(bot.getMongoClient().getDatabase(config.getMongoDatabase()));
    }

    private void initializeTickets(Guild guild, Config config) {
        TextChannel channel = guild.getTextChannelById(config.getTicketChannel());

        if (channel != null) {
            channel.clearReactionsById(config.getTicketMessage(), "U+1F3AB").queue(callback -> channel.addReactionById(config.getTicketMessage(), "U+1F3AB").queue());
        }

        FindIterable<Document> documents = Ticket.getCollection().find();
        for (Document document : documents) {
            Ticket ticket = new Ticket(document.getString("discordId"), document.getString("channelId"), document.getString("reactionMessageId"));
            if (!ticket.isValid()) {
                ticket.delete();
            } else {
                Ticket.getActiveTickets().put(ticket.getDiscordId(), ticket);
            }
        }
    }

    private void initializeGiveaways(Guild guild) {
        FindIterable<Document> documents = Giveaway.getCollection().find();
        for (Document document : documents) {
            Giveaway giveaway = new Giveaway(document.getLong("ends"), document.getString("channelId"), document.getString("messageId"), document.getString("prize"));

            giveaway.setFinished(document.getBoolean("finished"));

            giveaway.validate(callback -> {
                if (callback) {
                    guild.getTextChannelById(giveaway.getChannelId()).retrieveMessageById(giveaway.getMessageId()).queue(message -> new GiveawayTimer(giveaway).start());
                }
            });
        }
    }

    private void initializePunishments(Guild guild, Config config) {
        FindIterable<Document> documents = Profile.getCollection().find();
        for (Document document : documents) {
            Profile profile = new Profile(document.getString("discordId"));
            Punishment banPunishment = profile.getActivePunishmentByType(PunishmentType.BAN);

            if (banPunishment != null && !banPunishment.isPermanent()) {
                if(banPunishment.hasExpired() && !banPunishment.isRemoved()) {
                    banPunishment.setRemoved(true);
                    banPunishment.setRemovedReason("Punishment Expired");
                    banPunishment.setRemovedAt(System.currentTimeMillis());
                    banPunishment.setRemovedBy(null);
                    profile.save();
                    guild.retrieveMemberById(profile.getDiscordId()).queue(member -> guild.unban("Punishment Expired").queue());
                } else {
                    new BanTimer(profile, banPunishment).start();
                }
            }

            Punishment mutePunishment = profile.getActivePunishmentByType(PunishmentType.MUTE);

            if (mutePunishment != null && !mutePunishment.isPermanent()) {
                if(mutePunishment.hasExpired() && !mutePunishment.isRemoved()) {
                    mutePunishment.setRemoved(true);
                    mutePunishment.setRemovedReason("Punishment Expired");
                    mutePunishment.setRemovedAt(System.currentTimeMillis());
                    mutePunishment.setRemovedBy(null);
                    profile.save();
                    guild.retrieveMemberById(profile.getDiscordId()).queue(member -> guild.removeRoleFromMember(member, guild.getRoleById(config.getMutedRole())).queue());
                } else {
                    new MuteTimer(profile, mutePunishment).start();
                }
            }
        }
    }

}
