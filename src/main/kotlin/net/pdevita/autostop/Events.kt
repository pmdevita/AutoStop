package net.pdevita.autostop

import org.bukkit.Server
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.logging.Logger


class Events(val logger: Logger, val server: Server, val plugin: JavaPlugin, val delay: Long) : Listener {
    var task: BukkitTask? = null

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        if (server.onlinePlayers.size <= 1) { // Will be 1 if the last player is DC'ing
            // Start the timer
            logger.info("Scheduling shutdown")
            this.scheduleShutdown(20 * delay)  // ticks * seconds * minutes
        }
    }

    @EventHandler
    fun onPlayerLoginEvent(event: PlayerLoginEvent) {
        this.cancelShutdown()
    }

    fun scheduleShutdown(delay: Long) {
        task = server.scheduler.runTaskLater(plugin, DelayedCheck(server), delay)
    }

    fun cancelShutdown() {
        if (task != null) {
            task?.cancel()  // Cancel the shutdown safely
            logger.info("Shutdown cancelled")
        }
    }

}

class DelayedCheck(private val server: Server) : Runnable {

    override fun run() {
        if (server.onlinePlayers.isEmpty()) {
            server.shutdown()
        }
    }
}