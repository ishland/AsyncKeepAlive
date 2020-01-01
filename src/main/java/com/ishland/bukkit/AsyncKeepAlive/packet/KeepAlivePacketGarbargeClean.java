package com.ishland.bukkit.AsyncKeepAlive.packet;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class KeepAlivePacketGarbargeClean extends TimerTask {
    Map<Long, KeepAlivePacket> map;
    Long key;
    Map<Long, KeepAlivePacketGarbargeClean> GCList;
    Long index;
    List<Long> sentPackets;

    @Override
    public void finalize() throws Throwable {
	super.finalize();
    }

    public KeepAlivePacketGarbargeClean(Map<Long, KeepAlivePacket> map, Long key,
	    Map<Long, KeepAlivePacketGarbargeClean> hashMap, Long index, List<Long> sentPackets) {
	this.map = map;
	this.key = key;
	this.GCList = hashMap;
	this.index = index;
	this.sentPackets = sentPackets;
    }

    @Override
    public void run() {
	KeepAlivePacket packet = map.get(key);
	if (packet.receivedCount >= packet.expectedCount)
	    sentPackets.remove(key);
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
