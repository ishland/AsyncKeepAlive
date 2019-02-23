
/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.main;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.rules.Timeout;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

/**
 * @author ishland
 *
 */
public class AsyncPacketThread extends Thread {
    public static Timeout main;
    private ProtocolManager protocolManager;
    private Plugin plugin;

    public AsyncPacketThread() {
	setDaemon(true);
    }

    public void run() {
	protocolManager = ProtocolLibrary.getProtocolManager();
	getPlugin().getLogger().info("Packet thread started.");
	for (;;) {
	    try {
		Iterator<?> localIterator = Bukkit.getServer().getOnlinePlayers().iterator();
		while (localIterator.hasNext()) {
		    Player player = (Player) localIterator.next();
		    PacketContainer keepAlivePacket = protocolManager.createPacket(PacketType.Play.Server.KEEP_ALIVE);
		    try {
			protocolManager.sendServerPacket(player, keepAlivePacket);
			// System.out.println("Sent custom keepalive");
		    } catch (InvocationTargetException e) {
			throw new RuntimeException("Cannot send packet " + keepAlivePacket, e);
		    }
		}
	    } catch (Throwable t) {
		t.printStackTrace();
	    }
	    try {
		Thread.sleep(TimeUnit.SECONDS.toMillis(4L));
	    } catch (InterruptedException localInterruptedException) {
	    }
	}
    }

    public Plugin getPlugin() {
	return plugin;
    }

    public void setPlugin(Plugin plugin) {
	this.plugin = plugin;
    }
}
