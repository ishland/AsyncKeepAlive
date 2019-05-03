package com.ishland.bukkit.AsyncKeepAlive.thread;

import java.util.TimerTask;

import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacket;
import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacketFor1_12toLatest;

public class AsyncPacketThreadFor1_12toLatest extends AsyncPacketThread implements Runnable {

    public AsyncPacketThreadFor1_12toLatest() {
	setMainloop(new TimerTask() {
	    @Override
	    public void run() {
		try {
		    KeepAlivePacket packet = new KeepAlivePacketFor1_12toLatest(getPlugin());
		    packet.boardcast();
		    getPing().put(packet.getBody(), packet);
		    if (isDebug())
			getPlugin().getLogger().info("[Debug] Boardcasted plugin-sent keepalive");
		} catch (Throwable t) {
		    t.printStackTrace();
		}
	    }
	});
    }

}
