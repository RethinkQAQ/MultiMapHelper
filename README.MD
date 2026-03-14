Enligsh | [中文](./README_CN.MD)
# Description
This is a simple plugin designed to help multi-world map mods select the corresponding world. 
It retrieves the world name set by the proxy server and sends it to the client, allowing the client to choose the appropriate world.
> For now, it just works, and improvements will be made later.

# Same project
- [MapModCompanion](https://github.com/turikhay/MapModCompanion)
> MapModCompanion works better than this plugin, so I recommend using it instead.
- [worldnamepacket](https://github.com/kosmolot-mods/worldnamepacket)

# Usage
1. Download the plugin and install it to the plugins directory.
2. The configuration file is automatically generated with `servername: mapID` settings. The default map ID for each server is the server name. Adjust according to your needs.

# Plan

- [x] add support for Xaero's map
- [x] add support for VoxelMap mod
- [x] add support for JourneyMap mod
- [x] add config file to set each world name of server