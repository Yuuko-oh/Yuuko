package com.yuuko.modules.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.core.commands.BindCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;

import java.util.Arrays;

public class SyncGuildsCommand extends Command {

    public SyncGuildsCommand() {
        super("syncguilds", Arrays.asList("-syncguilds"));
    }

    @Override
    public void onCommand(MessageEvent context) {
        SnowflakeCacheView<Guild> guildCache = context.getJDA().getGuildCache();
        GuildFunctions.addGuilds(guildCache);
        BindCommand.DatabaseInterface.verifyBinds(guildCache);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n( "success"));
        MessageDispatcher.reply(context, embed.build());
    }

}
