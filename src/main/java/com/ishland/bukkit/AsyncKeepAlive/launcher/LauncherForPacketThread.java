package com.ishland.bukkit.AsyncKeepAlive.launcher;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.ishland.bukkit.AsyncKeepAlive.thread.AsyncPacketThread;
import com.ishland.bukkit.AsyncKeepAlive.thread.AsyncPacketThreadFor1_12toLatest;
import com.ishland.bukkit.AsyncKeepAlive.thread.AsyncPacketThreadFor1_8to1_11;

public class LauncherForPacketThread {
    protected Runnable object;
    protected Thread thread;

    public void launch(Plugin plugin, boolean debug, long freq) {
	if (Bukkit.getVersion().contains("1.7") || Bukkit.getVersion().contains("1.8")
		|| Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")
		|| Bukkit.getVersion().contains("1.11")) {
	    this.object = new AsyncPacketThreadFor1_8to1_11();
	} else if (Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.13")
		|| Bukkit.getVersion().contains("1.14")) {
	    this.object = new AsyncPacketThreadFor1_12toLatest();
	}
	if (debug)
	    ((AsyncPacketThread) this.object).doDebug();
	((AsyncPacketThread) this.object).setPlugin(plugin);
	((AsyncPacketThread) this.object).setFrequency(freq);
	this.thread = new Thread(this.object);
	this.thread.setDaemon(true);
	this.thread.setName("AsyncPacketThread");
	this.thread.run();
	((AsyncPacketThread) this.object).getPing().put((long) 0, null);
    }

    public AsyncPacketThread getObject() {
	return (AsyncPacketThread) this.object;
    }
}
