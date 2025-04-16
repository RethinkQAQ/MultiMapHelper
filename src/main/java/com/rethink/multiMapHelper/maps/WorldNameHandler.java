package com.rethink.multiMapHelper.maps;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

import static com.rethink.multiMapHelper.maps.Channels.*;

public class WorldNameHandler {
    private final Logger logger;

    public WorldNameHandler(Logger logger) {
        this.logger = logger;
    }

    public void sendWoldName(Player player, RegisteredServer server, ChannelIdentifier channel) {
        if (server == null) {
            return;
        }
        String worldName = server.getServerInfo().getName();
        CRC32 crc32 = new CRC32();
        byte[] worldNameBytes = worldName.getBytes(StandardCharsets.UTF_8);
        crc32.update(worldNameBytes, 0, worldNameBytes.length);
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(array)) {
            if (channel == XAERO_MINIMAP_CHANNEL || channel == XAERO_WORLDMAP_CHANNEL) {
                out.write(0);
                out.writeInt((int) crc32.getValue());
            } else if (channel == VOXELMAP_WORLDMAP_CHANNEL) {
                out.write(0);
                out.write(42);
                out.write(worldNameBytes.length);
                out.write(worldNameBytes, 0, worldNameBytes.length);
            }
        } catch (IOException e) {
            logger.error("Failed to write world name to byte array", e);
        }
        logger.debug("Sending world name {} to {}", worldName, player.getUsername());
        player.sendPluginMessage(channel, array.toByteArray());
    }
}
