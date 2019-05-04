package com.ishland.bukkit.AsyncKeepAlive.thread;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import com.ishland.bukkit.AsyncKeepAlive.packet.KeepAlivePacket;

public class AsyncPacketThread {
    private Plugin plugin;
    private boolean doStop = false;
    private boolean debug = false;
    private long frequency = 4000;
    private HashMap<Long, KeepAlivePacket> ping = new HashMap<Long, KeepAlivePacket>();
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

    private void stoploop() {
	getPlugin().getLogger().info("Stopping Packet thread.");
	try {
	    mainloop.cancel();
	    stopCheck.cancel();
	} catch (Throwable e) {
	    getPlugin().getLogger().log(Level.SEVERE, "Error while stopping Packet Thread", e);
	    e.printStackTrace();
	}
	getPlugin().getLogger().info("Packet thread stopped.");
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
}
