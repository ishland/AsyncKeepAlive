package com.ishland.bukkit.AsyncKeepAlive.packet;

import java.util.HashMap;
import java.util.TimerTask;

public class KeepAlivePacketGarbargeClean extends TimerTask {
    HashMap<Long, KeepAlivePacket> map;
    Long key;
    HashMap<Long, KeepAlivePacketGarbargeClean> GCList;
    Long index;

    @Override
    public void finalize() throws Throwable {
	super.finalize();
    }

    public KeepAlivePacketGarbargeClean(HashMap<Long, KeepAlivePacket> map, Long key,
	    HashMap<Long, KeepAlivePacketGarbargeClean> hashMap, Long index) {
	this.map = map;
	this.key = key;
	this.GCList = hashMap;
	this.index = index;
    }

    @Override
    public void run() {
	map.remove(key);
	GCList.remove(index);
	try {
	    this.finalize();
	} catch (Throwable e) {
	    System.out.println("Error while finalizing GC task");
	    e.printStackTrace();
	}
    }

}
