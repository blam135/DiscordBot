package discord.event;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Action {
    void handle(GuildMessageReceivedEvent event);
}
