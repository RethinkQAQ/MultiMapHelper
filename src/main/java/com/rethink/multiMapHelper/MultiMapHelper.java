package com.rethink.multiMapHelper;

import com.google.inject.Inject;
import com.rethink.multiMapHelper.maps.WorldNameHandler;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Arrays;

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
//    private final Config config;

    @Inject
    public MultiMapHelper(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.worldNameHandler = new WorldNameHandler(logger);
//        this.config = new Config(logger, dataDirectory.resolve("config.yaml"));
        this.server.getChannelRegistrar().register(VOXELMAP_WORLDMAP_CHANNEL);
    }

//    @Subscribe
//    private void onMessage(PluginMessageEvent event) {
//        if (event.getIdentifier() == VOXELMAP_WORLDMAP_CHANNEL
//                && event.getSource() instanceof Player player
//                && Arrays.equals(event.getData(), new byte[]{0, 42, 0})) {
//            logger.info("Received world name from {}", player.getUsername());
//            if (player.getCurrentServer().isEmpty()) {
//                return;
//            }
//            RegisteredServer server = player.getCurrentServer().get().getServer();
////            worldNameHandler.sendWoldName(player, server, VOXELMAP_WORLDMAP_CHANNEL);
//        }
//    }

    @Subscribe
    private void onProxyInitialization(ProxyInitializeEvent event) {
//        if (!config.load()) {
//            logger.error("Failed to load config");
//            return;
//        }
        logger.info("MultiMapHelper is enabled");
        this.server.getChannelRegistrar().register(XAERO_WORLDMAP_CHANNEL);
    }

    @Subscribe(priority = -32768)
    private void onServerConnection(ServerConnectedEvent event) {
        logger.info("Trying to send world name to {}", event.getPlayer().getUsername());
        worldNameHandler.sendWoldName(event.getPlayer(), event.getServer(), XAERO_WORLDMAP_CHANNEL);
        worldNameHandler.sendWoldName(event.getPlayer(), event.getServer(), VOXELMAP_WORLDMAP_CHANNEL);
    }
}
