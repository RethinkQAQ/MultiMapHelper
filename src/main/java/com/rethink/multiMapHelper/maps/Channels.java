package com.rethink.multiMapHelper.maps;

import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

public interface Channels {
    String XAERO_MINIMAP = "xaerominimap:main";
    String XAERO_WORLDMAP = "xaeroworldmap:main";
    String WORLDMAP = "worldinfo:world_id";
    String WORLDMAP_LEGACY = "world_id";

    ChannelIdentifier WORLDMAP_CHANNEL = MinecraftChannelIdentifier.from(WORLDMAP);
    ChannelIdentifier WORLDMAP_LEGACY_CHANNEL = new LegacyChannelIdentifier(WORLDMAP_LEGACY);
    ChannelIdentifier XAERO_MINIMAP_CHANNEL = MinecraftChannelIdentifier.from(XAERO_MINIMAP);
    ChannelIdentifier XAERO_WORLDMAP_CHANNEL = MinecraftChannelIdentifier.from(XAERO_WORLDMAP);
}
