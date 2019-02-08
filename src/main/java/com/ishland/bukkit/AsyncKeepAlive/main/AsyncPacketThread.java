
/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.main;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

    public AsyncPacketThread() {
	setDaemon(true);
    }

    public void run() {
	protocolManager = ProtocolLibrary.getProtocolManager();
	for (;;) {
	    try {
		Iterator<?> localIterator = Bukkit.getServer().getOnlinePlayers().iterator();
		while (localIterator.hasNext()) {
		    Player player = (Player) localIterator.next();
		    PacketContainer keepAlivePacket = protocolManager.createPacket(PacketType.Play.Server.KEEP_ALIVE);
                    keepAlivePacket.setMeta("data", -5000);
		    try {
			protocolManager.sendServerPacket(player, keepAlivePacket);
                        // getLogger().finer("Sent custom keepalive");
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
}
