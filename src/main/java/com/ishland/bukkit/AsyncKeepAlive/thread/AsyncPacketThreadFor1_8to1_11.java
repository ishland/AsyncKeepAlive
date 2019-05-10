package com.ishland.bukkit.AsyncKeepAlive.thread;

import java.util.TimerTask;

import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacket;
import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacketFor1_7to1_11;

public class AsyncPacketThreadFor1_8to1_11 extends AsyncPacketThread implements Runnable {

    public AsyncPacketThreadFor1_8to1_11() {
	setMainloop(new TimerTask() {
	    @Override
	    public void run() {
		if (!getPlugin().getServer().getOnlinePlayers().isEmpty())
		    try {
			KeepAlivePacket packet = new KeepAlivePacketFor1_7to1_11(getPlugin());
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
