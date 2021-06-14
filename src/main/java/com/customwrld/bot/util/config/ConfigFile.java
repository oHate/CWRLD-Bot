package com.customwrld.bot.util.config;

import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.managed.FileManaged;
import io.github.portlek.configs.section.ConfigSection;
import io.github.portlek.configs.type.YamlFileType;

import java.util.Collections;
import java.util.List;

@Config(name = "config", type = YamlFileType.class)
public class ConfigFile extends FileManaged {

    @Instance
    public final DiscordSection discordSection = new DiscordSection();

    @Instance
    public final StaffSection staffSection = new StaffSection();

    @Instance
    public final WelcomeSection welcomeSection = new WelcomeSection();

    @Instance
    public final TicketSection ticketSection = new TicketSection();

    @Instance
    public final MongoSection mongoSection = new MongoSection();

    @Section("discord")
    public static final class DiscordSection extends ConfigSection {

        @Property
        public String botToken = "";

        @Property
        public String botActivity = "";

        @Property
        public String botPrefix = "";

        @Property
        public String botGuild = "";

        @Property
        public String botColor = "";

    }

    @Section("staff")
    public static final class StaffSection extends ConfigSection {

        @Property
        public String staffRole = "";

        @Property
        public String mutedRole = "";

        @Property
        public List<String> filteredChannels = Collections.emptyList();

    }

    @Section("welcome")
    public static final class WelcomeSection extends ConfigSection {

        @Property
        public String welcomeChannel = "";

        @Property
        public String memberRole = "";

    }

    @Section("ticket")
    public static final class TicketSection extends ConfigSection {

        @Property
        public String ticketMessage = "";

        @Property
        public String ticketChannel = "";

        @Property
        public String ticketCategory = "";

    }

    @Section("mongo")
    public static final class MongoSection extends ConfigSection {

        @Property
        public String mongoHost = "";

        @Property
        public int mongoPort = 27017;

        @Property
        public String mongoDatabase = "";

        @Instance
        public final AuthenticationSection authenticationSection = new AuthenticationSection();

        @Section("authentication")
        public static final class AuthenticationSection extends ConfigSection {

            @Property
            public boolean mongoAuthEnabled = false;

            @Property
            public String mongoUsername = "";

            @Property
            public String mongoPassword = "";

        }

    }

}