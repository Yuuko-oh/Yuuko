package com.basketbandit.core.modules;

import com.basketbandit.core.modules.audio.commands.*;
import com.basketbandit.core.modules.core.commands.*;
import com.basketbandit.core.modules.developer.commands.CommandSetStatus;
import com.basketbandit.core.modules.game.commands.CommandOsuStats;
import com.basketbandit.core.modules.game.commands.CommandRuneScapeStats;
import com.basketbandit.core.modules.game.commands.CommandWorldOfWarcraftStats;
import com.basketbandit.core.modules.math.commands.CommandRoll;
import com.basketbandit.core.modules.math.commands.CommandSum;
import com.basketbandit.core.modules.moderation.commands.*;
import com.basketbandit.core.modules.nsfw.commands.CommandEfukt;
import com.basketbandit.core.modules.transport.commands.CommandLineStatus;
import com.basketbandit.core.modules.utility.commands.*;

public final class C {
    // Dev module commands.
    public static final Command SET_STATUS = new CommandSetStatus();

    // Core module commands.
    public static final Command SETUP = new CommandSetup();
    public static final Command MODULE = new CommandModule();
    public static final Command MODULES = new CommandModules();
    public static final Command HELP = new CommandHelp();
    public static final Command ABOUT = new CommandAbout();
    public static final Command SET_PREFIX = new CommandSetPrefix();

    // Moderation module commands.
    public static final Command NUKE = new CommandNuke();
    public static final Command KICK = new CommandKick();
    public static final Command BAN = new CommandBan();
    public static final Command MUTE = new CommandMute();
    public static final Command UNMUTE = new CommandUnmute();

    // Math module commands.
    public static final Command ROLL = new CommandRoll();
    public static final Command SUM = new CommandSum();

    // Utility module commands.
    public static final Command USER = new CommandUser();
    public static final Command SERVER = new CommandServer();
    public static final Command CREATE_CHANNEL = new CommandAddChannel();
    public static final Command DELETE_CHANNEL = new CommandDeleteChannel();
    public static final Command BIND = new CommandBind();
    public static final Command UNBIND = new CommandUnbind();
    public static final Command EXCLUDE = new CommandExclude();
    public static final Command INCLUDE = new CommandUnexclude();

    // Game module commands.
    public static final Command RUNESCAPE_STATS = new CommandRuneScapeStats();
    public static final Command WORLDOFWARCRAFT_CHARACTER = new CommandWorldOfWarcraftStats();
    public static final Command OSU_STATS = new CommandOsuStats();

    // Music module commands.
    public static final Command PLAY = new CommandPlay();
    public static final Command STOP = new CommandStop();
    public static final Command SKIP = new CommandSkip();
    public static final Command PAUSE = new CommandPause();
    public static final Command CURRENT_TRACK = new CommandCurrentTrack();
    public static final Command SHUFFLE = new CommandShuffle();
    public static final Command QUEUE = new CommandQueue();
    public static final Command SET_BACKGROUND = new CommandSetBackground();
    public static final Command UNSET_BACKGROUND = new CommandUnsetBackground();
    public static final Command LAST_TRACK = new CommandLastTrack();
    public static final Command TOGGLE_REPEAT = new CommandToggleRepeat();
    public static final Command SEARCH = new CommandSearch();
    public static final Command CLEAR_QUEUE = new CommandClearQueue();

    // Transport module commands.
    public static final Command LINE_STATUS = new CommandLineStatus();

    // NSFW module commands
    public static final Command EFUKT = new CommandEfukt();
}
