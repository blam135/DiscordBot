package discord;

import discord.factory.JDAFactory;
import net.dv8tion.jda.api.JDA;

public class Main {
    public static JDA jda;
    public static void main(String[] args) {
        jda = new JDAFactory().create();
    }
}
