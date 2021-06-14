package com.customwrld.bot.util.config;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Activity;

import java.awt.*;
import java.util.List;

public class Config {

    @Getter private final String botToken;
    @Getter private final Activity botActivity;
    @Getter private final String botPrefix;
    @Getter private final String botGuild;
    @Getter private final Color botColor;

    @Getter private final String staffRole;
    @Getter private final String mutedRole;
    @Getter private final List<String> filteredChannels;

    @Getter private final String welcomeChannel;
    @Getter private final String memberRole;

    @Getter private final String ticketMessage;
    @Getter private final String ticketChannel;
    @Getter private final String ticketCategory;

    @Getter private final String mongoHost;
    @Getter private final int mongoPort;
    @Getter private final String mongoDatabase;
    @Getter private final boolean mongoAuthEnabled;
    @Getter private final String mongoUsername;
    @Getter private final String mongoPassword;

    public Config() {
        ConfigFile file = new ConfigFile();

        file.load();

        botToken = file.discordSection.botToken;
        botActivity = Activity.playing(file.discordSection.botActivity);
        botPrefix = file.discordSection.botPrefix;
        botGuild = file.discordSection.botGuild;
        botColor = new Color(Integer.parseInt(file.discordSection.botColor));

        staffRole = file.staffSection.staffRole;
        mutedRole = file.staffSection.mutedRole;
        filteredChannels = file.staffSection.filteredChannels;

        welcomeChannel = file.welcomeSection.welcomeChannel;
        memberRole = file.welcomeSection.memberRole;

        ticketMessage = file.ticketSection.ticketMessage;
        ticketChannel = file.ticketSection.ticketChannel;
        ticketCategory = file.ticketSection.ticketCategory;

        mongoHost = file.mongoSection.mongoHost;
        mongoPort = file.mongoSection.mongoPort;
        mongoDatabase = file.mongoSection.mongoDatabase;
        mongoAuthEnabled = file.mongoSection.authenticationSection.mongoAuthEnabled;
        mongoUsername = file.mongoSection.authenticationSection.mongoUsername;
        mongoPassword = file.mongoSection.authenticationSection.mongoPassword;
    }

}
