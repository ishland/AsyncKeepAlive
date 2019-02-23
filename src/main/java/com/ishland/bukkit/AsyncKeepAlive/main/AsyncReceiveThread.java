/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.main;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;

/**
 * @author ishland
 *
 */
public class AsyncReceiveThread extends Thread implements Listener {
    private Plugin plugin;

    public AsyncReceiveThread() {
	setDaemon(true);
    }

    public void run() {
	ProtocolLibrary.getProtocolManager().addPacketListener(
		new PacketAdapter(getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Client.KEEP_ALIVE) {
		    @Override
		    public void onPacketReceiving(PacketEvent e) {
			if (e.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
			    try {
				PacketContainer keepAlivePacket = e.getPacket();
				if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.12")) {
				    StructureModifier<Long> packetData = keepAlivePacket.getLongs();
				    Long packetValue = packetData.readSafely(0);
				    // System.out.println("Got keepalive " + String.valueOf(packetValue));
				    if (packetValue == 0L) {
					e.setCancelled(true);
				    }
				} else {
				    StructureModifier<Integer> packetData = keepAlivePacket.getIntegers();
				    int packetValue = packetData.readSafely(0);
				    // System.out.println("Got keepalive " + String.valueOf(packetValue));
				    if (packetValue == 0L) {
					e.setCancelled(true);
				    }
				}
			    } catch (Throwable t) {
				System.out.println("Caught a exception");
				t.printStackTrace();
			    }
			}
		    }
		});
	getPlugin().getLogger().info("Receive thread started.");
    }

    public Plugin getPlugin() {
	return plugin;
    }

    public void setPlugin(Plugin plugin) {
	this.plugin = plugin;
    }

}
