/**
 * 
 */

package com.ishland.bukkit.AsyncKeepAlive.main;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.rules.Timeout;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;

/**
 * @author ishland
 *
 */
public class AsyncKeepAlive extends JavaPlugin implements Listener {
    public static Timeout main;
    private ProtocolManager protocolManager;

    public static Timeout getInstance() {
	return main;
    }

    public Set<UUID> inTimeout = new TreeSet<UUID>();
    private Metrics metrics;

    @Override
    public void onEnable() {
	setMetrics(new Metrics(this));
	protocolManager = ProtocolLibrary.getProtocolManager();
	getLogger().info("AsyncKeepAlive by ishland");
	if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
	    getLogger().warning("You need ProtocolLib in order to use this plugin");
	    getServer().getPluginManager().disablePlugin(this);
	    return;
	}
	if (!(Bukkit.getVersion().contains("1.13.2") || Bukkit.getVersion().contains("1.12.2")))
	    getLogger().warning("Minecraft " + Bukkit.getVersion() + " hasn't been tested yet!");
	try {
	    new AsyncPacketThread().start();
	    protocolManager.addPacketListener(
		    new PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Client.KEEP_ALIVE) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
			    try {
				PacketContainer keepAlivePacket = e.getPacket();
				StructureModifier<Long> packetData = keepAlivePacket.getLongs();
				Long packetValue = packetData.readSafely(0);
				getLogger().fine("Got keepalive " + String.valueOf(packetValue));
				if (packetValue == 0L) {
				    e.setCancelled(true);
				}
			    } catch (Throwable t) {
				getLogger().warning("Caught a exception");
				t.printStackTrace();
			    }
			}
		    });
	} catch (Throwable t) {
	    t.printStackTrace();
	    getServer().getPluginManager().disablePlugin(this);
	}
	getLogger().info("AsyncKeepAlive 0.2-SNAPSHOT is now Enabled!");
    }

    @Override
    public void onDisable() {
	getLogger().warning("Current version of AsyncKeepAlive cannot be disabled completely.");
	getLogger().info("AsyncKeepAlive 0.2-SNAPSHOT is now Disabled!");
    }

    public Metrics getMetrics() {
	return metrics;
    }

    public void setMetrics(Metrics metrics) {
	this.metrics = metrics;
    }
}
