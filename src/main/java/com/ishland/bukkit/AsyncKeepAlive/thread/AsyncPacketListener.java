package com.ishland.bukkit.AsyncKeepAlive.thread;

import org.bukkit.plugin.Plugin;

import com.ishland.bukkit.AsyncKeepAlive.main.PlaceHolderMain;

public class AsyncPacketListener {
    private long count = (long) 0;

    public void register(Plugin plugin, boolean debug, AsyncPacketThread packetThread, PlaceHolderMain placeHolder) {

    }

    /**
     * @return the count
     */
    public long getCount() {
	return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
	this.count = count;
    }
}
