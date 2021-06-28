package com.customwrld.bot.listeners;

import com.customwrld.bot.Bot;
import com.customwrld.bot.commandapi.CommandManager;
import com.customwrld.bot.modules.Server;
import com.customwrld.bot.pigeon.Listeners;
import com.customwrld.bot.util.config.Config;
import com.customwrld.bot.util.Giveaway;
import com.customwrld.bot.pigeon.payloads.AtomStatsRequestPayload;
import com.customwrld.bot.modules.Ticket;
import com.customwrld.bot.util.timer.timers.GiveawayTimer;
import com.customwrld.bot.util.*;
import com.customwrld.commonlib.CommonLib;
import com.customwrld.pigeon.Pigeon;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        Bot bot = Bot.getBot();
        Config config = bot.getConfig();

        bot.setGuild(event.getJDA().getGuildById(config.getBotGuild()));

        Util.initialize(bot.getGuild());

        if (bot.getGuild() == null) {
            Logger.log(Logger.LogType.ERROR, "Guild was marked as NULL! Is the bot in that Guild?");
            System.exit(0);
            return;
        }

        bot.setCommandManager(new CommandManager("com.customwrld.bot.commandapi.commands"));
        Logger.log(Logger.LogType.INFO, "Command Manager has been loaded and activated!");

        loadMongo(bot, config);
        Logger.log(Logger.LogType.INFO, "MongoDB has been loaded and activated!");

        loadPigeon(bot);

        bot.setCommonLib(new CommonLib(bot.getMongoClient(), bot.getMongoDatabase(), bot.getPigeon()));
        Logger.log(Logger.LogType.INFO, "CommonLib has been loaded and activated!");

        bot.getPigeon().acceptDelivery();

        loadTickets();
        Logger.log(Logger.LogType.INFO, "Support Tickets have been loaded and activated!");

        loadGiveaways(bot.getGuild());
        Logger.log(Logger.LogType.INFO, "Giveaways have been loaded and activated!");

        Logger.log(Logger.LogType.READY, event.getJDA().getSelfUser().getAsTag() + " started in " + (System.currentTimeMillis() - bot.getStart()) + "ms.");

        new Thread(() -> {
            while (true) {
                try {
                    TextChannel channel = bot.getGuild().getTextChannelById("847189427167166464");

                    if (channel != null) {
                        channel.retrieveMessageById("850893270497886208").queue(message -> {
                            bot.getPigeon().broadcast(new AtomStatsRequestPayload((payload) -> {
                                List<Server> servers = payload.getServers();

                                EmbedBuilder builder = new EmbedBuilder()
                                        .setColor(Bot.getBot().getConfig().getBotColor())
                                        .setTitle("Atom Status & Server Information")
                                        .setFooter("Atom Started")
                                        .setTimestamp(new Date(System.currentTimeMillis() - payload.getUptime()).toInstant());

                                int players = 0;

                                for (Server server : servers) {
                                    players += server.getPlayers().size();

                                    builder.addField(
                                            new MessageEmbed.Field(
                                                    "__**Server: " + server.getName() + "**__",
                                                    "```┌─────────────────\n" +
                                                            "│ Players: " + server.getPlayers().size() + "\n" +
                                                            "│ TPS: " + server.getTps() + "\n" +
                                                            "│ Time: " + TimeUtil.millisToRoundedTime(server.getUptime()) + "\n" +
                                                            "│ Type: " + server.getType() + "\n" +
                                                            "└─────────────────```",
                                                    true)
                                    );

                                    for (int i = 0; i < (3 - (servers.size() % 3)) % 3; i++) {
                                        builder.addField(new MessageEmbed.Field("__**Server: None**__", "```┌─────────────────\n│\n│\n│\n│\n└─────────────────```", true));
                                    }
                                }

                                builder.setDescription("```" + Table.of(new String[][]{
                                        new String[]{"Response Time", System.currentTimeMillis() - payload.getLatency() + "ms"}, // TODO: CACHE RESPONSE TIME SO IT CAN BE USED IN PING COMMAND
                                        new String[]{"Online Players", players + "/1000"},
                                        new String[]{"Uptime", TimeUtil.millisToRoundedTime(payload.getUptime())},
                                        new String[]{"CPU Usage", new DecimalFormat("#0.00").format(payload.getUsedCpu() * 100) + "% of 100%"}, // TODO: DEBUG CPU IT SOMETIMES DISPLAYS 0
                                        new String[]{"RAM Usage", Util.readableBytes(payload.getUsedMemory()) + " of " + Util.readableBytes(ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getTotalMemorySize())},
                                }) + "```");

                                message.editMessage(builder.build()).queue();
                            }));

                        });
                    }

                    Thread.sleep(30000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadMongo(Bot bot, Config config) {
        bot.setMongoClient(new MongoClient(config.getMongoHost(), config.getMongoPort()));
        bot.setMongoDatabase(bot.getMongoClient().getDatabase(config.getMongoDatabase()));
    }

    private void loadPigeon(Bot bot) {
        bot.setPigeon(new Pigeon());

        Pigeon pigeon = bot.getPigeon();

        pigeon.initialize("127.0.0.1", 5672, "customwrld", "discord");

        pigeon.getConvertersRegistry()
                .registerConvertersInPackage("com.customwrld.bot.pigeon.converters");

        pigeon.getPayloadsRegistry()
                .registerPayloadsInPackage("com.customwrld.bot.pigeon.payloads");

        pigeon.getListenersRegistry()
                .registerListener(new Listeners());

        pigeon.setupDefaultUpdater();
    }

    private void loadTickets() {
        FindIterable<Document> documents = Ticket.getCollection().find();

        for (Document document : documents) {
            Ticket ticket = new Ticket(document.getString("discordId"), document.getString("channelId"));
            if (!ticket.isValid()) {
                ticket.delete();
            } else {
                Ticket.getActiveTickets().put(ticket.getDiscordId(), ticket);
            }
        }
    }

    private void loadGiveaways(Guild guild) {
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

}
