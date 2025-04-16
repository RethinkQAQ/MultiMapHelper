package com.rethink.multiMapHelper.maps;

import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

public interface Channels {
    String XAERO_MINIMAP = "xaerominimap:main";
    String XAERO_WORLDMAP = "xaeroworldmap:main";
    String VOXELMAP_WORLDMAP = "worldinfo:world_id";
    String VOXELMAP_WORLDMAP_LEGACY = "world_id";

    ChannelIdentifier VOXELMAP_WORLDMAP_CHANNEL = MinecraftChannelIdentifier.from(VOXELMAP_WORLDMAP);
    ChannelIdentifier VOXELMAP_WORLDMAP_LEGACY_CHANNEL = new LegacyChannelIdentifier(VOXELMAP_WORLDMAP_LEGACY);
    ChannelIdentifier XAERO_MINIMAP_CHANNEL = MinecraftChannelIdentifier.from(XAERO_MINIMAP);
    ChannelIdentifier XAERO_WORLDMAP_CHANNEL = MinecraftChannelIdentifier.from(XAERO_WORLDMAP);
}
