package com.register.exercise1.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads and exposes application configuration from application.properties on the classpath.
 * Provides typed accessors so callers never deal with raw property keys.
 */
public class Config {

    private static final String CONFIG_FILE = "application.properties";

    private final Properties props;

    public Config() {
        this.props = load();
    }

    /** @return path to the source log file */
    public String inputPath() {
        return props.getProperty("input.path");
    }

    /** @return directory where the report is written */
    public String outputDir() {
        return props.getProperty("output.dir");
    }

    /** @return base name of the report file, without extension */
    public String outputFilename() {
        return props.getProperty("output.filename");
    }

    /** @return default output format when none is passed via CLI */
    public String defaultFormat() {
        return props.getProperty("default.format", "csv");
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException(CONFIG_FILE + " not found on classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + CONFIG_FILE, e);
        }
        return props;
    }
}
