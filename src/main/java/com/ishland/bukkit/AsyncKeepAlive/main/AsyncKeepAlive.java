/**
 * 
 */

package com.ishland.bukkit.AsyncKeepAlive.main;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.rules.Timeout;

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
		    new PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Client.KEEP_ALIVE) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
                            try {
			        PacketContainer keepAlivePacket = e.getPacket();
                                StructureModifier<Long> packetData = keepAlivePacket.getLongs();
                                int packetValue = Integer.parseInt(String.valueOf(packetData.readSafely(0)));
                                getLogger().finer("Got keepalive");
                                if(packetValue == -5000) {
                                    e.setCancelled(true);
                                    getLogger().finer("Got custom keepalive");
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
	getLogger().info("AsyncKeepAlive 0.0.1 is now Enabled!");
    }

    @Override
    public void onDisable() {
	getLogger().warning("Current version of AsyncKeepAlive cannot be disabled completely.");
	getLogger().info("AsyncKeepAlive 0.0.1 is now Disabled!");
    }
}
