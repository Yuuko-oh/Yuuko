package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.utility.UtilityModule;
import com.yuuko.core.database.BindFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", UtilityModule.class, 0, new String[]{"-bind <module>", "-bind <module> #channel..."}, false, new Permission[]{Permission.MANAGE_SERVER});
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(e.getCommand().length > 1) {
            String[] commandParameters = e.getCommandParameter().split("\\s+", 2);
            String selectedModule = "";

            boolean present = false;
            for(Module module : Configuration.MODULES) {
                if(module.getName().equalsIgnoreCase(commandParameters[0])) {
                    selectedModule = module.getName();
                    present = true;
                    break;
                }
            }

            if(!present) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("**" + commandParameters[0] + "** isn't a valid module. A list of valid commands can be found by using the **" + Utilities.getServerPrefix(e.getGuild()) + "commands** command.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            if(commandParameters.length > 1) {
                List<TextChannel> channels = e.getMessage().getMentionedChannels();

                if(channels.size() < 1) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Expected at least **1** \\#tagged channel to bind, found **0**.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                StringBuilder boundChannels = new StringBuilder();
                for(TextChannel channel : channels) {
                    BindFunctions.toggleBind(e.getGuild().getId(), channel.getId(), selectedModule);
                    boundChannels.append(channel.getName()).append(", ");
                }
                TextUtilities.removeLastOccurrence(boundChannels, ", ");

                EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully toggled **" + selectedModule + "** on **" + boundChannels.toString() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            } else {
                final int res = BindFunctions.toggleBind(e.getGuild().getId(), e.getTextChannel().getId(), selectedModule);

                if(res == 0) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully bound **" + selectedModule + "** to **" + e.getTextChannel().getName() + "**.");
                    MessageHandler.sendMessage(e, embed.build());
                } else if(res == 1) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully removed binding of **" + selectedModule + "** from **" + e.getTextChannel().getName() + "**.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
        } else {
            try {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Bound Modules")
                        .setDescription(BindFunctions.getGuildBinds(e.getGuild(), "\n"));
                MessageHandler.sendMessage(e, embed.build());
            } catch(Exception ex) {
                //
            }
        }

    }

}
