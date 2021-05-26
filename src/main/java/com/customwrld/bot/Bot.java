package com.customwrld.bot;

import com.customwrld.bot.commandapi.CommandManager;
import com.google.gson.GsonBuilder;
import lombok.Setter;
import com.customwrld.bot.config.Config;
import com.customwrld.bot.listeners.JoinListener;
import com.customwrld.bot.listeners.MessageListener;
import com.customwrld.bot.listeners.ReactionListener;
import com.customwrld.bot.listeners.ReadyListener;
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

    @Getter private static Bot instance;
    @Getter private final long start;
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
        instance = this;
        config = new Config();
        GSON = new GsonBuilder().setPrettyPrinting().create();
        timerManager = new TimerManager();
        jda = JDABuilder.createDefault(config.getBotToken())
                .setActivity(config.getBotActivity())
                .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new JoinListener(), new MessageListener(), new ReactionListener(), new ReadyListener())
                .build();
    }

}
