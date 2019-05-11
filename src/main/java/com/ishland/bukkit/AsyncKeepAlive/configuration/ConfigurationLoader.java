package com.ishland.bukkit.AsyncKeepAlive.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigurationLoader {
    private Plugin plugin;
    private FileConfiguration configuration;
    public static final int VERSION = 2;

    public ConfigurationLoader(Plugin plugin) {
	this.setPlugin(plugin);
    }

    protected void load() {
	getPlugin().getLogger().info("Loading configurations...");
	getPlugin().saveDefaultConfig();
	setConfiguration(getPlugin().getConfig());
	if (getConfiguration().getInt("version") != VERSION) {
	    getPlugin().getLogger().info("Updating your configuration file...");
	    com.ishland.bukkit.AsyncKeepAlive.configuration.updater.UpdaterFrom1to2.update(getConfiguration());
	    getPlugin().saveConfig();
	    getPlugin().reloadConfig();
	    setConfiguration(getPlugin().getConfig());
	}
    }

    /**
     * @return the plugin
     */
    public Plugin getPlugin() {
	return plugin;
    }

    /**
     * @param plugin the plugin to set
     */
    public void setPlugin(Plugin plugin) {
	this.plugin = plugin;
    }

    /**
     * @return the configuration
     */
    public FileConfiguration getConfiguration() {
	return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(FileConfiguration configuration) {
	this.configuration = configuration;
    }
}
