package discord.event.ActionImplementation;

import discord.event.Action;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Clear implements Action {
    @Override
    public void handle(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        try {
            int numOfMessages;
            try { numOfMessages = Integer.parseInt(args[1]); }
            catch (NumberFormatException | IndexOutOfBoundsException e) { numOfMessages = 100;}

            if (numOfMessages < 0 || numOfMessages > 100) {
                event.getChannel().sendMessage(String.format("Number of messages must be a positive number less than 100"));
                return;
            }

            int numOfPastMessages = event.getChannel().getHistory().size();
            numOfMessages = Math.min(numOfMessages, numOfPastMessages);

            List<Message> messages = event.getChannel().
                    getHistory().
                    retrievePast(numOfMessages).
                    complete();
            event.getChannel().deleteMessages(messages).queue();
        } catch (IllegalArgumentException iae) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("Error");
            error.setDescription(iae.getMessage());
            event.getChannel().sendMessage(error.build()).queue();
        }
    }
}
