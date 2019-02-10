/**
 * 
 */

package com.ishland.bukkit.AsyncKeepAlive.main;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.rules.Timeout;
import org.bstats.bukkit.Metrics;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketContainer;
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

    @Override
    public void onEnable() {
	Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        String javaVersion = System.getProperty("java.version");
        Map<String, Integer> entry = new HashMap<>();
        entry.put(javaVersion, 1);
        if (javaVersion.startsWith("1.7")) {
            map.put("Java 1.7", entry);
        } else if (javaVersion.startsWith("1.8")) {
            map.put("Java 1.8", entry);
        } else if (javaVersion.startsWith("1.9")) {
            map.put("Java 1.9", entry);
        } else {
            map.put("Other", entry);
        }
        return map;
    }));
	protocolManager = ProtocolLibrary.getProtocolManager();
	getLogger().info("AsyncKeepAlive by ishland");
	if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
	    getLogger().warning("You need ProtocolLib in order to use this plugin");
	    getServer().getPluginManager().disablePlugin(this);
	    return;
	}
	try {
	    new AsyncPacketThread().start();
	    protocolManager.addPacketListener(
		    new PacketAdapter(this, ListenerPriority.LOWEST, PacketType.Play.Client.KEEP_ALIVE) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
                            try {
			        PacketContainer keepAlivePacket = e.getPacket();
                                StructureModifier<Long> packetData = keepAlivePacket.getLongs();
                                Long packetValue = packetData.readSafely(0);
                                getLogger().fine("Got keepalive " + String.valueOf(packetValue));
                                if(packetValue == 0L) {
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
	getLogger().info("AsyncKeepAlive 0.1-SNAPSHOT is now Enabled!");
    }

    @Override
    public void onDisable() {
	getLogger().warning("Current version of AsyncKeepAlive cannot be disabled completely.");
	getLogger().info("AsyncKeepAlive 0.1-SNAPSHOT is now Disabled!");
    }
}
