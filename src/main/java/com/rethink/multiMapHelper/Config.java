package com.rethink.multiMapHelper;

import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Config {

    private ConfigData configData;
    private final ProxyServer server;
    private final Logger logger;
    private final Path configPath;
    private final Yaml loadYaml;
    private final Yaml dumpYaml;

    public Config(ProxyServer server, Logger logger, Path configPath) {
        this.server = server;
        this.logger = logger;
        this.configPath = configPath;

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setMaxAliasesForCollections(50);
        loaderOptions.setWarnOnDuplicateKeys(false);
        this.loadYaml = new Yaml(new Constructor(ConfigData.class, loaderOptions));

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setPrettyFlow(true);
        this.dumpYaml = new Yaml(options);
    }

    public boolean load() {
        File configDir = this.configPath.getParent().toFile();
        if (!configDir.exists() && !configDir.mkdirs()) {
            this.logger.error("Failed to create config directory");
            return false;
        }

        File file = this.configPath.toFile();
        if (!file.exists()) {
            this.configData = new ConfigData();
            this.server.getAllServers().forEach(
                    server -> {
                        String name = server.getServerInfo().getName();
                        this.configData.getMapID().put(name, name);
                    }
            );
            if (!this.save()) {
                this.logger.error("Failed to save config");
                return false;
            }
            this.logger.info("Config created");
            return true;
        }

        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            this.configData = loadYaml.loadAs(reader, ConfigData.class);
            if (this.configData == null) {
                this.configData = new ConfigData();
            }
            if (this.configData.getMapID() == null) {
                this.configData.setMapID(new ConcurrentHashMap<>());
            }
        } catch (Exception e) {
            this.logger.error("Failed to load config", e);
            return false;
        }
        return true;
    }

    public boolean save() {
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("mapID", this.configData.getMapID());

        try(Writer writer = Files.newBufferedWriter(configPath)) {
            dumpYaml.dump(root, writer);
        } catch (IOException e) {
            this.logger.error("Failed to save config", e);
            return false;
        }
        return true;
    }

    public String getMapID(String serverName) {
        String mapID = this.configData.getMapID().get(serverName);
        if (mapID == null) {
            mapID = serverName;
            configData.getMapID().put(serverName, mapID);
            save();
        }
        return mapID;
    }

    public static class ConfigData {
        private Map<String, String> mapID = new LinkedHashMap<>();

        public Map<String, String> getMapID() {
            return mapID;
        }

        public void setMapID(Map<String, String> mapID) {
            this.mapID = mapID;
        }
    }
}
