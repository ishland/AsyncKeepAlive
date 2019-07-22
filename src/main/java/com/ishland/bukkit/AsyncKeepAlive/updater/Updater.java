package com.ishland.bukkit.AsyncKeepAlive.updater;

import org.bukkit.plugin.Plugin;

public class Updater {
    private Plugin plugin;
    private Runnable updaterThread;
    private Thread thr;

    public Updater(Plugin plugin) {
	this.plugin = plugin;
	if (!this.plugin.getDescription().getVersion().contains("SNAPSHOT")) {
	    this.updaterThread = new UpdaterThreadForReleases(getPlugin());
	    thr = new Thread(this.updaterThread);
	    thr.setName("AsyncKeepAlive Updater");
	    thr.start();
	} else {
	    plugin.getLogger().info("You are using a SNAPSHOT version of AsyncKeepAlive. ");
	}
    }

    private Plugin getPlugin() {
	return this.plugin;
    }
}
