/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.command;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * @author ishland
 *
 */
public class CommandHandler {
    private Plugin plugin;

    public CommandHandler(Plugin plugin) {
	this.setPlugin(plugin);
	Bukkit.getPluginCommand("asynckeepalive").setExecutor(new Command_asynckeepalive_Handler(getPlugin()));
	Bukkit.getPluginCommand("ak").setExecutor(new Command_asynckeepalive_Handler(getPlugin()));
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
}
