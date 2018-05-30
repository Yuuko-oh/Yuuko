package basketbandit.core.modules.developer.commands;

import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSetStatus extends Command {

    public CommandSetStatus() {
        super("setstatus", "basketbandit.core.modules.developer.ModuleDeveloper", null);
    }

    public CommandSetStatus(MessageReceivedEvent e) {
        super("setstatus", "basketbandit.core.modules.developer.ModuleDeveloper", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+", 3);

        switch(commandArray[1]) {
            case "playing":
                e.getJDA().getPresence().setGame(Game.of(Game.GameType.DEFAULT, commandArray[2]));
                break;
            case "listening":
                e.getJDA().getPresence().setGame(Game.of(Game.GameType.LISTENING, commandArray[2]));
                break;
            case "streaming":
                e.getJDA().getPresence().setGame(Game.of(Game.GameType.STREAMING, commandArray[2]));
                break;
            case "watching":
                e.getJDA().getPresence().setGame(Game.of(Game.GameType.WATCHING, commandArray[2]));
                break;
        }

    }

}