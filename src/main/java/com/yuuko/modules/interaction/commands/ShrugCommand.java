package com.yuuko.modules.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.List;

public class ShrugCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/ghlye0C.gif",
            "https://i.imgur.com/nUacE87.gif",
            "https://i.imgur.com/0ttnPkG.gif",
            "https://i.imgur.com/1Pfi4Qp.gif",
            "https://i.imgur.com/EaAgfes.gif"
    );

    public ShrugCommand() {
        super("shrug", 0, -1L, Arrays.asList("-shrug", "-shrug @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        if(MessageUtilities.checkIfUserMentioned(e)) {
            Member target = MessageUtilities.getMentionedMember(e, true);
            if(target != null) {
                EmbedBuilder embed = new EmbedBuilder().setDescription(I18n.getText(e, "target").formatted(e.getMember().getEffectiveName(), target.getEffectiveName())).setImage(interactionImage.get(getRandom(interactionImage.size())));
                MessageDispatcher.sendMessage(e, embed.build());
            }
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setDescription(I18n.getText(e, "self").formatted(e.getMember().getEffectiveName())).setImage(interactionImage.get(getRandom(interactionImage.size())));
        MessageDispatcher.sendMessage(e, embed.build());
    }
}
