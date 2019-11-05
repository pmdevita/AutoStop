package net.pdevita.autostop

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.configuration.file.FileConfiguration



class AutoStop : JavaPlugin {
    var eventInstance: Events? = null
    constructor() : super()

    override fun onEnable() {
        saveDefaultConfig()
        var event = Events(logger, server, this, config.getLong("disconnectDelay"))
        server.pluginManager.registerEvents(event, this)
        event.scheduleShutdown(config.getLong("startDelay") * 20)
        eventInstance = event
    }
}