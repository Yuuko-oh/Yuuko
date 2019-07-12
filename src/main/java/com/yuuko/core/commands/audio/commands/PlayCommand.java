package com.yuuko.core.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.commands.audio.handlers.YouTubeSearchHandler;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.LavalinkUtilities;
import com.yuuko.core.utilities.TextUtilities;
import lavalink.client.io.Link;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;
import java.util.List;

public class PlayCommand extends Command {

    public PlayCommand() {
        super("play", AudioModule.class, 0, Arrays.asList("-play", "-play <url>", "-play <term>"), false, Arrays.asList(Permission.VOICE_CONNECT));
    }

    @Override
    public void onCommand(MessageEvent e) {
        GuildAudioManager manager = AudioManagerController.getGuildAudioManager(e.getGuild());

        if(!LavalinkUtilities.isState(e.getGuild(), Link.State.CONNECTED)) {
            Configuration.LAVALINK.openConnection(e.getMember().getVoiceState().getChannel());
        }

        if(!e.hasParameters()) {
            if(manager.getPlayer().isPaused()) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Resuming").setDescription("The player has been resumed.");
                MessageHandler.sendMessage(e, embed.build());
                manager.getPlayer().setPaused(false);
                new CurrentCommand();
            }

        } else {
            manager.getPlayer().setPaused(false);

            if(e.getParameters().startsWith("https://") || e.getParameters().startsWith("http://")) {
                loadAndPlay(manager, e);

            } else {
                String trackId = YouTubeSearchHandler.search(e.getParameters());

                if(trackId == null || trackId.equals("")) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Those search parameters failed to return a result.");
                    MessageHandler.sendMessage(e, embed.build());
                } else {
                    loadAndPlay(manager, e.setParameters(trackId));
                }
            }
        }

    }

    /**
     * Loads a track from a given url and plays it if possible, else adds it to the queue.
     *
     * @param manager GuildAudioManager.
     * @param e MessageEvent.
     */
    private void loadAndPlay(GuildAudioManager manager, MessageEvent e) {
        final String trackUrl;

        if(e.getParameters().startsWith("<") && e.getParameters().endsWith(">")) {
            trackUrl = e.getParameters().substring(1, e.getParameters().length() - 1);
        } else {
            trackUrl = e.getParameters();
        }

        AudioManagerController.getPlayerManager().loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                try {
                    String[] uri = track.getInfo().uri.split("=");
                    String imageUrl = (uri.length > 1) ? "https://img.youtube.com/vi/" + uri[1] + "/1.jpg" : "https://i.imgur.com/bCNQlm6.jpg";

                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor(e.getMember().getEffectiveName() + " added to the queue!", null, e.getAuthor().getAvatarUrl())
                            .setTitle(track.getInfo().title, trackUrl)
                            .setThumbnail(imageUrl)
                            .addField("Duration", TextUtilities.getTimestamp(track.getDuration()), true)
                            .addField("Channel", track.getInfo().author, true)
                            .addField("Position in queue", manager.getScheduler().queue.size() + "", false)
                            .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                    MessageHandler.sendMessage(e, embed.build());

                    track.setUserData(e);
                    manager.getScheduler().queue(track);
                } catch(Exception ex) {
                    log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                try {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Adding `" + playlist.getTracks().size() + "` tracks to queue from playlist: `" + playlist.getName() + "`");
                    MessageHandler.sendMessage(e, embed.build());

                    List<AudioTrack> tracks = playlist.getTracks();
                    for(AudioTrack track: tracks) {
                        track.setUserData(e);
                        manager.getScheduler().queue(track);
                    }

                } catch(Exception ex) {
                    log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No matches found using that parameter.");
                MessageHandler.sendMessage(e, embed.build());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Loading failed: " + exception.getMessage());
                MessageHandler.sendMessage(e, embed.build());
            }
        });
    }
}
