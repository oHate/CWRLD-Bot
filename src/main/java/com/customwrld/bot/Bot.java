package com.customwrld.bot;

import com.customwrld.bot.commandapi.button.ButtonHandler;
import com.customwrld.bot.commandapi.CommandManager;
import com.customwrld.bot.listeners.*;
import com.customwrld.pigeon.Pigeon;
import com.google.gson.GsonBuilder;
import lombok.Setter;
import com.customwrld.bot.util.config.Config;
import com.customwrld.bot.timer.TimerManager;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {

    @Getter private static Bot bot;
    @Getter private final long start;
    @Getter private final Pigeon pigeon;
    @Getter private final Config config;
    @Getter private final JDA jda;
    @Getter private final Gson GSON;
    @Getter @Setter private CommandManager commandManager;
    @Getter @Setter private Guild guild;
    @Getter @Setter private MongoClient mongoClient;
    @Getter @Setter private MongoDatabase mongoDatabase;
    @Getter @Setter private TimerManager timerManager;

    @SneakyThrows
    public Bot() {
        start = System.currentTimeMillis();
        bot = this;

        this.pigeon = new Pigeon();

        this.pigeon.initialize("127.0.0.1", 5672, "customwrld", "discord");

        this.pigeon.getConvertersRegistry()
                .registerConvertersInPackage("com.customwrld.bot.pigeon.converters");

        this.pigeon.getPayloadsRegistry()
                .registerPayloadsInPackage("com.customwrld.bot.pigeon.payloads");

        this.pigeon.getListenersRegistry().registerListener(new PigeonListener());

        this.pigeon.setupDefaultUpdater();

        this.pigeon.acceptDelivery();

        config = new Config();

        GSON = new GsonBuilder().setPrettyPrinting().create();

        timerManager = new TimerManager();

        jda = JDABuilder.createDefault(config.getBotToken())
                .setActivity(config.getBotActivity())
                .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new JoinListener(), new MessageListener(), new ReadyListener(), new ButtonHandler())
                .build();
    }


    //    public void sendEmbed(Profile profile) {
//        JDA jda = CustomLib.getCustomLib().getJda();
//        String name = profile.getName().replace("_", "\\_");
//
//        Guild guild = jda.getGuildById(Config.DISCORD_GUILD.format());
//        Color color = this.getType().getTypeData().getColor().getColor();
//
//        if (guild != null) {
//            TextChannel channel = guild.getTextChannelById(Config.DISCORD_PUNISHMENTS.format());
//
//            if (channel != null) {
//                EmbedBuilder builder = new EmbedBuilder();
//
//                builder.setTitle(name + " has been " + this.getContext())
//                        .setColor(color)
//                        .addField("**Player Name**", name, true)
//                        .addField("**Player UUID**", profile.getUuid().toString(), true);
//
//                if (profile.getDiscord() != null) {
//                    User user = jda.getUserById(profile.getDiscord());
//
//                    if (profile.isSyncedWithDiscord() && user != null) {
//                        builder.addField("**Discord ID**", profile.getDiscord(), true)
//                                .addField("**Discord Name**", user.getName() + "#" + user.getDiscriminator(), true);
//                    }
//                }
//
//                if (!this.isRemoved()) {
//                    if (this.getAddedBy() != null) {
//                        builder.addField("**Staff Member Name**", Profile.getByUuid(this.getAddedBy()).getName(), true)
//                                .addField("**Staff Member UUID**", this.getAddedBy().toString(), true);
//                    } else {
//                        builder.addField("**Staff Member Name**", "Console", true);
//                    }
//                    if (!this.isPermanent() && !this.hasExpired()) {
//                        builder.addField("**Duration**", TimeUtil.millisToRoundedTime(duration), true);
//                    }
//                } else {
//                    if (this.getRemovedBy() != null) {
//                        builder.addField("**Staff Member Name**", Profile.getByUuid(this.getRemovedBy()).getName(), true)
//                                .addField("**Staff Member UUID**", this.getRemovedBy().toString(), true);
//                    } else {
//                        builder.addField("**Staff Member Name**", "Console", true);
//                    }
//                }
//
//                builder.addField("**Reason**", this.getAddedReason(), true)
//                        .setThumbnail("https://crafatar.com/avatars/" + profile.getUuid().toString().replaceAll("-", ""));
//
//                channel.sendMessage(builder.build()).queue();
//            }
//        }
//    }

}
