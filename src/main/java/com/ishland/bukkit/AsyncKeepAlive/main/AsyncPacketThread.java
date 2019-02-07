/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.AsyncKeepAlive;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

/**
 * @author ishland
 *
 */
public class AsyncPacketThread extends Thread {
    public AsyncPacketThread() {
	setDaemon(true);
    }

    public void run() {
	ProtocolManager protocolManager;
	protocolManager = ProtocolLibrary.getProtocolManager();
	for (;;) {
	    try {
		Iterator<? extends Player> localIterator = Bukkit.getServer().getOnlinePlayers().iterator();
		Player player = (Player) localIterator.next();
		PacketContainer keepAlivePacket = protocolManager.
			createPacket(PacketType.Play.Server.KEEP_ALIVE);
		try {
		    protocolManager.sendServerPacket(player, keepAlivePacket);
		} catch (InvocationTargetException e) {
		    throw new RuntimeException("Cannot send packet " + keepAlivePacket, e);
		}
		if (localIterator.hasNext()) {
		    continue;
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
}
