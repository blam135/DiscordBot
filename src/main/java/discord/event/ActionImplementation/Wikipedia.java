package discord.event.ActionImplementation;

import api.RESTApi;
import discord.event.Action;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class Wikipedia implements Action {

    @Override
    public void handle(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args.length < 2) {
            event.getChannel().sendMessage("USAGE --wiki <Article name>").queue();
            return;
        }

        String[] articleArray = Arrays.copyOfRange(args, 1, args.length);
        String article = Arrays.toString(articleArray).replaceAll("[\\[\\],]", "");

        String result = parseAPIRequest(article);

        JSONObject json;
        try {
            json = (JSONObject) new JSONParser().parse(result);
        } catch (ParseException pe) {
            return;
        }

        JSONObject root = ((JSONObject) ((JSONObject) json.get("query")).get("pages"));
        String pageID = root.keySet().iterator().next().toString();
        String extract = (String) ((JSONObject) root.get(pageID)).get("extract");
        event.getChannel().sendMessage(extract).queue();
    }

    private String parseAPIRequest(String article) {
        article = article.replace(" ", "%20");
        String url = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=" + article;
        String result = RESTApi.GET(url);
        return result;
    }

}
