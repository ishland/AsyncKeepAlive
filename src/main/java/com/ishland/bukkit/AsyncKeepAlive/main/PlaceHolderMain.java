/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.main;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

/**
 * @author ishland
 *
 */
public class PlaceHolderMain extends PlaceholderExpansion {

    public HashMap<String, Long> latency = new HashMap<>();
    private Plugin origPlugin;

    @Override
    public boolean canRegister() {
	return true;
    }

    /**
     * Because this is an internal class, you must override this method to let
     * PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist() {
	return true;
    }

    @Override
    public String getAuthor() {
	return getOrigPlugin().getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
	return "asynckeepalive";
    }

    @Override
    public String getVersion() {
	return getOrigPlugin().getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier is found and
     * needs a value. <br>
     * We specify the value identifier in this method. <br>
     * Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param player     A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param identifier A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

	if (identifier.equals("ping")) {
	    return latency.containsKey(player.getName()) ? String.valueOf(latency.get(player.getName())) : "-1";
	}

	// We return null if an invalid placeholder
	// was provided
	return null;
    }

    /**
     * @return the origPlugin
     */
    public Plugin getOrigPlugin() {
	return origPlugin;
    }

    /**
     * @param origPlugin the origPlugin to set
     */
    public void setOrigPlugin(Plugin origPlugin) {
	this.origPlugin = origPlugin;
    }

}
