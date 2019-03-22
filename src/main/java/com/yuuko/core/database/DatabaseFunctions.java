package com.yuuko.core.database;

import com.yuuko.core.Configuration;
import com.yuuko.core.database.connections.MetricsDatabaseConnection;
import com.yuuko.core.database.connections.SettingsDatabaseConnection;
import com.yuuko.core.metrics.handlers.MetricsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseFunctions {

    private static final Logger log = LoggerFactory.getLogger(DatabaseFunctions.class);

    /**
     * Updates the database with the latest metrics.
     */
    public static void updateMetricsDatabase() {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `SystemMetrics`(`shardId`, `uptime`, `memoryTotal`, `memoryUsed`) VALUES(?, ?, ?, ?)");
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `EventMetrics`(`shardId`, `botMessagesProcessed`, `humanMessagesProcessed`, `botReactsProcessed`, `humanReactsProcessed`, `outputsProcessed`, `commandsExecuted`, `commandsFailed`) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO `DiscordMetrics`(`shardId`, `ping`, `guildCount`, `channelCount`, `userCount`, `roleCount`, `emoteCount`) VALUES(?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement stmt4 = conn.prepareStatement("INSERT INTO `DatabaseMetrics`(`shardId`, `selects`, `inserts`, `updates`, `deletes`) VALUES(?, ?, ?, ?, ?)")) {

            int shardId = Configuration.BOT.getJDA().getShardInfo().getShardId();

            stmt.setInt(1, shardId);
            stmt.setLong(2, MetricsManager.getSystemMetrics().UPTIME);
            stmt.setLong(3, MetricsManager.getSystemMetrics().MEMORY_TOTAL);
            stmt.setLong(4, MetricsManager.getSystemMetrics().MEMORY_USED);
            stmt.execute();

            stmt2.setInt(1, shardId);
            stmt2.setInt(2, MetricsManager.getEventMetrics().BOT_MESSAGES_PROCESSED.get());
            stmt2.setInt(3, MetricsManager.getEventMetrics().HUMAN_MESSAGES_PROCESSED.get());
            stmt2.setInt(4, MetricsManager.getEventMetrics().BOT_REACTS_PROCESSED.get());
            stmt2.setInt(5, MetricsManager.getEventMetrics().HUMAN_REACTS_PROCESSED.get());
            stmt2.setInt(6, MetricsManager.getEventMetrics().OUTPUTS_PROCESSED.get());
            stmt2.setInt(7, MetricsManager.getEventMetrics().COMMANDS_EXECUTED.get());
            stmt2.setInt(8, MetricsManager.getEventMetrics().COMMANDS_FAILED.get());
            stmt2.execute();

            stmt3.setInt(1, shardId);
            stmt3.setDouble(2, MetricsManager.getDiscordMetrics().PING.get());
            stmt3.setInt(3, MetricsManager.getDiscordMetrics().GUILD_COUNT);
            stmt3.setInt(4, MetricsManager.getDiscordMetrics().CHANNEL_COUNT);
            stmt3.setInt(5, MetricsManager.getDiscordMetrics().USER_COUNT);
            stmt3.setInt(6, MetricsManager.getDiscordMetrics().ROLE_COUNT);
            stmt3.setInt(7, MetricsManager.getDiscordMetrics().EMOTE_COUNT);
            stmt3.execute();

            stmt4.setInt(1, shardId);
            stmt4.setInt(2, MetricsManager.getDatabaseMetrics().SELECT.get());
            stmt4.setInt(3, MetricsManager.getDatabaseMetrics().INSERT.get());
            stmt4.setInt(4, MetricsManager.getDatabaseMetrics().UPDATE.get());
            stmt4.setInt(5, MetricsManager.getDatabaseMetrics().DELETE.get());
            stmt4.execute();

            MetricsManager.getDatabaseMetrics().INSERT.getAndAdd(4);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates the database with the latest command.
     *
     * @param guildId String
     * @param command String
     */
    public static void updateCommandsLog(String guildId, String command) {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `CommandsLog`(`shardId`, `guildId`, `command`) VALUES(?, ?, ?)")) {

            stmt.setInt(1, Configuration.BOT.getJDA().getShardInfo().getShardId());
            stmt.setString(2, guildId);
            stmt.setString(3, command);
            stmt.execute();

            MetricsManager.getDatabaseMetrics().INSERT.getAndIncrement();

        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Truncates the metrics database. (This happens when the bot is first loaded.)
     */
    public static void truncateMetrics() {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `SystemMetrics`");
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `EventMetrics`");
            PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM `DiscordMetrics`");
            PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM `DatabaseMetrics`");
            PreparedStatement stmt5 = conn.prepareStatement("DELETE FROM `CommandsLog`")) {

            stmt.execute();
            stmt2.execute();
            stmt3.execute();
            stmt4.execute();
            stmt5.execute();

            MetricsManager.getDatabaseMetrics().DELETE.getAndAdd(5);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Truncates anything 6 hours old in the system and discord metrics. Others are kept for total values.
     */
    public static void truncateDatabase() {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `SystemMetrics` WHERE dateInserted < UNIX_TIMESTAMP(NOW() - INTERVAL 6 HOUR);");
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `DiscordMetrics` WHERE dateInserted < UNIX_TIMESTAMP(NOW() - INTERVAL 6 HOUR);")) {

            stmt.execute();
            stmt2.execute();

            MetricsManager.getDatabaseMetrics().DELETE.getAndAdd(2);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Cleans up any guild's that ask the bot to leave. (Uses CASCADE)
     *
     * @param guild the guild's id.
     */
    public static void cleanup(String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `Guilds` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            stmt.execute();

            MetricsManager.getDatabaseMetrics().DELETE.getAndIncrement();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates settings from channels that are deleted.
     *
     * @param setting the setting to cleanup.
     * @param guildId the guild to cleanup.
     */
    public static void cleanupSettings(String setting, String guildId) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `GuildSettings` SET " + setting + " = null WHERE `guildId` = ?")){

            stmt.setString(1, guildId);
            stmt.execute();

            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

}
