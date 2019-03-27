package com.yuuko.core.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import net.dv8tion.jda.core.EmbedBuilder;

public class LastCommand extends Command {

	public LastCommand() {
		super("last", AudioModule.class, 0, new String[]{"-last"}, false, null);
	}

	@Override
	public void onCommand(MessageEvent e) {
		try {
			AudioTrack track = AudioManagerManager.getGuildAudioManager(e.getGuild().getId()).player.getPlayingTrack();

			if(track != null) {
				String[] uri = track.getInfo().uri.split("=");
				String imageUrl = (uri.length > 1) ? "https://img.youtube.com/vi/" + uri[1] + "/1.jpg" : "https://i.imgur.com/bCNQlm6.jpg";

				EmbedBuilder queuedTrack = new EmbedBuilder()
						.setAuthor("Last track")
						.setTitle(track.getInfo().title, track.getInfo().uri)
						.setThumbnail(imageUrl)
						.addField("Duration", TextUtilities.getTimestamp(track.getDuration()), true)
						.addField("Channel", track.getInfo().author, true)
						.setFooter(Configuration.STANDARD_STRINGS[0], Configuration.BOT.getAvatarUrl());
				MessageHandler.sendMessage(e, queuedTrack.build());
			} else {
				EmbedBuilder embed = new EmbedBuilder().setTitle("There isn't a previous track to return.");
				MessageHandler.sendMessage(e, embed.build());
			}

		} catch(Exception ex) {
			log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
		}
	}

}
