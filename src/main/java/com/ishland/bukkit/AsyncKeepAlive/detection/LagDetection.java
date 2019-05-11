/**
 * 
 */
package com.ishland.bukkit.AsyncKeepAlive.detection;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author ishland
 *
 */
public class LagDetection {
    Plugin plugin;
    BukkitTask syncloop;
    TimerTask asyncloop;
    long lastSyncloop = -1;
    byte state = 0;
    private Timer timer = new Timer();

    public LagDetection(Plugin plugin) {
	this.plugin = plugin;
	registerSyncTask();
	registerAsyncTask();
    }

    public void stopDetection() {
	this.asyncloop.cancel();
	this.syncloop.cancel();
    }

    private void registerSyncTask() {
	this.syncloop = getPlugin().getServer().getScheduler().runTaskTimer(getPlugin(), new Runnable() {
	    @Override
	    public void run() {
		lastSyncloop = System.currentTimeMillis();
	    }
	}, 20, 20);
    }

    private void registerAsyncTask() {
	this.asyncloop = new TimerTask() {
	    @Override
	    public void run() {
		if (System.currentTimeMillis() - lastSyncloop > 3000 && lastSyncloop != -1) {
		    state = 1;
		    getPlugin().getLogger().warning("Server had stopped responding for more than 3 seconds!");
		    Iterator<? extends Player> iterator = getPlugin().getServer().getOnlinePlayers().iterator();
		    while (iterator.hasNext()) {
			Player player = iterator.next();
			player.sendTitle("Warning", "Server had stopped responding for more than 3 seconds!", 0, 100,
				10);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
		    }
		} else if (state == 1) {
		    state = 0;
		    getPlugin().getLogger().info("Server is responding now!");
		    Iterator<? extends Player> iterator = getPlugin().getServer().getOnlinePlayers().iterator();
		    while (iterator.hasNext()) {
			Player player = iterator.next();
			player.sendTitle("Recover", "The server is now responding", 0, 10, 10);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1.2f);
		    }
		}
	    }
	};
	timer.schedule(asyncloop, 1000, 1000);
    }

    private Plugin getPlugin() {
	return plugin;
    }
}
