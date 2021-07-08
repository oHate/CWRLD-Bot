package com.customwrld.bot.listeners;

import com.customwrld.bot.Bot;
import com.customwrld.bot.util.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class VerificationListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getMember() == null)
            return;
        if(event.getChannel().getId().equals("843189261514571786") && event.getMember().getId().equals("836340297281699873")) {
//            event.getChannel().sendMessage("Hello ")
//                    .append("World!")
//                    .embed(new EmbedBuilder().setDescription("Test").build())
//                    .queue( (message) -> message.editMessage("Removed Embed...").override(true).queue() )
            // TODO: Use above for sending a message and a embed
            event.getChannel().sendMessage("> <@836340297281699873>").queue();
            event.getChannel().sendMessage(
                    Util.builder(event.getAuthor())
                            .setTitle("**Account Successfully Linked**")
                            .setDescription("You have successfully linked your Discord account, and\n" +
                                    "have been granted access to talk in the Discord guild!")
                            .addField("Discord Account","> **Tag:** oHate#0001\n" +
                                    "> **ID:** 836340297281699873", false)
                            .addField("Minecraft Account", "> **Username:** oHate\n" +
                                    "> **UUID:** 239c8573-0675-4a78-82f3-d178c72d5dc9", false)
                            .setThumbnail("https://cravatar.eu/helmhead/oHate")
                    .build()
            ).queue();
        }
    }

}
