package basketbandit.core;

import basketbandit.core.modules.Command;
import basketbandit.core.modules.Module;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.List;

public class Utils {

    public static List<Command> commandList;
    public static List<Module> moduleList;
    public static String commandCount;

    /**
     * Returns a username and discriminator in format username#discriminator.
     * @param member the member to retrieve
     * @return username#discriminator
     */
    public static String getTag(Member member) {
        return getTag(member.getUser());
    }

    /**
     * Returns a username and discriminator in format username#discriminator.
     * @param user the user to retrieve
     * @return username#discriminator
     */
    public static String getTag(User user) {
        return user.getName() + "#" + user.getDiscriminator();
    }

    /**
     * Sends a message, saving those precious bytes.
     * @param event GenericMessageEvent
     * @param message String
     */
    public static void sendMessage(GenericMessageEvent event, String message) {
        event.getTextChannel().sendMessage(message).queue();
    }

    /**
     * Sends an embedded message.
     * @param event GenericMessageEvent
     * @param message MessageEmbed
     */
    public static void sendMessage(GenericMessageEvent event, MessageEmbed message) {
        event.getTextChannel().sendMessage(message).queue();
    }

    /**
     * Sends a message via message channel.
     * @param channel MessageChannel
     * @param message String
     */
    public static void sendMessage(MessageChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

    /**
     * Sends an embedded message via message channel.
     * @param channel MessageChannel
     * @param message MessageEmbed
     */
    public static void sendMessage(MessageChannel channel, MessageEmbed message) {
        channel.sendMessage(message).queue();
    }

    /**
     * Replaces the last occurrence of a pattern with nothing.
     * @param stringBuilder StringBuilder
     * @param pattern String
     * @return StringBuilder
     */
    public static StringBuilder removeLastOccurance(StringBuilder stringBuilder, String pattern) {
        int index = stringBuilder.lastIndexOf(pattern);
        if(index > -1) {
            stringBuilder.replace(index, index + 1, "");
        }

        return stringBuilder;
    }

    /**
     * Extracts the module name from a class path.
     * @param string String
     * @param shortened boolean
     * @return String
     */
    public static String extractModuleName(String string, boolean shortened) {
        if(shortened) {
            return string.substring(string.lastIndexOf(".") + 7).toLowerCase();
        } else {
            return string.substring(string.lastIndexOf(".") + 1).toLowerCase();
        }
    }

}
