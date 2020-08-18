package discord.event;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;

public class EventHandler extends ListenerAdapter {
    private JDA jda;
    private String prefix;
    ArrayList<String> admins; // ID, Name
    private JSONArray commands;
    private String ActionImplementationPackagePrefix = "discord.event.ActionImplementation.";

    public EventHandler(JDA jda, JSONArray admins, String prefix) throws IOException, ParseException{
        this.jda = jda;
        this.prefix = prefix;
        initializeAdmins(admins);
        initializeJSONCommands();
    }

    private void initializeAdmins(JSONArray admins) {
        this.admins = new ArrayList<>();
        for (int i = 0; i < admins.size(); i++) {
            String adminID = (String) admins.get(i);
            this.admins.add(adminID);
        }
    }
    private void initializeJSONCommands() throws IOException, ParseException {
        URL file = this.getClass().getResource("/Command.json");
        FileReader fr = new FileReader(file.getFile());
        Object obj = new JSONParser().parse(fr);
        this.commands = (JSONArray) obj;
        fr.close();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String firstArgument = args[0];
        if (event.getAuthor().isBot() || firstArgument.length() < 2 || !firstArgument.substring(0,2).equals(this.prefix)) {
            return;
        }
        String userCommand = args[0];
        boolean messengerIsAdmin = admins.contains(event.getAuthor().getId());

        for (int i = 0; i < commands.size(); i++) {
            String typedCommand;
            Class classCommand;
            boolean adminRights;
            try {
                JSONObject commandGroup = (JSONObject) commands.get(i);
                typedCommand = (String) commandGroup.get("command");
                classCommand = Class.forName(ActionImplementationPackagePrefix + (String) commandGroup.get("class"));
                adminRights = (boolean) commandGroup.get("isAdminCommand");
            } catch (ClassNotFoundException cnfe) {
                continue;
            } catch (ClassCastException cce) {
                continue;
            }
            if (userCommand.equalsIgnoreCase(this.prefix + typedCommand)) {
                if (adminRights && !messengerIsAdmin) {
                    break;
                }
                // Parse things here (ref. https://stackoverflow.com/questions/7495785/java-how-to-instantiate-a-class-from-string)
                try {
                    // Create the constructor
                    Class[] paramTypes = {};
                    Constructor cons = classCommand.getConstructor(paramTypes);
                    Action instanceOfMyClass = (Action) cons.newInstance();
                    instanceOfMyClass.handle(event);
                } catch (NoSuchMethodException nsme) {
                    // getConstructor error
                    nsme.printStackTrace();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException iae) {
                    // newInstance erorr
                    iae.printStackTrace();
                }
                break;
            }
        }
    }

}
