package com.customwrld.bot.commandapi.button;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public interface Handler {

    void run(String authorId, ButtonClickEvent event);

}
