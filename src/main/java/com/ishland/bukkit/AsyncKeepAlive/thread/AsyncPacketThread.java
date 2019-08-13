package com.ishland.bukkit.AsyncKeepAlive.thread;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacket;
import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacketGarbargeClean;

public class AsyncPacketThread {
    private Plugin plugin;
    private boolean debug = false;
    private long frequency = 4000;
    private HashMap<Long, KeepAlivePacket> ping = new HashMap<Long, KeepAlivePacket>();
    private HashMap<Long, KeepAlivePacketGarbargeClean> garbargeCleanList = new HashMap<Long, KeepAlivePacketGarbargeClean>();
    private Timer timer = new Timer();
    private TimerTask mainloop;
    protected Long index = (long) 0;
    protected Long count = (long) 0;

    public void doDebug() {
	debug = true;
    }

    public boolean setFrequency(long freq) {
	if (freq >= 1) {
	    frequency = freq;
	    return true;
	}
	return false;
    }

    private void stopGCTasks() {
	getPlugin().getLogger().info("Stopping " + String.valueOf(getGarbargeCleanList().size())
		+ " GC tasks (including non-exists tasks)...");
	Iterator<Entry<Long, KeepAlivePacketGarbargeClean>> iterator = getGarbargeCleanList().entrySet().iterator();
	while (iterator.hasNext()) {
	    Entry<Long, KeepAlivePacketGarbargeClean> entry = iterator.next();
	    if (entry.getValue() == null)
		continue;
	    try {
		entry.getValue().finalize();
	    } catch (Throwable e) {
		getPlugin().getLogger().log(Level.SEVERE, "Error while finalizing keepAlivePacket", e);
	    }
	}
    }

    private void cleanPackets() {
	getPlugin().getLogger()
		.info("Cleaning up " + String.valueOf(getPing().size()) + " packets (including non-exists packets)...");
	Iterator<Entry<Long, KeepAlivePacket>> iterator = getPing().entrySet().iterator();
	while (iterator.hasNext()) {
	    Entry<Long, KeepAlivePacket> entry = iterator.next();
	    if (entry.getValue() == null)
		continue;
	    try {
		entry.getValue().finalize();
	    } catch (Throwable e) {
		getPlugin().getLogger().log(Level.SEVERE, "Error while finalizing keepAlivePacket", e);
	    }
	}
    }

    public void stoploop() {
	long startTime = System.currentTimeMillis();
	try {
	    getPlugin().getLogger().info("Stopping loops...");
	    mainloop.cancel();
	    stopGCTasks();
	    cleanPackets();
	} catch (Throwable e) {
	    getPlugin().getLogger().log(Level.SEVERE, "Error while stopping Packet Thread", e);
	    e.printStackTrace();
	}
	getPlugin().getLogger()
		.info("Packet thread stopped in " + String.valueOf(System.currentTimeMillis() - startTime) + "ms");
    }

    public void run() {
	getPlugin().getDescription().getVersion();
	getPlugin().getLogger().info("Packet thread started.");

	timer.scheduleAtFixedRate(mainloop, 1000, this.frequency);
    }

    public Plugin getPlugin() {
	return plugin;
    }

    public void setPlugin(Plugin plugin) {
	this.plugin = plugin;
    }

    /**
     * @return the ping
     */
    public HashMap<Long, KeepAlivePacket> getPing() {
	return ping;
    }

    /**
     * @return the timer
     */
    protected Timer getTimer() {
	return timer;
    }

    /**
     * @return the mainloop
     */
    protected TimerTask getMainloop() {
	return mainloop;
    }

    /**
     * @param mainloop the mainloop to set
     */
    protected void setMainloop(TimerTask mainloop) {
	this.mainloop = mainloop;
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
	return debug;
    }

    /**
     * @return the garbargeCleanList
     */
    public HashMap<Long, KeepAlivePacketGarbargeClean> getGarbargeCleanList() {
	return garbargeCleanList;
    }

    /**
     * @return the index
     */
    public long getIndex() {
	return index;
    }

    /**
     * @return the index
     */
    public long getCount() {
	return count;
    }

}
