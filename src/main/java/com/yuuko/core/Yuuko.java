// Program: Yuuko (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 12/01/2019 - JDK 12.0.0

package com.yuuko.core;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import com.yuuko.core.events.GenericEventManager;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.ScheduleHandler;
import com.yuuko.core.scheduler.jobs.FiveSecondlyJob;
import com.yuuko.core.scheduler.jobs.ThirtySecondlyJob;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import org.discordbots.api.client.DiscordBotListAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Yuuko {

    private static final Logger log = LoggerFactory.getLogger(Yuuko.class);

    /**
     * Initialises the bot and JDA.
     * @param args -> program arguments (currently unused)
     * @throws LoginException -> If there was an error logging in.
     * @throws IllegalArgumentException -> If a JDA argument was incorrect.
     */
    public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException {
        long loadConfigStart = System.nanoTime();

        Configuration.load();

        Configuration.SHARD_ID = Integer.parseInt(args[0]);

        log.info("Setting up the shard manager...");
        Configuration.SHARD_MANAGER = new DefaultShardManagerBuilder()
                .setToken(Configuration.BOT_TOKEN)
                .addEventListeners(new GenericEventManager(), Configuration.LAVALINK.getLavalink())
                .setAudioSendFactory(new NativeAudioSendFactory())
                .setGame(Game.of(Game.GameType.LISTENING, Configuration.STATUS))
                .setShardsTotal(Configuration.SHARD_COUNT)
                .setShards(Configuration.SHARD_ID)
                .build();

        while(!isConstructed()) {
            log.info("Still waiting...");
            Thread.sleep(2500);
        }

        log.info("Shard manager setup; active shards: " + Configuration.SHARD_MANAGER.getShards().size() + ".");

        Configuration.BOT = Utils.getSelfUser();
        Configuration.GLOBAL_PREFIX = "<@" + Configuration.BOT.getId() + "> ";
        MetricsManager.updateDiscordMetrics();

        log.info("Setting up bot-list objects...");
        if(Configuration.API_KEYS.containsKey("discordbots")) {
            Configuration.BOT_LIST = new DiscordBotListAPI.Builder().botId(Configuration.BOT.getId()).token(Utils.getApiKey("discordbots")).build();
            Utils.updateDiscordBotList();
        }

        log.info("Setting up scheduled jobs...");
        ScheduleHandler.registerJob(new FiveSecondlyJob());
        ScheduleHandler.registerJob(new ThirtySecondlyJob());

        log.info("Loading complete... time taken: " + (new BigDecimal((System.nanoTime() - loadConfigStart)/1000000000.0).setScale(2, RoundingMode.HALF_UP)) + " seconds.");
    }

    private static boolean isConstructed() {
        if(Configuration.SHARD_MANAGER == null) {
            return false;
        }

        for(JDA shard : Configuration.SHARD_MANAGER.getShards()) {
            if(shard.getStatus() != JDA.Status.CONNECTED) {
                return false;
            }
        }
        return true;
    }

}


