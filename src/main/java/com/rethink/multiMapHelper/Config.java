package com.rethink.multiMapHelper;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Config {
    private ConfigData configData;

    private final Logger logger;
    private final Path configPath;

    public Config(Logger logger, Path configPath) {
        this.logger = logger;
        this.configPath = configPath;
    }

    public boolean load() {
        File configDir = this.configPath.getParent().toFile();
        if (!configDir.exists() && !configDir.mkdirs()) {
            this.logger.error("Create data directory failed: {}", configDir);
            return false;
        }

        File file = this.configPath.toFile();
        if (!file.exists()) {
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.yaml")) {
                Files.copy(Objects.requireNonNull(in), file.toPath());
            } catch (Exception e) {
                this.logger.error("Generate config failed", e);
                return false;
            }
        }

        try {
            Yaml yaml = new Yaml(new Constructor(ConfigData.class));
            this.configData = yaml.loadAs(Files.readString(file.toPath()), ConfigData.class);
        } catch (Exception e) {
            this.logger.error("Read config failed", e);
            return false;
        }
        return true;
    }

    private static class ConfigData {
    }
}
