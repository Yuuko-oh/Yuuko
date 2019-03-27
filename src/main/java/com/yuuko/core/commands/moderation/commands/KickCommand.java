package com.yuuko.core.commands.moderation.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.settings.ModerationLogSetting;
import com.yuuko.core.commands.moderation.ModerationModule;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

public class KickCommand extends Command {

    public KickCommand() {
        super("kick", ModerationModule.class,1, new String[]{"-kick @user", "-kick @user <reason>"}, false, new Permission[]{Permission.KICK_MEMBERS});
    }

    @Override
    public void onCommand(MessageEvent e) {
        String[] commandParameters = e.getCommandParameter().split("\\s+", 3);
        Member target = MessageUtilities.getMentionedMember(e, true);

        if(target == null) {
            return;
        }

        if(!Sanitiser.canInteract(e, target, "kick", true)) {
            return;
        }

        if(commandParameters.length < 3) {
            e.getGuild().getController().kick(target).queue(s -> {
                e.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(e, "Kick", target.getUser(), "None");
            }, f -> e.getMessage().addReaction("❌").queue());
        } else {
            e.getGuild().getController().kick(target, commandParameters[1]).queue(s -> {
                e.getMessage().addReaction("✅").queue();
                ModerationLogSetting.execute(e, "Kick", target.getUser(), commandParameters[1]);
            }, f -> e.getMessage().addReaction("❌").queue());
        }
    }

}
