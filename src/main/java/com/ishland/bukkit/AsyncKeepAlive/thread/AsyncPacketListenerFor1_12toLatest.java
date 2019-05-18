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

public class AsyncPacketListenerFor1_12toLatest extends AsyncPacketListener {
    private boolean debug;
    private AsyncPacketThread packetThread;

    @Override
    public void register(Plugin plugin, boolean debug, AsyncPacketThread packetThread,
	    final PlaceHolderMain placeHolder) {
	setDebug(debug);
	setPacketThread(packetThread);
	getPacketThread().getPing().put((long) 1, null);
	try {
	    ProtocolLibrary.getProtocolManager().getAsynchronousManager().registerAsyncHandler(
		    new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.KEEP_ALIVE) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
			    if (e.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
				try {
				    long startTime = System.currentTimeMillis();
				    PacketContainer keepAlivePacket = e.getPacket();
				    StructureModifier<Long> packetData = keepAlivePacket.getLongs();
				    Long packetValue = packetData.readSafely(0);
				    if (getPacketThread().getPing().get(packetValue) != null) {
					Long latency = getPacketThread().getPing().get(packetValue).getLatencyMillis();
					if (placeHolder != null)
					    placeHolder.latency.put(e.getPlayer().getName(), latency);
					e.setCancelled(true);
					setCount(getCount() + 1);
					if (isDebug())
					    plugin.getLogger().info("[Debug] Got plugin-sent keepalive "
						    + String.valueOf(packetValue) + " from " + e.getPlayer().getName()
						    + " after " + latency.toString() + " ms" + " (event processed in "
						    + String.valueOf(System.currentTimeMillis() - startTime) + "ms)");
				    } else {
					if (isDebug())
					    plugin.getLogger().info("[Debug] Got server-sent keepalive "
						    + String.valueOf(packetValue) + " from " + e.getPlayer().getName()
						    + " (event processed in "
						    + String.valueOf(System.currentTimeMillis() - startTime) + "ms)");
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

    /**
     * @return the debug
     */
    public boolean isDebug() {
	return debug;
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(boolean debug) {
	this.debug = debug;
    }

    /**
     * @return the packetThread
     */
    public AsyncPacketThread getPacketThread() {
	return packetThread;
    }

    /**
     * @param packetThread the packetThread to set
     */
    public void setPacketThread(AsyncPacketThread packetThread) {
	this.packetThread = packetThread;
    }
}
