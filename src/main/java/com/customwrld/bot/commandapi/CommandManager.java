package com.customwrld.bot.commandapi;

import com.customwrld.bot.Bot;
import com.customwrld.bot.util.Logger;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager(String location) {
        Reflections reflections = new Reflections(location);
        Set<Class<? extends ICommand>> allClasses = reflections.getSubTypesOf(ICommand.class);

        try {
            for(Class<?> aClass : allClasses) {
                if (!ICommand.class.isAssignableFrom(aClass))
                    continue;
                ICommand command = (ICommand) aClass.getDeclaredConstructor().newInstance();
                addCommand(command);
                Logger.log(Logger.LogType.COMMAND, command.commandName().toUpperCase() + " Command has successfully been registered!");
            }
        } catch (Exception exception) {
            Logger.log(Logger.LogType.ERROR, "An exception has occurred when trying to register a command!");
        }
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.commandName().equalsIgnoreCase(cmd.commandName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.commandName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    public void handle(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Bot.getBot().getConfig().getBotPrefix()), "")
                .split("\\s+");

        Member member = event.getMember();
        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null && member != null) {
            String[] args = Arrays.copyOfRange(split, 1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }

}