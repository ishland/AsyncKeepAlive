package com.ishland.bukkit.AsyncKeepAlive.thread;

import java.util.TimerTask;

import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacket;
import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacketFor1_12toLatest;
import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacketGarbargeClean;

public class AsyncPacketThreadFor1_12toLatest extends AsyncPacketThread implements Runnable {

    public AsyncPacketThreadFor1_12toLatest() {
	setMainloop(new TimerTask() {
	    @Override
	    public void run() {
		if (!getPlugin().getServer().getOnlinePlayers().isEmpty())
		    try {
			KeepAlivePacket packet = new KeepAlivePacketFor1_12toLatest(getPlugin());
			packet.boardcast();
			getPing().put(packet.getBody(), packet);
			KeepAlivePacketGarbargeClean gc = new KeepAlivePacketGarbargeClean(getPing(), packet.getBody());
			getTimer().schedule(gc, 30 * 1000);
			getGarbargeCleanList().add(gc);
			if (isDebug())
			    getPlugin().getLogger().info("[Debug] Boardcasted plugin-sent keepalive");
		    } catch (Throwable t) {
			t.printStackTrace();
		    }
	    }
	});
    }

}
