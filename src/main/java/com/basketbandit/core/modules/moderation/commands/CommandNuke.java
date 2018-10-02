package com.basketbandit.core.modules.moderation.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Sanitise;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.List;

public class CommandNuke extends Command {

    public CommandNuke() {
        super("nuke", "com.basketbandit.core.modules.moderation.ModuleModeration", new String[]{"-nuke [value]"}, Permission.MESSAGE_MANAGE);
    }

    public CommandNuke(MessageReceivedEvent e, String[] command) {
        if(!Sanitise.checkParameters(e, command, 1)) {
            return;
        }

        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        int value = Integer.parseInt(command[1]);

        if(value < 1 || value > 100) {
            Utils.sendMessage(e, "Sorry, you have entered an out of bounds argument for the nuke command.");
            return;
        }

        List<Message> nukeList = e.getTextChannel().getHistory().retrievePast(value).complete();

        if(value < 2) {
            e.getTextChannel().deleteMessageById(nukeList.get(0).getId()).complete();
        } else {
            // If a message in the nuke list is older than 2 weeks it can't be mass deleted, so recursion will need to take place.
            for(Message message: nukeList) {
                if(message.getCreationTime().isBefore(OffsetDateTime.now().minusWeeks(2))) {
                    message.delete().complete();
                }
            }
            e.getGuild().getTextChannelById(e.getTextChannel().getId()).deleteMessages(nukeList).complete();
        }
    }
}
