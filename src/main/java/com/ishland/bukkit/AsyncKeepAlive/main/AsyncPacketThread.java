
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

    public void doStop() {
	doStop = true;
    }

    public void doDebug() {
	debug = true;
    }

    public void run() {
	protocolManager = ProtocolLibrary.getProtocolManager();
	getPlugin().getLogger().info("Packet thread started.");
	for (; !doStop;) {
	    try {
		Iterator<?> localIterator = Bukkit.getServer().getOnlinePlayers().iterator();
		while (localIterator.hasNext()) {
		    Player player = (Player) localIterator.next();
		    PacketContainer keepAlivePacket = protocolManager.createPacket(PacketType.Play.Server.KEEP_ALIVE);
		    try {
			protocolManager.sendServerPacket(player, keepAlivePacket);
			if (debug)
			    getPlugin().getLogger().info("[Debug] Sent extra keepalive to " + player.getName());
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
	getPlugin().getLogger().info("Packet thread stopped.");
    }

    public Plugin getPlugin() {
	return plugin;
    }

    public void setPlugin(Plugin plugin) {
	this.plugin = plugin;
    }
}
