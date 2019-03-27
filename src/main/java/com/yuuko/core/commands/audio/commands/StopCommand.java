package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.LavalinkUtilities;
import lavalink.client.io.Link;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", AudioModule.class, 0, new String[]{"-stop"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(!LavalinkUtilities.isState(e.getGuild(), Link.State.NOT_CONNECTED)) {
            GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());
            manager.scheduler.queue.clear();
            manager.scheduler.setBackground(null);
            manager.scheduler.setLooping(false);
            manager.player.stopTrack();
            manager.player.setPaused(false);
            Configuration.LAVALINK.closeConnection(e.getGuild());

            if(e.getCommand() != null) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Stopping").setDescription("Audio connection closed.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

    /**
     * Executes command when everyone leaves the channel the bot is in.
     *
     * @param e GenericGuildEvent
     */
    public void onCommand(GenericGuildEvent e) {
        GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());
        manager.scheduler.queue.clear();
        manager.scheduler.setBackground(null);
        manager.scheduler.setLooping(false);
        manager.player.stopTrack();
        manager.player.setPaused(false);
        Configuration.LAVALINK.closeConnection(e.getGuild());
    }
}
