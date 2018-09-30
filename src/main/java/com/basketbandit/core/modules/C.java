package com.basketbandit.core.modules;

import com.basketbandit.core.modules.audio.commands.*;
import com.basketbandit.core.modules.core.commands.*;
import com.basketbandit.core.modules.developer.commands.CommandAddServers;
import com.basketbandit.core.modules.developer.commands.CommandSetStatus;
import com.basketbandit.core.modules.math.commands.CommandRoll;
import com.basketbandit.core.modules.math.commands.CommandSum;
import com.basketbandit.core.modules.media.commands.CommandKitsu;
import com.basketbandit.core.modules.media.commands.CommandOsuStats;
import com.basketbandit.core.modules.media.commands.CommandRuneScapeStats;
import com.basketbandit.core.modules.media.commands.CommandWorldOfWarcraftStats;
import com.basketbandit.core.modules.moderation.commands.*;
import com.basketbandit.core.modules.nsfw.commands.CommandEfukt;
import com.basketbandit.core.modules.transport.commands.CommandLineStatus;
import com.basketbandit.core.modules.utility.commands.*;

public final class C {
    // Dev module commands.
    public static final Command SET_STATUS = new CommandSetStatus();
    public static final Command ADD_SERVERS = new CommandAddServers();

    // Core module commands.
    public static final Command SETUP = new CommandSetup();
    public static final Command MODULE = new CommandModule();
    public static final Command MODULES = new CommandModules();
    public static final Command HELP = new CommandHelp();
    public static final Command ABOUT = new CommandAbout();
    public static final Command SET_PREFIX = new CommandSetPrefix();
    public static final Command SETTINGS = new CommandSettings();

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
    public static final Command CHANNEL = new CommandChannel();
    public static final Command BIND = new CommandBind();
    public static final Command EXCLUDE = new CommandExclude();
    public static final Command WEATHER = new CommandWeather();

    // Media module commands.
    public static final Command RUNESCAPE = new CommandRuneScapeStats();
    public static final Command WOW = new CommandWorldOfWarcraftStats();
    public static final Command OSU = new CommandOsuStats();
    public static final Command KITSU = new CommandKitsu();

    // Music module commands.
    public static final Command PLAY = new CommandPlay();
    public static final Command STOP = new CommandStop();
    public static final Command SKIP = new CommandSkip();
    public static final Command PAUSE = new CommandPause();
    public static final Command CURRENT = new CommandCurrent();
    public static final Command SHUFFLE = new CommandShuffle();
    public static final Command QUEUE = new CommandQueue();
    public static final Command BACKGROUND = new CommandBackground();
    public static final Command LAST = new CommandLast();
    public static final Command REPEAT = new CommandRepeat();
    public static final Command SEARCH = new CommandSearch();
    public static final Command CLEAR = new CommandClear();

    // Transport module commands.
    public static final Command LINE_STATUS = new CommandLineStatus();

    // NSFW module commands
    public static final Command EFUKT = new CommandEfukt();
}
