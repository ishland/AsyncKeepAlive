/**
 * 
 */

package com.ishland.bukkit.AsyncKeepAlive.AsyncKeepAlive;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author ishland
 *
 */
public class AsyncKeepAlive extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
	getLogger().info("AsyncKeepAlive by ishland");
	try {

	    new AsyncPacketThread().start();
	} catch (Throwable t) {
	    t.printStackTrace();
	    getServer().getPluginManager().disablePlugin(this);
	}
	getLogger().info("AsyncKeepAlive 0.0.1-SNAPSHOT is now Enabled!");
    }

    @Override
    public void onDisable() {
	getLogger().warning("Current version of AsyncKeepAlive cannot be disabled completely.");
	getLogger().info("AsyncKeepAlive 0.0.1-SNAPSHOT is now Disabled!");
    }
}
