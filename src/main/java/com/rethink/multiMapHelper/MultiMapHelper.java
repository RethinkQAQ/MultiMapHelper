package com.rethink.multiMapHelper;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.google.inject.Inject;
import com.rethink.multiMapHelper.maps.WorldNameHandler;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

import static com.rethink.multiMapHelper.maps.Channels.*;

@Plugin(
        id = PluginMeta.PLUGIN_ID,
        name = PluginMeta.PLUGIN_NAME,
        version = PluginMeta.PLUGIN_VERSION,
        dependencies = {@Dependency(id="packetevents")},
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
    }

    @Subscribe
    private void onProxyInitialization(ProxyInitializeEvent event) {
        if (!this.config.load()) {
            logger.error("Failed to load config");
        }
        PacketEvents.getAPI().init();
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerImpl());
        logger.info("MultiMapHelper is enabled");
        this.server.getChannelRegistrar().register(XAERO_WORLDMAP_CHANNEL);
        this.server.getChannelRegistrar().register(XAERO_MINIMAP_CHANNEL);
        this.server.getChannelRegistrar().register(VOXELMAP_WORLDMAP_CHANNEL);
    }

    private class PacketListenerImpl extends PacketListenerAbstract {
        @Override
        public void onPacketSend(PacketSendEvent event) {
            if (event.getPacketType() != PacketType.Play.Server.INITIALIZE_WORLD_BORDER) {
                return;
            }

            User user = event.getUser();

            Player player = server.getPlayer(user.getUUID()).orElse(null);
            if (player == null) return;

            String serverName = player.getCurrentServer()
                    .map(conn -> conn.getServerInfo().getName())
                    .orElse(null);
            if (serverName == null) return;

            String mapID = config.getMapID(serverName);
            if ("#none".equals(mapID)) return;

            worldNameHandler.sendWorldName(player, mapID, XAERO_WORLDMAP_CHANNEL);
            worldNameHandler.sendWorldName(player, mapID, XAERO_MINIMAP_CHANNEL);
            worldNameHandler.sendWorldName(player, mapID, VOXELMAP_WORLDMAP_CHANNEL);
        }
    }
}

