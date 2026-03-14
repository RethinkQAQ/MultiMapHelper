package com.rethink.multiMapHelper;

import com.google.inject.Inject;
import com.rethink.multiMapHelper.maps.WorldNameHandler;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

import static com.rethink.multiMapHelper.maps.Channels.*;

@Plugin(
        id = PluginMeta.PLUGIN_ID, name = PluginMeta.PLUGIN_NAME, version = PluginMeta.PLUGIN_VERSION,
        description = "A plugin that helps with managing multi-world-maps in Minecraft",
        authors = {"Rethink"},
        url = "https://github.com/RethinkQAQ/MultiMapHelper"
)
public class MultiMapHelper {

    private final ProxyServer server;
    private final Logger logger;
    private final WorldNameHandler worldNameHandler;
    private final Config config;

    @Inject
    public MultiMapHelper(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.config = new Config(server, logger, dataDirectory.resolve("config.yaml"));
        this.worldNameHandler = new WorldNameHandler(logger, this.config);
        this.server.getChannelRegistrar().register(VOXELMAP_WORLDMAP_CHANNEL);
    }

    @Subscribe
    private void onProxyInitialization(ProxyInitializeEvent event) {
        if (!this.config.load()) {
            logger.error("Failed to load config");
        }
        logger.info("MultiMapHelper is enabled");
        this.server.getChannelRegistrar().register(XAERO_WORLDMAP_CHANNEL);
        this.server.getChannelRegistrar().register(VOXELMAP_WORLDMAP_CHANNEL);
    }

    @Subscribe(priority = 100)
    private void onServerConnection(ServerConnectedEvent event) {
        String mapID = config.getMapID(event.getServer().getServerInfo().getName());
        worldNameHandler.sendWoldName(event.getPlayer(), mapID, XAERO_WORLDMAP_CHANNEL);
        worldNameHandler.sendWoldName(event.getPlayer(), mapID, VOXELMAP_WORLDMAP_CHANNEL);
    }
}
