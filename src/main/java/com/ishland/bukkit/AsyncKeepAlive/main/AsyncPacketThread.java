
/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.main;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

/**
 * @author ishland
 *
 */
public class AsyncPacketThread implements Runnable {
    private ProtocolManager protocolManager;
    private Plugin plugin;
    private boolean doStop = false;
    private boolean debug = false;
    private long frequency = 4000;
    private HashMap<String, String> ping = new HashMap<>();

    public void doStop() {
	doStop = true;
    }

    public void doDebug() {
	debug = true;
    }

    public boolean setFrequency(long freq) {
	if (freq >= 1) {
	    frequency = freq;
	    return true;
	}
	return false;
    }

    public void run() {
	protocolManager = ProtocolLibrary.getProtocolManager();
	getPlugin().getLogger().info("Packet thread started.");
	for (; !doStop;) {
	    try {
		Iterator<?> localIterator = Bukkit.getServer().getOnlinePlayers().iterator();
		while (localIterator.hasNext()) {
		    Player player = (Player) localIterator.next();
		    if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.12")) {
			long body = System.currentTimeMillis();
			PacketContainer keepAlivePacket = protocolManager
				.createPacket(PacketType.Play.Server.KEEP_ALIVE);
			keepAlivePacket.getLongs().write(0, System.currentTimeMillis());
			try {
			    protocolManager.sendServerPacket(player, keepAlivePacket);
			    if (debug)
				getPlugin().getLogger().info("[Debug] Sent extra keepalive " + String.valueOf(body)
					+ " to " + player.getName());
			} catch (InvocationTargetException e) {
			    throw new RuntimeException("Cannot send packet " + keepAlivePacket, e);
			}
		    } else {
			PacketContainer keepAlivePacket = protocolManager
				.createPacket(PacketType.Play.Server.KEEP_ALIVE);
			try {
			    protocolManager.sendServerPacket(player, keepAlivePacket);
			    if (debug)
				getPlugin().getLogger().info("[Debug] Sent extra keepalive to " + player.getName());
			} catch (InvocationTargetException e) {
			    throw new RuntimeException("Cannot send packet " + keepAlivePacket, e);
			}
		    }

		}
	    } catch (Throwable t) {
		t.printStackTrace();
	    }
	    try {
		Thread.sleep(TimeUnit.MILLISECONDS.toMillis(frequency));
	    } catch (InterruptedException localInterruptedException) {
	    }
	}
	getPlugin().getLogger().info("Packet thread stopped.");
    }

    public Plugin getPlugin() {
	return plugin;
    }

    public void setPlugin(Plugin plugin) {
	this.plugin = plugin;
    }

    /**
     * @return the ping
     */
    public HashMap<String, String> getPing() {
	return ping;
    }

}
