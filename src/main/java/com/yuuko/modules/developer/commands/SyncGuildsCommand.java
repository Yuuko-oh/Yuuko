package com.yuuko.modules.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import com.yuuko.modules.core.commands.BindCommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class SyncGuildsCommand extends Command {

    public SyncGuildsCommand() {
        super("syncguilds", 0, -1L, Arrays.asList("-syncguilds"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        e.getJDA().getGuildCache().forEach(guild -> {
            GuildFunctions.addOrUpdateGuild(guild);
            BindCommand.DatabaseInterface.verifyBinds(guild);
        });
        EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "success"));
        MessageDispatcher.reply(e, embed.build());
    }

}
