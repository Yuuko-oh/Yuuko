package basketbandit.core.modules.utility;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.utility.commands.CommandServer;
import basketbandit.core.modules.utility.commands.CommandUser;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public class ModuleUtility extends Module {

    public ModuleUtility() {
        super("ModuleUtility", "modUtility");
    }

    public ModuleUtility(MessageReceivedEvent e) {
        super("ModuleUtility", "modUtility");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e);
    }

    /**
     * Module constructor for MessageReactionAddEvents
     * @param e MessageReactionAddEvent
     */
    public ModuleUtility(MessageReactionAddEvent e) {
        super("ModuleUtility", "modUtility");

        if(e.getReaction().getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            Message message = e.getTextChannel().getMessageById(e.getMessageId()).complete();
            message.pin().queue();
        }
    }

    /**
     * Module constructor for MessageReactionRemoveEvents
     * @param e MessageReactionRemoveEvent
     */
    public ModuleUtility(MessageReactionRemoveEvent e) {
        super("ModuleUtility", "modUtility");

        Message message = e.getTextChannel().getMessageById(e.getMessageId()).complete();

        if(e.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            for(MessageReaction emote: message.getReactions()) {
                if(emote.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
                    return;
                }
            }
            message.unpin().queue();
        }
    }

    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(command.contains(C.USER.getCommandName())) {
            new CommandUser(e);
            return;
        }

        if(command.contains(C.SERVER.getCommandName())) {
            new CommandServer(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
    }
}