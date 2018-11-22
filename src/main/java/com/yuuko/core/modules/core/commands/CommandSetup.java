package com.yuuko.core.modules.core.commands;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSetup extends Command {

    public CommandSetup() {
        super("setup", "com.yuuko.core.modules.core.ModuleCore", 0, new String[]{"-setup"}, Permission.ADMINISTRATOR);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String serverId = e.getGuild().getId();

        if(!new DatabaseFunctions().addNewServer(serverId)) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Setup").setDescription("Server setup was successful.");
            MessageHandler.sendMessage(e, embed.build());
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Setup").setDescription("Server setup was unsuccessful.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

    public void executeAutomated(GuildJoinEvent e) {
        String serverId = e.getGuild().getId();
        try {
            new DatabaseFunctions().addNewServer(serverId);
        } catch(Exception ex) {
            Utils.sendException(ex, "Server setup was unsuccessful (" + e.getGuild().getId() + ") [CommandSetup] (Automated)");
        }
    }

}