package com.ishland.bukkit.AsyncKeepAlive.thread;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.ishland.bukkit.AsyncKeepAlive.main.PlaceHolderMain;

public class AsyncPacketListenerFor1_8to1_11 extends AsyncPacketListener {

    @Override
    public void register(Plugin plugin, final boolean debug, final AsyncPacketThread packetThread,
	    final PlaceHolderMain placeHolder) {
	try {
	    ProtocolLibrary.getProtocolManager().getAsynchronousManager().registerAsyncHandler(
		    new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.KEEP_ALIVE) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
			    if (e.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
				try {
				    PacketContainer keepAlivePacket = e.getPacket();
				    StructureModifier<Integer> packetData = keepAlivePacket.getIntegers();
				    Integer packetValue = packetData.readSafely(0);
				    if (packetThread.getPing().containsKey(Long.valueOf(packetValue))) {
					Long latency = packetThread.getPing().get(Long.valueOf(packetValue))
						.getPlayerPing(e.getPlayer());
					if (latency.longValue() == -1) {
					    if (debug)
						plugin.getLogger()
							.info("[Debug] Got server-sent keepalive "
								+ String.valueOf(packetValue) + " from "
								+ e.getPlayer().getName());
					    return;
					}
					if (placeHolder != null)
					    placeHolder.latency.put(e.getPlayer().getName(), latency);
					if (debug)
					    plugin.getLogger().info("[Debug] Got plugin-sent keepalive "
						    + String.valueOf(packetValue) + " from " + e.getPlayer().getName()
						    + " after " + latency.toString() + " ms");
					e.setCancelled(true);
					setCount(getCount() + 1);
				    } else {
					if (debug)
					    plugin.getLogger().info("[Debug] Got server-sent keepalive "
						    + String.valueOf(packetValue) + " from " + e.getPlayer().getName());
				    }

				} catch (Throwable t) {
				    System.out.println("Caught a exception");
				    System.out.println(t.getMessage());
				    t.printStackTrace();
				}
			    }
			}
		    }).start();
	} catch (

	Throwable t) {
	    t.printStackTrace();
	    return;
	}
    }
}
