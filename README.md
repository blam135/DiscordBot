# Personalized Discord Bot 
My own implementation of a Discord Bot using the [Java Discord API](https://github.com/DV8FromTheWorld/JDA) library

## Current Features
TBA

## Feature extension
To extend this bot to handle a new typed command:
1. Create a new JSON Object in Command.json file in `resource` folder. See below for details on the JSON File
2. Extend the `Action` class 
3. Implement the `handle()` function

### Config JSON 
Ensure a `Config.json` file exists under the resources folder. By default this is not provided in the repository. The format should be the following:
```json
[ 
  {
      "prefix": "A Prefix Here",
      "adminIDs": [
        "AdminID1",
        "AdminID2"
      ],
      "token": "Your Discord token here"
  }
]
```
- `prefix`: The prefix the user has to type in first to execute the command (String)
- `adminIDs`: List of users that can execute the admin functionality (String)
- `token`: Token of the bot (String). [Read here on how to create a bot and get the token](https://discordpy.readthedocs.io/en/latest/discord.html)

### Command JSON Format
Ensure a `Command.json` file exists under the resource folder. The format should be the following
```json
[
  {
    "command": "The command user will type in after the prefix",
    "class": "The name of the Java Class",
    "isAdminCommand": true/false
  }, 
]
```
- `command`: The command which the user will type in to execute it (String)
- `class`: The name of the class which will handle the command functionality (String)
- `isAdminCommand`: Checks whether this command is executable by the admin only (Boolean)

### Authors
- [Brendon Lam](https://github.com/blam135)