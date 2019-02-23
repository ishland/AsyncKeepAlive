/**
 * 
 */

package com.ishland.bukkit.AsyncKeepAlive.main;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.rules.Timeout;

import com.comphenix.protocol.ProtocolLibrary;

/**
 * @author ishland
 *
 */
public class AsyncKeepAlive extends JavaPlugin {
    public static Timeout main;
    private AsyncReceiveThread ReceiveThread;
    private AsyncPacketThread PacketThread;

    public static Timeout getInstance() {
	return main;
    }

    public Set<UUID> inTimeout = new TreeSet<UUID>();
    private Metrics metrics;

    @Override
    public void onEnable() {
	setMetrics(new Metrics(this));
	ProtocolLibrary.getProtocolManager();
	getLogger().info("AsyncKeepAlive by ishland");
	if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
	    getLogger().warning("You need ProtocolLib in order to use this plugin");
	    getServer().getPluginManager().disablePlugin(this);
	    return;
	}
	if (!(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.12")
		|| Bukkit.getVersion().contains("1.11")))
	    getLogger().warning("Minecraft " + Bukkit.getVersion() + " hasn't been tested yet!");
	try {
	    setPacketThread(new AsyncPacketThread());
	    getPacketThread().setPlugin(this);
	    getPacketThread().start();
	    setReceiveThread(new AsyncReceiveThread());
	    getReceiveThread().setPlugin(this);
	    getReceiveThread().run();
	} catch (Throwable t) {
	    t.printStackTrace();
	    getServer().getPluginManager().disablePlugin(this);
	}
	getLogger().info("AsyncKeepAlive 0.2 is now Enabled!");
    }

    @Override
    public void onDisable() {
	getLogger().warning("Current version of AsyncKeepAlive cannot be disabled completely.");
	getLogger().info("AsyncKeepAlive 0.2 is now Disabled!");
    }

    public Metrics getMetrics() {
	return metrics;
    }

    public void setMetrics(Metrics metrics) {
	this.metrics = metrics;
    }

    public AsyncReceiveThread getReceiveThread() {
	return ReceiveThread;
    }

    public void setReceiveThread(AsyncReceiveThread receiveThread) {
	ReceiveThread = receiveThread;
    }

    public AsyncPacketThread getPacketThread() {
	return PacketThread;
    }

    public void setPacketThread(AsyncPacketThread packetThread) {
	PacketThread = packetThread;
    }
}
