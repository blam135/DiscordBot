package discord.factory;
import discord.event.EventHandler;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class JDAFactory {

    private JDA jda;
    private String status = "No Error Message";

    public JDAFactory() {}

    public JDA create() {
        parseConfig();
        setPresence();
        initiateAuto();
        return jda;
    }

    public void parseConfig() {
        try {
            URL file = this.getClass().getResource("/Config.json");
            FileReader fr = new FileReader(file.getFile());
            JSONObject obj = (JSONObject) new JSONParser().parse(fr);

            // Set token
            String token = (String) obj.get("token");
            this.jda = new JDABuilder(AccountType.BOT).setToken(token).build();

            // Create Event Handler
            String prefix = (String) obj.get("prefix");
            JSONArray admins = (JSONArray) obj.get("adminIDs");
            addEventListener(admins, prefix);

            fr.close();
            return;
        } catch (FileNotFoundException fnfe) {
            status = "Could not find Config.json";
        } catch (ParseException | IOException ioe ) {
            status = ioe.getMessage();
        } catch (LoginException le) {
            status = "Token Invalid";
        }
        jda = null;

    }

    public void setPresence() {
        this.jda.getPresence().setActivity(Activity.playing("Java"));
    }

    public void addEventListener(JSONArray admins, String prefix) {
        try {
            jda.addEventListener(new EventHandler(jda, admins, prefix));
        } catch (IOException ioe) {
            this.jda = null;
            this.status = "Unable to open JSON File";
        } catch (ParseException pe) {
            this.jda = null;
            this.status = "Unable to parse the JSON File correctly";
        }
    }

    public void initiateAuto() {
        // Initiate all classes under auto as a thread
    }

}
