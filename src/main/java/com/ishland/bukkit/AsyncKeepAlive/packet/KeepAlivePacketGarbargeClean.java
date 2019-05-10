package com.ishland.bukkit.AsyncKeepAlive.packet;

import java.util.HashMap;
import java.util.TimerTask;

public class KeepAlivePacketGarbargeClean extends TimerTask {
    HashMap<Long, KeepAlivePacket> map;
    Long key;

    public KeepAlivePacketGarbargeClean(HashMap<Long, KeepAlivePacket> map, Long key) {
	this.map = map;
	this.key = key;
    }

    @Override
    public void run() {
	map.remove(key);
	try {
	    this.finalize();
	} catch (Throwable e) {
	    System.out.println("Error while finalizing GC task");
	    e.printStackTrace();
	}
    }

}
