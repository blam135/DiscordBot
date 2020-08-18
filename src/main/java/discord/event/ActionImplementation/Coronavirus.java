package discord.event.ActionImplementation;

import api.RESTApi;
import discord.event.Action;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Coronavirus implements Action {

    @Override
    public void handle(GuildMessageReceivedEvent event) {
        EmbedBuilder info = new EmbedBuilder();
        info.setTitle("Coronavirus Information");
        String[] corona = info();
        info.setDescription(String.format("In Australia, there are currently %s cases and %s new cases in the last 24 hours", corona[0], corona[1]));
        event.getChannel().sendMessage(info.build()).queue();
    }

    public String[] info() {
        return null;
    }

    public static void main(String[] args) throws ParseException {
        String response = RESTApi.GET("https://api.covid19api.com/country/australia/status/confirmed/live?from=2020-03-01T00:00:00Z&to=2020-04-01T00:00:00Z");
        JSONObject jo = (JSONObject) new JSONParser().parse(response);
        System.out.println(jo.toJSONString());
    }

}
