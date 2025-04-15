package com.rethink.multiMapHelper;

import com.google.inject.Inject;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

import static com.rethink.multiMapHelper.maps.Channels.XAERO_WORLDMAP_CHANNEL;

@Plugin(
        id = PluginMeta.PLUGIN_ID, name = PluginMeta.PLUGIN_NAME, version = PluginMeta.PLUGIN_VERSION,
        description = "A plugin that helps with managing multi-world-maps in Minecraft",
        authors = {"Rethink"}
)
public class MultiMapHelper {

    private final ProxyServer server;
    private final Logger logger;
//    private final Config config;

    @Inject
    public MultiMapHelper(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
//        this.config = config;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
//        if (!config.load()) {
//            logger.error("Failed to load config");
//            return;
//        }
        logger.info("MultiMapHelper is enabled");
        this.server.getChannelRegistrar().register(XAERO_WORLDMAP_CHANNEL);
    }

    @Subscribe(priority = -32768)
    private void onServerConnection(ServerConnectedEvent event) {
        logger.info("Trying to send world name to " + event.getPlayer().getUsername());
        sendWoldName(event.getPlayer(), event.getServer(), XAERO_WORLDMAP_CHANNEL);
    }

    private void sendWoldName(Player player, RegisteredServer server, ChannelIdentifier channel) {
        if (server == null) {
            return;
        }
        String worldName = server.getServerInfo().getName();
        CRC32 crc32 = new CRC32();
        byte[] worldNameBytes = worldName.getBytes(StandardCharsets.UTF_8);
        crc32.update(worldNameBytes, 0 , worldNameBytes.length);
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(array)) {
            out.write(0);
            out.writeInt((int)crc32.getValue());
        } catch (IOException e) {
            logger.error("Failed to write world name to byte array", e);
        }
        logger.debug("Sending world name {} to {}", worldName, player.getUsername());
        player.sendPluginMessage(channel,array.toByteArray());
    }
}
