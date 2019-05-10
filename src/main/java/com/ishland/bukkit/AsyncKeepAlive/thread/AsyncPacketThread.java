package com.ishland.bukkit.AsyncKeepAlive.thread;

import java.util.ArrayList;
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
    private boolean doStop = false;
    private boolean debug = false;
    private long frequency = 4000;
    private HashMap<Long, KeepAlivePacket> ping = new HashMap<Long, KeepAlivePacket>();
    private ArrayList<KeepAlivePacketGarbargeClean> garbargeCleanList = new ArrayList<KeepAlivePacketGarbargeClean>();
    private Timer timer = new Timer();
    private TimerTask stopCheck = new TimerTask() {
	public void run() {
	    stopCheck();
	}
    };
    private TimerTask mainloop;

    public void doStop() {
	doStop = true;
    }

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

    private void stopCheck() {
	if (doStop)
	    stoploop();
    }

    private void stopGCTasks() {
	getPlugin().getLogger().info("Stopping GC tasks...");
	for (int i = 0; i < getGarbargeCleanList().size(); i++)
	    getGarbargeCleanList().get(i).cancel();
	getGarbargeCleanList().clear();
    }

    private void cleanPackets() {
	getPlugin().getLogger().info("Cleaning up packets...");
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

    private void stoploop() {
	long startTime = System.currentTimeMillis();
	getPlugin().getLogger().info("Stopping Packet thread...");
	getPlugin().getLogger().info("Note that if the stop task did not finish completely in time: ");
	getPlugin().getLogger().info("If this is a shutdown, you can leave it alone");
	getPlugin().getLogger().info("If this is a reload, it may cause extra cpu usage");
	try {
	    getPlugin().getLogger().info("Stopping loops...");
	    mainloop.cancel();
	    stopCheck.cancel();
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

	timer.schedule(mainloop, 1000, this.frequency);
	timer.schedule(stopCheck, 1000, 500);
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
    public ArrayList<KeepAlivePacketGarbargeClean> getGarbargeCleanList() {
	return garbargeCleanList;
    }
}
