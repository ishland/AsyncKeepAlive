package com.ishland.sponge.AsyncKeepAlive.configuration;

import java.io.IOException;
import java.nio.file.Paths;

import org.spongepowered.api.Sponge;

import com.ishland.sponge.AsyncKeepAlive.main.Launcher;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

/**
 * @author ishland
 *
 */
public class ConfigurationLoader {
    private Launcher plugin;
    private YAMLConfigurationLoader loader;
    private ConfigurationNode config;

    public ConfigurationLoader(Launcher plugin) {
	this.plugin = plugin;
    }

    public void load() throws IOException {
	loadDefault();
	this.loader = YAMLConfigurationLoader.builder()
		.setPath(Paths.get(getPlugin().getPrivateConfigDir().toString(), "config.yml")).build();
	this.loader.load();
    }

    private void loadDefault() throws IOException {
	try {
	    Sponge.getAssetManager().getAsset(this, "config.yml").get()
		    .copyToFile(Paths.get(getPlugin().getPrivateConfigDir().toString(), "config.yml"), false, true);
	} catch (IOException e) {
	    getPlugin().getLogger().warn("Error while writing configuration file", e);
	    throw new IOException();
	}
    }

    /**
     * @return the plugin
     */
    public Launcher getPlugin() {
	return plugin;
    }

    /**
     * @return the loader
     */
    public YAMLConfigurationLoader getLoader() {
	return loader;
    }

    /**
     * @return the config
     */
    public ConfigurationNode getConfig() {
	return config;
    }

}
