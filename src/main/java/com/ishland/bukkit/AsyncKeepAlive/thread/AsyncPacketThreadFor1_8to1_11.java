package com.ishland.bukkit.AsyncKeepAlive.thread;

import java.util.TimerTask;

import org.bukkit.Bukkit;

import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacket;
import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacketFor1_7to1_11;
import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacketGarbargeClean;

public class AsyncPacketThreadFor1_8to1_11 extends AsyncPacketThread implements Runnable {

    public AsyncPacketThreadFor1_8to1_11() {
	setMainloop(new TimerTask() {
	    @Override
	    public void run() {
		if (!getPlugin().getServer().getOnlinePlayers().isEmpty())
		    try {
			KeepAlivePacket packet = new KeepAlivePacketFor1_7to1_11(getPlugin());
			count += Bukkit.getOnlinePlayers().size();
			packet.boardcast();
			getPing().put(packet.getBody(), packet);
			KeepAlivePacketGarbargeClean gc = new KeepAlivePacketGarbargeClean(getPing(), packet.getBody(),
				getGarbargeCleanList(), index);
			getTimer().schedule(gc, 60 * 1000);
			getGarbargeCleanList().put(index, gc);
			index++;
			if (isDebug())
			    getPlugin().getLogger().info("[Debug] Boardcasted plugin-sent keepalive");
		    } catch (Throwable t) {
			t.printStackTrace();
		    }
	    }
	});
    }

}
